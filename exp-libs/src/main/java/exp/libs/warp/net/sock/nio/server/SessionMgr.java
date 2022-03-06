package exp.libs.warp.net.sock.nio.server;

import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.os.ThreadUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.net.sock.bean.SocketByteBuffer;
import exp.libs.warp.net.sock.nio.common.cache.MsgQueue;
import exp.libs.warp.net.sock.nio.common.envm.Protocol;
import exp.libs.warp.net.sock.nio.common.envm.Times;
import exp.libs.warp.net.sock.nio.common.filterchain.impl.FilterChain;

/**
 * <pre>
 * 会话管理器线程
 * 
 * 用于管理所有连接到Socket服务端的会话（会话有效性验证、会话消息接收）
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
final class SessionMgr extends Thread {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(SessionMgr.class);
	
	/** 会话列表  */
	private List<Session> sessions;

	/** Socket服务端配置 */
	private NioServerConfig sockConf;

	/** 工作锁 */
	private byte[] lock;
	
	/** 线程运行状态 */
	private boolean running;
	
	/**
	 * 构造函数
	 * @param sockConf 服务器配置
	 */
	protected SessionMgr(NioServerConfig sockConf) {
		this.sockConf = sockConf;
		this.sessions = new LinkedList<Session>();
		this.lock = new byte[1];
		this.running = false;
	}

	protected void _start() {
		this.start();
	}
	
	protected void _stop() {
		this.running = false;
	}
	
	protected void _join() {
		try {
			join(Times.WAIT);
		} catch (Exception e) {}
	}
	
	protected boolean isRunning() {
		return running;
	}
	
	/**
	 * 会话管理线程核心
	 */
	@Override
	public void run() {
		running = true;
		do {
			try {
				synchronized (lock) {
					filterSessions();
				}

				// 设置陷阱，在阻塞前诱导线程自杀
				if(running == false) {
					break;
				}
				
				synchronized (lock) {
					for (Session session : sessions) {
						
						// 把未验证的会话交付到会话级事件处理
						if (session.isVerfied() == false) {
							handleSessionEvent(session);
							
						// 已验证的会话交付给消息级事件处理
						} else {
							handleMessageEvent(session);
						}
					}
				}
				
				ThreadUtils.tSleep(Times.SLEEP);
			} catch (Exception e) {
				log.error("会话管理器异常.", e);
				break;
			}
		} while (running);
		
		clear();
		_stop();
	}

	/**
	 * 过滤失效的会话
	 */
	private void filterSessions() {
		long curTime = System.currentTimeMillis();
		for (Iterator<Session> sIts = sessions.iterator(); sIts.hasNext();) {
			Session session = sIts.next();
			
			// 若该会话处于等待关闭状态，但超时仍未被远端机关闭，则本地主动关闭
			if (session.isWaitingToClose() == true) {
				if(curTime - session.getNotifyDisconTime() > sockConf.getOvertime()) {
					close(session);
				}
			}
			
			// 检查会话是否超时无动作
			if(session.isOvertime(curTime)) {
				log.debug("会话 [{}] 超时无动作, 关闭会话", session);
				close(session);
			}
			
			// 跳过未验证的会话
			if (session.isVerfied() == false) {
				continue;
				
			// 把发生异常、验证失败或已关闭的会话，进行关闭确认，并从会话维护队列中移除
			} else if (session.isError() == true || 
					session.isPassVerfy() == false || 
					session.isClosed() == true) {
				close(session);
				sIts.remove();
				log.debug("会话 [{}]已移除", session);
			}
		}
	}
	
	/**
	 * 交付会话处理器，处理会话级事件（会话验证）
	 * 
	 * @param session 会话
	 */
	private void handleSessionEvent(Session session) throws Exception {
		FilterChain filterChain = sockConf.getFilterChain();
		filterChain.onSessionCreated(session);
	}

	/**
	 * 交付消息处理器，处理会话中的消息级事件.
	 * 
	 * 	这里没有通过while循环一次读完session的消息队列，主要是为了session间的公平性，
	 *  避免当某个session一次有很多消息到来时，其他session要等很久。
	 *  但此时如果某个session有很多消息、而另一个几乎没有消息，则会引起处理缓慢的假象。
	 *  没有消息时的处理时延、主要受事件选择器的blockTime影响，其次是迭代的sleepTime。
	 * 
	 * @param session 会话
	 */
	private void handleMessageEvent(Session session) throws Exception {
		try {
			if (hasNewMsg(session)) {
				String msg = session.getMsgQueue().getMsg();
				FilterChain filterChain = sockConf.getFilterChain();

				session.flashActionTime();
				filterChain.onMessageReceived(session, msg);
			}
			
		} catch (ClosedSelectorException e) {
			// Undo 关闭事件选择器失败, 此为可忽略异常，不影响程序运行
        	
		} catch(ArrayIndexOutOfBoundsException e) {
			log.error("会话 [{}] 的本地缓冲区溢出, 上一条消息的数据可能已丢失或缺失.", session, e);
        	
		} catch (SocketTimeoutException e) {
			log.error("会话 [{}] 超时无动作, 关闭会话.", session, e);
			close(session);
			
		} catch (Exception e) {
			log.error("会话 [{}] 异常, 关闭会话.", session, e);
			close(session);
		}
	}

	/**
	 * 检查会话是否有新的待处理消息 首先采集会话通道中的数据，把新到得消息存放在原始消息队列末尾
	 * 然后检查原始消息队列的队头是否为空，非空则将其作为即将处理的消息
	 * 
	 * @param session 会话
	 * @return 只要原始消息队列非空，且会话未关闭，则返回成功状态
	 * @throws Exception 异常
	 */
	private boolean hasNewMsg(Session session) throws Exception {
		SocketChannel sc = session.getLayerSession();
		Selector selector = Selector.open();
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);

		int eventNum = selector.select(Times.BLOCK);
		if (eventNum > 0) {
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey sk = iterator.next();
				iterator.remove();

				// 会话通道已断开
				if (!handleKey(sk, session)) {
					session.close();
					break;
				}
			}
		}
		selector.close();
		return (!session.isClosed() && session.getMsgQueue().hasNewMsg());
	}

	/**
	 * 从会话通道采集数据，返回-1表示通道已断开
	 * 
	 * @param sk 关注事件键
	 * @param session 会话
	 * @return 
	 * @throws Exception 异常
	 */
	private boolean handleKey(SelectionKey sk, Session session) throws Exception {
		boolean isOk = true;
		SocketChannel sc = session.getLayerSession();
		ByteBuffer channelBuffer = session.getChannelBuffer();
		SocketByteBuffer socketBuffer = session.getSocketBuffer();
		
		if (sk.channel().equals(sc) && sk.isReadable()) {
			int count = 0;
			channelBuffer.clear();
			while ((count = sc.read(channelBuffer)) > 0) {
				channelBuffer.flip();
				for (int i = 0; i < count; i++) {
					socketBuffer.append(channelBuffer.get(i));
				}

				String[] readDelimiters = sockConf.getReadDelimiters();
				int[] rdIdxs = new int[readDelimiters.length];	// 对应每个消息分隔符的索引
				while (true) {	// 可能一次性收到多条消息，在缓冲区可读时需全部处理完，减少处理迟延
					
					// 枚举所有分隔符，取索引值最小的分隔符位置（索引值>=0有效）
					int iEnd = -1;
					for(int i = 0; i < readDelimiters.length; i++) {
						rdIdxs[i] = socketBuffer.indexOf(readDelimiters[i]);
						
						if(rdIdxs[i] >= 0) {
							if(iEnd < 0) {
								iEnd = rdIdxs[i];
								
							} else if(iEnd > rdIdxs[i]) {
								iEnd = rdIdxs[i];
							}
						}
					}
					
					// 所有分隔符都无法截获消息
					if(iEnd < 0) {
						break;
					}

					// 把原始消息添加到原始消息队列，并剔除空消息和越限消息(防止攻击)
					String newMsg = socketBuffer.subString(iEnd).trim();
					if(StrUtils.isNotEmpty(newMsg)) {
						if (!session.getMsgQueue().addNewMsg(newMsg)) {
							session.writeErrMsg(Protocol.MSG_LIMIT);
							
							log.warn("会话 [{}] 连续发送超过 [{}] 条未处理消息.最新消息被抛弃:\r\n{}", 
									session, MsgQueue.MAX_MSG_LIMIT, newMsg);
						}
					}
					socketBuffer.delete(iEnd);
				}
				channelBuffer.clear();
			}
			
			// Socket通道已断开(客户端主动关闭会话)
			if (count < 0) {
				isOk = false;
			}
		}
		return isOk;
	}

	/**
	 * 添加新客户端到会话管理队列
	 * 
	 * @param newSession 新客户端的
	 * @return true:添加成功; false:添加失败
	 * @throws Exception 异常
	 */
	protected boolean addSession(Session newSession) throws Exception {
		boolean isOk = false;
		int maxLinkNum = sockConf.getMaxConnectionCount();

		synchronized (lock) {
			if (newSession != null && 
				(maxLinkNum < 0 || sessions.size() < maxLinkNum)) {
				sessions.add(newSession);
				isOk = true;
			}
		}
		return isOk;
	}
	
	/**
	 * 关闭会话
	 * @param session 会话
	 */
	private void close(Session session) {
		try {
			if(session != null) {
				session.close();
			}
		} catch (Exception e) {
			session.markError();
			log.error("关闭会话 [{}] 异常.", session, e);
		}
	}

	/**
	 * 移除所有前台客户代理线程
	 * 
	 * @return true:移除成功; false:移除失败
	 * @throws Exception 
	 */
	private boolean clear() {
		boolean isOk = false;

		synchronized (lock) {
			if(sessions != null) {
				for(Session session : sessions) {
					close(session);
				}
				
				sessions.clear();
				isOk = true;
			}
		}
		return isOk;
	}

	/**
	 * 获取当前活动的客户端连接数
	 * 
	 * @return 当前活动的客户端连接数
	 */
	protected int getSessionCnt() {
		synchronized (lock) {
			return sessions.size();
		}
	}
	
}

package exp.libs.warp.net.ping;

import java.util.List;

import exp.libs.envm.Charset;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.os.OSUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.utils.verify.RegexUtils;
import exp.libs.warp.cmd.CmdUtils;
import exp.libs.warp.io.flow.StringFlowReader;

/**
 * <pre>
 * 简单的ping操作
 * </pre>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-06-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Ping {
	
	/** ping命令 */
	private final static String CMD_PING = "ping";
	
	/** tracert命令 */
	private final static String CMD_TRACERT = OSUtils.isWin() ? 
			"tracert -d -w 1 " : "traceroute -w 0.1 -n -q 1 ";
	
	/** 私有化构造函数 */
	protected Ping() {}
	
	/**
	 * 执行ping操作
	 * @param host 被ping的主机, 可以是IP(如192.168.11.22) 或 域名(如:www.exp-blog.com)
	 * @return ping结果
	 */
	public static Pong ping(String host) {
		return ping(host, 4, 0, 0);
	}
	
	/**
	 * 执行ping操作
	 * @param host 被ping的主机, 可以是IP(如192.168.11.22) 或 域名(如:www.exp-blog.com)
	 * @param count 执行ping的次数(默认4次)
	 * @return ping结果
	 */
	public static Pong ping(String host, int count) {
		return ping(host, count, 0, 0);
	}
	
	/**
	 * 执行ping操作
	 * @param host 被ping的主机, 可以是IP(如192.168.11.22) 或 域名(如:www.exp-blog.com)
	 * @param count 执行ping的次数(默认4次)
	 * @param timeout 仅linux有效:命令超时(s). 若超过这个时间则ping命令强制结束
	 * @param intervalSecond 仅linux有效:执行ping的间隔(s), 默认值1秒
	 * @return ping结果
	 */
	public static Pong ping(String host, 
			int count, int timeout, double intervalSecond) {
		String cmd = getPingCommand(host, count, timeout, intervalSecond);
		return toPong(CmdUtils.execute(cmd).getInfo());
	}

	/**
	 * 组装ping命令
	 * @param host 被ping的主机, 可以是IP(如192.168.11.22) 或 域名(如:www.exp-blog.com)
	 * @param count 执行ping的次数(默认4次)
	 * @param timeout 仅linux有效:命令超时(s). 若超过这个时间则ping命令强制结束
	 * @param intervalSecond 仅linux有效:执行ping的间隔(s), 默认值1秒
	 * @return ping命令
	 */
	private static String getPingCommand(String host, 
			int count, int timeout, double intervalSecond) {
		StringBuilder cmd = new StringBuilder(CMD_PING);
		cmd.append(" ").append(host);
		
		count = (count <= 0 ? 4 : count);	// 默认ping 4次
		cmd.append(OSUtils.isWin() ? " -n " : " -c ").append(count);
		
		if(OSUtils.isUnix()) {
			if(timeout > 0) {
				cmd.append(" -w ").append(timeout);
				
			} else if(OSUtils.isUnix()) {
				cmd.append(" -w 10");	// linux环境强制指定超时(否则会无限阻塞)
			}
			
			if(intervalSecond > 0) {
				cmd.append(" -i ").append(intervalSecond);
			}
		}
		return cmd.toString();
	}
	
	/**
	 * 把 [ping执行结果] 转换成 [pong对象]
	 * @param pingRst ping执行结果
	 * @return pong对象
	 */
	protected static Pong toPong(String pingRst) {
		return OSUtils.isWin() ? 
				_toPongByWin(pingRst) : _toPongByLinux(pingRst);
	}
	
	/**
	 * 把 [win平台下的ping执行结果] 转换成 [pong对象]
	 * @param pingRst win平台下的ping执行结果
	 * @return
	 */
	private static Pong _toPongByWin(String pingRst) {
		return StrUtils.containsCh(pingRst) ? 
				_toPongByWinCh(pingRst) : _toPongByWinEn(pingRst);
	}
	
	/**
	 * 把 [(英文版)win平台下的ping执行结果] 转换成 [pong对象]
	 * @param pingRst (英文版)win平台下的ping执行结果
	 * <PRE>
	 * PING IP：
	    	Pinging 183.232.231.173 with 32 bytes of data:
	    	
	 * 或 PING 域名：
			Pinging www.exp-blog.com [183.232.231.173] with 32 bytes of data:
			Reply from 183.232.231.173: bytes=32 time<1ms TTL=50
			Reply from 183.232.231.173: bytes=32 time=6ms TTL=50
			Reply from 183.232.231.173: bytes=32 time=7ms TTL=50
			Request timed out.
			
			Ping statistics for 183.232.231.173:
			    Packets: Sent = 4, Received = 3, Lost = 1 (25% loss),
			Approximate round trip times in milli-seconds:
			    Minimum = 6ms, Maximum = 10ms, Average = 8ms
		</PRE>
	 * @return pong对象
	 */
	private static Pong _toPongByWinEn(String pingRst) {
		Pong pong = new Pong();
		StringFlowReader sfr = new StringFlowReader(pingRst, Charset.DEFAULT);
		while(sfr.hasNextLine()) {
			String line = sfr.readLine().trim();
			
			if(StrUtils.isEmpty(line)) {
				continue;
				
			} else if(line.startsWith("Pinging")) {
				int bytes = NumUtils.toInt(
						RegexUtils.findFirst(line, "(\\d+) bytes"), -1);
				pong.setPacketBytes(bytes);
				
			} else if(line.startsWith("Reply")) {
				line = line.replace("<1ms", "=0ms");
				List<String> nums = RegexUtils.findGroups(line, 
						"time=([\\d\\.]+)ms TTL=(\\d+)");
				double rtt = NumUtils.toDouble(nums.get(1), 0);
				int ttl = NumUtils.toInt(nums.get(2), 0);
				
				pong.addRTT(rtt);
				pong.setTTL(ttl);
				
			} else if(line.startsWith("Request timed out")) {
				pong.addRTT(-1);
				
			} else if(line.contains("statistics")) {
				String snkIp = RegexUtils.findFirst(line, "for ([\\d\\.]+)");
				pong.setSnkIp(snkIp);
				
			} else if(line.startsWith("Packets")) {
				List<String> nums = RegexUtils.findGroups(line, 
						"Sent = (\\d+), Received = (\\d+), Lost = (\\d+) \\(([\\d\\.]+)% loss");
				int sent = NumUtils.toInt(nums.get(1), 0);
				int recv = NumUtils.toInt(nums.get(2), 0);
				int lost = NumUtils.toInt(nums.get(3), 0);
				double loss = NumUtils.toDouble(nums.get(4), 0);
				
				pong.setSent(sent);
				pong.setRecv(recv);
				pong.setLost(lost);
				pong.setLoss(loss);
				
			} else if(line.startsWith("Minimum")) {
				List<String> nums = RegexUtils.findGroups(line, 
						"Minimum = (\\d+)ms, Maximum = (\\d+)ms, Average = (\\d+)ms");
				double minRTT = NumUtils.toDouble(nums.get(1), 0);
				double maxRTT = NumUtils.toDouble(nums.get(2), 0);
				double avgRTT = NumUtils.toDouble(nums.get(3), 0);
				
				pong.setMinRTT(minRTT);
				pong.setMaxRTT(maxRTT);
				pong.setAvgRTT(avgRTT);
			}
		}
		sfr.close();
		return pong;
	}
	
	/**
	 * 把 [(中文版)win平台下的ping执行结果] 转换成 [pong对象]
	 * @param pingRst (中文版)win平台下的ping执行结果
	 * <PRE>
	 * PING IP：
	    	正在 Ping 183.232.231.173 具有 32 字节的数据:
	    	
	 * 或 PING 域名：
			正在 Ping www.exp-blog.com [183.232.231.172] 具有 32 字节的数据:
			来自 183.232.231.172 的回复: 字节=32 时间<1ms TTL=54
			来自 183.232.231.172 的回复: 字节=32 时间=58ms TTL=54
			来自 183.232.231.172 的回复: 字节=32 时间=69ms TTL=54
			请求超时。
			
			183.232.231.172 的 Ping 统计信息:
			    数据包: 已发送 = 4，已接收 = 3，丢失 = 1 (25% 丢失)，
			往返行程的估计时间(以毫秒为单位):
			    最短 = 51ms，最长 = 69ms，平均 = 59ms
		</PRE>
	 * @return pong对象
	 */
	private static Pong _toPongByWinCh(String pingRst) {
		Pong pong = new Pong();
		StringFlowReader sfr = new StringFlowReader(pingRst, Charset.DEFAULT);
		while(sfr.hasNextLine()) {
			String line = sfr.readLine().trim();
			
			if(StrUtils.isEmpty(line)) {
				continue;
				
			} else if(line.startsWith("正在 Ping")) {
				int bytes = NumUtils.toInt(
						RegexUtils.findFirst(line, "(\\d+) 字节"), -1);
				pong.setPacketBytes(bytes);
				
			} else if(line.startsWith("来自")) {
				line = line.replace("<1ms", "=0ms");
				List<String> nums = RegexUtils.findGroups(line, 
						"时间=([\\d\\.]+)ms TTL=(\\d+)");
				double rtt = NumUtils.toDouble(nums.get(1), 0);
				int ttl = NumUtils.toInt(nums.get(2), 0);
				
				pong.addRTT(rtt);
				pong.setTTL(ttl);
				
			} else if(line.startsWith("请求超时")) {
				pong.addRTT(-1);
				
			} else if(line.contains("统计")) {
				String snkIp = RegexUtils.findFirst(line, "([\\d\\.]+) 的 Ping");
				pong.setSnkIp(snkIp);
				
			} else if(line.startsWith("数据包")) {
				List<String> nums = RegexUtils.findGroups(line, 
						"已发送 = (\\d+)，已接收 = (\\d+)，丢失 = (\\d+) \\(([\\d\\.]+)% 丢失");
				int sent = NumUtils.toInt(nums.get(1), 0);
				int recv = NumUtils.toInt(nums.get(2), 0);
				int lost = NumUtils.toInt(nums.get(3), 0);
				double loss = NumUtils.toDouble(nums.get(4), 0);
				
				pong.setSent(sent);
				pong.setRecv(recv);
				pong.setLost(lost);
				pong.setLoss(loss);
				
			} else if(line.startsWith("最短")) {
				List<String> nums = RegexUtils.findGroups(line, 
						"最短 = (\\d+)ms，最长 = (\\d+)ms，平均 = (\\d+)ms");
				double minRTT = NumUtils.toDouble(nums.get(1), 0);
				double maxRTT = NumUtils.toDouble(nums.get(2), 0);
				double avgRTT = NumUtils.toDouble(nums.get(3), 0);
				
				pong.setMinRTT(minRTT);
				pong.setMaxRTT(maxRTT);
				pong.setAvgRTT(avgRTT);
			}
		}
		sfr.close();
		return pong;
	}
	
	/**
	 * 把 [linux平台下的ping执行结果] 转换成 [pong对象]
	 * @param pingRst linux平台下的ping执行结果
	 * <PRE>
	 * PING IP：
	    	PING 220.181.112.244 (220.181.112.244) 56(84) bytes of data.
	    	
	 * 或 PING 域名：
		 	PING www.exp-blog.com (220.181.112.244) 56(84) bytes of data.
			64 bytes from 220.181.112.244 (220.181.112.244): icmp_seq=1 ttl=55 time<1 ms
			64 bytes from 220.181.112.244 (220.181.112.244): icmp_seq=2 ttl=55 time=43.9 ms
			64 bytes from 220.181.112.244 (220.181.112.244): icmp_seq=3 ttl=55 time=43.9 ms
			64 bytes from 220.181.112.244 (220.181.112.244): icmp_seq=4 ttl=55 time=43.9 ms
			
			--- www.exp-blog.com ping statistics ---
			4 packets transmitted, 4 received, 0% packet loss, time 3003ms
			rtt min/avg/max/mdev = 43.928/43.953/43.989/0.257 ms 
		</PRE>
	 * @return pong对象
	 */
	private static Pong _toPongByLinux(String pingRst) {
		Pong pong = new Pong();
		StringFlowReader sfr = new StringFlowReader(pingRst, Charset.DEFAULT);
		while(sfr.hasNextLine()) {
			String line = sfr.readLine().trim();
			
			if(StrUtils.isEmpty(line)) {
				continue;
				
			} else if(line.startsWith("PING")) {
				String snkIp = RegexUtils.findFirst(line, " \\(([\\d\\.]+)\\) ");
				pong.setSnkIp(snkIp);
				
			} else if(line.contains("bytes from")) {
				line = line.replace("<1 ms", "=0 ms");
				List<String> nums = RegexUtils.findGroups(line, 
						"(\\d+) bytes from.*?ttl=(\\d+) time=([\\d\\.]+) ms");
				int bytes = NumUtils.toInt(nums.get(1), 0);
				int ttl = NumUtils.toInt(nums.get(2), 0);
				double rtt = NumUtils.toDouble(nums.get(3), 0);
				
				pong.setPacketBytes(bytes);
				pong.setTTL(ttl);
				pong.addRTT(rtt);
				
			} else if(line.contains("packets transmitted")) {
				List<String> nums = RegexUtils.findGroups(line, 
						"(\\d+) packets transmitted, (\\d+) received, ([\\d\\.]+)% packet loss, time ([\\d\\.]+)ms");
				
				int sent = NumUtils.toInt(nums.get(1), 0);
				int recv = NumUtils.toInt(nums.get(2), 0);
				double loss = NumUtils.toDouble(nums.get(3), 0);
				double totalRTT = NumUtils.toDouble(nums.get(4), 0);
				
				pong.setSent(sent);
				pong.setRecv(recv);
				pong.setLost(sent - recv);
				pong.setLoss(loss);
				pong.setTotalRTT(totalRTT);
				
			} else if(line.startsWith("rtt")) {
				List<String> nums = RegexUtils.findGroups(line, 
						"([\\d\\.]+)/([\\d\\.]+)/([\\d\\.]+)/([\\d\\.]+) ms");
				double minRTT = NumUtils.toDouble(nums.get(1), 0);
				double avgRTT = NumUtils.toDouble(nums.get(2), 0);
				double maxRTT = NumUtils.toDouble(nums.get(3), 0);
				double mdevRTT = NumUtils.toDouble(nums.get(4), 0);
				
				pong.setMinRTT(minRTT);
				pong.setMaxRTT(maxRTT);
				pong.setAvgRTT(avgRTT);
				pong.setMdevRTT(mdevRTT);
			}
		}
		sfr.close();
		return pong;
	}
	
	/**
	 * 执行tracert操作
	 * 
	 * @param host 被tracert的主机, 可以是IP(如192.168.11.22) 或 域名(如:www.exp-blog.com)
	 * @return tracert结果
	 */
	public static String tracert(String host) {
		String cmd = StrUtils.concat(CMD_TRACERT, host);
		return CmdUtils.execute(cmd).getInfo();
	}

}

package exp.libs.warp.net.mq.jms.sup;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * jms工厂类
 * 
 * <pre>
 * public void onCommand(Object command)<br/>
 * public void onException(IOException error)<br/>
 * public void transportInterupted()<br/>
 * public void transportResumed()<br/>
 * </pre>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class JMSFactory {

	/**
	 * jms的url
	 */
	private String url;

	/**
	 * 上下文工厂字符串
	 */
	private String contextFactory;

	/**
	 * 连接工厂字符串
	 */
	private String connectionFactory;

	/**
	 * 上下文工厂对象
	 */
	private Context context;

	/**
	 * 连接工厂对象
	 */
	private ConnectionFactory factory;

	/**
	 * 构造方法
	 * 
	 * @param url
	 *            jms的url
	 * @param contextFactory
	 *            上下文工厂字符串
	 * @param connectionFactory
	 *            连接工厂字符串
	 */
	public JMSFactory(String url, String contextFactory,
			String connectionFactory) {
		this.url = url;
		this.contextFactory = contextFactory;
		this.connectionFactory = connectionFactory;
		createFactory();
	}

	/**
	 * 创建工厂
	 */
	private void createFactory() {

		Properties prop = new Properties();
		prop.put(Context.PROVIDER_URL, url);
		prop.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);

		try {
			context = new InitialContext(prop);
			factory = (ConnectionFactory) context.lookup(connectionFactory);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建连接
	 * 
	 * @return 连接
	 * @throws Exception
	 */
	public Connection createConnection() throws Exception {
		return factory.createConnection();
	}

	/**
	 * 创建连接
	 * 
	 * @param user
	 *            用户名
	 * @param password
	 *            密码
	 * @return 连接
	 * @throws Exception
	 */
	public Connection createConnection(String user, String password)
			throws Exception {
		return factory.createConnection(user, password);
	}

}

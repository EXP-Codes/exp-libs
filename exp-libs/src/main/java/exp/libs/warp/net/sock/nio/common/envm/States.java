package exp.libs.warp.net.sock.nio.common.envm;

/**
 * <pre>
 * 枚举类：状态值
 * 
 * 主要用于程序执行中的即时状态定义，根据不同状态执行不同操作。
 * 对于同类型高级的状态，必定已包含低级的成功状态，且同级状态互斥。
 * </pre>	
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public enum States {

	/** 成功状态，适用所有操作 */
	SUCCESS(0, Event.COMMON, 0, "成功，适用所有操作"),

	/** 失败状态，适用所有操作 */
	FAIL(1, Event.COMMON, 0, "失败，适用所有操作"),

	/** 会话未验证状态 */
	NO_VERIFY(2, Event.SESSION, 0, "未验证"),

	/** 会话通过验证状态 */
	VERIFY_SUCCESS(3, Event.SESSION, 0, "已验证"),

	/** 会话验证失败状态 */
	VERIFY_FAIL(4, Event.SESSION, 0, "验证信息非法"),

	/** 等待关闭状态，此状态下会话仅可读，不可写，等待远程机器断开连接 */
	WAIT_TO_CLOSE(5, Event.SESSION, 1, "等待关闭状态"),
	
	/** 会话异常状态 */
	EXCEPTION(6, Event.SESSION, 2, "会话发生异常"),
	
	/** 会话已关闭状态 */
	CLOSED(7, Event.SESSION, 2, "会话已关闭"),

	;

	/** 状态值 */
	public int id;

	/** 状态类型 */
	public Event type;

	/** 状态等级 */
	public int level;

	/** 状态描述 */
	public String desc;

	/**
	 * 构造函数
	 * @param id 状态值
	 * @param type 状态类型
	 * @param level 状态等级
	 * @param desc 状态描述
	 */
	private States(int id, Event type, int level, String desc) {
		this.id = id;
		this.type = type;
		this.level = level;
		this.desc = desc;
	}
	
}

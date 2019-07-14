package exp.libs.warp.task.cron;

import org.quartz.CronExpression;

import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 适用于任务调度管理器的cron对象（用于生成cron表达式的规则字符串）
 * ----------------------------
 * cron表达式共有7个时间字段, 依次为:
 * 	秒, 分, 时, 日, 月, 周, 年
 * ----------------------------
 * 每个时间字段的取值范围如下:<br/>
+--------------------+---------------------------+------------------+
| [时间字段]         | [取值范围]                | [允许的特殊字符] |
+--------------------+---------------------------+------------------+
| 秒(Second)         | 0-59                      | , - * /          |
| 分(Minute)         | 0-59                      | , - * /          |
| 小时(Hour)         | 0-23                      | , - * /          |
| 日期(DayOfMonth)   | 1-31                      | , - * / ? L W C  |
| 月份(Month)        | 1-12(或JAN-DEC)           | , - * /          |
| 星期(Week)         | 1-7(或SUN-SAT, 其中1=SUN) | , - * / ? L C #  |
| 年(Year)[可选字段] | 空值或1970-2099           | , - * /          |
+--------------------+---------------------------+------------------+
 * <br/>
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-06-28
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Cron {

	/** [秒]字段的表达式对象 */
	private final _Second second;
	
	/** [分钟]字段的表达式对象 */
	private final _Minute minute;
	
	/** [小时]字段的表达式对象 */
	private final _Hour hour;
	
	/** [日期]字段的表达式对象 */
	private final _Day day;
	
	/** [月份]字段的表达式对象 */
	private final _Month month;
	
	/** [星期]字段的表达式对象 */
	private final _Week week;
	
	/** [年份]字段的表达式对象 */
	private final _Year year;
	
	/**
	 * 构造函数
	 */
	public Cron() {
		this.second = new _Second(this);
		this.minute = new _Minute(this);
		this.hour = new _Hour(this);
		this.day = new _Day(this);
		this.month = new _Month(this);
		this.week = new _Week(this);
		this.year = new _Year(this);
	}
	
	/**
	 * 构造函数
	 * @param expression cron表达式
	 */
	public Cron(String expression) {
		this();
		setExpression(expression);
	}
	
	/**
	 * 重置cron表达式
	 */
	public void reset() {
		second.reset();
		minute.reset();
		hour.reset();
		day.reset();
		month.reset();
		week.reset();
		year.reset();
	}
	
	/**
	 * 设置cron表达式
	 * @param expression cron表达式字符串
	 * @return cron表达式对象
	 */
	public Cron setExpression(String expression) {
		if(expression != null && CronExpression.isValidExpression(expression)) {
			expression = expression.trim();
			String[] subExpressions = expression.split(__TimeUnit.SPACE);
			
			boolean isOk = (subExpressions.length >= 6);
			if(isOk == true) {
				second._setSubExpression(subExpressions[0]);
				minute._setSubExpression(subExpressions[1]);
				hour._setSubExpression(subExpressions[2]);
				day._setSubExpression(subExpressions[3]);
				month._setSubExpression(subExpressions[4]);
				week._setSubExpression(subExpressions[5]);
				
				if(subExpressions.length == 7) {
					second._setSubExpression(subExpressions[6]);
				}
			}
		}
		return this;
	}
	
	/**
	 * [秒] 时间字段
	 * @return [秒] 时间字段
	 */
	final public _Second Second() {
		return second;
	}
	
	/**
	 * [分钟] 时间字段
	 * @return [分钟] 时间字段
	 */
	final public _Minute Minute() {
		return minute;
	}
	
	/**
	 * [小时] 时间字段
	 * @return [小时] 时间字段
	 */
	final public _Hour Hour() {
		return hour;
	}
	
	/**
	 * [日期] 时间字段
	 * @return [日期] 时间字段
	 */
	final public _Day Day() {
		return day;
	}
	
	/**
	 * [月份] 时间字段
	 * @return [月份] 时间字段
	 */
	final public _Month Month() {
		return month;
	}
	
	/**
	 * [星期] 时间字段
	 * @return [星期] 时间字段
	 */
	final public _Week Week() {
		return week;
	}
	
	/**
	 * [年份] 时间字段
	 * @return [年份] 时间字段
	 */
	final public _Year Year() {
		return year;
	}
	
	/**
	 * 生成cron表达式
	 * @return cron表达式
	 */
	public String toExpression() {
		String expression = StrUtils.concat(
				second.getSubExpression(), __TimeUnit.SPACE, 
				minute.getSubExpression(), __TimeUnit.SPACE, 
				hour.getSubExpression(), __TimeUnit.SPACE, 
				day.getSubExpression(), __TimeUnit.SPACE, 
				month.getSubExpression(), __TimeUnit.SPACE, 
				week.getSubExpression(), __TimeUnit.SPACE, 
				year.getSubExpression()
		);
		return expression;
	}
	
	@Override
	public String toString() {
		return toExpression();
	}
	
}

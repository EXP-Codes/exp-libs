package exp.libs.warp.task.cron;

import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * cron表达式的中[日期]字段的子表达式对象.
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
public class _Day extends __TimeUnit {

	/** [日期] 的最小值 */
	public final static int MIN = 1;
	
	/** [日期] 的最大值 */
	public final static int MAX = 31;
	
	/**
	 * 构造函数
	 * @param cron 所属的Cron表达式对象
	 */
	protected _Day(Cron cron) {
		super(cron);
		reset();
	}
	
	/**
	 * 重置子表达式的初始值
	 */
	protected void reset() {
		_setSubExpression(EVERY);
	}

	/**
	 * 设置为"无意义"值, 使得该值对最终的cron规则无约束影响
	 * @return
	 */
	public String withNone() {
		return setSubExpression(NONE);
	}
	
	/**
	 * 设置为: 即某月的最后一天触发
	 * @return
	 */
	public String withLastDay() {
		return setSubExpression(L);
	}
	
	/**
	 * 设置为: 在离日期day最近的"工作日"触发
	 * @param day
	 * @return
	 */
	public String withWorkday(int day) {
		return !_checkRange(day) ? getSubExpression() : 
			setSubExpression(StrUtils.concat(day, W));
	}
	
	/**
	 * 设置为: 某月的最后一个工作日触发
	 * @return
	 */
	public String withLastWorkday() {
		return setSubExpression(LW);
	}
	
	/**
	 * 设置为: 日历中day日以后的第一天
	 * @param day
	 * @return
	 */
	public String withAfterDay(int day) {
		return !_checkRange(day) ? getSubExpression() : 
			setSubExpression(StrUtils.concat(day, C));
	}

	/**
	 * 检查[日期]值是否在合法范围内
	 * @param day
	 * @return
	 */
	private boolean inRange(int day) {
		return (MIN <= day && day <= MAX);
	}

	@Override
	protected boolean _checkRange(int... values) {
		boolean isOk = (values != null && values.length > 0);
		if(isOk == true) {
			for(int day : values) {
				isOk &= inRange(day);
			}
		}
		return isOk;
	}

	@Override
	protected boolean _checkSubExpression(String subExpression) {
		boolean isOk = false;
		
		// 子表达式为: [*] 或 [?] 或 [L] 或 [LW]
		if(EVERY.equals(subExpression) || NONE.equals(subExpression) || 
				L.equals(subExpression) || LW.equals(subExpression)) {
			isOk = true;
		
		// 子表达式为: [xW] 或  [xC]
		} else if(subExpression.matches(StrUtils.concat("^\\d+[", W, "|", C, "]$"))) {
			String sDay = subExpression.replace(W, "").replace(C, "");
			int day = NumUtils.toInt(sDay, -1);
			isOk = inRange(day);
						
		// 子表达式为: [x] 或 [ a,b,c,...,n]
		} else if(subExpression.matches(
				StrUtils.concat("^\\d+(", SEQUENCE, "\\d+){0,", MAX, "}$"))) {
			String[] sequence = subExpression.split(SEQUENCE);
			isOk = true;
			for(String day : sequence) {
				isOk &= inRange(NumUtils.toInt(day, -1));
			}
			
		// 子表达式为: [x-y]
		} else if(subExpression.matches(
				StrUtils.concat("^\\d+", RANGE, "\\d+$"))) {
			String[] range = subExpression.split(RANGE);
			int from = NumUtils.toInt(range[0], -1);
			int to = NumUtils.toInt(range[1], -1);
			isOk = inRange(from) && inRange(to);
			
		// 子表达式为: [x/y]
		} else if(subExpression.matches(
				StrUtils.concat("^\\d+", STEP, "\\d+$"))) {
			String[] step = subExpression.split(STEP);
			int from = NumUtils.toInt(step[0], -1);
			int interval = NumUtils.toInt(step[1], -1);
			isOk = inRange(from) && interval > 0;
		
		}
		return isOk;
	}

	@Override
	protected void _trigger(Cron cron, String subExpression) {
		
		// [日期]字段的子表达式为: [*] 时, 比[日期]大的时间单位不再有参考意义
		if(EVERY.equals(subExpression)) {
			cron.Month()._setSubExpression(EVERY);
			cron.Week()._setSubExpression(NONE);
			cron.Year()._setSubExpression(EVERY);
			
		// [日期]字段的子表达式为"有效值"时, [星期]字段的子表达式为要变成"无效值"
		} else if(!NONE.equals(subExpression)) {
			cron.Week()._setSubExpression(NONE);
			
		}
		
		// 比[日期]小的时间单位若为 [*] 则自动变成 [最小值]
		if(cron.Second().isEvery()) { cron.Second()._setSubExpression(_Second.MIN); }
		if(cron.Minute().isEvery()) { cron.Minute()._setSubExpression(_Minute.MIN); }
		if(cron.Hour().isEvery()) { cron.Hour()._setSubExpression(_Hour.MIN); }
	}

}

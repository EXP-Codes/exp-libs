package exp.libs.warp.task.cron;

import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * cron表达式的中[小时]字段的子表达式对象.
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
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class _Hour extends __TimeUnit {

	/** [小时] 的最小值 */
	public final static int MIN = 0;
	
	/** [小时] 的最大值 */
	public final static int MAX = 23;
	
	/**
	 * 构造函数
	 * @param cron 所属的Cron表达式对象
	 */
	protected _Hour(Cron cron) {
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
	 * 检查[小时]值是否在合法范围内
	 * @param hour
	 * @return
	 */
	private boolean inRange(int hour) {
		return (MIN <= hour && hour <= MAX);
	}

	@Override
	protected boolean _checkRange(int... values) {
		boolean isOk = (values != null && values.length > 0);
		if(isOk == true) {
			for(int hour : values) {
				isOk &= inRange(hour);
			}
		}
		return isOk;
	}

	@Override
	protected boolean _checkSubExpression(String subExpression) {
		boolean isOk = false;
		
		// 子表达式为: [*]
		if(EVERY.equals(subExpression)) {
			isOk = true;
		
		// 子表达式为: [x] 或 [ a,b,c,...,n]
		} else if(subExpression.matches(
				StrUtils.concat("^\\d+(", SEQUENCE, "\\d+){0,", (MAX + 1), "}$"))) {
			String[] sequence = subExpression.split(SEQUENCE);
			isOk = true;
			for(String hour : sequence) {
				isOk &= inRange(NumUtils.toInt(hour, -1));
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
		
		// [小时]字段的子表达式为: [*] 时, 比[小时]大的时间单位不再有参考意义
		if(EVERY.equals(subExpression)) {
			cron.Day()._setSubExpression(EVERY);
			cron.Month()._setSubExpression(EVERY);
			cron.Week()._setSubExpression(NONE);
			cron.Year()._setSubExpression(EVERY);
		}
		
		// 比[小时]小的时间单位若为 [*] 则自动变成 [最小值]
		if(cron.Second().isEvery()) { cron.Second()._setSubExpression(_Second.MIN); }
		if(cron.Minute().isEvery()) { cron.Minute()._setSubExpression(_Minute.MIN); }
	}

}

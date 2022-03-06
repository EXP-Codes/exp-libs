package exp.libs.warp.task.cron;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.ListUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * cron表达式的中[月份]字段的子表达式对象.
 * ----------------------------
 * cron表达式共有7个时间字段, 依次为:
 * 	秒, 分, 时, 日, 月, 周, 年
 * ----------------------------
 * 每个时间字段的取值范围如下:<br/>
+--------------------+---------------------------+------------------+
| [时间字段]         | [取值范围]                | [允许的特殊字符] |
+--------------------+---------------------------+------------------+
| 秒(Second)         | 0-59                      | , - * /          |
| 分(month)         | 0-59                      | , - * /          |
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
public class _Month extends __TimeUnit {

	// 月份英文缩写
	public final static String JAN = "JAN";
	public final static String FEB = "FEB";
	public final static String MAR = "MAR";
	public final static String APR = "APR";
	public final static String MAY = "MAY";
	public final static String JUN = "JUN";
	public final static String JUL = "JUL";
	public final static String AUG = "AUG";
	public final static String SEP = "SEP";
	public final static String OCT = "OCT";
	public final static String NOV = "NOV";
	public final static String DEC = "DEC";
	
	/** 月份枚举表 */
	private final static Map<String, Integer> MONTHS = new HashMap<String, Integer>(); 
	static {
		MONTHS.put(JAN, 1);		MONTHS.put("1", 1);
		MONTHS.put(FEB, 2);		MONTHS.put("2", 2);
		MONTHS.put(MAR, 3);		MONTHS.put("3", 3);
		MONTHS.put(APR, 4);		MONTHS.put("4", 4);
		MONTHS.put(MAY, 5);		MONTHS.put("5", 5);
		MONTHS.put(JUN, 6);		MONTHS.put("6", 6);
		MONTHS.put(JUL, 7);		MONTHS.put("7", 7);
		MONTHS.put(AUG, 8);		MONTHS.put("8", 8);
		MONTHS.put(SEP, 9);		MONTHS.put("9", 9);
		MONTHS.put(OCT, 10);	MONTHS.put("10", 10);
		MONTHS.put(NOV, 11);	MONTHS.put("11", 11);
		MONTHS.put(DEC, 12);	MONTHS.put("12", 12);
	}
	
	/** [月份] 的最小值 */
	public final static int MIN = 1;
	
	/** [月份] 的最大值 */
	public final static int MAX = 12;
	
	/**
	 * 构造函数
	 * @param cron 所属的Cron表达式对象
	 */
	protected _Month(Cron cron) {
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
	 * 设置为 [from-to] 月份内触发
	 * @param from 开始月份(包括)
	 * @param to 结束月份(包括)
	 * @return 月份时间字段的表达式
	 */
	public String withRange(String from, String to) {
		Integer _from = MONTHS.get(from);
		Integer _to = MONTHS.get(from);
		return !(_from != null && _to != null && _from <= _to) ? getSubExpression() : 
			setSubExpression(StrUtils.concat(from, RANGE, to));
	}
	
	/**
	 * 设置为若干个月份触发
	 * @param months 月份列表
	 * @return 月份时间字段的表达式
	 */
	public String withSequence(String... months) {
		if(ListUtils.isNotEmpty(months)) {
			boolean isOk = true;
			for(String month : months) {
				isOk &= inRange(month);
			}
			
			if(isOk == true) {
				List<String> list = Arrays.asList(months);
				ListUtils.removeDuplicate(list);	// 去重
				setSubExpression(StrUtils.concat(list, SEQUENCE));
			}
		}
		return getSubExpression();
	}
	
	/**
	 * 设置为从 from 月份开始, 每间隔 interval 个月触发
	 * @param from 起始月份
	 * @param interval 间隔月数
	 * @return 月份时间字段的表达式
	 */
	public String withStep(String from, int interval) {
		Integer _from = MONTHS.get(from);
		return !(_from != null && _checkRange(interval)) ? getSubExpression() : 
				setSubExpression(StrUtils.concat(_from, STEP, interval));
	}
	
	/**
	 * 检查[月份]值是否在合法范围内
	 * @param month 月份值
	 * @return 月份时间字段的表达式
	 */
	private boolean inRange(int month) {
		return (MIN <= month && month <= MAX);
	}
	
	/**
	 * 检查[月份]值是否在合法范围内
	 * @param month 月份值
	 * @return true:范围内; false:范围外
	 */
	private boolean inRange(String month) {
		return MONTHS.containsKey(month);
	}

	@Override
	protected boolean _checkRange(int... values) {
		boolean isOk = (values != null && values.length > 0);
		if(isOk == true) {
			for(int month : values) {
				isOk &= inRange(month);
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
				StrUtils.concat("^\\d+(", SEQUENCE, "\\d+){0,", MAX, "}$"))) {
			String[] sequence = subExpression.split(SEQUENCE);
			isOk = true;
			for(String month : sequence) {
				isOk &= inRange(NumUtils.toInt(month, -1));
			}
			
		// 子表达式为: [x] 或 [ a,b,c,...,n] (月份英文缩写)
		} else if(subExpression.matches(
				StrUtils.concat("^[a-zA-Z]{3}(", SEQUENCE, "[a-zA-Z]{3}){0,", MAX, "}$"))) {
			String[] sequence = subExpression.split(SEQUENCE);
			isOk = true;
			for(String month : sequence) {
				isOk &= inRange(month);
			}
			
		// 子表达式为: [x-y]
		} else if(subExpression.matches(
				StrUtils.concat("^\\d+", RANGE, "\\d+$"))) {
			String[] range = subExpression.split(RANGE);
			int from = NumUtils.toInt(range[0], -1);
			int to = NumUtils.toInt(range[1], -1);
			isOk = inRange(from) && inRange(to);
			
		// 子表达式为: [x-y] (月份英文缩写)
		} else if(subExpression.matches(
				StrUtils.concat("^[a-zA-Z]{3}", RANGE, "[a-zA-Z]{3}$"))) {
			String[] range = subExpression.split(RANGE);
			String from = range[0];
			String to = range[1];
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
		
		// [月份]字段的子表达式为: [*] 时, 比[月份]大的时间单位不再有参考意义
		if(EVERY.equals(subExpression)) {
			cron.Year()._setSubExpression(EVERY);
		}
		
		// 比[月份]小的时间单位若为 [*] 则自动变成 [最小值]
		if(cron.Second().isEvery()) { cron.Second()._setSubExpression(_Second.MIN); }
		if(cron.Minute().isEvery()) { cron.Minute()._setSubExpression(_Minute.MIN); }
		if(cron.Hour().isEvery()) { cron.Hour()._setSubExpression(_Hour.MIN); }
		if(cron.Day().isEvery()) { cron.Day()._setSubExpression(_Day.MIN); }
		if(cron.Week().isEvery()) { cron.Week()._setSubExpression(_Week.MIN); }
	}

}

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
 * cron表达式的中[星期]字段的子表达式对象.
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
| 日期(DayOfweek)   | 1-31                      | , - * / ? L W C  |
| 星期(week)        | 1-12(或JAN-DEC)           | , - * /          |
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
public class _Week extends __TimeUnit {

	// 星期英文缩写
	public final static String SUN = "SUN";
	public final static String MON = "MON";
	public final static String TUE = "TUE";
	public final static String WED = "WED";
	public final static String THU = "THU";
	public final static String FRI = "FRI";
	public final static String SAT = "SAT";
	
	/** 星期枚举表 */
	private final static Map<String, Integer> WEEKS = new HashMap<String, Integer>(); 
	static {
		WEEKS.put(SUN, 1);		WEEKS.put("1", 1);
		WEEKS.put(MON, 2);		WEEKS.put("2", 2);
		WEEKS.put(TUE, 3);		WEEKS.put("3", 3);
		WEEKS.put(WED, 4);		WEEKS.put("4", 4);
		WEEKS.put(THU, 5);		WEEKS.put("5", 5);
		WEEKS.put(FRI, 6);		WEEKS.put("6", 6);
		WEEKS.put(SAT, 7);		WEEKS.put("7", 7);
	}
	
	/** [星期] 的最小值 */
	public final static int MIN = 1;
	
	/** [星期] 的最大值 */
	public final static int MAX = 7;
	
	/**
	 * 构造函数
	 * @param cron 所属的Cron表达式对象
	 */
	protected _Week(Cron cron) {
		super(cron);
		reset();
	}
	
	/**
	 * 重置子表达式的初始值
	 */
	protected void reset() {
		_setSubExpression(NONE);
	}

	/**
	 * 设置为"无意义"值, 使得该值对最终的cron规则无约束影响
	 * @return
	 */
	public String withNone() {
		return setSubExpression(NONE);
	}
	
	/**
	 * 设置值为周六（星期的最后一天）
	 * @return
	 */
	public String withLastDay() {
		return setSubExpression(L);
	}
	
	/**
	 * 设置为: 日历中周week后的第一天
	 * @param week
	 * @return
	 */
	public String withAfterWeek(int week) {
		return !_checkRange(week) ? getSubExpression() : 
			setSubExpression(StrUtils.concat(week, C));
	}
	
	/**
	 * 设置为: 日历中周week后的第一天
	 * @param week
	 * @return
	 */
	public String withAfterWeek(String week) {
		Integer _week = WEEKS.get(week);
		return (_week == null) ? getSubExpression() : 
			setSubExpression(StrUtils.concat(_week, C));
	}
	
	/**
	 * 设置为 [from-to] 星期内触发
	 * @param from 开始星期(包括)
	 * @param to 结束星期(包括)
	 * @return
	 */
	public String withRange(String from, String to) {
		Integer _from = WEEKS.get(from);
		Integer _to = WEEKS.get(from);
		return !(_from != null && _to != null && _from <= _to) ? getSubExpression() : 
			setSubExpression(StrUtils.concat(from, RANGE, to));
	}
	
	/**
	 * 设置为若干个星期触发
	 * @param WEEKS 星期列表
	 * @return
	 */
	public String withSequence(String... WEEKS) {
		if(ListUtils.isNotEmpty(WEEKS)) {
			boolean isOk = true;
			for(String week : WEEKS) {
				isOk &= inRange(week);
			}
			
			if(isOk == true) {
				List<String> list = Arrays.asList(WEEKS);
				ListUtils.removeDuplicate(list);	// 去重
				setSubExpression(StrUtils.concat(list, SEQUENCE));
			}
		}
		return getSubExpression();
	}
	
	/**
	 * 设置为从 from 星期开始, 每间隔 interval 个星期触发
	 * @param from 起始星期
	 * @param interval 间隔星期数
	 * @return
	 */
	public String withStep(String from, int interval) {
		Integer _from = WEEKS.get(from);
		return !(_from != null && _checkRange(interval)) ? getSubExpression() : 
				setSubExpression(StrUtils.concat(_from, STEP, interval));
	}
	
	/**
	 * 设置为某月 第index个星期week
	 * @param week 星期
	 * @param index 第index个
	 * @return
	 */
	public String withOrder(int week, int index) {
		return !(inRange(week) && index > 0) ? getSubExpression() : 
			setSubExpression(StrUtils.concat(week, ORDER, index));
	}
	
	/**
	 * 设置为某月 第index个星期week
	 * @param week 星期
	 * @param index 第index个
	 * @return
	 */
	public String withOrder(String week, int index) {
		Integer _week = WEEKS.get(week);
		return !(_week != null && index > 0) ? getSubExpression() : 
			setSubExpression(StrUtils.concat(_week, ORDER, index));
	}
	
	/**
	 * 检查[星期]值是否在合法范围内
	 * @param week
	 * @return
	 */
	private boolean inRange(int week) {
		return (MIN <= week && week <= MAX);
	}
	
	/**
	 * 检查[星期]值是否在合法范围内
	 * @param week
	 * @return
	 */
	private boolean inRange(String week) {
		return WEEKS.containsKey(week);
	}

	@Override
	protected boolean _checkRange(int... values) {
		boolean isOk = (values != null && values.length > 0);
		if(isOk == true) {
			for(int week : values) {
				isOk &= inRange(week);
			}
		}
		return isOk;
	}

	@Override
	protected boolean _checkSubExpression(String subExpression) {
		boolean isOk = false;
		
		// 子表达式为: [*] 或 [?] 或 [L] 
		if(EVERY.equals(subExpression) || 
				NONE.equals(subExpression) || 
				L.equals(subExpression)) {
			isOk = true;
		
		// 子表达式为: [xC]
		} else if(subExpression.matches(StrUtils.concat("^\\d+", C, "$"))) {
			String sWeek = subExpression.replace(C, "");
			int week = NumUtils.toInt(sWeek, -1);
			isOk = inRange(week);
						
		// 子表达式为: [x] 或 [ a,b,c,...,n]
		} else if(subExpression.matches(
				StrUtils.concat("^\\d+(", SEQUENCE, "\\d+){0,", MAX, "}$"))) {
			String[] sequence = subExpression.split(SEQUENCE);
			isOk = true;
			for(String week : sequence) {
				isOk &= inRange(NumUtils.toInt(week, -1));
			}
			
		// 子表达式为: [x] 或 [ a,b,c,...,n] (星期英文缩写)
		} else if(subExpression.matches(
				StrUtils.concat("^[a-zA-Z]{3}(", SEQUENCE, "[a-zA-Z]{3}){0,", MAX, "}$"))) {
			String[] sequence = subExpression.split(SEQUENCE);
			isOk = true;
			for(String week : sequence) {
				isOk &= inRange(week);
			}
			
		// 子表达式为: [x-y]
		} else if(subExpression.matches(
				StrUtils.concat("^\\d+", RANGE, "\\d+$"))) {
			String[] range = subExpression.split(RANGE);
			int from = NumUtils.toInt(range[0], -1);
			int to = NumUtils.toInt(range[1], -1);
			isOk = inRange(from) && inRange(to);
			
		// 子表达式为: [x-y] (星期英文缩写)
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
			
		// 子表达式为: [x#y]
		} else if(subExpression.matches(
				StrUtils.concat("^\\d+", ORDER, "\\d+$"))) {
			String[] order = subExpression.split(ORDER);
			int week = NumUtils.toInt(order[0], -1);
			int index = NumUtils.toInt(order[1], -1);
			isOk = inRange(week) && index > 0;
		
		}
		return isOk;
	}

	@Override
	protected void _trigger(Cron cron, String subExpression) {
		
		// [星期]字段的子表达式为: [*] 时, 比[星期]大的时间单位不再有参考意义
		if(EVERY.equals(subExpression)) {
			cron.Day()._setSubExpression(NONE);
			cron.Year()._setSubExpression(EVERY);
			
		// [星期]字段的子表达式为"有效值"时, [日期]字段的子表达式为要变成"无效值"
		} else if(!NONE.equals(subExpression)) {
			cron.Day()._setSubExpression(NONE);
			
		}
		
		// 比[星期]小的时间单位若为 [*] 则自动变成 [最小值]
		if(cron.Second().isEvery()) { cron.Second()._setSubExpression(_Second.MIN); }
		if(cron.Minute().isEvery()) { cron.Minute()._setSubExpression(_Minute.MIN); }
		if(cron.Hour().isEvery()) { cron.Hour()._setSubExpression(_Hour.MIN); }
	}
	
}

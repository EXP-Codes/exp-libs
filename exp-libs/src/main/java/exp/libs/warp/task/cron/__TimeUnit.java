package exp.libs.warp.task.cron;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import exp.libs.utils.other.ListUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * cron表达式的中某个时间字段的子表达式.
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
abstract class __TimeUnit {

	/**
	 * <PRE>
	 * 特殊值：星号(*), 表示 "每..."。
	 * 可用在所有字段中，表示对应时间域的每一个时刻，例如， 在分钟字段时，表示"每分钟"
	 * </PRE>
	 */
	protected final static String EVERY = "*";
	
	/**
	 * <PRE>
	 * 特殊值：问号(?)：表示"无意义"的值, 亦即为"非参考值", 取任何值均对表达式规则无影响。
	 * 只在日期和星期字段中使用（这两个字段由于存在冲突, 只要其中一个有具体值, 另一个必为 ?值）
	 * </PRE>
	 */
	protected final static String NONE = "?";
	
	/**
	 * <PRE>
	 * 特殊值：减号(-)：表达一个范围。
	 * 如在小时字段中使用"10-12", 则表示从10到12点
	 * </PRE>
	 */
	protected final static String RANGE = "-";
	
	/**
	 * <PRE>
	 * 特殊值：逗号(,)：表达一个列表值。
	 * 如在星期字段中使用"MON,WED,FRI", 则表示星期一，星期三和星期五
	 * </PRE>
	 */
	protected final static String SEQUENCE = ",";
	
	/**
	 * <PRE>
	 * 特殊值：斜杠(/)： x/y 表示一个等步长序列, x为起始值, y为增量步长值。
	 * 如在分钟字段中使用0/15, 则表示为0,15,30和45分钟;
	 * 而在分钟字段中使用5/15, 则表示5,20,35,50分钟;
	 * 注意：  "0 / y" 等价于 "* / y"
	 * </PRE>
	 */
	protected final static String STEP = "/";
	
	/**
	 * <PRE>
	 * 特殊值：井号(#)：x#y 表示第y个星期x。
	 * 该字符只能在星期字段中使用，如: 
	 * 	6#3表示当月的第三个星期五(6表示星期五，#3表示当前的第三个);
	 * 	而4#5表示当月的第五个星期三，假设当月没有第五个星期三，忽略不触发。
	 * --------------------------------------------------
	 * 注意： 星期字段的取值范围为 1-7 或 SUN-SAT （其中1=SUN）
	 * </PRE>
	 */
	protected final static String ORDER = "#";
	
	/**
	 * <PRE>
	 * 特殊值：L: 表示"Last", 该字符只能在日期和星期字段中使用。
	 *  在日期字段中, 表示这个月份的最后一天, 如一月的31号, 非闰年二月的28号;
	 *  在星期字段中, 表示星期六, 等同于7。
	 *  
	 * 特别地，如果L出现在星期字段里，而且在前面有一个数值X, 则表示"这个月的最后的周X", 例如: 6L表示该月的最后一个星期五.
	 * L 只能指定单一日期/星期, 而不能指定日期/星期范围。
	 * </PRE>
	 */
	protected final static String L = "L";
	
	/**
	 * <PRE>
	 * 特殊值：W: 该字符只能在日期字段中使用, 表示离该日期最近的"工作日"。
	 * 	例如: 15W表示离该月15号最近的工作日，
	 * 		如果该月15号是星期六, 则匹配14号星期五;
	 * 		如果15日是星期日, 则匹配16号星期一;
	 * 		如果15号是星期二, 那结果就是15号星期二。
	 * 
	 * 但必须注意关联的匹配日期不能够跨月，如你指定1W，如果1号是星期六，结果匹配的是3号星期一，而非上个月最后的那天。
	 * W 只能指定单一日期, 而不能指定日期范围。
	 * 
	 * 特别地, 在日期字段可以组合使用LW，它的意思是当月的最后一个工作日。
	 * </PRE>
	 */
	protected final static String W = "W";
	
	/**
	 * 特殊值：LW: 表示某月的最后一个"工作日"。
	 * 该字符只能在日期字段中使用。
	 */
	protected final static String LW = L.concat(W);
	
	/**
	 * <PRE>
	 * 特殊值：C: 表示"Calendar", 该字符只在日期和星期字段中使用。
	 * 	它用于修饰前导时间值, 表示日历在该时间点之后的第一天。
	 * 
	 * 	例如: 在日期字段中, 5C就相当于"日历5日以后的第一天"。
	 * 		在星期字段中, 1C相当于"星期日后的第一天"。
	 * </PRE>
	 */
	protected final static String C = "C";
	
	/**
	 * <PRE>
	 * 特殊值：空值: 该字符只在年份字段中使用。
	 * 	年份字段是可选字段, 可不设值。
	 * </PRE>
	 */
	protected final static String EMPTY = "";
	
	/** 空格符，用于分割每个字段的子表达式 */
	protected final static String SPACE = " ";
	
	/** 当前时间字段的子表达式 */
	private String subExpression;
	
	/** 所属的Cron表达式对象 */
	private Cron cron;
	
	/**
	 * 构造函数
	 * @param cron 所属的Cron表达式对象
	 */
	protected __TimeUnit(Cron cron) {
		this.cron = cron;
		this.subExpression = "";
	}
	
	/**
	 * 当前时间字段的值是否为 [*]
	 * @return true:是; false:否
	 */
	protected boolean isEvery() {
		return EVERY.equals(getSubExpression());
	}
	
	/**
	 * 当前时间字段的值是否为 [?]
	 * @return true:是; false:否
	 */
	protected boolean isNone() {
		return NONE.equals(getSubExpression());
	}
	
	/**
	 * 当前时间字段的值为 [*]
	 * @return 当前时间字段的子表达式
	 */
	public String withEvery() {
		return setSubExpression(EVERY);
	}
	
	/**
	 * 设置当前时间字段的值为 [某个时间点]
	 * @param timePoint 指定时间点
	 * @return 当前时间字段的子表达式
	 */
	public String withTimePoint(int timePoint) {
		return withSequence(timePoint);
	}
	
	/**
	 * 设置当前时间字段的值为 [from-to] 的时间范围
	 * @param from 开始时间点(包括)
	 * @param to 结束时间点(包括)
	 * @return 当前时间字段的子表达式
	 */
	public String withRange(int from, int to) {
		return !(from <= to && _checkRange(from, to)) ? subExpression : 
			setSubExpression(StrUtils.concat(from, RANGE, to));
	}
	
	/**
	 * 设置当前时间字段的值为 [若干个时间点序列]
	 * @param sequence 指定的时间点序列
	 * @return 当前时间字段的子表达式
	 */
	public String withSequence(int... sequence) {
		if(sequence != null && _checkRange(sequence)) {
			List<Integer> list = new LinkedList<Integer>();
			for(int e : sequence) {
				list.add(e);
			}
			ListUtils.removeDuplicate(list);	// 去重
			Collections.sort(list);	// 排序
			
			setSubExpression(StrUtils.concat(list, SEQUENCE));
		}
		return subExpression;
	}
	
	/**
	 * 设置当前时间字段的值为 [from/interval] 的时间步长
	 * @param from 开始时间点(包括)
	 * @param interval 步长
	 * @return 当前时间字段的子表达式
	 */
	public String withStep(int from, int interval) {
		return !_checkRange(from) || interval <= 0 ? subExpression : 
				setSubExpression(StrUtils.concat(from, STEP, interval));
	}

	/**
	 * 设置当前时间字段的子表达式的值(会触发内置触发器, 影响相关时间字段的值)
	 * @param subExpression cron子表达式
	 * @return 当前时间字段的子表达式
	 */
	public String setSubExpression(String subExpression) {
		if(subExpression != null) {
			subExpression = StrUtils.trimAll(subExpression);
			if(_checkSubExpression(subExpression)) {
				_setSubExpression(subExpression);
				
				if(cron != null) {	// 构造函数时不触发
					_trigger(cron, getSubExpression());
				}
			}
		}
		return getSubExpression();
	}
	
	/**
	 * 强制设置当前时间字段的子表达式的值（不会触发触发器）
	 * @param subExpression cron子表达式
	 * @return 当前时间字段的子表达式
	 */
	protected String _setSubExpression(String subExpression) {
		this.subExpression = subExpression;
		return this.subExpression;
	}
	
	/**
	 * 强制设置当前时间字段的子表达式的值为 [某个时间点]（不会触发触发器）
	 * @param timePoint 指定时间点
	 * @return 当前时间字段的子表达式
	 */
	protected String _setSubExpression(int timePoint) {
		this.subExpression = String.valueOf(timePoint);
		return this.subExpression;
	}
	
	/**
	 * 检查子表达式的数字取值是否在范围内
	 * @param values 数字值列表
	 * @return true:在范围内; false:不在范围内
	 */
	protected abstract boolean _checkRange(int... values);
	
	/**
	 * 校验设置的子表达式是否合法
	 * @param subExpression 准备设值的cron子表达式
	 * @return true:合法; false:非法
	 */
	protected abstract boolean _checkSubExpression(String subExpression);
	
	/**
	 * 当前字段的子表达式发生变化时的触发器（使得其他时间字段可以对应发生变化）
	 * @param cron 所属的Cron表达式对象
	 * @param subExpression 刚设值的cron子表达式
	 */
	protected abstract void _trigger(Cron cron, String subExpression);
	
	/**
	 * 获取当前时间字段的子表达式
	 * @return 当前时间字段的子表达式
	 */
	public String getSubExpression() {
		return subExpression;
	}
	
	@Override
	public String toString() {
		return getSubExpression();
	}
	
}

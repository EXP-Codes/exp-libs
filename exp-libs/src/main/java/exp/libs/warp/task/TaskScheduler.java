package exp.libs.warp.task;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.num.IDUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.task.cron.Cron;

/**
 * <PRE>
 * 定时任务调度管理器
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-06-28
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TaskScheduler {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(TaskScheduler.class);
	
	/** 默认计划组(即任务触发器) */
	private final static String DEFAULT_TRIGGER_GROUP = "DEFAULT_TRIGGER_GROUP";
	
	/** 默认工作组(即任务执行器) */
	private final static String DEFAULT_JOB_GROUP = "DEFAULT_JOB_GROUP";
	
	/** 触发器组组名 */
	private String triggerGroupName;
	
	/** 执行器组组名 */
	private String jobGroupName;
	
	/** 任务调度管理器 */
	private SchedulerFactory schedulerFactory;
	
	/** 任务名称集 */
	private Set<String> taskNames;
	
	/**
	 * 构造函数
	 */
	public TaskScheduler() {
		this(DEFAULT_TRIGGER_GROUP, DEFAULT_JOB_GROUP);
	}
	
	/**
	 * 构造函数
	 * @param triggerGroupName 触发器组组名
	 * @param jobGroupName 执行器组组名
	 */
	public TaskScheduler(String triggerGroupName, String jobGroupName) {
		this.triggerGroupName = (triggerGroupName == null ? 
				DEFAULT_TRIGGER_GROUP : triggerGroupName);
		this.jobGroupName = (jobGroupName == null ? 
				DEFAULT_JOB_GROUP : jobGroupName);
		
		this.schedulerFactory = new StdSchedulerFactory();
		this.taskNames = new HashSet<String>();
	}
	
	/**
	 * <PRE>
	 * 启动任务调度器.
	 * ----------------------
	 *  注意：调度器 [启动前] 或 [启动后] 均可添加任务.
	 * </PRE>
	 */
	public boolean _start() {
		boolean isOk = true;
		try {
			
			// 若调度器已被shutdown, 会返回一个新的调度器实例
			Scheduler scheduler = schedulerFactory.getScheduler();
			
			// 检查调度器是否已被启动过(含shutdown状态)
			if(!scheduler.isStarted()) {	
				scheduler.start();
			}
			
		} catch (Exception e) {
			isOk = false;
			log.error("启动计划任务调度器失败", e);
		}
		return isOk;
	}
	
	/**
	 * <PRE>
	 * 停止任务调度器.
	 * ----------------------
	 *  注意：调度器停止后, 之前已添加的所有计划任务会同时被移除.
	 * </PRE>
	 */
	public boolean _stop() {
		boolean isOk = true;
		try {
			
			// 若调度器已被shutdown, 会返回一个新的调度器实例
			Scheduler scheduler = schedulerFactory.getScheduler();
			
			// 检查调度器是否已被启动过(含shutdown状态)
			if(scheduler.isStarted()) {
				removeAll();
				scheduler.shutdown();
			}
			
		} catch (Exception e) {
			isOk = false;
			log.error("停止计划任务调度器失败", e);
		}
		return isOk;
	}
	
	/**
	 * 添加计划任务
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param cronExpression 定义任务触发时机的corn表达式
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(Job job, Map<?, ?> params, String cronExpression) {
		return add("", job, params, cronExpression);
	}
	
	/**
	 * 添加计划任务
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param cron 定义任务触发时机的corn规则
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(Job job, Map<?, ?> params, Cron cron) {
		return add("", job, params, cron);
	}
	
	/**
	 * 添加计划任务
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param cronExpression 定义任务触发时机的corn表达式
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(String taskName, Job job, 
			Map<?, ?> params, String cronExpression) {
		return add(taskName, job, params, new Cron(cronExpression));
	}
	
	
	/**
	 * 添加计划任务
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param cron 定义任务触发时机的corn规则
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(String taskName, Job job, 
			Map<?, ?> params, Cron cron) {
		boolean isOk = true;
		try {
			_add(taskName, job, params, cron);
			
		} catch (Exception e) {
			isOk = false;
			log.error("添加计划任务 [", taskName, "] 失败", e);
		}
		return isOk;
	}
	
	/**
	 * 添加计划任务
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param cron 定义任务触发时机的corn规则
	 * @throws Exception
	 */
	private void _add(String taskName, Job job, 
			Map<?, ?> params, Cron cron) throws Exception {
		if(job == null) {
			throw new RuntimeException("任务实现接口不能为空");
		}
		
		if(cron == null) {
			throw new RuntimeException("Cron规则不能为空");
		}
		
		if(StrUtils.isTrimEmpty(taskName)) {
			taskName = String.valueOf(IDUtils.getTimeID());
		}
		
		if(taskNames.contains(taskName)) {
			throw new RuntimeException(StrUtils.concat("任务 [", taskName, "] 已存在"));
		}
		
		// 调度器
		Scheduler scheduler = schedulerFactory.getScheduler();
		
		// 定义任务触发器
		CronTrigger trigger = TriggerBuilder.newTrigger().	
				withIdentity(taskName, triggerGroupName).
				withSchedule(CronScheduleBuilder.cronSchedule(cron.toExpression())).
				build();
				
		// 定义任务执行对象
		JobDetail jobDetail = JobBuilder.newJob().	
				ofType(job.getClass()).
				withIdentity(taskName, jobGroupName).
				usingJobData(new JobDataMap(params == null ? 
						new HashMap<Object, Object>() : params)).
				build();
		
		// 添加任务
		scheduler.scheduleJob(jobDetail, trigger);
		taskNames.add(taskName);
	}
	
	/**
	 * 添加计划任务
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param intervalMillis 任务执行间隔(单位: ms)
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(Job job, Map<?, ?> params, long intervalMillis) {
		return add("", job, params, intervalMillis, null, null);
	}
	
	/**
	 * 添加计划任务
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param intervalMillis 任务执行间隔(单位: ms)
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(String taskName, Job job, 
			Map<?, ?> params, long intervalMillis) {
		return add(taskName, job, params, intervalMillis, null, null);
	}
	
	/**
	 * 添加计划任务
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param intervalMillis 任务执行间隔(单位: ms)
	 * @param startAt 任务启动时间点(若为null表示立即执行)
	 * @param endAt 任务结束时间点(若为null表示无限期执行)
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(Job job, Map<?, ?> params, 
			long intervalMillis, Date startAt, Date endAt) {
		return add("", job, params, intervalMillis, startAt, endAt);
	}
	
	/**
	 * 添加计划任务
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param intervalMillis 任务执行间隔(单位: ms)
	 * @param startAt 任务启动时间点(若为null表示立即执行)
	 * @param endAt 任务结束时间点(若为null表示无限期执行)
	 * @return true:添加成功; false:添加失败
	 */
	public boolean add(String taskName, Job job, Map<?, ?> params, 
			long intervalMillis, Date startAt, Date endAt) {
		boolean isOk = true;
		try {
			_add(taskName, job, params, intervalMillis, startAt, endAt);
			
		} catch (Exception e) {
			isOk = false;
			log.error("添加计划任务 [", taskName, "] 失败", e);
		}
		return isOk;
	}
	
	/**
	 * 添加计划任务
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param intervalMillis 任务执行间隔(单位: ms)
	 * @param startAt 任务启动时间点(若为null表示立即执行)
	 * @param endAt 任务结束时间点(若为null表示无限期执行)
	 * @throws Exception
	 */
	private void _add(String taskName, Job job, Map<?, ?> params, 
			long intervalMillis, Date startAt, Date endAt) throws Exception {
		if(job == null) {
			throw new RuntimeException("任务实现接口不能为空");
		}
		
		if(StrUtils.isTrimEmpty(taskName)) {
			taskName = String.valueOf(IDUtils.getTimeID());
		}
		
		if(taskNames.contains(taskName)) {
			throw new RuntimeException(StrUtils.concat("任务 [", taskName, "] 已存在"));
		}
		
		if(startAt == null) {
			startAt = new Date();
		}
		
		// 调度器
		Scheduler scheduler = schedulerFactory.getScheduler();
		
		// 定义任务触发器
		SimpleTrigger trigger = TriggerBuilder.newTrigger().
				withIdentity(taskName, triggerGroupName).
				startAt(startAt).	// 启动时间点
				endAt(endAt).		// 终止时间点
				withSchedule(SimpleScheduleBuilder.simpleSchedule().
						withIntervalInMilliseconds(intervalMillis)).	// 执行间隔
				build();
				
		// 定义任务执行对象
		JobDetail jobDetail = JobBuilder.newJob().
				ofType(job.getClass()).
				withIdentity(taskName, jobGroupName).
				usingJobData(new JobDataMap(params == null ? 
						new HashMap<Object, Object>() : params)).
				build();
		
		// 添加任务
		scheduler.scheduleJob(jobDetail, trigger);
		taskNames.add(taskName);
	}
	
	/**
	 * 删除计划任务
	 * @param taskName 任务名称
	 * @return true:删除成功; false:删除失败
	 */
	public boolean remove(String taskName) {
		boolean isOk = true;
		try {
			_remove(taskName);
			
		} catch (Exception e) {
			isOk = false;
			log.error("删除计划任务 [", taskName, "] 失败", e);
		}
		return isOk;
	}
	
	/**
	 * 删除计划任务
	 * @param taskName 任务名称
	 * @throws Exception
	 */
	private void _remove(String taskName) throws Exception {
		if(StrUtils.isTrimEmpty(taskName)) {
			throw new RuntimeException("任务名称不能为空");
		}
		
		Scheduler scheduler = schedulerFactory.getScheduler();
		TriggerKey tk = new TriggerKey(taskName, triggerGroupName);
		JobKey jk = new JobKey(taskName, jobGroupName);
		
		if(!taskNames.contains(taskName)) {
			throw new RuntimeException(StrUtils.concat("任务 [", taskName, "] 不存在"));
		}
		
		scheduler.pauseTrigger(tk);		// 暂停触发器
		scheduler.pauseJob(jk);			// 暂停执行器
		scheduler.unscheduleJob(tk);	// 移除触发器
		scheduler.deleteJob(jk);		// 移除执行器
		taskNames.remove(taskName);
	}
	
	/**
	 * 删除所有计划任务(但不停止任务调度器)
	 */
	public void removeAll() {
		List<String> taskNames = new LinkedList<String>(this.taskNames);
		for(String taskName : taskNames) {
			remove(taskName);
		}
	}
	
	/**
	 * 修改已有的计划任务(若不存在则新增一个计划任务)
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param cronExpression 定义任务触发时机的corn表达式
	 * @return true:添加成功; false:添加失败
	 */
	public boolean modify(String taskName, Job job, 
			Map<?, ?> params, String cronExpression) {
		return modify(taskName, job, params, new Cron(cronExpression));
	}
	
	/**
	 * 修改已有的计划任务(若不存在则新增一个计划任务)
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param cron 定义任务触发时机的corn规则
	 * @return true:添加成功; false:添加失败
	 */
	public boolean modify(String taskName, Job job, 
			Map<?, ?> params, Cron cron) {
		boolean isOk = remove(taskName);
		if(isOk == true) {
			isOk = add(taskName, job, params, cron);
		}
		return isOk;
	}
	
	/**
	 * 修改已有的计划任务(若不存在则新增一个计划任务)
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param intervalMillis 任务执行间隔(单位: ms)
	 * @return true:添加成功; false:添加失败
	 */
	public boolean modify(String taskName, Job job, Map<?, ?> params, 
			long intervalMillis) {
		return modify(taskName, job, params, intervalMillis, null, null);
	}
	
	/**
	 * 修改已有的计划任务(若不存在则新增一个计划任务)
	 * @param taskName 任务名称
	 * @param job 任务执行接口(需提供org.quartz.Job 或 JobNonCurrent 的接口实现类)
	 * @param params 任务执行参数.
	 * 		[接口Job/JobNonCurrent] 的实现类可在 [execute方法] 中提取这些参数:
	 * 		JobExecutionContext.getJobDetail().getJobDataMap().get("key")
	 * @param intervalMillis 任务执行间隔(单位: ms)
	 * @param startAt 任务启动时间点(若为null表示立即执行)
	 * @param endAt 任务结束时间点(若为null表示无限期执行)
	 * @return true:添加成功; false:添加失败
	 */
	public boolean modify(String taskName, Job job, Map<?, ?> params, 
			long intervalMillis, Date startAt, Date endAt) {
		boolean isOk = remove(taskName);
		if(isOk == true) {
			isOk = add(taskName, job, params, intervalMillis, startAt, endAt);
		}
		return isOk;
	}
	
}

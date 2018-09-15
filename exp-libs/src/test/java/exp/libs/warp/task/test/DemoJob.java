package exp.libs.warp.task.test;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DemoJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap datas = context.getJobDetail().getJobDataMap();
		System.out.println(datas.get("EXP"));
	}

}

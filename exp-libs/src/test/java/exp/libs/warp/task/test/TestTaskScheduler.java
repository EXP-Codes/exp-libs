package exp.libs.warp.task.test;

import java.util.HashMap;
import java.util.Map;

import exp.libs.warp.task.TaskScheduler;
import exp.libs.warp.task.cron.Cron;

public class TestTaskScheduler {

	public static void main(String[] args) {
		DemoJob task = new DemoJob();
		
		Map<String, String> taskDatas = new HashMap<String, String>();
		taskDatas.put("EXP", "http://exp-blog.com");
		
		Cron cron = new Cron();
		cron.Second().withStep(5, 6);
		System.out.println(cron.toExpression());
		
		TaskScheduler ts = new TaskScheduler();
		ts.add("DEMO", task, taskDatas, cron);
		
		ts._start();
	}
	
}

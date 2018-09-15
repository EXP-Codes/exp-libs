package exp.libs.algorithm.struct.queue.sc.test;

import exp.libs.algorithm.struct.queue.sc.SCQBean;
import exp.libs.algorithm.struct.queue.sc.SCQueue;

public class TestSCQueue {

	/**
	 * demo
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		
		// 构造队列
		SCQueue<MyBean> queue = new SCQueue<MyBean>(1024);
		
		// 线程A顺序写入
		MyBean newBean = new MyBean(new MyElement("A"));
		queue.add(newBean);	// 写入的同时在队列内部并发处理
		
		// 线程B顺序取出
		SCQBean bean = queue.get();
		if(bean != null) {
			MyBean myBean = (MyBean) bean;
			System.out.println(myBean.getBean().getResult());	// 处理结果
		}
		
		// 释放资源，停止线程池
		queue.clear();
	}
	
}

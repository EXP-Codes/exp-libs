package exp.libs.algorithm.struct.queue.sc.test;

public class MyElement {	// 自定义类，包装原始对象以及处理后的结果

	private String s;	// 最原始的对象
	
	private String result;	// 存储处理结果

	public MyElement(String s) {
		this.s = s;
	}

	public String getS() {
		return s;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}

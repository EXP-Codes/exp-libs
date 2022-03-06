package exp.libs.warp.io.serial;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 批量序列化读取器(需配合写入器使用).
 * 	用于把外存序列化文件记录的多个序列化对象反序列化到内存.
 * 
 * 使用示例:
 * 	SerialFlowReader sfr = new SerialFlowReader(FILE_PATH);
 * 	while(sfr.hasNext()) {
 * 		Object obj = sfr.next();
 * 		// .....
 * 	}
 * 	sfr.close();
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-07-01
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SerialFlowReader extends _SerialFlow {

	/** 序列化文件 */
	private File file;
	
	/** 序列化输入流 */
	private ObjectInputStream ois;
	
	/** 当前得到的反序列化对象 */
	private Object obj;
	
	/** 序列化输入流是否已关闭 */
	private boolean closed;
	
	/**
	 * <PRE>
	 * 构造函数.
	 * 序列化文件使用默认位置: ./serializable.dat
	 * </PRE>
	 */
	public SerialFlowReader() {
		init(null);
	}
	
	/**
	 * 构造函数
	 * @param filePath 序列化文件存储位置
	 */
	public SerialFlowReader(String filePath) {
		init(StrUtils.isEmpty(filePath) ? 
				new File(DEFAULT_FILEPATH) : new File(filePath));
	}
	
	/**
	 * 构造函数
	 * @param file 序列化文件
	 */
	public SerialFlowReader(File file) {
		init(file);
	}
	
	/**
	 * 初始化
	 * @param file 序列化文件
	 */
	private void init(File file) {
		this.file = (file == null ? new File(DEFAULT_FILEPATH) : file);
		try {
			this.ois = new ObjectInputStream(new FileInputStream(this.file));
			this.closed = false;
			
		} catch (Exception e) {
			this.closed = true;
		}
		this.obj = null;
	}
	
	/**
	 * 检测是否还有下一个序列化对象
	 * @return true:有; false:无
	 */
	public boolean hasNext() {
		obj = get();
		return (obj != null);
	}
	
	/**
	 * 获取下一个序列化对象，将其反序列化到内存
	 * @return 反序列化对象
	 */
	public Object next() {
		return obj;
	}
	
	private Object get() {
		Object o = null;
		if(ois != null && closed == false) {
			try {
				o = ois.readObject();
				
			} catch (Exception e) {
				// Undo 通过捕获异常判定已到结尾
			}
		}
		return o;
	}
	
	/**
	 * 测试序列化输入流是否关闭
	 * @return true:关闭; false:未关闭
	 */
	public boolean isClosed() {
		return closed;
	}
	
	/**
	 * 关闭序列化输入流
	 * @return true:成功; false:写入
	 */
	public boolean close() {
		if(ois != null && closed == false) {
			try {
				ois.close();
				closed = true;
				
			} catch (Exception e) {
				log.error("关闭 SerialFlowReader 对象失败.", e);
			}
		}
		return closed;
	}

	/**
	 * 获取内存序列化文件对象
	 * @return 内存序列化文件对象
	 */
	public File getFile() {
		return file;
	}
	
}

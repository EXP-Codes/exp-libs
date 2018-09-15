package exp.libs.utils.os;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

import exp.libs.jvm.JVMAgent;

/**
 * <PRE>
 * JVM代理功能组件.
 * 
 *  注意：需要在启动脚本添加参数 -javaagent:./lib/jvm-agent.jar
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public final class JVMUtils {

	/** 私有化构造函数 */
	protected JVMUtils() {}
	
	/**
	 * <PRE>
	 * 直接计算当前对象占用空间大小.
	 * 
	 * 包括：
	 *  当前类及超类的基本类型实例字段大小
	 *  引用类型实例字段引用大小
	 *  实例基本类型数组总占用空间
	 *  实例引用类型数组引用本身占用空间大小
	 * 
	 * 但是不包括：
	 *  超类继承下来的和当前类声明的实例引用字段的对象本身的大小、
	 *  实例引用数组引用的对象本身的大小
	 * </PRE>
	 * @param o 当前对象
	 * @return 占用内存大小（byte） 若计算失败则返回 -1.
	 */
	public static long sizeOf(Object o) {
		if (JVMAgent.getInstn() == null) {
			System.err.println("无法访问 instrumentation 环境: "
					+ "请检查启动脚本是否包含参数 [\"-javaagent\"].");
			return -1;
		}
		return JVMAgent.getInstn().getObjectSize(o);
	}
	
	/**
	 * 递归计算当前对象占用空间总大小.
	 * 	(包括当前类和超类的实例字段大小以及实例字段引用对象大小)
	 * 
	 * @param obj 当前对象
	 * @return 占用内存大小（byte） 若计算失败则返回 -1.
	 */
	public static long fullSizeOf(Object obj) {
		long size = 0;
		try {
			size = _fullSizeOf(obj);
			
		} catch (Throwable e) {
			size = -1;
		}
		return size;
	}
	
	/**
	 * 递归计算当前对象占用空间总大小.
	 * 	(包括当前类和超类的实例字段大小以及实例字段引用对象大小)
	 * 
	 * 此方法可能会引起堆栈溢出.
	 * @param obj 当前对象
	 * @return 占用内存大小（byte）
	 */
	private static long _fullSizeOf(Object obj) {
		Map<Object, Object> visited = new IdentityHashMap<Object, Object>();	//已访问列表，比较key时是以 [引用相等] 为标准, 而非[对象相等]
		Stack<Object> stack = new Stack<Object>();	// 登记对象的内部对象的栈(只要对象嵌套层次不深、单个对象数组长度不大，栈可保证不会在计算大量对象时导致内存溢出)
		stack.push(obj);
		
		// 通过栈对对象内部进行遍历
		long size = 0;
		do  {
			Object o = stack.pop();
			
			// 跳过常量池对象、跳过已经访问过的对象
			if (!isSkipObject(o, visited)) {
				
				// 计算当前对象大小后，将其放入已访问列表
				size += sizeOf(o);
				visited.put(o, null);	
				
				// 若当前对象非数组， 收集其内部对象
				Class<?> clazz = o.getClass();
				if (!clazz.isArray()) {
					collectInternalObject(clazz, o, stack);	
					
				// 若当前对象是 数组，逐个计算其元素大小
				} else {
					int len = Array.getLength(o);
					
					// 基本类型数组，如 int[]
					if (clazz.getName().length() == 2) {
						if(len > 0) {
							Object e = Array.get(o, 0);
							size += sizeOf(e) * len;
						}
						
					// 对象类型数组，如 String[]
					} else {
						for (int i = 0; i < len; i++) {
							stack.add(Array.get(o, i));
						}
					}
				}
			}
		} while(!stack.isEmpty());
		visited.clear();
		return size;
	}
	
	/**
	 * 判定哪些对象是需要跳过不计算大小的
	 * @param obj
	 * @param visited
	 * @return
	 */
	private static boolean isSkipObject(Object obj, Map<Object, Object> visited) {
		if(obj == null) {
			return true;
		}
		
		if (obj instanceof String) {
			if (obj == ((String) obj).intern()) {
				return true;
			}
		}
		return visited.containsKey(obj);
	}

	/**
	 * 收集对象内部的非数组成员对象，并且可以通过父类进行向上搜索
	 * @param clazz
	 * @param obj
	 * @param stack
	 */
	private static void collectInternalObject(Class<?> clazz, Object obj, Stack<Object> stack) {
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			
			for (Field field : fields) {
				
				// 这里抛开静态属性
				if (!Modifier.isStatic(field.getModifiers())) {
					
					// 这里抛开基本关键字（因为基本关键字在调用java默认提供的方法就已经计算过了）
					if (field.getType().isPrimitive()) {
						continue;
						
					} else {
						field.setAccessible(true);
						try {
							Object o = field.get(obj);
							if (o != null) {
								stack.add(o);	// 将成员对象放入栈中，一遍弹出后继续检索
							}
						} catch (IllegalAccessException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
			clazz = clazz.getSuperclass();// 找父类class，直到没有父类
		}
	}
	
}

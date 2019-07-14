package exp.libs.utils.other;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.DateFormat;
import exp.libs.utils.io.IOUtils;
import exp.libs.utils.time.TimeUtils;

/**
 * <PRE>
 * 对象工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-02
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ObjUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(ObjUtils.class);
	
	/** 私有化构造函数 */
	protected ObjUtils() {}
	
	/**
	 * <PRE>
	 * 把String对象转换成其他实体对象.
	 * 	允许转换的对象包括：Integer、Long、BigInteger、Float、Double、Date、Timestamp
	 * </PRE>
	 * @param s String对象
	 * @param clazz 期望转换的对象类
	 * @return 允许转换的对象(不支持转换则返回String)
	 */
	public static Object toObj(String s, Class<?> clazz) {
		if(StrUtils.isEmpty(s)) {
			return (String.class == clazz ? "" : null);
		}
		
		Object o = s;
		String str = s.trim();
		if (Integer.class == clazz) {
			o = Integer.valueOf(str);
			
		} else if (Long.class == clazz) {
			o = Long.valueOf(str);
			
		} else if (BigInteger.class == clazz) {
			o = new BigInteger(str);
			
		} else if (Float.class == clazz) {
			o = Float.valueOf(str);
			
		} else if (Double.class == clazz) {
			o = Double.valueOf(str);
			
		} else if (Date.class == clazz) {
			o = TimeUtils.toDate(str);
			
		} else if (Timestamp.class == clazz) {
			o = TimeUtils.toTimestamp(TimeUtils.toDate(str));
		}
		return o;
	}
	
	/**
	 * <PRE>
	 * 把其他实体对象转换成String.
	 * 	(对于Date和Timestamp对象会返回 yyyy-MM-dd HH:mm:ss.SSS格式字符串)
	 * </PRE>
	 * @param o 被转换的实体对象
	 * @param clazz 被转换的实体对象类型
	 * @return String对象(若转换失败返回"")
	 */
	public static String toStr(Object o, Class<?> clazz) {
		String s = "";
		if(o == null) {
			return s;
		}
		
		if(clazz == null) {
			return toStr(o);
		}
		
		if (isSubclass(clazz, Number.class)) {
			s = String.valueOf(o);
			
		} else if (Date.class == clazz) {
			s = TimeUtils.toStr((Date) o, DateFormat.YMDHMSS);
			
		} else if (Timestamp.class == clazz) {
			Date date = TimeUtils.toDate((Timestamp) o);
			s = TimeUtils.toStr(date, DateFormat.YMDHMSS);
			
		} else {
			s = o.toString();
		}
		return s;
	}
	
	/**
	 * <PRE>
	 * 把其他实体对象转换成String.
	 * 	(对于Date和Timestamp对象会返回 yyyy-MM-dd HH:mm:ss.SSS格式字符串)
	 * </PRE>
	 * @param o 被转换的实体对象
	 * @return String对象(若转换失败返回"")
	 */
	public static String toStr(Object o) {
		String s = "";
		if(o == null) {
			return s;
		}
		
		if (o instanceof Number) {
			s = String.valueOf(o);
			
		} else if (o instanceof Date) {
			s = TimeUtils.toStr((Date) o, DateFormat.YMDHMSS);
			
		} else if (o instanceof Timestamp) {
			Date date = TimeUtils.toDate((Timestamp) o);
			s = TimeUtils.toStr(date, DateFormat.YMDHMSS);
			
		} else {
			s = o.toString();
		}
		return s;
	}
	
	/**
	 * 检查cClazz是否为fClazz的子类
	 * @param cClass (期望的)子类
	 * @param fClass (期望的)父类
	 * @return true:是; false:否
	 */
	public static boolean isSubclass(Class<?> cClass, Class<?> fClass) {
		boolean isChild = false;
		try {
			cClass.asSubclass(fClass);
			isChild = true;
			
		} catch (Exception e) {
			// 报错说明不是子类
		}
		return isChild;
	}
	
	/**
	 * <pre>
	 * 通过Serializable序列化方式深度克隆对象，
	 * 要求所克隆的对象及其下所有成员都要实现Serializable接口。
	 * 
	 * 因为java的[基本数据类型]是值传递，可以直接复制，
	 * 而其[包装类]（如String, Integer等）也都已经实现了Serializable接口，
	 * 因此对于一般的待克隆对象，实现Serializable接口后，直接使用即可。
	 * 
	 * 若待克隆对象下存在[引用数据类型]（如自定义的class），则要求它必须实现Serializable接口。
	 * </pre>
	 * @param serialObject 被克隆的对象(必须实现Serializable接口)
	 * @return 克隆的对象
	 */
	public static Object clone(Object serialObject) {
		Object newObj = null;
		if(serialObject == null) {
			return newObj;
		}
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(serialObject);
			out.close();
			
			ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bin);
			newObj = in.readObject();
			in.close();
			
		} catch (Exception e) {
			log.debug("克隆对象 [{}] 失败.", serialObject, e);
		}
		return newObj;
	}
	
	/**
	 * 实例化对象
	 * @param clazzPath 类路径, 如: foo.bar.Test （该类必须支持无参构造函数）
	 * @return 实例化对象（若失败则返回null）
	 */
	public static Object instanceClass(String clazzPath) {
		Object inst = null;
		try {
			Class<?> clazz = Class.forName(clazzPath);
			inst = clazz.newInstance();
		} catch(Exception e) {
			log.debug("实例化类 [{}] 失败", clazzPath, e);
		}
		return inst;
	}
	
	/**
	 * <PRE>
	 * 获取指定基类的所有子类.
	 * (由于java父类不清楚其下的子孙是什么, 此方式通过递归检索编译目录判断所有类之间的关联性,以确认父子关系.)
	 * </PRE>
	 * @param baseClass 基类
	 * @return 子类列表
	 */
	public static List<String> getAllChildClass(Class<?> baseClass) {
		String compilePath = PathUtils.getProjectCompilePath();	//根编译目录
		File rootDir = new File(compilePath);
		
		// 路径分隔符转换为包分隔符
		compilePath = compilePath.replaceAll("[\\\\|/]", ".");
		if(!compilePath.endsWith(".")) {
			compilePath = compilePath.concat(".");
		}
		
		List<String> childClazzs = new LinkedList<String>();
		searchChildClass(rootDir, compilePath, baseClass, childClazzs);
		return childClazzs;
	}
	
	/**
	 * 递归检索所有类，并通过父转子异常测试以获取指定基类的所有子类。
	 * 
	 * @param curFile 当前处理的文件类
	 * @param pathPrefix 路径前缀（包路径格式）
	 * @param baseClass 需查找子类的基类
	 * @param childClazzs 存储检索的子类列表（包路径格式）
	 */
	private static void searchChildClass(File curFile, String pathPrefix, 
			Class<?> baseClass, List<String> childClazzs) {
		if(childClazzs == null || pathPrefix == null) {
			return;
		}
		
		// 若是目录,向下递归
		if(curFile.isDirectory()) {
			File[] files = curFile.listFiles();
			for (File file : files) {
				searchChildClass(file, pathPrefix, baseClass, childClazzs);
			}
			
		// 若是类文件,判定处理
		} else if (curFile.getPath().endsWith(".class")) {
			try {
				String childClassName = curFile.getPath().
						replaceAll("[\\\\|/]", ".").//路径分隔符转换为包分隔符
						replace(pathPrefix, "").	//去前缀
						replace(".class", "");		//去后缀
				
				//基类不会是自身的子类
				if(childClassName.equals(baseClass.getName())) {
					// Undo
					
				// 实例化当前类,并尝试将指定基类做转换测试,只要不抛出异常则说明为父子关系
				} else {
					Class<?> childClass = Class.forName(childClassName);
					childClass.asSubclass(baseClass);
					childClazzs.add(childClassName);
				}
			} catch (ClassNotFoundException e) {
				// forName 类不存在
				
			} catch (ClassCastException e) {
				// asSubclass 非父子关系都会抛出此异常
			}
		}
	}
	
	/**
	 * <PRE>
	 * 把map转换成clazz类声明的Bean实例对象.
	 * 	(map的key为Bean的成员域，value为对应的成员值)
	 * </PRE>
	 * @param map KV表
	 * @param clazz Bean所属类(该类需支持无参构造函数)
	 * @return Bean对象(转换失败返回null)
	 */
	public static Object toBean(Map<String, Object> map, 
			Class<? extends Object> clazz) {
		if(map == null || clazz == null) {
			return null;
		}
		
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getMethods();
		Object obj = null;
		try {
			obj = clazz.newInstance();
			
		} catch (Exception e) {
			log.debug("构造 [{}] 实例失败.", clazz.getName(), e);
			return null;
		}
		
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				String propertyName = methodName.substring(3);
				
				String fieldName = null;
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					if(propertyName.equalsIgnoreCase(field.getName())) {
						fieldName = field.getName();
						break;
					}
				}
				
				// 利用setter方法对对应的成员域置值
				if (fieldName != null) {
					Object value = map.get(fieldName);
					try {
						method.invoke(obj, value);
					} catch (Exception e) {
						log.debug("[{}]: 为成员域 [{}] 置值失败.", 
								clazz.getName(), fieldName);
					}
				} else {
					log.warn("[{}]: 不存在属性值 [{}] 对应的成员域.", 
							clazz.getName(), propertyName);
				}
			}
		}
		return obj;
	}
	
	/**
	 * <PRE>
	 * 通过反射调用对象内部方法.
	 * 	(私有方法也可调用, 可用于单元测试)
	 * </PRE>
	 * @param instnOrClazz
	 *            如果是调用实例方法，该参数为实例对象，
	 *            如果调用静态方法，该参数为实例对象或对应类***.class
	 * @param methodName 调用的方法名
	 * @param paramVals 调用方法的参数
	 * @param valClazzs 调用方法的参数对应的类型类
	 * @return 调用结果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object instnOrClazz, String methodName,
			Object[] paramVals, Class[] valClazzs) {
		if(instnOrClazz == null || StrUtils.isEmpty(methodName)) {
			log.debug("反射调用方法失败: [{}.{}()], 无效的类或方法.", 
					instnOrClazz, methodName);
			return null;
		}
		
		Class clazz = (instnOrClazz instanceof Class ? 
				(Class) instnOrClazz : instnOrClazz.getClass());
		paramVals = (paramVals == null ? new Object[0] : paramVals);
		Class[] valTypes = (valClazzs == null ? new Class[0] : valClazzs);
		
		if(paramVals.length > valTypes.length) {
			if(valTypes.length <= 0) {
				valTypes = new Class[paramVals.length];
				for (int i = 0; i < paramVals.length; i++) {
					valTypes[i] = (paramVals[i] != null ? 
							paramVals[i].getClass() : Object.class);
				}
			} else {
				log.debug("反射调用方法失败: [{}.{}()], 入参与类型的个数不一致.", 
						clazz, methodName);
				return null;
			}
		}
		
		Object result = null;
		try {
			Method method = clazz.getDeclaredMethod(methodName, valTypes);
			method.setAccessible(true);	// 临时开放调用权限(针对private方法)
			result = method.invoke(instnOrClazz, paramVals);
			
		} catch (Exception e) {
			log.debug("反射调用方法失败: [{}.{}()]", clazz, methodName, e);
		}
		return result;
	}
	
	/**
	 * 生成Bean中的所有成员域的KV对信息（使用MULTI_LINE_STYLE风格）
	 * @param bean bean对象
	 * @return 所有成员域的KV对信息
	 */
	public static String toBeanInfo(Object bean) {
		return toBeanInfo(bean, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	/**
	 * 生成Bean中的所有成员域的KV对信息
	 * @param bean bean对象
	 * @param style 打印风格, 建议值 MULTI_LINE_STYLE
	 * @return 所有成员域的KV对信息
	 */
	public static String toBeanInfo(Object bean, ToStringStyle style) {
		String info = "";
		if(bean != null) {
			info = new ReflectionToStringBuilder(bean, style).toString();
		}
		return info;
	}
	
	/**
	 * 把内存对象序列化并保存到外存文件
	 * @param o 内存对象（需实现Serializable接口）
	 * @param outFilePath 外存序列化文件位置
	 * @return true:序列化成功; false:序列化失败
	 */
	public static boolean toSerializable(Serializable o, String outFilePath) {
		boolean isOk = false;
		if(o == null) {
			return isOk;
		}
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(outFilePath));
			oos.writeObject(o);
			oos.flush();
			oos.close();
			isOk = true;
			
		} catch (Exception e) {
			log.debug("序列化对象到外存文件失败: [{}]", outFilePath, e);
		}
		return isOk;
	}
	
	/**
	 * 反序列化外存文件，还原为内存对象
	 * @param inFilePath 外存序列化文件位置
	 * @return 内存对象(失败返回null)
	 */
	public static Object unSerializable(String inFilePath) {
		Object o = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(inFilePath));
			o = ois.readObject();
			ois.close();
			
		} catch (Exception e) {
			log.debug("从外存文件反序列化对象失败: [{}]", inFilePath, e);
		}
		return o;
	}
	
	/**
	 * 把内存对象序列化为byte[]字节数组
	 * @param object 内存对象（需实现Serializable接口）
	 * @return byte[]字节数组 （失败返回null）
	 */
	public static byte[] toSerializable(Serializable object) {
		byte[] bytes = null;
		if(object == null) {
			return bytes;
		}
		
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			bytes = baos.toByteArray();
			
		} catch (Exception e) {
			log.debug("序列化对象为字节数组失败", e);
			
		} finally {
			IOUtils.close(oos);
			IOUtils.close(baos);
		}
		return bytes;

	}

	/**
	 * 反序列化byte[]字节数组，还原为内存对象
	 * @param bytes 序列化的字节数组
	 * @return 内存对象(失败返回null)
	 */
	public static Object unSerializable(byte[] bytes) {
		Object object = null;
		if(bytes == null) {
			return object;
		}
		
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			object = ois.readObject();
			
		} catch (Exception e) {
			log.debug("从字节数组反序列化对象失败", e);
			
		} finally {
			IOUtils.close(ois);
			IOUtils.close(bais);
		}
		return object;
	}
	
}

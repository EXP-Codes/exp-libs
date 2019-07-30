package exp.libs.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.num.UnitUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * jar工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-02
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class JarUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(JarUtils.class);
	
	/** 私有化构造函数 */
	protected JarUtils() {}
	
	/**
	 * <PRE>
	 * 获取Jar文件的类加载器（用于动态加载jar文件中的类）.
	 * 
	 * 示例：
	 * 	File jarFile = new File(JAR_FILE_PATH);
	 * 	URLClassLoader load = JarUtils.getURLClassLoader(jarFile);
	 * 	Class<?> clazz = load.loadClass("foo.bar.TTest");
	 * 	ITest test = (ITest) clazz.newInstance();
	 * 	System.out.println(test.toString());
	 * </PRE>
	 * @param jarFilePath jar文件路径/jar文件目录路径
	 * @return 类加载器 
	 */
	public static URLClassLoader getURLClassLoader(String jarFilePath) {
		return getURLClassLoader(new File(jarFilePath));
	}
	
	/**
	 * <PRE>
	 * 获取Jar文件的类加载器（用于动态加载jar文件中的类）.
	 * 
	 * 示例：
	 * 	File jarFile = new File(JAR_FILE_PATH);
	 * 	URLClassLoader load = JarUtils.getURLClassLoader(jarFile);
	 * 	Class<?> clazz = load.loadClass("foo.bar.TTest");
	 * 	ITest test = (ITest) clazz.newInstance();
	 * 	System.out.println(test.toString());
	 * </PRE>
	 * @param jarFile jar文件/jar文件目录
	 * @return 类加载器（失败返回null） 
	 */
	public static URLClassLoader getURLClassLoader(File jarFile) {
		List<File> jarFiles = FileUtils.listFiles(jarFile, ".jar");
		URL[] tmpURLs = new URL[jarFiles.size()];
		
		int cnt = 0;
		for(int i = 0; i < tmpURLs.length; i++) {
			String jarPath = jarFiles.get(i).getAbsolutePath();
			String urlPath = "file:".concat(jarPath);
			try {
				tmpURLs[i] = new URL(urlPath);
				cnt++;
				
			} catch (MalformedURLException e) {
				tmpURLs[i] = null;
				log.error("动态加载Jar文件失败: [{}]", jarPath, e);
			}
		}
		
		URL[] URLs = new URL[cnt];
		for(int i = 0, j = 0; i < cnt; i++) {
			if(tmpURLs[j] == null) {
				j++;
			} else {
				URLs[i] = tmpURLs[j];
			}
		}
		URLClassLoader loader = new URLClassLoader(URLs);
		return loader;
	}

	/**
	 * 复制jar包内中的文件到磁盘
	 * @param packagePath 包内文件的包路径, 如: /foo/bar/test.txt
	 * @param snkPath 磁盘文件路径
	 * @return true:复制成功; false:复制失败
	 */
	public static boolean copyFile(String packagePath, String snkPath) {
		boolean isOk = false;
		InputStream is = FileUtils.class.getResourceAsStream(packagePath);
		File snkFile = FileUtils.createFile(snkPath);
		try {
			FileOutputStream fos = new FileOutputStream(snkFile);
			byte[] buff = new byte[UnitUtils._1_MB];
			int rc = 0;
			while ((rc = is.read(buff, 0, UnitUtils._1_MB)) > 0) {
				fos.write(buff, 0, rc);
			}
			fos.flush();
			fos.close();
			isOk = true;
			
		} catch (Exception e) {
			log.error("复制文件失败: 从 [{}] 到 [{}].", packagePath, snkPath, e);
		}
		IOUtils.close(is);
		return isOk;
	}
	
	/**
	 * 读取jar包中的文件
	 * @param packagePath 包内文件的包路径, 如: /foo/bar/test.txt
	 * @param charset 文件编码
	 * @return 文件内容
	 */
	public static String read(String packagePath, String charset) {
		InputStream is = FileUtils.class.getResourceAsStream(packagePath);
		String str = "";
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buff = new byte[UnitUtils._1_MB];
			int rc = 0;
			while ((rc = is.read(buff, 0, UnitUtils._1_MB)) > 0) {
				bos.write(buff, 0, rc);
			}
			byte[] arrByte = bos.toByteArray();
			str = new String(arrByte, charset);
			bos.close();
			
		} catch (Exception e) {
			log.error("读取Jar内文件失败: [{}]", packagePath, e);
		}
		IOUtils.close(is);
		return str;
	}
	
	/**
	 * 列举jar包内的文件清单
	 * @param jarFilePath jar文件的磁盘位置
	 * @return jar内文件清单
	 */
	public static List<String> listFiles(String jarFilePath) {
		return listFiles(jarFilePath, null);
	}

	/**
	 * 列举jar包内的文件清单
	 * @param jarFilePath jar文件的磁盘位置
	 * @param extension 文件后缀
	 * @return 后缀匹配的文件清单
	 */
	public static List<String> listFiles(String jarFilePath, String extension) {
		return listFiles(new File(jarFilePath), extension);
	}
	
	/**
	 * 列举jar包内的文件清单
	 * @param jarFile jar文件
	 * @param extension 文件后缀
	 * @return 后缀匹配的文件清单
	 */
	public static List<String> listFiles(File jarFile, String extension) {
		List<String> list = new LinkedList<String>();
		boolean isFilter = StrUtils.isNotEmpty(extension);
		try {
			JarFile jar = new JarFile(jarFile);
			Enumeration<JarEntry> envm = jar.entries();
			while (envm.hasMoreElements()) {
				JarEntry e = envm.nextElement();
				String path = e.getName();
				
				if(isFilter && path.toLowerCase().endsWith(extension.toLowerCase())) {
					list.add(path);
				}
			}
			jar.close();
		} catch (Exception e) {
			log.error("读取Jar内文件列表失败: [{}]", 
					(jarFile == null ? "null" : jarFile.getPath()), e);
		}
		return list;
	}
	
}

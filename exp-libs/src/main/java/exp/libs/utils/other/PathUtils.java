package exp.libs.utils.other;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.os.OSUtils;
import exp.libs.utils.verify.RegexUtils;

/**
 * <PRE>
 * 路径处理工具.
 * 	处理原则： 本工具类的所有函数返回值, 只要是路径, 就一定【不以】 分隔符 结尾.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class PathUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(PathUtils.class);
	
	/** 私有化构造函数 */
	protected PathUtils() {}
	
	/**
	 * 判断是否为全路径（即以根开始的路径）
	 * 
	 * @param path 路径
	 * @return true:全路径; false:非全路径
	 */
	public static boolean isFullPath(String path) {
		return isWinFullPath(path) || isUnixFullPath(path);
	}
	
	/**
	 * 判断是否为Windows的全路径（即以根开始的路径）
	 * 
	 * @param path 路径
	 * @return true:全路径; false:非全路径
	 */
	public static boolean isWinFullPath(String path) {
		return RegexUtils.matches(path, "^[A-Za-z]:[/|\\\\].*$");
	}
	
	/**
	 * 判断是否为Unix的全路径（即以根开始的路径）
	 * 
	 * @param path 路径
	 * @return true:全路径; false:非全路径
	 */
	public static boolean isUnixFullPath(String path) {
		return RegexUtils.matches(path, "^/.*$");
	}
	
	/**
	 * <PRE>
	 * 路径合并。
	 * <PRE>
	 * @param prefixPath 路径前缀，全路径或相对路径
	 * @param suffixPath 路径后缀，必须是相对路径
	 * @return 路径前缀 + 路径分隔符 + 路径后缀 
	 */
	public static String combine(String prefixPath, String suffixPath) {
		String combPath = "";
		if(StrUtils.isNotTrimEmpty(prefixPath, suffixPath)) {
			prefixPath = prefixPath.trim().replace('\\', '/');
			suffixPath = suffixPath.trim().replace('\\', '/');
			
			if(isWinFullPath(suffixPath)) {
				combPath = prefixPath;
				
			} else {
				if(!prefixPath.endsWith("/")) {
					prefixPath = prefixPath.concat("/");
				}
				combPath = StrUtils.concat(prefixPath, suffixPath);
				combPath = combPath.replace("/./", "/").replace("//", "/");
			}
			
		} else if(StrUtils.isNotTrimEmpty(prefixPath)) {
			combPath = suffixPath.trim();
			
		} else if(StrUtils.isNotTrimEmpty(suffixPath)) {
			combPath = prefixPath.trim();
		}
		
		combPath = combPath.replace('\\', '/');
		return combPath;
	}
	
	/**
	 * 获取文件的父目录
	 * @param filePath 文件路径
	 * @return 文件的父目录
	 */
	public static String getParentDir(String filePath) {
		String parentDir = "";
		try {
			parentDir = new File(filePath).getParent();
		} catch(Exception e) {
			parentDir = RegexUtils.findFirst(filePath, "(.*)[/\\\\]");
		}
		return parentDir;
	}
	
	/**
	 * 取得项目的绝对路径, 如: X:\foo\project
	 * @return 项目的绝对路径
	 */
	public static String getProjectPath() {
		String path = "";
		try {
			path = new File(".").getCanonicalPath();
		} catch (IOException e) {
			log.error("获取项目绝对路径失败.", e);
		}
		return path;
	}
	
	/**
	 * 获取 项目的根路径，如： X:\foo\project
	 * @return 项目的根路径
	 */
	public static String getProjectRootPath() {
		return System.getProperty("user.dir").concat(File.separator);
	}
	
	/**
	 * <PRE>
	 * 获取项目的编译目录的根路径。
	 *   非Tomcat项目形, 如： X:/foo/bar/project/target/classes
	 *   Tomcat项目形, 如:  %tomcat%/%wepapp%/%project%/classes
	 * <PRE>
	 * @return 项目的编译根路径
	 */
	public static String getProjectCompilePath() {
		return new File(PathUtils.class.getResource("/").getPath()).
				getAbsolutePath().concat(File.separator);
	}
	
	/**
	 * 获取 类的编译路径，如：X:/workspace/project/target/classes/foo/bar
	 * @param clazz 类
	 * @return 类的编译路径
	 */
	public static String getClassCompilePath(Class<?> clazz) {
		return new File(clazz.getResource("").getPath()).
				getAbsolutePath();
	}
	
	/**
	 * <PRE>
	 * 获取项目自身引用的所有jar包的路径。
	 * 只能获取运行main方法的项目所需要的jar包路径，而不能获取其他项目的jar包类路径。
	 * (如果是外部jdk调用，则返回的是 -cp 的参数表)
	 * </PRE>
	 * @return 项目引用的所有包的路径
	 */
	public static String[] getAllClassPaths() {
		return System.getProperty("java.class.path").split(";");
	}
	
	/**
	 * 把路径转换为运行平台的标准路径。
	 * @param paths 路径集
	 * @return 标准路径集
	 */
	public static List<String> toStandard(List<String> paths) {
		return (OSUtils.isWin() ? toWin(paths) : toLinux(paths));
	}
	
	/**
	 * 把路径转换为运行平台的标准路径。
	 * @param path 路径
	 * @return 标准路径
	 */
	public static String toStandard(String path) {
		return (OSUtils.isWin() ? toWin(path) : toLinux(path));
	}
	
	/**
	 * 把linux路径转换为win路径
	 * @param linuxPaths linux路径集
	 * @return win路径集
	 */
	public static List<String> toWin(List<String> linuxPaths) {
		List<String> winPaths = new LinkedList<String>();
		for(String linuxPath : linuxPaths) {
			winPaths.add(toWin(linuxPath));
		}
		return winPaths;
	}
	
	/**
	 * 把linux路径转换为win路径（仅对相对路径有效）
	 * @param linuxPath linux路径
	 * @return win路径
	 */
	public static String toWin(String linuxPath) {
		String winPath = "";
		if(linuxPath != null) {
			winPath = linuxPath.replace('/', '\\');
		}
		return winPath;
	}
	
	/**
	 * 把win路径转换为linux路径
	 * @param winPaths win路径集
	 * @return linux路径集
	 */
	public static List<String> toLinux(List<String> winPaths) {
		List<String> linuxPaths = new LinkedList<String>();
		for(String winPath : winPaths) {
			linuxPaths.add(toLinux(winPath));
		}
		return linuxPaths;
	}
	
	/**
	 * 把win路径转换为linux路径（仅对相对路径有效）
	 * @param winPath win路径
	 * @return linux路径
	 */
	public static String toLinux(String winPath) {
		String linuxPath = "";
		if(winPath != null) {
			linuxPath = winPath.replace('\\', '/');
		}
		return linuxPath;
	}
	
	/**
	 * 获取 [当前代码运行处] 的调用堆栈路径, 并用“|”将堆栈路径拼接起来。
	 * @return 用“|”连接的堆栈路径
	 */
	public static String getCallStackPath() {
		StringBuilder sb = new StringBuilder();
		try {
			throw new Exception();
			
		} catch (Exception e) {
			StackTraceElement[] stes = e.getStackTrace();

			for (StackTraceElement ste : stes) {
				sb.append(ste.getClassName());
				sb.append("(").append(ste.getFileName());
				sb.append(":").append(ste.getLineNumber());
				sb.append(")|");
			}
			
			if(stes.length > 0) {
				sb.setLength(sb.length() - 1);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取桌面路径
	 * @return 桌面路径
	 */
	public static String getDesktopPath() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		return fsv.getHomeDirectory().getPath();
	}
	
	/**
	 * 获取系统临时目录
	 * @return 系统临时目录
	 */
	public static String getSysTmpDir() {
		return System.getProperty("java.io.tmpdir");
	}
	
}

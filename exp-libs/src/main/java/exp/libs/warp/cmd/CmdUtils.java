package exp.libs.warp.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.Charset;
import exp.libs.envm.Delimiter;
import exp.libs.utils.os.OSUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 系统命令行操作工具.	
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class CmdUtils {
	
	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(CmdUtils.class);
	
	/** 私有化构造函数 */
	protected CmdUtils() {}
	
	/**
	 * 执行控制台命令（简单命令）
	 * @param cmd 控制台命令（不可包含重定向符 '>'、'<' 和 管道符 '|'）
	 * @return 执行结果
	 */
	public static CmdResult execute(String cmd) {
		return execute(cmd, false);
	}
	
	/**
	 * <PRE>
	 * 执行控制台命令（简单命令）
	 * 若启动debug模式, 则命令会阻塞等待异常码返回.
	 * </PRE>
	 * @param cmd 控制台命令（不可包含重定向符 '>'、'<' 和 管道符 '|'）
	 * @param debug 调试模式. true:返回包含异常码和异常信息的结果(会阻塞); false:只返回命令执行结果 
	 * @return 执行结果
	 */
	public static CmdResult execute(String cmd, boolean debug) {
		CmdResult cmdRst = CmdResult.DEFAULT;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			cmdRst = _execute(process, debug);
			process.destroy();
			
		} catch (Exception e) {
			log.error("执行控制台命令失败: {}", cmd, e);
		}
		return cmdRst;
	}
	
	/**
	 * 执行控制台命令（复杂命令）
	 * @param cmds 控制台命令
	 * @return 执行结果
	 */
	public static CmdResult execute(String[] cmds) {
		return execute(cmds, false);
	}
	
	/**
	 * <PRE>
	 * 执行控制台命令（复杂命令）
	 * 若启动debug模式, 则命令会阻塞等待异常码返回.
	 * </PRE>
	 * @param cmds 控制台命令
	 * @param debug 调试模式. true:返回包含异常码和异常信息的结果(会阻塞); false:只返回命令执行结果 
	 * @return 执行结果
	 */
	public static CmdResult execute(String[] cmds, boolean debug) {
		CmdResult cmdRst = CmdResult.DEFAULT;
		try {
			Process process = Runtime.getRuntime().exec(cmds, null, null);
			cmdRst = _execute(process, debug);
			process.destroy();
			
		} catch (Exception e) {
			log.error("执行控制台命令失败: {}", StrUtils.concat(cmds), e);
		}
		return cmdRst;
	}

	private static CmdResult _execute(Process process, boolean debug) {
		CmdResult cmdRst = new CmdResult();
		try {
			InputStream infoIs = process.getInputStream();
			cmdRst.setInfo(_readProcessLine(infoIs));
			
			if(debug == true) {
				InputStream errIs = process.getErrorStream();
				cmdRst.setErr(_readProcessLine(errIs));
				
				int errCode = process.waitFor();	// 此方法会阻塞, 直到命令执行结束
				cmdRst.setErrCode(errCode);
				
				errIs.close();
			}
			infoIs.close();
	
		} catch (Exception e) {
			cmdRst = CmdResult.DEFAULT;
			log.error("执行控制台命令失败", e);
		}
		return cmdRst;
	}
	
	/**
	 * 读取(同时打印)命令行的信息流
	 * @param is 信息流/异常流
	 * @return 执行结果
	 * @throws IOException
	 */
	private static String _readProcessLine(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(is, Charset.DEFAULT));
		
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append(Delimiter.CRLF);
			System.out.println(line);	// 实时打印命令行执行结果
		}
		return sb.toString();
	}

	/**
	 * 通过命令行进行文件/文件夹复制
	 * @param srcPath 源文件路径
	 * @param snkPath 目标文件路径
	 * @return 执行结果
	 */
	public static String copy(String srcPath, String snkPath) {
		String cmd = "";
		File file = new File(srcPath);
		if (OSUtils.isWin()) {
			if (file.isFile()) {
				cmd = StrUtils.concat("copy ", srcPath, " ", snkPath, " /Y");
				
			} else {
				srcPath = srcPath.trim().replaceAll("\\\\$", "");
				cmd = StrUtils.concat("xcopy ", srcPath, " ", snkPath, " /e /y");
			}
		} else {
			srcPath = srcPath.trim().replaceAll("/$", "");
			cmd = StrUtils.concat("cp -r ", srcPath, " ", snkPath);
		}
		return execute(cmd).getInfo();
	}
	
	/**
	 * 打开DOS控制台（只支持win系统）
	 * @return 执行结果
	 */
	public static String openDosUI() {
		String result = "";
		if(!OSUtils.isWin()) {
			result = "Unsupport except windows-system.";
			
		} else {
			String cmd = "cmd /c start";
			result = execute(cmd).getInfo();
		}
		return result;
	}
	
	/**
	 * 打开文件（只支持win系统）
	 * @param filePath 文件路径
	 * @return 执行结果
	 */
	public static String openFile(String filePath) {
		String result = "";
		if(!OSUtils.isWin()) {
			result = "Unsupport except windows-system.";
			
		} else if(StrUtils.isEmpty(filePath)) {
			result = StrUtils.concat("Invaild file: ", filePath);
			
		} else {
			File file = new File(filePath);
			if(!file.exists()) {
				result = StrUtils.concat("Invaild file: ", filePath);
				
			} else {
				String cmd = StrUtils.concat(
						"rundll32 url.dll FileProtocolHandler ", 
						file.getAbsolutePath());
				result = execute(cmd).getInfo();
			}
		}
		return result;
	}
	
	/**
	 * 打开网页（只支持win系统）
	 * @param url 网页地址
	 * @return 执行结果
	 */
	public static String openHttp(String url) {
		String result = "";
		if(!OSUtils.isWin()) {
			result = "Unsupport except windows-system.";
			
		} else if(StrUtils.isEmpty(url)) {
			result = StrUtils.concat("Invaild url: ", url);
			
		} else {
			String cmd = StrUtils.concat("cmd /c start ", url);
			result = execute(cmd).getInfo();
		}
		return result;
	}
	
	/**
	 * 运行bat脚本（只支持win系统）
	 * @param batFilePath 批处理脚本路径
	 * @return 执行结果
	 */
	public static String runBat(String batFilePath) {
		String result = "";
		if(!OSUtils.isWin()) {
			result = "Unsupport except windows-system.";
			
		} else if(StrUtils.isEmpty(batFilePath)) {
			result = StrUtils.concat("Invaild bat: ", batFilePath);
			
		} else {
			String cmd = StrUtils.concat("cmd /c start ", batFilePath);
			result = execute(cmd).getInfo();
		}
		return result;
	}
	
	/**
	 * 终止进程（只支持win系统）
	 * @param processName 进程名称
	 */
	public static void kill(String processName) {
		if(!OSUtils.isWin()) {
			return;
		}
		
		Pattern ptn = Pattern.compile(StrUtils.concat(processName, " +?(\\d+) "));
		String tasklist = execute("tasklist").getInfo();
		String[] tasks = tasklist.split("\n");
		
		for(String task : tasks) {
			if(task.startsWith(StrUtils.concat(processName, " "))) {
				Matcher mth = ptn.matcher(task);
				if(mth.find()) {
					String pid = mth.group(1);
					execute(StrUtils.concat("taskkill /f /t /im ", pid));
				}
			}
		}
	}

}

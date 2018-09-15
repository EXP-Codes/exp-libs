package exp.libs.utils.os;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.Charset;
import exp.libs.utils.io.IOUtils;

/**
 * <PRE>
 * 退出程序工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
final public class ExitUtils extends Thread {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(ExitUtils.class);
	
	/** 被监听的停止命令文件位置 */
	private final static String EXIT_CMD_FILE_PATH = "./exit.cmd";
	
	/**
	 * 监听停止命令文件是否存在的间隔(ms).
	 * 最小值为1000ms, 不必过于频繁.
	 */
	private long millis;
	
	/** 单例 */
	private static volatile ExitUtils instance;
	
	/**
	 * 构造函数
	 * @param millis 监听停止命令文件是否存在的间隔(ms) - 最小值为1000ms, 不必过于频繁
	 */
	private ExitUtils(long millis) {
		this.millis = (millis >= 1000 ? millis : 1000);
	}
	
	/**
	 * 设置控制台输入[任意命令(包括无命令)+回车], 可触发正常退出程序逻辑.
	 */
	public static void setConsoleExit() {
		setConsoleExit(null);
	}
	
	/**
	 * 设置控制台输入[指定命令+回车], 可触发正常退出程序逻辑.
	 * @param cmd 退出程序命令
	 */
	public static void setConsoleExit(final String cmd) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		do {
			try {
				String input = in.readLine();	// 阻塞等待命令
				if(cmd == null || "".equals(cmd)) {
					break;
				} else if(cmd.equals(input)) {
					break;
				}
			} catch (IOException e) {
				break;
			}
		} while(true);
		
		IOUtils.close(in);
		System.exit(0);
	}
	
	/**
	 * 添加终止程序监听器(会构造一个仅适用于win环境的stop.bat脚本)
	 * @param millis 监听间隔(ms)
	 */
	public static void addWinExitListener(long millis) {
		if(OSUtils.isWin() == false) {
			return;
		}
		
		if(instance == null) {
			synchronized (ExitUtils.class) {
				if(instance == null) {
					instance = new ExitUtils(millis);
					
					if(deleteExitCmdFile() == true) {
						if(createStopBat() == true) {
							instance.start();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 创建dos停止脚本
	 * @return 是否创建成功
	 */
	private static boolean createStopBat() {
		boolean isOk = true;
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream("./stop.bat"), Charset.ISO);
			out.append("@echo off\r\n");
			out.append("title stop\r\n");
			out.append("echo exit>").append(EXIT_CMD_FILE_PATH).append("\r\n");
			out.append("echo \"The program is going to stop...\"\r\n");
			out.append("pause\r\n");
			out.flush();
			out.close();
			
		} catch (Exception e) {
			log.error("构造停止脚本 [./stop.bat] 失败.", e);
			isOk = false;
		}
		return isOk;
	}
	
	/**
	 * 删除被监听的停止命令文件
	 * @return 是否删除成功
	 */
	private static boolean deleteExitCmdFile() {
		boolean isOk = true;
		File file = new File(EXIT_CMD_FILE_PATH);
		if(file.exists() && file.isFile()) {
			isOk = file.delete();
		}
		return isOk;
	}
	
	/**
	 * 检查被监听的停止命令文件是否存在
	 * @return 是否存在
	 */
	private static boolean checkExitCmdFile() {
		File file = new File(EXIT_CMD_FILE_PATH);
		return file.exists() && file.isFile();
	}
	
	/**
	 * 定时检查被监听的停止命令文件是否存在（由停止脚本运行时创建）
	 */
	@Override
	public void run() {
		while(checkExitCmdFile() == false) {
			ThreadUtils.tSleep(millis);
		}
		System.exit(0);
	}

}

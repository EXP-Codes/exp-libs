package exp.libs.warp.net.ftp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * <PRE>
 * FTP实现
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-14
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class FTPManagerImpl implements FTPConnection {

	/**
	 * 	ftp对象
	 */
	private FTPClient ftpClient = null;

	/**
	 * 构造方法
	 * @param ftpIp ip地址
	 * @param ftpPort 端口号
	 * @param ftpUsername 账号
	 * @param ftpPassword 密码
	 * @param timeOut 超时 单位秒
	 * @throws Exception 异常
	 */
	public FTPManagerImpl(String ftpIp, int ftpPort, String ftpUsername,
			String ftpPassword, int timeOut) throws Exception {
		ftpClient = new FTPClient();// 由于重连时不能定位到远程的中文目录，故这里重新赋值一个对象
		ftpClient.setConnectTimeout(timeOut);// 连接超时60秒
		ftpClient.setDataTimeout(timeOut);// 访问超时60秒
		ftpClient.setControlKeepAliveTimeout(timeOut);
		ftpClient.setControlKeepAliveReplyTimeout(timeOut);
		ftpClient.setControlEncoding("GBK");
		if (ftpPort == -1) {
			ftpClient.connect(ftpIp);
		} else {
			ftpClient.connect(ftpIp, ftpPort);
		}
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(ftpUsername, ftpPassword)) {
				ftpClient.enterLocalPassiveMode();
			} else {
				throw new Exception("登陆失败");
			}
		} else {
			throw new Exception("连接失败");
		}
	}

	@Override
	public void close() {
		// Add By WuZhongtian 2014-11-2上午10:23:10
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.disconnect();
			} else {
			}
		} catch (Exception e) {
		} finally {
			ftpClient = null;
		}
	}

	@Override
	public List<String> listFiles(String remoteDirectory) throws Exception {
		// Add By WuZhongtian 2014-11-2上午10:23:10
		FTPFile[] ftpFiles = ftpClient.listFiles(remoteDirectory);
		List<String> fileList = new ArrayList<String>();
		for (int i = 0; i < ftpFiles.length; i++) {
			FTPFile fileFileTemp = ftpFiles[i];
			if (fileFileTemp.isFile()) {
				fileList.add(remoteDirectory + "/" + fileFileTemp.getName());
			}
		}
		return fileList;
	}

	@Override
	public void upload(String localFile, String remoteDirectory)
			throws Exception {
		// Add By WuZhongtian 2014-11-2上午10:23:10
		File file = new File(localFile);
		if (file.exists()) {
			if (!file.isFile()) {
				throw new FileNotFoundException();
			}
			ftpClient.makeDirectory(remoteDirectory);
			ftpClient.changeWorkingDirectory(remoteDirectory);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 设置传输为2进制
			InputStream is = new FileInputStream(file);
			if (!ftpClient.storeFile(file.getName(), is)) {
				throw new Exception("upload error");
			}
		} else {
			throw new FileNotFoundException();
		}
	}

	@Override
	public void download(String localDirectory, String remoteFile)
			throws Exception {
		// Add By WuZhongtian 2014-11-2上午10:53:39
		File localDir = new File(localDirectory);
		File reFile = new File(remoteFile);
		if (!(localDir.exists() && localDir.isDirectory())) {
			localDir.mkdirs();
		}
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 设置传输为2进制
		OutputStream is = new FileOutputStream(localDirectory + File.separator
				+ reFile.getName());
		if (!ftpClient.retrieveFile(remoteFile, is)) {
			throw new Exception("download error");
		}

	}

	@Override
	public long getFileLength(String remoteFileName) throws IOException {
		long fileSize = -1;
		FTPFile file = ftpClient.mlistFile(remoteFileName);
		if (file != null) {
			fileSize = file.getSize();
		}
		return fileSize;
	}

	@Override
	public long getLastModified(String remoteFileName) throws IOException {
		long time = 0;
		FTPFile file = ftpClient.mlistFile(remoteFileName);
		if (file != null) {
			time = file.getTimestamp().getTimeInMillis();
		}
		return time;
	}

	@Override
	public List<String> listDirs(String remoteDirectory) throws IOException {
		// Add By WuZhongtian 2014-11-2上午10:23:10
		FTPFile[] ftpFiles = ftpClient.listFiles(remoteDirectory);
		List<String> fileList = new ArrayList<String>();
		for (int i = 0; i < ftpFiles.length; i++) {
			FTPFile fileFileTemp = ftpFiles[i];
			if (fileFileTemp.isDirectory()) {
				fileList.add(remoteDirectory + "/" + fileFileTemp.getName());
			}
		}
		return fileList;
	}

}

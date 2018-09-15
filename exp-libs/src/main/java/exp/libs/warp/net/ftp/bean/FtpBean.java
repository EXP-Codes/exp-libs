package exp.libs.warp.net.ftp.bean;

/**
 * <PRE>
 * ftp连接配置对象
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-14
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class FtpBean {

	public final static int SFTP = 1;

	public final static int FTP = 0;

	private String ip;
	private int port;
	private String username;
	private String password;
	private int ftpType;
	private String dir;

	public FtpBean(String ip, int port, String username, String password,
			int ftpType) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		this.ftpType = ftpType;
	}

	public FtpBean() {
	}

	/**
	 * GET ip
	 * @return the ip
	 */
	public final String getIp() {
		return ip;
	}

	/**
	 * SET ip
	 * @param ip the ip to set
	 */
	public final void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * GET port
	 * @return the port
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * SET port
	 * @param port the port to set
	 */
	public final void setPort(int port) {
		this.port = port;
	}

	/**
	 * GET username
	 * @return the username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * SET username
	 * @param username the username to set
	 */
	public final void setUsername(String username) {
		this.username = username;
	}

	/**
	 * GET password
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * SET password
	 * @param password the password to set
	 */
	public final void setPassword(String password) {
		this.password = password;
	}

	/**
	 * GET ftpType
	 * @return the ftpType
	 */
	public final int getFtpType() {
		return ftpType;
	}

	/**
	 * SET ftpType
	 * @param ftpType the ftpType to set
	 */
	public final void setFtpType(int ftpType) {
		this.ftpType = ftpType;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new org.apache.commons.lang3.builder.ReflectionToStringBuilder(
				this,
				org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE)
				.toString();

	}

	/**
	 * GET dir
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * SET dir
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

}

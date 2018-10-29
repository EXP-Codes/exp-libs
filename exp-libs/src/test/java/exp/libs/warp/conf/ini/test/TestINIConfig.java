package exp.libs.warp.conf.ini.test;

import exp.libs.warp.conf.ini.INIConfig;

/**
 * <PRE>
 * ini配置文件读写演示.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-07-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TestINIConfig {

	public static void main(String[] args) {
		
		// 读取jar包内的ini配置文件
		INIConfig iniJar = new INIConfig("/global.properties");
		System.out.println(iniJar.getAllKVs());
		
		// 读取磁盘的ini配置文件
		INIConfig iniFile = new INIConfig("./conf/global.properties");
		System.out.println(iniFile.getAllKVs());
		iniFile.save();
	}
	
}

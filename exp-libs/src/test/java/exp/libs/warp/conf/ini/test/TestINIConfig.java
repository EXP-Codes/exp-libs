package exp.libs.warp.conf.ini.test;

import exp.libs.warp.conf.ini.INIConfig;

public class TestINIConfig {

	public static void main(String[] args) {
		INIConfig ini = new INIConfig("/global.properties");
		System.out.println(ini.getAllKVs());
	}
	
}

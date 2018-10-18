## 项目版本管理组件

- 通过UI便捷管理项目版本信息
- 为 [`Maven项目发布插件 mojo-release-plugin`](https://github.com/lyy289065406/mojo-release-plugin) 的版本信息打印脚本提供数据支持
- 为 [`自动化升级插件 auto-upgrader`](https://github.com/lyy289065406/auto-upgrader) 提供版本管理支持

--------

## 使用示例


　在项目中新建一个main入口类（如`Version.java`），然后直接运行 `exp.libs.warp.ver.VersionMgr.exec()` 方法即可： 

```java
import exp.libs.utils.other.LogUtils;
import exp.libs.warp.ver.VersionMgr;

/**
 * <PRE>
 * 版本类.
 *  版本信息记录在 ./src/main/resources/.verinfo 中, 请勿删除该文件.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-07-11
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Version {

	/**
	 * 版本管理入口, 任何项目均不需修改此代码.
	 * @param args 入口参数（win下默认为-m, linux下强制为-p）
	 * 		[-p] 打印最后的版本信息（DOS界面）
	 * 		[-m] 版本管理（UI界面）
	 */
	public static void main(String[] args) {
		LogUtils.loadLogBackConfig();
		VersionMgr.exec(args);
	}
	
}
```

## 运行效果

- 项目信息管理界面：<br/><br/>
![项目信息管理](https://raw.githubusercontent.com/lyy289065406/exp-libs/master/exp-libs/doc/07_%E6%BC%94%E7%A4%BA%E6%96%87%E6%A1%A3/01-%E9%A1%B9%E7%9B%AE%E4%BF%A1%E6%81%AF%E7%AE%A1%E7%90%86.png)

- 查看某个历史版本信息界面：<br/><br/>
![查看某个历史版本信息](https://raw.githubusercontent.com/lyy289065406/exp-libs/master/exp-libs/doc/07_%E6%BC%94%E7%A4%BA%E6%96%87%E6%A1%A3/02-%E6%9F%A5%E7%9C%8B%E6%9F%90%E4%B8%AA%E5%8E%86%E5%8F%B2%E7%89%88%E6%9C%AC%E4%BF%A1%E6%81%AF.png)

- 查看当前版本信息界面：<br/><br/>
![查看当前版本信息](https://raw.githubusercontent.com/lyy289065406/exp-libs/master/exp-libs/doc/07_%E6%BC%94%E7%A4%BA%E6%96%87%E6%A1%A3/03-%E6%9F%A5%E7%9C%8B%E5%BD%93%E5%89%8D%E7%89%88%E6%9C%AC%E4%BF%A1%E6%81%AF.png)


<br/>　若需要获取所有历史版本信息，可以这样做：

```java
public static void main(String[] args) {
	boolean onlyCurVersion = false;	// 仅当前版本(即最新版本)
	boolean detailHistoty = true;	// 打印历史版本升级内容详单
	String hisVers = VersionMgr.getVersionInfo(false, true);
	System.out.println(hisVers);
}
```

　这样就会列印所有历史版本信息：

![列印所有历史版本信息](https://raw.githubusercontent.com/lyy289065406/exp-libs/master/exp-libs/doc/07_%E6%BC%94%E7%A4%BA%E6%96%87%E6%A1%A3/04-%E5%88%97%E5%8D%B0%E6%89%80%E6%9C%89%E5%8E%86%E5%8F%B2%E7%89%88%E6%9C%AC%E4%BF%A1%E6%81%AF.png)



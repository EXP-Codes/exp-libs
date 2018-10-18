## 项目版本管理组件

--------

## 组件特色

- 通过UI便捷管理项目版本信息
- 为 [`Maven项目发布插件 mojo-release-plugin`](https://github.com/lyy289065406/mojo-release-plugin) 的版本信息打印脚本提供数据支持
- 为 [`自动化升级插件 auto-upgrader`](https://github.com/lyy289065406/auto-upgrader) 提供版本管理支持

## 使用示例


　新建一个main入口类，然后直接运行 `VersionMgr.exec()` 方法即可： 

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
 * @author    EXP: 272629724@qq.com
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


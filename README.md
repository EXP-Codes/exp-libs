# exp-libs
　经验构件库


> *既然别人不甘造轮子，那我就来奠造基石*<br/>*他人会用，只是用。我要用，则随心所欲*

------


## 环境

　![](https://img.shields.io/badge/Platform-Windows|Unix-brightgreen.svg) ![](https://img.shields.io/badge/IDE-Eclipse-brightgreen.svg) ![](https://img.shields.io/badge/Maven-3.2.5%2B-brightgreen.svg) ![](https://img.shields.io/badge/JDK-1.6%2B-brightgreen.svg)


## 简介

　此构件库为本人多年编程总结提炼而成，把常用的功能模块作为原子API进行封装。<br/>　另外也借用了不少出色的第三方构件，在其之上进行二次封装。

　过程中尽量确保了低耦合、高性能、强稳健、高复用、更易用等，使之能够满足日常开发需要、提高开发效率。


## 安装与使用

　由于`exp-libs`封装了大量第三方构件，因此需要在**开发环境**直接导入到Maven的`pom.xml`文件使用，<br/>　利用pom文件自动加载第三方依赖构件（若仅导入单个`exp-libs.jar`，有很多功能是无法使用的）。

- [在线Javadoc](https://lyy289065406.github.io/api-online/javadoc/exp-libs/1.1/index.html)
- POM坐标（完全版）：
```xml
<dependency>
  <groupId>exp.libs</groupId>
  <artifactId>exp-libs</artifactId>
  <version>1.1-SNAPSHOT</version>
</dependency>
```
- POM坐标（精简版，过滤了不常用的第三方构件）：
```xml
<dependency>
  <groupId>exp.libs</groupId>
  <artifactId>exp-libs-simple</artifactId>
  <version>1.1-SNAPSHOT</version>
</dependency>
```

> 含有此构件的 [Nexus 仓库](https://github.com/lyy289065406/nexus-docker/tree/produce)


## 功能模块


| 主模块 | 子模块 | 组件包/类 | 说明 | 测试<br/>示例 |
|:---:|:---:|:---:|:---|:---:|
| **常用工具包**<br/>[`exp.libs.utils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils) | 编码工具<br/>[`encode`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/encode) | [`Base64`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/encode/Base64.java) | Base64编解码 | &nbsp; |
| &nbsp; | &nbsp; | [`CharsetUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/encode/CharsetUtils.java) | 字符集编码转换 | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestCharsetUtils.java) |
| &nbsp; | &nbsp; | [`CompressUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/encode/CompressUtils.java) | 压缩/解压（zip，gzip，tar，bz2） | &nbsp; |
| &nbsp; | &nbsp; | [`CryptoUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/encode/CryptoUtils.java) | 加解密（MD5，DES，RSA） | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestCryptoUtils.java) |
| &nbsp; | &nbsp; | [`TXTUtils`]() | 任意文件与txt文件互转 | &nbsp; |
| &nbsp; | 格式转换工具<br/>[`format`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/format) | [`ESCUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/format/ESCUtils.java) | 数据格式转换<br/>（转义字符，BCP，CSV，TSV） | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestEscUtils.java) |
| &nbsp; | &nbsp; | [`JsonUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/format/JsonUtils.java) | JSON数据处理 | &nbsp; |
| &nbsp; | &nbsp; | [`XmlUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/format/XmlUtils.java) | XML数据处理 | &nbsp; |
| &nbsp; | &nbsp; | [`StandardUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/format/StandardUtils.java) | 标准化处理 | &nbsp; |
| &nbsp; | 图像工具<br/>[`img`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/img) | [`ImageUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/img/ImageUtils.java) | 图像处理 | &nbsp; |
| &nbsp; | &nbsp; | [`QRCodeUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/img/QRCodeUtils.java) | 二维码生成/解析 | &nbsp; |
| &nbsp; | 读写工具<br/>[`io`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/io) | [`FileUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/io/FileUtils.java) | 磁盘文件处理 | &nbsp; |
| &nbsp; | &nbsp; | [`IOUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/io/IOUtils.java) | IO流处理 | &nbsp; |
| &nbsp; | &nbsp; | [`JarUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/io/JarUtils.java) | Jar文件处理 | &nbsp; |
| &nbsp; | 数值工具<br/>[`num`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/num) | [`BODHUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/num/BODHUtils.java) | 进制处理 | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestBODHUtils.java) |
| &nbsp; | &nbsp; | [`IDUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/num/IDUtils.java) | 唯一性ID生成器 | &nbsp; |
| &nbsp; | &nbsp; | [`NumUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/num/NumUtils.java) | 数值处理 | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestNumUtils.java) |
| &nbsp; | &nbsp; | [`UnitUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/num/UnitUtils.java) | 单位转换 | &nbsp; |
| &nbsp; | 系统工具<br/>[`os`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/os) | [`ExitUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/os/ExitUtils.java) | 程序终止控制 | &nbsp; |
| &nbsp; | &nbsp; | [`JavaUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/os/JavaUtils.java) | Java语言处理 | &nbsp; |
| &nbsp; | &nbsp; | [`OSUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/os/OSUtils.java) | 系统环境参数处理 | &nbsp; |
| &nbsp; | &nbsp; | [`ThreadUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/os/ThreadUtils.java) | 线程处理 | &nbsp; |
| &nbsp; | 日期/时间工具<br/>[`time`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/time) | [`DateUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/time/DateUtils.java) | 日期工具 | &nbsp; |
| &nbsp; | &nbsp; | [`TimeUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/time/TimeUtils.java) | 时间工具 | &nbsp; |
| &nbsp; | 校验工具<br/>[`verify`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/verify) | [`RegexUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/verify/RegexUtils.java) | 正则表达式处理 | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestVerifyUtils.java) |
| &nbsp; | &nbsp; | [`VerifyUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/verify/VerifyUtils.java) | 数据格式校验 | &nbsp; |
| &nbsp; | 其他工具<br/>[`other`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other) | [`AnnotationUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/AnnotationUtils.java) | 神ta喵注释生成器 | &nbsp; |
| &nbsp; | &nbsp; | [`BoolUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/BoolUtils.java) | 布尔值处理 | &nbsp; |
| &nbsp; | &nbsp; | [`JSUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/JSUtils.java) | JavaScript脚本处理 | &nbsp; |
| &nbsp; | &nbsp; | [`ListUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/ListUtils.java) | 队列/集合操作 | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestListUtils.java) |
| &nbsp; | &nbsp; | [`LogUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/LogUtils.java) | 日志设置。<br/>基于`org.slf4j(1.7.5)`接口与<br/>`ch.qos.logback(1.0.13)`封装 | &nbsp; |
| &nbsp; | &nbsp; | [`ObjUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/ObjUtils.java) | 对象处理 | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestObjUtils.java) |
| &nbsp; | &nbsp; | [`PathUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/PathUtils.java) | 路径处理 | &nbsp; |
| &nbsp; | &nbsp; | [`RandomUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/RandomUtils.java) | 随机生成器<br/>（随机数、随机汉字、随机姓名等） | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/utils/test/TestRandomUtils.java) |
| &nbsp; | &nbsp; | [`StrUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/utils/other/StrUtils.java) | 字符串处理 | &nbsp; |
| **二次封装组件**<br/>[`exp.libs.warp`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp) | 版本管理组件<br/>[`ver`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/ver) | &nbsp; | 借助Sqlite以UI方式管理项目版本信息，<br/>[`Maven项目发布插件`](https://github.com/lyy289065406/mojo-release-plugin)与[`自动化升级插件`](https://github.com/lyy289065406/auto-upgrader)<br/>的部分功能也依赖此组件实现 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/ver) |
| &nbsp; | 函数解析组件<br/>[`cep`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/cep) | &nbsp; | 基于`com.singularsys.jep(3.3.1)`封装。<br/>去除时效限制，并新增多种自定义函数 | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/cep) |
| &nbsp; | 命令行组件<br/>[`cmd`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/cmd) | &nbsp; | 封装系统命令行操作 | &nbsp; |
| &nbsp; | 配置解析组件<br/>[`cmd`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/conf) | xml配置文件解析<br/>[`xml`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/conf/xml) | 基于`org.dom4j(1.6.1)`封装，支持定时<br/>刷新配置项、加载固有格式的配置区块。<br/>[`数据库组件`](#db)、[`网络组件`](#net)等均利用此组件<br/>加载独立的配置区块 | &nbsp; |
| &nbsp; | &nbsp; | ini配置文件解析<br/>[`ini`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/conf/ini) | 基于`org.dtools.javaini(1.1.0.0)`封装<br/>扩展对ini文件的编码支持范围 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/conf/ini/test/TestINIConfig.java) |
| &nbsp; | <p id="db">数据库组件</p>[`db`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/db) | 关系型数据库工具<br/>[`sql`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/db/sql) | 基于`com.cloudhopper.proxool(0.9.1)`<br/>封装，支持mysql/oracle/sqlite等。<br/>提供连接池与JDBC两种数据库连接方<br/>式、及多种常用的增删改查操作，且可<br/>根据物理表模型反向生成JavaBean代码 | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/db/sql/test/TestDBUtils.java) |
| &nbsp; | &nbsp; | Redis客户端组件<br/>[`redis`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/db/redis) | 基于`redis.clients.jedis(2.9.0)`封装。<br/>支持单机/主从/哨兵/集群模式，屏蔽<br/>不同模式之间获取实例连接的差异性 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/db/redis/test/TestRedisClient.java) |
| &nbsp; | <p id="net">网络组件</p>[`net`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net) | Cookie组件<br/>[`cookie`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/cookie) | 用于解析HTTP/HTTPS响应头中的<br/>Set-Cookie参数 | &nbsp; |
| &nbsp; | &nbsp; | FTP组件<br/>[`ftp`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/ftp) | 未实装 | &nbsp; |
| &nbsp; | &nbsp; | HTTP/HTTPS组件<br/>[`http`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/http) | 提供在HTTP/HTTPS协议下，以长/短<br/>连接实现的GET、POST、Download<br/>方法，并支持自动解析Gzip流。<br/>基于`java.net.HttpURLConnection`与<br/>`commons-httpclient(3.1-rc1)`封装，<br/>更借助`org.bouncycastle.bcprov`<br/>`-jdk15on(1.54)`使得可以在JDK1.6+环<br/>境均支持HTTPS-TLSv1.2协议[`(详见)`](#TLSv12) | &nbsp; |
| &nbsp; | &nbsp; | Email组件<br/>[`mail`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/mail) | 基于`javax.mail(1.4.1)`封装，<br/>可用于邮件发送/抄送（支持加密） | [`JUnit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/net/mail/test/TestEmail.java) |
| &nbsp; | &nbsp; | MQ组件<br/>[`mq`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/mq) | 未实装（jms/kafka） | &nbsp; |
| &nbsp; | &nbsp; | 端口转发器<br/>[`pf`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/pf) | 利用Socket实现的端口转发程序 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/net/pf/test) |
| &nbsp; | &nbsp; | Ping组件<br/>[`ping`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/ping) | 利用系统命令实现的ping/tracert，支持<br/>解析中/英文的win/linux系统的结果集 | &nbsp; |
| &nbsp; | &nbsp; | Socket组件<br/>[`sock`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/sock) | 封装IO/NIO模式的Socket客户端/服务<br/>端的交互行为，只需实现业务逻辑 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/net/sock) |
| &nbsp; | &nbsp; | Telnet组件<br/>[`telnet`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/telnet) | 未实装 | &nbsp; |
| &nbsp; | &nbsp; | Webkit组件<br/>[`webkit`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/webkit) | 基于`org.seleniumhq.selenium(2.53.0)`<br/>封装，主要提供无头浏览器<br/>`com.codeborne.phantomjsdriver(1.2.1)`<br/>的常用操作 | &nbsp; |
| &nbsp; | &nbsp; | WebSocket客户端<br/>[`websock`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/websock) | 基于`org.java-websocket(1.3.4)`封装，<br/>支持ws与wss，提供数据帧的收发接口 | &nbsp; |
| &nbsp; | &nbsp; | WebServices组件<br/>[`wsdl`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/wsdl) | 基于`net.webservices.client(1.6.2)`封<br/>装，支持http/axis2/cxf，支持SSL模式 | &nbsp; |
| &nbsp; | IO组件<br/>[`io`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/io) | 流式读取器<br/>[`flow`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/io/flow) | 流式读取超大文件/字符串 | &nbsp; |
| &nbsp; | &nbsp; | 文件监听器<br/>[`listn`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/io/listn) | 可监听并触发指定目录树下所有<br/>文件/文件夹的增删改事件 | &nbsp; |
| &nbsp; | &nbsp; | 批量序列化读写器<br/>[`serial`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/io/serial) | 批量序列化/反序列任意实现了<br/>`java.io.Serializable`接口的对象 | &nbsp; |
| &nbsp; | OCR图文识别<br/>[`ocr`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/ocr) | &nbsp; | 基于`com.sun.media.jai-imageio`<br/>`(1.1-alpha)`封装。<br/>可识别图片中打印体的文字<br/>（文字不能旋转、变形，越正规的<br/>文字识别率越高） | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/ocr/test/TestOCR.java) |
| &nbsp; | 定时任务调度<br/>[`task`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/task) | [`在线生成cron表达式`](https://lyy289065406.github.io/cron-expression/) | 基于`org.quartz-scheduler(2.2.1)`<br/>封装。仅保留了simple与cron调度器，<br/>并提供cron表达式换算对象 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/task/test) |
| &nbsp; | 线程组件<br/>[`thread`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/thread) | &nbsp; | 提供抽象循环线程、回调线程池组件 | &nbsp; |
| &nbsp; | 模板文件组件<br/>[`tpl`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/tpl) | &nbsp; | 可定制含占位符的内容模板文件 | &nbsp; |
| &nbsp; | Swing界面工具<br/>[`ui`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/ui) | Swing组件<br/>[`cpt`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/ui/cpt) | 部分功能基于`org.jb.beauty-eye(3.7)`<br/>封装。可美化Swing外观，提供主窗口、<br/>浮动窗口、面板、表单、选框、系统托<br/>盘、布局样式等常用组件 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/ui/test) |
| &nbsp; | &nbsp; | 拓扑图绘制器<br/>[`topo`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/ui/topo) | 基于`org.eclipse.draw2d(1.0.0)`与<br/>`org.jgraph.jGraph(1.0.0)`封装。<br/>通过输入邻接矩阵（可含源宿点/必经<br/>点）自动根据边权换算边距，同时映射<br/>到极坐标系，绘制对应的拓扑图 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/ui/test/TestTopoGraphUI.java) |
| &nbsp; | Excel组件<br/>[`xls`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/xls) | &nbsp; | 基于`org.apache.poi.poi-ooxml(3.9)`<br/>封装。支持对xls/xlsx文件操作 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/warp/xls/test/TestExcel.java) |
| **算法包**<br/>[`exp.libs.algorithm`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm) | 基本算法<br/>[`basic`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/basic) | &nbsp; | 排序、布隆过滤器、哈夫曼编码、<br/>哈希算法等（未实装） | &nbsp; |
| &nbsp; | 数据结构<br/>[`struct`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/struct) | 拓扑图<br/>[`graph`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/struct/graph) | 拓扑图模型<br/>（支持有向/无向、源宿点、必经点） | &nbsp; |
| &nbsp; | &nbsp; | 队列<br/>[`queue`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/struct/queue) | 循环队列、生产者消费者队列、<br/>优先队列、流式并发队列等 | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/algorithm/struct/queue/sc/test) |
| &nbsp; | &nbsp; | 红黑树<br/>[`rbt`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/struct/rbt) | 未实装 | &nbsp; |
| &nbsp; | 搜索<br/>[`search`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/search) | &nbsp; | 未实装 | &nbsp; |
| &nbsp; | 数学<br/>[`math`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/math) | 数学算法<br/>[`MathUtils`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/math/MathUtils.java) | 归一化函数、卡马克浮点数快速算法、<br/>异或交换、二进制计数等 | &nbsp; |
| &nbsp; | &nbsp; | 素数<br/>[`Prime`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/math/Prime.java) | 埃拉托斯特尼筛法<br/>时/空复杂度：O(n) | &nbsp; |
| &nbsp; | &nbsp; | 矩阵运算<br/>[`Mat`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/math/Mat.java) | 未实装 | &nbsp; |
| &nbsp; | 图论<br/>[`graph`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/graph) | &nbsp; | 未实装 | &nbsp; |
| &nbsp; | 计算几何<br/>[`gmtry`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/gmtry) | &nbsp; | 未实装 | &nbsp; |
| &nbsp; | 动态规划<br/>[`dync`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/dync) | &nbsp; | 未实装 | &nbsp; |
| &nbsp; | 最短路径算法<br/><p id="spa" href="https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/spa">`spa`</p> | [`Dijkstra`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/spa/Dijkstra.java) | 适用单源最短路问题<br/>时间复杂度：O(V \* lgV+E) | [`测试`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/algorithm/spa/test/TestDijkstra.java) |
| &nbsp; | &nbsp; | [`BellmanFord`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/spa/BellmanFord.java) | 适用单源最短路问题（未实装）<br/>时间复杂度：O(V \* E)<br/>空间复杂度：O(V + E) | &nbsp; |
| &nbsp; | &nbsp; | [`SPFA`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/spa/SPFA.java) | （BellmanFord的优化版（未实装）<br/>时间复杂度：O(k \* E)<br/>空间复杂度：O(V + E) | &nbsp; |
| &nbsp; | &nbsp; | [`Johonson`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/spa/Johonson.java) | 适用全源最短路问题（未实装）<br/>时间复杂度：O(V \* E \* lgV) | &nbsp; |
| &nbsp; | &nbsp; | [`Floyd`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/spa/Floyd.java) | 适用全源最短路问题（未实装）<br/>时间复杂度：O(V^3)<br/>空间复杂度：O(V^2) | &nbsp; |
| &nbsp; | 启发式算法<br/><p id="heuristic" href="https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/heuristic">`heuristic`</p> | [`aca`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/heuristic/aca) | 蚁群算法（未实装） | &nbsp; |
| &nbsp; | &nbsp; | [`qaca`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/heuristic/qaca) | 量子蚁群算法（引入量子环境改进的<br/>蚁群算法：量子比特、量子概率幅、<br/>量子态、量子旋转门等） | [`示例`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/algorithm/heuristic/qaca/test/TestQACA.java) |
| &nbsp; | NP难问题<br/>[`np`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/np) | [`ispa`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/np/ispa) | 含必经点的最短路问题：<br/>自动根据场景用[`spa`](#spa)或[`启发式算法`](#heuristic)求解 | [`测试`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/test/java/exp/libs/algorithm/np/ispa/test/TestISPA.java) |
| &nbsp; | &nbsp; | [`tsp`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/np/tsp) | 旅行商问题（未实装） | &nbsp; |
| &nbsp; | 深度学习<br/>[`dl`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/dl) | [`tensorflow`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/algorithm/dl/tensorflow) | 基于`org.tensorflow(1.6.0)`封装。<br/>提供TensorFlow训练模型调用接口 | &nbsp; |


## 第三方构件修正记录

### [修正commons-httpclient-3.1重定向丢失cookie问题](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/org/apache/commons/httpclient)

```xml
<dependency>
  <groupId>commons-httpclient</groupId>
  <artifactId>commons-httpclient</artifactId>
  <version>3.1-rc1</version>
</dependency>
```

使用此版本的HttpClient访问一个会发生重定向跳转的URL时，会自动执行跳转，直到跳转到最后一个目标URL为止，而这个自动的重定向行为无法被禁止。由此会发生两个现象：

1. 使用HttpClient访问原始URL后，直接返回的状态码就是200 （而非302）
2. 得到的Response Header是最后一个URL的Response Header，而中间跳转的URL的Response Header全部丢失

第1点 其实影响不大，但 第2点 会**导致中间跳转的URL所返回的Set-Cookie丢失**。

对爬虫开发而言，这个问题是致命的：<br/>如要模拟登陆时，通常登陆成功后会返回cookie并自动重定向到网站主页，而这个过程中丢失了cookie就相当于登陆失败。

因此主要针对 第2点 修正如下：<br/>**记录中间所有URL返回的Response Header中的Set-Cookie，并全部追加到最后一个URL的Response Header中**。


### <a id="TLSv12" href="https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/exp/libs/warp/net/http">修正JDK1.6/1.7不支持TLSv1.2协议的HTTPS问题</a>

```xml
<dependency>
  <groupId>org.bouncycastle</groupId>
  <artifactId>bcprov-jdk15on</artifactId>
  <version>1.54</version>
</dependency>
```

> *Bouncy Castle是一种用于 Java 平台的开放源码的轻量级密码术包，它支持大量的密码术算法，并提供JCE 1.2.1的实现*

JDK1.6/1.7仅支持遵循TLSv1.1协议的HTTPS访问，若网站强制要求使用TLSv1.2协议，JDK1.6/1.7会直接报错。

为了解决这个问题，从而**引入Bouncy Castle重写JDK的默认SSLSocket的通信安全密级协议**，使得JDK1.6/1.7支持TLSv1.2。

> *由于JDK1.8已支持TLSv1.2，因此此问题仅针对JDK1.6/1.7环境*


### [屏蔽proxool-0.9.1连接池调试日志](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/org/logicalcobwebs/proxool)

```xml
<dependency>
  <groupId>com.cloudhopper.proxool</groupId>
  <artifactId>proxool</artifactId>
  <version>0.9.1</version>
</dependency>
```

此版本的连接池在运行期间会打印很多并非`slf4j`所打印的调试日志（基本都是使用`commons-logging`打印），无法通过配置屏蔽之，导致程序输出混乱。

为此**强制注释相关日志代码禁止其输出调试日志**。


### [重写commons-io-2.4返回值](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/org/apache/commons/io)

```xml
<dependency>
  <groupId>commons-io</groupId>
  <artifactId>commons-io</artifactId>
  <version>2.4</version>
</dependency>
```

主要针对其中的`org.apache.commons.io.FileUtils`的函数返回值进行重写：<br/>把其中的void返回值修正为boolean，并捕获相关抛出的异常，**以便进行二次封装**。


### [修正构造函数可见性](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/org/apache/poi/xssf/usermodel)

```xml
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi-ooxml</artifactId>
  <version>3.9</version>
</dependency>
```

修改`org.apache.poi.xssf.usermodel.XSSFHyperlink`的可见性为public，**以便进行二次封装**。


### [去除类的final声明](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs/src/main/java/org/quartz)

```xml
<dependency>
  <groupId>org.quartz-scheduler</groupId>
  <artifactId>quartz</artifactId>
  <version>2.2.1</version>
</dependency>
```

去除类`org.quartz.CronExpression`的final声明，**以便进行二次封装**。



## 版权声明

　[![Copyright (C) EXP,2016](https://img.shields.io/badge/Copyright%20(C)-EXP%202016-blue.svg)](http://exp-blog.com)　[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
  

- Site: [http://exp-blog.com](http://exp-blog.com) 
- Mail: <a href="mailto:289065406@qq.com?subject=[EXP's Github]%20Your%20Question%20（请写下您的疑问）&amp;body=What%20can%20I%20help%20you?%20（需要我提供什么帮助吗？）">289065406@qq.com</a>


------

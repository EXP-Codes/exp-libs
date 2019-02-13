# exp-libs-simple
　经验构件库（精简版）


------


## 环境

　![](https://img.shields.io/badge/Platform-Windows|Linux-brightgreen.svg) ![](https://img.shields.io/badge/IDE-Eclipse-brightgreen.svg) ![](https://img.shields.io/badge/Maven-3.2.5-brightgreen.svg) ![](https://img.shields.io/badge/JDK-1.6%2B-brightgreen.svg)


## 简介

　由于**完全版**的[`exp-libs`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs)封装了大量第三方构件，<br/>　导致用在一些简单的项目上时，每每需要在pom编写大量`exclusion`滤掉没有使用的第三方构件。

　虽然不过滤多余的第三方构件也没有影响，但是在发布项目时会使得安装包过大。

　因此在[`exp-libs`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs)基础上，再构建了这个**精简版**的[`exp-libs-simple`](https://github.com/lyy289065406/exp-libs/tree/master/exp-libs-simple)。<br/>　此**精简版**构件库不含任何代码，仅仅利用pom文件`exclusion`了**完全版**的不常用第三方构件。


## 安装与使用

　由于`exp-libs`封装了大量第三方构件，因此需要在**开发环境**直接导入到Maven的`pom.xml`文件使用，<br/>　利用pom文件自动加载第三方依赖构件（若仅导入单个`exp-libs[-simple].jar`，有很多功能是无法使用的）。

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


## 版权声明

　[![Copyright (C) 2016-2019 By EXP](https://img.shields.io/badge/Copyright%20(C)-2016~2019%20By%20EXP-blue.svg)](http://exp-blog.com)　[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
  

- Site: [http://exp-blog.com](http://exp-blog.com) 
- Mail: <a href="mailto:289065406@qq.com?subject=[EXP's Github]%20Your%20Question%20（请写下您的疑问）&amp;body=What%20can%20I%20help%20you?%20（需要我提供什么帮助吗？）">289065406@qq.com</a>


------

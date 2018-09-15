package exp.libs.warp.net.mail.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.envm.Charset;
import exp.libs.envm.SMTP;
import exp.libs.utils.os.ThreadUtils;
import exp.libs.warp.net.mail.Email;

public class TestEmail {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSend() {
		Email mail = new Email(SMTP._126, "***REMOVED***@126.com", "***REMOVED***", 
				"***REMOVED***@qq.com", "desKey", Charset.UTF8);
		mail.send("无加密无抄送测试", "测试内容A");
		ThreadUtils.tSleep(2000);
		
		mail.send("加密无抄送测试", "测试内容B", true);
		ThreadUtils.tSleep(2000);
		
		mail.send("加密抄送测试", "测试内容C", 
				new String[] { "***REMOVED***@qq.com" , "***REMOVED***@139.com" }, true);
		ThreadUtils.tSleep(2000);
		
		mail.debug(true);
		mail.send("重置收件人测试", "测试内容D", 
				new String[] { "***REMOVED***@139.com" }, 
				new String[] { "***REMOVED***@qq.com" }, true);
		
	}

}

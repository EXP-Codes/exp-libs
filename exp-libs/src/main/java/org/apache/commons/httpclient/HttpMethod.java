/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   HttpMethod.java

package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.auth.AuthState;
import org.apache.commons.httpclient.params.HttpMethodParams;

// Referenced classes of package org.apache.commons.httpclient:
//            URIException, HttpException, HostConfiguration, URI, 
//            Header, NameValuePair, HttpState, HttpConnection, 
//            StatusLine

public interface HttpMethod
{

    public abstract String getName();

    /**
     * @deprecated Method getHostConfiguration is deprecated
     */

    public abstract HostConfiguration getHostConfiguration();

    public abstract void setPath(String s);

    public abstract String getPath();

    public abstract URI getURI()
        throws URIException;

    public abstract void setURI(URI uri)
        throws URIException;

    /**
     * @deprecated Method setStrictMode is deprecated
     */

    public abstract void setStrictMode(boolean flag);

    /**
     * @deprecated Method isStrictMode is deprecated
     */

    public abstract boolean isStrictMode();

    public abstract void setRequestHeader(String s, String s1);

    public abstract void setRequestHeader(Header header);

    /**
     * 追加方法: 用于修正 commons-httpclient 自动重定向页面后导致响应cookies丢失问题
     * @param name cookie键
     * @param value cookie值
     * @author EXP
     */
    public abstract void addResponseHeader(String name, String value);

    /**
     * 追加方法: 用于修正 commons-httpclient 自动重定向页面后导致响应cookies丢失问题
     * @param header 响应头
     * @author EXP
     */
    public abstract void addResponseHeader(Header header);
    
    public abstract void addRequestHeader(String name, String value);

    public abstract void addRequestHeader(Header header);

    public abstract Header getRequestHeader(String s);

    public abstract void removeRequestHeader(String s);

    public abstract void removeRequestHeader(Header header);

    public abstract boolean getFollowRedirects();

    public abstract void setFollowRedirects(boolean flag);

    public abstract void setQueryString(String s);

    public abstract void setQueryString(NameValuePair anamevaluepair[]);

    public abstract String getQueryString();

    public abstract Header[] getRequestHeaders();

    public abstract Header[] getRequestHeaders(String s);

    public abstract boolean validate();

    public abstract int getStatusCode();

    public abstract String getStatusText();

    public abstract Header[] getResponseHeaders();

    public abstract Header getResponseHeader(String s);

    public abstract Header[] getResponseHeaders(String s);

    public abstract Header[] getResponseFooters();

    public abstract Header getResponseFooter(String s);

    public abstract byte[] getResponseBody()
        throws IOException;

    public abstract String getResponseBodyAsString()
        throws IOException;

    public abstract InputStream getResponseBodyAsStream()
        throws IOException;

    public abstract boolean hasBeenUsed();

    public abstract int execute(HttpState httpstate, HttpConnection httpconnection)
        throws HttpException, IOException;

    public abstract void abort();

    /**
     * @deprecated Method recycle is deprecated
     */

    public abstract void recycle();

    public abstract void releaseConnection();

    public abstract void addResponseFooter(Header header);

    public abstract StatusLine getStatusLine();

    public abstract boolean getDoAuthentication();

    public abstract void setDoAuthentication(boolean flag);

    public abstract HttpMethodParams getParams();

    public abstract void setParams(HttpMethodParams httpmethodparams);

    public abstract AuthState getHostAuthState();

    public abstract AuthState getProxyAuthState();

    public abstract boolean isRequestSent();
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\mavenRepository\commons-httpclient\commons-httpclient\3.1-rc1\commons-httpclient-3.1-rc1.jar
	Total time: 70 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/
package exp.libs.algorithm.dl.tensorflow;

/**
 * <PRE>
 * 运行状态
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-03-04
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
class RunStats {
    
    /** 全跟踪运行选项 */
    private final static byte[] FULL_TRACE_RUN_OPTIONS = 
    		new byte[] { 0x08, 0x03 };
    
    /** 执行状态句柄 */
    private long nativeHandle;
    
    /**
     * 构造函数
     */
    protected RunStats() {
        nativeHandle = allocate();
    }
    
    protected final static byte[] RUN_OPTIONS() {
        return FULL_TRACE_RUN_OPTIONS;
    }
    
    protected void close() {
        if (nativeHandle != 0) {
            delete(nativeHandle);
        }
        nativeHandle = 0;
    }
    
    protected synchronized void add(byte[] runMetadata) {
        add(nativeHandle, runMetadata);
    }
    
    protected synchronized String summary() {
        return summary(nativeHandle);
    }
    
    private static native long allocate();
    
    private static native void delete(long handle);
    
    private static native void add(long handle, byte[] runMetadata);
    
    private static native String summary(long handle);
    
}

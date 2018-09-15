package exp.libs.algorithm.dl.tensorflow;

import exp.libs.utils.num.NumUtils;

/**
 * <PRE>
 * 张量索引
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-03-04
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
class TensorIndex {
    
    /** 张量名称 */
    private String name;
    
    /** 张量索引（默认为0） */
    private int index;
    
    /**
     * 构造函数
     */
    private TensorIndex() {
    	this.name = "";
    	this.index = 0;
    }
    
    /**
     * 获取张量名称
     * @return 张量名称
     */
    public String NAME() {
    	return name;
    }
    
    /**
     * 获取张量索引
     * @return 张量索引
     */
    public int IDX() {
    	return index;
    }
    
    /**
     * 解析张量名称， 将其拆分成 name和index两部分
     * 	若张量名称中不存在index，则index取默认值为0
     * @param tensorName 张量名称, 格式为 name:index
     * @return
     */
    protected static TensorIndex parse(String tensorName) {
        TensorIndex ti = new TensorIndex();
        int pos = tensorName.lastIndexOf(':');
        if (pos < 0) {
        	ti.name = tensorName;
        	ti.index = 0;
        	
        } else {
        	ti.name = tensorName.substring(0, pos);
        	ti.index = NumUtils.toInt(tensorName.substring(pos + 1), 0);
        }
        return ti;
    }
}

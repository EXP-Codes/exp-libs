package exp.libs.warp.ocr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * OCR图像文字识别组件
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class OCR {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(OCR.class);
	
	/** 默认OCR组件目录 */
	public final static String OCR_DIR = "./conf/ocr/tesseract";
	
	/** OCR组件 */
	private _OCR ocr;
	
	/**
	 * 构造函数
	 * @param tesseractDir OCR组件驱动目录
	 */
	public OCR(String tesseractDir) {
		this.ocr = new _OCR(
				StrUtils.isTrimEmpty(tesseractDir) ? OCR_DIR : tesseractDir);
	}
	
	/**
	 * 图像识别
	 * @param imgPath 图像
	 * @return 识别文本
	 */
	public String recognizeText(String imgPath) {
		String txt = "";
		try {
			txt = ocr.recognizeText(imgPath);
		} catch (Exception e) {
			log.error("识别图片文字失败: {}", imgPath, e);
		}
		return txt.trim();
	}
	
}

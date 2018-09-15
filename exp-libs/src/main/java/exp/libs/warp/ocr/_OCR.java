package exp.libs.warp.ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import exp.libs.envm.FileType;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.PathUtils;

/**
 * <PRE>
 * OCR图像文字识别组件
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
final class _OCR {
	
	// tessdata
	protected final static String TESSDATA = "tessdata";
		
	// tesseract.exe
	protected final static String TESSERACT = "tesseract";
		
	private final static String CHARSET = "UTF-8";
	
	private final static String EOL = System.getProperty("line.separator");
	
	private final static String TMP_FILENAME = "tmp";
	
	private final static String TMP_SUFFIX = ".txt";
	
	/** OCR组件驱动目录 */
	private String tesseractDir;

	/**
	 * 构造函数
	 * @param tesseractDir OCR组件驱动目录
	 */
	protected _OCR(String tesseractDir) {
		this.tesseractDir = new File(tesseractDir).getAbsolutePath();
	}
	
	/**
	 * 图像识别
	 * @param imgPath 图像
	 * @return 识别文本
	 * @throws Exception
	 */
	protected String recognizeText(String imgPath) throws Exception {
		File imgFile = new File(imgPath);
		FileType type = FileUtils.getFileType(imgFile);	// 目前仅适用于png/jpg格式图片
		
		File tmpImg = _ImageIOHelper.createImage(imgFile, type.NAME);
		File tmpTxt = new File(imgFile.getParentFile(), TMP_FILENAME);
		String tmpTxtPath = tmpTxt.getAbsolutePath().concat(TMP_SUFFIX);
		
		int status = analyseImg(imgFile, tmpImg, tmpTxt);
		String rst = (status == 0 ? readFile(tmpTxtPath) : toErrDesc(status));
		
		tmpImg.delete();
		FileUtils.delete(tmpTxtPath);
		new File(tmpTxtPath).delete();
		
		if(status != 0) {
			throw new Exception(rst);
		}
		return rst;
	}
	
	private int analyseImg(File imgFile, File tmpImg, File tmpTxt) throws Exception {
		String[] cmd = {	// 组装orc命令
				PathUtils.combine(tesseractDir, TESSERACT), 
				tmpImg.getName(), tmpTxt.getName(), 
				"-l", "eng"	// 图片语言
		};
		
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imgFile.getParentFile());
		pb.command(cmd);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		return process.waitFor();
	}
	
	private String readFile(String filePath) throws Exception {
		StringBuilder rst = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath), CHARSET));
		String tmp = "";
		while ((tmp = in.readLine()) != null) {
			rst.append(tmp).append(EOL);
		}
		in.close();
		return rst.toString();
	}
	
	private String toErrDesc(int status) {
		String errDesc;
		switch (status) {
			case 0: {
				errDesc = "";
				break;
			}
			case 1: {
				errDesc = "Errors accessing files. There may be spaces in your image's filename.";
				break;
			}
			case 29: {
				errDesc = "Cannot recognize the image or its selected region.";
				break;
			}
			case 31: {
				errDesc = "Unsupported image format.";
				break;
			}
			default: {
				errDesc = "Errors occurred.";
			}
		}
		return errDesc;
	}
	
}

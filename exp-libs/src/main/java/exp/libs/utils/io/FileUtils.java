package exp.libs.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.FileType;
import exp.libs.utils.num.BODHUtils;
import exp.libs.utils.os.OSUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.utils.verify.RegexUtils;
import exp.libs.warp.cmd.CmdUtils;
import exp.libs.warp.io.flow.FileFlowReader;

/**
 * <PRE>
 * 文件工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-01-19
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
@SuppressWarnings("deprecation")
public class FileUtils extends org.apache.commons.io.FileUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(FileUtils.class);
	
	/**
	 * 文件头长度表.
	 *  文件后缀 -> 文件头长度
	 */
	private final static Map<String, Integer> HEAD_LENS = 
			new HashMap<String, Integer>();
	
	/** 当前已知的文件类型中, 文件头最长的长度 */
	private static int MAX_HEAD_LEN = -1;

	/**
	 * 文件类型表.
	 *   文件头 -> 文件后缀 -> 文件类型
	 */
	private final static Map<String, Map<String, FileType>> FILE_TYPES = 
			new HashMap<String, Map<String,FileType>>();
	
	/**
	 * 初始化文件类型查询表单
	 */
	static {
		initFileHeadLens();
		initFileTypes();
	}
	
	/**
	 * 初始化文件头长度表
	 */
	private static void initFileHeadLens() {
		HEAD_LENS.put(FileType.UNKNOW.EXT, FileType.UNKNOW.HEAD_LEN);
		HEAD_LENS.put(FileType.TXT.EXT, FileType.TXT.HEAD_LEN);
		HEAD_LENS.put(FileType.BAT.EXT, FileType.BAT.HEAD_LEN);
		HEAD_LENS.put(FileType.BIN.EXT, FileType.BIN.HEAD_LEN);
		HEAD_LENS.put(FileType.INI.EXT, FileType.INI.HEAD_LEN);
		HEAD_LENS.put(FileType.TMP.EXT, FileType.TMP.HEAD_LEN);
		HEAD_LENS.put(FileType.MP3.EXT, FileType.MP3.HEAD_LEN);
		HEAD_LENS.put(FileType.WAVE.EXT, FileType.WAVE.HEAD_LEN);
		HEAD_LENS.put(FileType.MIDI.EXT, FileType.MIDI.HEAD_LEN);
		HEAD_LENS.put(FileType.JPG.EXT, FileType.JPG.HEAD_LEN);
		HEAD_LENS.put(FileType.PNG.EXT, FileType.PNG.HEAD_LEN);
		HEAD_LENS.put(FileType.BMP.EXT, FileType.BMP.HEAD_LEN);
		HEAD_LENS.put(FileType.GIF.EXT, FileType.GIF.HEAD_LEN);
		HEAD_LENS.put(FileType.TIFF.EXT, FileType.TIFF.HEAD_LEN);
		HEAD_LENS.put(FileType.CAD.EXT, FileType.CAD.HEAD_LEN);
		HEAD_LENS.put(FileType.PSD.EXT, FileType.PSD.HEAD_LEN);
		HEAD_LENS.put(FileType.RTF.EXT, FileType.RTF.HEAD_LEN);
		HEAD_LENS.put(FileType.XML.EXT, FileType.XML.HEAD_LEN);
		HEAD_LENS.put(FileType.HTML.EXT, FileType.HTML.HEAD_LEN);
		HEAD_LENS.put(FileType.EMAIL.EXT, FileType.EMAIL.HEAD_LEN);
		HEAD_LENS.put(FileType.OUTLOOK.EXT, FileType.OUTLOOK.HEAD_LEN);
		HEAD_LENS.put(FileType.OE.EXT, FileType.OE.HEAD_LEN);
		HEAD_LENS.put(FileType.ACCESS.EXT, FileType.ACCESS.HEAD_LEN);
		HEAD_LENS.put(FileType.DOC.EXT, FileType.DOC.HEAD_LEN);
		HEAD_LENS.put(FileType.XLS.EXT, FileType.XLS.HEAD_LEN);
		HEAD_LENS.put(FileType.PPT.EXT, FileType.PPT.HEAD_LEN);
		HEAD_LENS.put(FileType.DOCX.EXT, FileType.DOCX.HEAD_LEN);
		HEAD_LENS.put(FileType.XLSX.EXT, FileType.XLSX.HEAD_LEN);
		HEAD_LENS.put(FileType.PPTX.EXT, FileType.PPTX.HEAD_LEN);
		HEAD_LENS.put(FileType.ZIP.EXT, FileType.ZIP.HEAD_LEN);
		HEAD_LENS.put(FileType.RAR.EXT, FileType.RAR.HEAD_LEN);
		HEAD_LENS.put(FileType.TAR.EXT, FileType.TAR.HEAD_LEN);
		HEAD_LENS.put(FileType.GZ.EXT, FileType.GZ.HEAD_LEN);
		HEAD_LENS.put(FileType.BZ2.EXT, FileType.BZ2.HEAD_LEN);
		HEAD_LENS.put(FileType.WPD.EXT, FileType.WPD.HEAD_LEN);
		HEAD_LENS.put(FileType.EPS.EXT, FileType.EPS.HEAD_LEN);
		HEAD_LENS.put(FileType.PS.EXT, FileType.PS.HEAD_LEN);
		HEAD_LENS.put(FileType.PDF.EXT, FileType.PDF.HEAD_LEN);
		HEAD_LENS.put(FileType.QDF.EXT, FileType.QDF.HEAD_LEN);
		HEAD_LENS.put(FileType.PWL.EXT, FileType.PWL.HEAD_LEN);
		HEAD_LENS.put(FileType.AVI.EXT, FileType.AVI.HEAD_LEN);
		HEAD_LENS.put(FileType.RAM.EXT, FileType.RAM.HEAD_LEN);
		HEAD_LENS.put(FileType.RM.EXT, FileType.RM.HEAD_LEN);
		HEAD_LENS.put(FileType.MPEG_VIDEO.EXT, FileType.MPEG_VIDEO.HEAD_LEN);
		HEAD_LENS.put(FileType.MPEG.EXT, FileType.MPEG.HEAD_LEN);
		HEAD_LENS.put(FileType.MOV.EXT, FileType.MOV.HEAD_LEN);
		HEAD_LENS.put(FileType.ASF.EXT, FileType.ASF.HEAD_LEN);
		HEAD_LENS.put(FileType.DLL.EXT, FileType.DLL.HEAD_LEN);
		HEAD_LENS.put(FileType.EXE.EXT, FileType.EXE.HEAD_LEN);
		
		Iterator<Integer> lens = HEAD_LENS.values().iterator();
		while(lens.hasNext()) {
			Integer len = lens.next().intValue();
			MAX_HEAD_LEN = (MAX_HEAD_LEN < len ? len : MAX_HEAD_LEN);
		}
	}
	
	/**
	 * 初始化文件类型表
	 */
	private static void initFileTypes() {
		FILE_TYPES.put(FileType.UNKNOW.HEADER, toMap(FileType.UNKNOW, FileType.TXT, 
				FileType.BAT, FileType.BIN, FileType.INI, FileType.TMP, FileType.MP3));
		FILE_TYPES.put(FileType.WAVE.HEADER, toMap(FileType.WAVE));
		FILE_TYPES.put(FileType.MIDI.HEADER, toMap(FileType.MIDI));
		FILE_TYPES.put(FileType.JPG.HEADER, toMap(FileType.JPG));
		FILE_TYPES.put(FileType.PNG.HEADER, toMap(FileType.PNG));
		FILE_TYPES.put(FileType.BMP.HEADER, toMap(FileType.BMP));
		FILE_TYPES.put(FileType.GIF.HEADER, toMap(FileType.GIF));
		FILE_TYPES.put(FileType.TIFF.HEADER, toMap(FileType.TIFF));
		FILE_TYPES.put(FileType.CAD.HEADER, toMap(FileType.CAD));
		FILE_TYPES.put(FileType.PSD.HEADER, toMap(FileType.PSD));
		FILE_TYPES.put(FileType.RTF.HEADER, toMap(FileType.RTF));
		FILE_TYPES.put(FileType.XML.HEADER, toMap(FileType.XML));
		FILE_TYPES.put(FileType.HTML.HEADER, toMap(FileType.HTML));
		FILE_TYPES.put(FileType.EMAIL.HEADER, toMap(FileType.EMAIL));
		FILE_TYPES.put(FileType.OUTLOOK.HEADER, toMap(FileType.OUTLOOK));
		FILE_TYPES.put(FileType.OE.HEADER, toMap(FileType.OE));
		FILE_TYPES.put(FileType.ACCESS.HEADER, toMap(FileType.ACCESS));
		FILE_TYPES.put(FileType.DOC.HEADER, toMap(FileType.DOC, FileType.XLS, FileType.PPT));
		FILE_TYPES.put(FileType.ZIP.HEADER, toMap(FileType.ZIP, 
				FileType.DOCX, FileType.XLSX, FileType.PPTX));
		FILE_TYPES.put(FileType.RAR.HEADER, toMap(FileType.RAR));
		FILE_TYPES.put(FileType.TAR.HEADER, toMap(FileType.TAR));
		FILE_TYPES.put(FileType.GZ.HEADER, toMap(FileType.GZ));
		FILE_TYPES.put(FileType.BZ2.HEADER, toMap(FileType.BZ2));
		FILE_TYPES.put(FileType.WPD.HEADER, toMap(FileType.WPD));
		FILE_TYPES.put(FileType.EPS.HEADER, toMap(FileType.PS, FileType.EPS));
		FILE_TYPES.put(FileType.PDF.HEADER, toMap(FileType.PDF));
		FILE_TYPES.put(FileType.QDF.HEADER, toMap(FileType.QDF));
		FILE_TYPES.put(FileType.PWL.HEADER, toMap(FileType.PWL));
		FILE_TYPES.put(FileType.AVI.HEADER, toMap(FileType.AVI));
		FILE_TYPES.put(FileType.RAM.HEADER, toMap(FileType.RAM));
		FILE_TYPES.put(FileType.RM.HEADER, toMap(FileType.RM));
		FILE_TYPES.put(FileType.MPEG_VIDEO.HEADER, toMap(FileType.MPEG_VIDEO));
		FILE_TYPES.put(FileType.MPEG.HEADER, toMap(FileType.MPEG));
		FILE_TYPES.put(FileType.MOV.HEADER, toMap(FileType.MOV));
		FILE_TYPES.put(FileType.ASF.HEADER, toMap(FileType.ASF));
		FILE_TYPES.put(FileType.DLL.HEADER, toMap(FileType.DLL, FileType.EXE));
	}
	
	/**
	 * 把若干个文件类型构造成Hash表单
	 * @param fileTypes
	 * @return Map: 文件后缀 -> 文件类型
	 */
	private static Map<String, FileType> toMap(FileType... fileTypes) {
		Map<String, FileType> map = new HashMap<String, FileType>();
		if(fileTypes != null) {
			for(FileType fileType : fileTypes) {
				map.put(fileType.EXT, fileType);
			}
		}
		return map;
	}
	
	/** 私有化构造函数 */
	protected FileUtils() {}
	
	/**
	 * 检查文件是否存在
	 * @param filePath 文件路径
	 * @return true:存在; false:不存在
	 */
	public static boolean exists(String filePath) {
		return (filePath != null && exists(new File(filePath)));
	}
	
	/**
	 * 检查文件是否都存在
	 * @param filePath 文件路径集
	 * @return true:都存在; false:某些不存在
	 */
	public static boolean exists(String... filePaths) {
		boolean isExists = true;
		if(filePaths != null && filePaths.length > 0) {
			for(String filePath : filePaths) {
				isExists &= exists(filePath);
				if(isExists == false) {
					break;
				}
			}
		} else {
			isExists = false;
		}
		return isExists;
	}
	
	/**
	 * 检查文件是否存在
	 * @param file 文件
	 * @return true:存在; false:不存在
	 */
	public static boolean exists(File file) {
		return (file != null && file.exists());
	}
	
	/**
	 * 检查文件是否都存在
	 * @param files 文件集
	 * @return true:都存在; false:某些不存在
	 */
	public static boolean exists(File... files) {
		boolean isExists = true;
		if(files != null && files.length > 0) {
			for(File file : files) {
				isExists &= exists(file);
				if(isExists == false) {
					break;
				}
			}
		} else {
			isExists = false;
		}
		return isExists;
	}
	
	/**
	 * 检查文件是否不存在
	 * @param filePath 文件路径
	 * @return true:不存在; false:存在
	 */
	public static boolean notExists(String filePath) {
		return !exists(filePath);
	}
	
	/**
	 * 检查文件是否都不存在
	 * @param filePath 文件路径集
	 * @return true:都不存在; false:某些存在
	 */
	public static boolean notExists(String... filePaths) {
		boolean isNotExists = true;
		if(filePaths != null) {
			for(String filePath : filePaths) {
				isNotExists &= notExists(filePath);
				if(isNotExists == false) {
					break;
				}
			}
		}
		return isNotExists;
	}
	
	/**
	 * 检查文件是否不存在
	 * @param file 文件对象
	 * @return true:不存在; false:存在
	 */
	public static boolean notExists(File file) {
		return !exists(file);
	}
	
	/**
	 * 检查文件是否都不存在
	 * @param files 文件集
	 * @return true:都不存在; false:某些存在
	 */
	public static boolean notExists(File... files) {
		boolean isNotExists = true;
		if(files != null) {
			for(File file : files) {
				isNotExists &= notExists(file);
				if(isNotExists == false) {
					break;
				}
			}
		}
		return isNotExists;
	}
	
	/**
	 * 检查目录是否为空
	 * @param dirPath 目录路径
	 * @return true:空或不存在; false:存在且非空
	 */
	public static boolean isEmpty(String dirPath) {
		return (dirPath == null || isEmpty(new File(dirPath)));
	}
	
	/**
	 * 检查目录是否为空
	 * @param dir 目录
	 * @return true:空或不存在; false:存在且非空
	 */
	public static boolean isEmpty(File dir) {
		return (!exists(dir) || dir.listFiles().length <= 0);
	}
	
	/**
	 * 测试文件类型是否为[文件]
	 * @param filePath 文件路径
	 * @return true:是; false:否
	 */
	public static boolean isFile(String filePath) {
		if(StrUtils.isTrimEmpty(filePath)) {
			return false;
		}
		return new File(filePath).isFile();
	}
	
	/**
	 * 测试所有文件的类型是否均为[文件]
	 * @param filePaths 文件路径集
	 * @return true:都是; false:某些不是
	 */
	public static boolean isFile(String... filePaths) {
		boolean isFile = true;
		if(filePaths != null && filePaths.length > 0) {
			for(String filePath : filePaths) {
				isFile &= isFile(filePath);
				if(isFile == false) {
					break;
				}
			}
		} else {
			isFile = false;
		}
		return isFile;
	}
	
	/**
	 * 测试文件类型是否为[文件]
	 * @param file 文件对象
	 * @return true:是; false:否
	 */
	public static boolean isFile(File file) {
		return (file != null && file.isFile());
	}
	
	/**
	 * 测试所有文件的类型是否均为[文件]
	 * @param files 文件集
	 * @return true:都是; false:某些不是
	 */
	public static boolean isFile(File... files) {
		boolean isFile = true;
		if(files != null && files.length > 0) {
			for(File file : files) {
				isFile &= isFile(file);
				if(isFile == false) {
					break;
				}
			}
		} else {
			isFile = false;
		}
		return isFile;
	}
	
	/**
	 * 测试文件类型是否为[文件夹]
	 * @param filePath 文件路径
	 * @return true:是; false:否
	 */
	public static boolean isDirectory(String filePath) {
		if(StrUtils.isTrimEmpty(filePath)) {
			return false;
		}
		return new File(filePath).isDirectory();
	}
	
	/**
	 * 测试所有文件的类型是否均为[文件夹]
	 * @param filePaths 文件路径集
	 * @return true:都是; false:某些不是
	 */
	public static boolean isDirectory(String... filePaths) {
		boolean isDirectory = true;
		if(filePaths != null && filePaths.length > 0) {
			for(String filePath : filePaths) {
				isDirectory &= isDirectory(filePath);
				if(isDirectory == false) {
					break;
				}
			}
		} else {
			isDirectory = false;
		}
		return isDirectory;
	}
	
	/**
	 * 测试文件类型是否为[文件夹]
	 * @param file 文件对象
	 * @return true:是; false:否
	 */
	public static boolean isDirectory(File file) {
		return (file != null && file.isDirectory());
	}
	
	/**
	 * 测试所有文件的类型是否均为[文件夹]
	 * @param files 文件集
	 * @return true:都是; false:某些不是
	 */
	public static boolean isDirectory(File... files) {
		boolean isDirectory = true;
		if(files != null && files.length > 0) {
			for(File file : files) {
				isDirectory &= isDirectory(file);
				if(isDirectory == false) {
					break;
				}
			}
		} else {
			isDirectory = false;
		}
		return isDirectory;
	}
	
	/**
	 * 获取文件名
	 * @param filePath 文件路径
	 * @return 文件名（包括后缀）
	 */
	public static String getName(String filePath) {
		String name = "";
		if(StrUtils.isTrimEmpty(filePath)) {
			return name;
		}
		
		File file = new File(filePath);
		if(file.exists()) {
			name = file.getName();
			
		} else {
			filePath = filePath.replace('\\', '/');
			if(name.contains("/")) {
				name = filePath.replaceFirst("[^/]*/", "");
						
			} else {
				name = filePath;
			}
		}
		return name;
	}

	/**
	 * <pre>
	 * 复制文件.
	 * ----------------------------
	 * 复制规则:
	 *   1.srcPath 的文件必须存在
	 *   2.srcPath 与 snkPath 必须是文件(而非文件夹)
	 *   3.若 snkPath 是文件夹名但不存在, 则自动变成无后缀的文件; 若是文件夹名且存在, 则报错
	 *   4.snkPath 与 srcPath 不能同源, 但可同名
	 *   5.若 snkFile 文件的祖先目录不存在则自动创建
	 *   6.若 snkFile 文件已存在则覆写
	 * </pre>
	 * @param srcPath 源位置
	 * @param snkPath 目标位置
	 */
	public static boolean copyFile(String srcPath, String snkPath) {
		boolean isOk = false;
		if(StrUtils.isNotTrimEmpty(srcPath, snkPath)) {
			File srcFile = new File(srcPath);
			File snkFile = new File(snkPath);
			isOk = copyFile(srcFile, snkFile);
		}
		return isOk;
	}
	
	/**
	 * <pre>
	 * 复制文件.
	 * ----------------------------
	 * 复制规则:
	 *   1.srcFile 的文件必须存在
	 *   2.srcFile 与 snkFile 必须是文件(而非文件夹)
	 *   3.若 snkFile 是文件夹名但不存在, 则自动变成无后缀的文件; 若是文件夹名且存在, 则报错
	 *   4.snkFile 与 srcFile 不能同源, 但可同名
	 *   5.若 snkFile 文件的祖先目录不存在则自动创建
	 *   6.若 snkFile 文件已存在则覆写
	 * </pre>
	 * @param srcFile 源文件
	 * @param snkFile 目标文件
	 */
	public static boolean copyFile(File srcFile, File snkFile) {
		boolean isOk = true;
		try {
			org.apache.commons.io.FileUtils.copyFile(srcFile, snkFile);
			
		} catch (Exception e) {
			isOk = false;
			log.error("复制文件失败: 从 [{}] 到 [{}].", 
					(srcFile == null ? "null" : srcFile.getPath()), 
					(snkFile == null ? "null" : snkFile.getPath()), e);
		}
		return isOk;
	}

	/**
	 * <pre>
	 * 复制文件夹.
	 * ----------------------------
	 * 复制规则:
	 *   1.srcPath 的文件夹必须存在
	 *   2.srcPath 与 snkPath 必须是文件夹(而非文件)
	 *   3.若 snkPath 是文件名但不存在, 则自动变成含后缀的文件夹; 若是文件名且存在, 则报错
	 *   4.snkPath 与 srcPath 不能同源, 但可同名
	 *   5.若 snkPath 文件的祖先目录不存在则自动创建
	 *   6.若 snkPath 内的子文件/文件夹已存在则自动对应覆写 (只覆写同位置的同名文件)
	 * </pre>
	 * @param srcPath 原位置
	 * @param snkPath 目标位置
	 */
	public static boolean copyDirectory(String srcPath, String snkPath) {
		boolean isOk = false;
		if(StrUtils.isNotTrimEmpty(srcPath, snkPath)) {
			File srcFile = new File(srcPath);
			File snkFile = new File(snkPath);
			isOk = copyDirectory(srcFile, snkFile);
		}
		return isOk;
	}
	
	/**
	 * <pre>
	 * 复制文件夹.
	 * ----------------------------
	 * 复制规则:
	 *   1.srcDir 的文件夹必须存在
	 *   2.srcDir 与 snkDir 必须是文件夹(而非文件)
	 *   3.若 snkDir 是文件名但不存在, 则自动变成含后缀的文件夹; 若是文件名且存在, 则报错
	 *   4.snkDir 与 srcDir 不能同源, 但可同名
	 *   5.若 snkDir 文件的祖先目录不存在则自动创建
	 *   6.若 snkDir 内的子文件/文件夹已存在则自动对应覆写 (只覆写同位置的同名文件)
	 * </pre>
	 * @param srcDir 源目录
	 * @param snkDir 目标目录
	 */
	public static boolean copyDirectory(File srcDir, File snkDir)  {
		boolean isOk = true;
		try {
			org.apache.commons.io.FileUtils.copyDirectory(srcDir, snkDir);
			
		} catch (Exception e) {
			isOk = false;
			log.error("复制文件夹失败: 从 [{}] 到 [{}].", 
					(srcDir == null ? "null" : srcDir.getPath()), 
					(snkDir == null ? "null" : snkDir.getPath()), e);
		}
		return isOk;
	}
	
	/**
	 * <pre>
	 * 移动文件.
	 * ----------------------------
	 * 移动规则:
	 *   1.srcPath 的文件必须存在, snkPath 的文件必须不存在
	 *   2.srcPath 与 snkPath 必须是文件(而非文件夹)
	 *   3.若 snkPath 是文件夹名但不存在, 则自动变成无后缀的文件; 若是文件夹名且存在, 则报错
	 *   4.snkPath 与 srcPath 不能同源, 但可同名
	 *   5.若 snkPath 文件的祖先目录不存在则自动创建
	 * </pre>
	 * @param srcPath 源位置
	 * @param snkPath 目标位置
	 */
	public static boolean moveFile(String srcPath, String snkPath) {
		boolean isOk = false;
		if(StrUtils.isNotTrimEmpty(srcPath, snkPath)) {
			File srcFile = new File(srcPath);
			File snkFile = new File(snkPath);
			isOk = moveFile(srcFile, snkFile);
		}
		return isOk;
	}
	
	/**
	 * <pre>
	 * 移动文件.
	 * ----------------------------
	 * 移动规则:
	 *   1.srcFile 的文件必须存在, snkFile 的文件必须不存在
	 *   2.srcFile 与 snkFile 必须是文件(而非文件夹)
	 *   3.若 snkFile 是文件夹名但不存在, 则自动变成无后缀的文件; 若是文件夹名且存在, 则报错
	 *   4.snkFile 与 srcFile 不能同源, 但可同名
	 *   5.若 snkFile 文件的祖先目录不存在则自动创建
	 * </pre>
	 * @param srcPath 源位置
	 * @param snkPath 目标位置
	 */
	public static boolean moveFile(File srcFile, File snkFile) {
		boolean isOk = true;
		try {
			org.apache.commons.io.FileUtils.moveFile(srcFile, snkFile);
			
		} catch (Exception e) {
			isOk = false;
			log.error("移动文件失败: 从 [{}] 到 [{}].", 
					(srcFile == null ? "null" : srcFile.getPath()), 
					(snkFile == null ? "null" : snkFile.getPath()), e);
		}
		return isOk;
	}
	
	/**
	 * <pre>
	 * 移动文件夹.
	 * ----------------------------
	 * 移动规则:
	 *   1.srcPath 的文件夹必须存在, snkPath 的文件夹必须不存在
	 *   2.srcPath 与 snkPath 必须是文件夹(而非文件)
	 *   3.若 snkPath 是文件名但不存在, 则自动变成含后缀的文件夹; 若是文件名且存在, 则报错
	 *   4.snkPath 与 srcPath 不能同源, 但可同名
	 *   5.若 snkPath 文件的祖先目录不存在则自动创建
	 * </pre>
	 * @param srcPath 源位置
	 * @param snkPath 目标位置
	 */
	public static boolean moveDirectory(String srcPath, String snkPath) {
		boolean isOk = false;
		if(StrUtils.isNotTrimEmpty(srcPath, snkPath)) {
			File srcFile = new File(srcPath);
			File snkFile = new File(snkPath);
			isOk = moveDirectory(srcFile, snkFile);
		}
		return isOk;
	}
	
	/**
	 * <pre>
	 * 移动文件夹.
	 * ----------------------------
	 * 移动规则:
	 *   1.srcDir 的文件夹必须存在, snkDir 的文件夹必须不存在
	 *   2.srcDir 与 snkDir 必须是文件夹(而非文件)
	 *   3.若 snkDir 是文件名但不存在, 则自动变成含后缀的文件夹; 若是文件名且存在, 则报错
	 *   4.snkDir 与 srcDir 不能同源, 但可同名
	 *   5.若 snkDir 文件的祖先目录不存在则自动创建
	 * </pre>
	 * @param srcDir 源位置
	 * @param snkDir 目标位置
	 */
	public static boolean moveDirectory(File srcDir, File snkDir)  {
		boolean isOk = true;
		try {
			org.apache.commons.io.FileUtils.moveDirectory(srcDir, snkDir);
			
		} catch (Exception e) {
			isOk = false;
			log.error("移动文件夹失败: 从 [{}] 到 [{}].", 
					(srcDir == null ? "null" : srcDir.getPath()), 
					(snkDir == null ? "null" : snkDir.getPath()), e);
		}
		return isOk;
	}
	
	/**
	 * 创建文件（若为linux系统, 创建的文件会自动授权可读写）
	 * @param filePath 文件路径
	 * @return true:创建成功; false:创建失败
	 */
	public static File createFile(String filePath) {
		return create(filePath, true);
	}
	
	/**
	 * 创建目录（若为linux系统, 创建的目录会自动授权可读写）
	 * @param dirPath 目录路径
	 * @return true:创建成功; false:创建失败
	 */
	public static File createDir(String dirPath) {
		return create(dirPath, false);
	}

	/**
	 * 创建文件/目录
	 * @param path 文件/目录位置
	 * @param isFile true:创建文件; false:创建目录
	 * @return true:创建成功; false:创建失败
	 */
	public static File create(String path, boolean isFile) {
		File file = null;
		boolean isCreated = false;
		try {
			file = new File(path);
			file.setWritable(true, false); // 处理linux的权限问题
			
			if (file.exists() == false) {
				if (false == file.getParentFile().exists()) {
					isCreated = file.getParentFile().mkdirs();
				}
				isCreated = (isFile ? file.createNewFile() : file.mkdir());
			} else {
				isCreated = true;
			}
		} catch (Exception e) {
			log.error("创建文件{} [{}] 失败", (isFile ? "" : "夹"), path, e);
		}
		return (isCreated ? file : null);
	}
	
	/**
	 * 删除文件/目录(包括子文件/子目录)
	 * @param path 文件/目录路径
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean delete(String path) {
		return delete(path, "");
	}
	
	/**
	 * 删除文件/目录(包括子文件/子目录)
	 * @param path 文件/目录路径
	 * @param filterRegex 过滤正则（匹配过滤的文件/目录保留）
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean delete(String path, String filterRegex) {
		boolean isOk = true;
		if(StrUtils.isNotTrimEmpty(path)) {
			isOk = delete(new File(path), filterRegex, false);
		}
		return isOk;
	}
	
	/**
	 * 删除文件/目录(包括子文件/子目录)
	 * @param file 文件/目录
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean delete(File file) {
		return delete(file, "", false);
	}
	
	/**
	 * 删除文件/目录(包括子文件/子目录)
	 * @param file 文件/目录
	 * @param filterRegex 过滤正则（匹配过滤的文件/目录保留）
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean delete(File file, String filterRegex) {
		return delete(file, filterRegex, false);
	}
	
	/**
	 * 在程序退出时删除文件/目录(包括子文件/子目录)
	 * @param path 文件/目录路径
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean deleteOnExit(String path) {
		return deleteOnExit(path, "");
	}
	
	/**
	 * 在程序退出时删除文件/目录(包括子文件/子目录)
	 * @param path 文件/目录路径
	 * @param filterRegex 过滤正则（匹配过滤的文件/目录保留）
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean deleteOnExit(String path, String filterRegex) {
		boolean isOk = true;
		if(StrUtils.isNotTrimEmpty(path)) {
			isOk = delete(new File(path), filterRegex, true);
		}
		return isOk;
	}
	
	/**
	 * 在程序退出时删除文件/目录(包括子文件/子目录)
	 * @param file 文件/目录
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean deleteOnExit(File file) {
		return delete(file, "", true);
	}
	
	/**
	 * 在程序退出时删除文件/目录(包括子文件/子目录)
	 * @param file 文件/目录
	 * @param filterRegex 过滤正则（匹配过滤的文件/目录保留）
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean deleteOnExit(File file, String filterRegex) {
		return delete(file, filterRegex, true);
	}
	
	/**
	 * 删除文件/目录(包括子文件/子目录)
	 * @param file 文件/目录
	 * @param filterRegex 过滤正则（匹配过滤的文件/目录保留）
	 * @param onExit 是否在程序退出时才删除
	 * @return true:全部删除成功; false:全部删除失败
	 */
	public static boolean delete(File file, String filterRegex, boolean onExit) {
		boolean isOk = true;
		if(file != null && file.exists()) {
			if(file.isFile()) {
				if(!RegexUtils.matches(file.getName(), filterRegex)) {
					if(onExit == true) {
						file.deleteOnExit();
					} else {
						isOk &= file.delete();
					}
				}
				
			} else if(file.isDirectory()) {
				File[] files = file.listFiles();
				for(File f : files) {
					isOk &= delete(f, filterRegex, onExit);
				}
				
				if(!RegexUtils.matches(file.getName(), filterRegex)) {
					if(onExit == true) {
						file.deleteOnExit();
					} else {
						isOk &= file.delete();
					}
				}
			}
		}
		return isOk;
	}
	
	/**
	 * <PRE>
	 * 使用系统默认编码读取文件内容.
	 * 	(此方法会一次性读取文件内所有内容, 不适用于大文件读取)
	 * </PRE>
	 * @param filePath 文件路径
	 * @return 文件内容
	 */
	public static String read(String filePath) {
		if(StrUtils.isTrimEmpty(filePath)) {
			return "";
		}
        return read(new File(filePath));
    }
	
	/**
	 * <PRE>
	 * 使用系统默认编码读取文件内容.
	 * 	(此方法会一次性读取文件内所有内容, 不适用于大文件读取)
	 * </PRE>
	 * @param file 文件
	 * @return 文件内容
	 */
	public static String read(File file) {
    	String str = "";
        try {
        	str = org.apache.commons.io.FileUtils.readFileToString(file);
        	
		} catch (Exception e) {
			log.error("读取文件失败: ", (file == null ? "null" : file.getPath()), e);
		}
        return str;
    }
    
    /**
     * <PRE>
	 * 读取文件内容.
	 * 	(此方法会一次性读取文件内所有内容, 不适用于大文件读取)
	 * </PRE>
     * @param filePath 文件路径
     * @param charset 文件编码
     * @return 文件内容
     */
    public static String read(String filePath, String charset) {
    	if(StrUtils.isTrimEmpty(filePath)) {
			return "";
		}
        return read(new File(filePath), charset);
    }
    
    /**
     * <PRE>
	 * 读取文件内容.
	 * 	(此方法会一次性读取文件内所有内容, 不适用于大文件读取)
	 * </PRE>
     * @param file 文件
     * @param charset 文件编码
     * @return 文件内容
     */
    public static String read(File file, String charset) {
    	String str = "";
        try {
        	str = org.apache.commons.io.FileUtils.readFileToString(file, charset);
        	
		} catch (Exception e) {
			log.error("读取文件失败: ", (file == null ? "null" : file.getPath()), e);
		}
        return str;
    }
    
    /**
     * <PRE>
	 * 使用系统默认编码分行读取文件所有内容.
	 * 	(此方法会一次性读取文件内所有内容, 不适用于大文件读取)
	 * </PRE>
     * @param filePath 文件路径
     * @return 文件内容
     */
    public static List<String> readLines(String filePath) {
    	if(StrUtils.isTrimEmpty(filePath)) {
    		return new LinkedList<String>();
    	}
        return readLines(new File(filePath));
	}
    
    /**
     * <PRE>
	 * 使用系统默认编码分行读取文件所有内容.
	 * 	(此方法会一次性读取文件内所有内容, 不适用于大文件读取)
	 * </PRE>
     * @param file 文件
     * @return 文件内容
     */
    public static List<String> readLines(File file) {
    	List<String> lines = new LinkedList<String>();
        try {
        	lines = org.apache.commons.io.FileUtils.readLines(file);
        	
		} catch (Exception e) {
			log.error("读取文件失败: ", (file == null ? "null" : file.getPath()), e);
		}
        return lines;
	}
    
    /**
     * <PRE>
	 * 分行读取文件所有内容.
	 * 	(此方法会一次性读取文件内所有内容, 不适用于大文件读取)
	 * </PRE>
     * @param filePath 文件路径
     * @param charset 文件编码
     * @return 文件内容
     */
    public static List<String> readLines(String filePath, String charset) {
    	if(StrUtils.isTrimEmpty(filePath)) {
    		return new LinkedList<String>();
    	}
    	return readLines(new File(filePath), charset);
	}
    
    /**
     * <PRE>
	 * 分行读取文件所有内容.
	 * 	(此方法会一次性读取文件内所有内容, 不适用于大文件读取)
	 * </PRE>
     * @param file 文件
     * @param charset 文件编码
     * @return 文件内容
     */
    public static List<String> readLines(File file, String charset) {
    	List<String> lines = new LinkedList<String>();
        try {
        	lines = org.apache.commons.io.FileUtils.readLines(file, charset);
        	
		} catch (Exception e) {
			log.error("读取文件失败: ", (file == null ? "null" : file.getPath()), e);
		}
        return lines;
	}
    
    /**
     * <PRE>
	 * 流式读取文件内容.
	 * 	(此方法会流式分段读取文件内容，适用于读取任意文件)
	 * 
	 * 示例:
	 * 	FileFlowReader ffr = readFlow(FILE_PATH, Charset.UTF8);
	 *  while(ffr.hasNextLine()) {
	 *  	String line = ffr.readLine('\n');
	 *  	// ... do for line
	 *  }
	 *  ffr.close();
	 * </PRE>
     * @param filePath 文件路径
     * @param charset 文件编码
     * @return 文件流式读取器
     */
    public static FileFlowReader readFlow(String filePath, String charset) {
    	return new FileFlowReader(filePath, charset);
    }
    
    /**
     * <PRE>
	 * 流式读取文件内容.
	 * 	(此方法会流式分段读取文件内容，适用于读取任意文件)
	 * 
	 * 示例:
	 * 	FileFlowReader ffr = readFlow(FILE_PATH, Charset.UTF8);
	 *  while(ffr.hasNextLine()) {
	 *  	String line = ffr.readLine('\n');
	 *  	// ... do for line
	 *  }
	 *  ffr.close();
	 * </PRE>
     * @param file 文件
     * @param charset 文件编码
     * @return 文件流式读取器
     */
    public static FileFlowReader readFlow(File file, String charset) {
    	return new FileFlowReader(file, charset);
    }
    
    /**
     * <PRE>
	 * 把数据覆写到指定文件.
	 * </PRE>
     * @param filePath 文件路径
     * @param data 文件数据(使用系统默认编码)
     * @return true：写入成功; false:写入失败
     */
	public static boolean write(String filePath, String data) {
		if(StrUtils.isTrimEmpty(filePath)) {
    		return false;
    	}
    	return write(new File(filePath), data);
	}
	
	/**
     * <PRE>
	 * 把数据覆写到指定文件.
	 * </PRE>
     * @param file 文件
     * @param data 文件数据(使用系统默认编码)
     * @return true：写入成功; false:写入失败
     */
	public static boolean write(File file, String data) {
    	boolean isOk = true;
        try {
        	org.apache.commons.io.FileUtils.write(file, data);
        	
		} catch (Exception e) {
			isOk = false;
			log.error("写文件失败: ", (file == null ? "null" : file.getPath()), e);
		}
        return isOk;
	}
	
	/**
	 * <PRE>
	 * 把数据写到指定文件.
	 * </PRE>
	 * @param filePath 文件路径
	 * @param data 文件数据(使用系统默认编码)
	 * @param append true:附加到末尾; false:覆写
	 * @return true：写入成功; false:写入失败
	 */
	public static boolean write(String filePath, String data, boolean append) {
		if(StrUtils.isTrimEmpty(filePath)) {
    		return false;
    	}
		return write(new File(filePath), data, append);
	}
	
	/**
	 * <PRE>
	 * 把数据写到指定文件.
	 * </PRE>
	 * @param file 文件
	 * @param data 文件数据(使用系统默认编码)
	 * @param append true:附加到末尾; false:覆写
	 * @return true：写入成功; false:写入失败
	 */
	public static boolean write(File file, String data, boolean append) {
    	boolean isOk = true;
        try {
        	org.apache.commons.io.FileUtils.write(file, data, append);
        	
		} catch (Exception e) {
			isOk = false;
			log.error("写文件失败: ", (file == null ? "null" : file.getPath()), e);
		}
        return isOk;
	}
	
	/**
	 * <PRE>
	 * 把数据覆写到指定文件.
	 * </PRE>
	 * @param filePath 文件路径
	 * @param data 文件数据
	 * @param charset 数据编码
	 * @return true：写入成功; false:写入失败
	 */
	public static boolean write(String filePath, String data, String charset) {
		if(StrUtils.isTrimEmpty(filePath)) {
    		return false;
    	}
		return write(new File(filePath), data, charset);
	}
	
	/**
	 * <PRE>
	 * 把数据覆写到指定文件.
	 * </PRE>
	 * @param file 文件
	 * @param data 文件数据
	 * @param charset 数据编码
	 * @return true：写入成功; false:写入失败
	 */
	public static boolean write(File file, String data, String charset) {
    	boolean isOk = true;
        try {
        	org.apache.commons.io.FileUtils.write(file, data, charset);
        	
		} catch (Exception e) {
			isOk = false;
			log.error("写文件失败: ", (file == null ? "null" : file.getPath()), e);
		}
        return isOk;
	}
	
	/**
	 * <PRE>
	 * 把数据写到指定文件.
	 * </PRE>
	 * @param filePath 文件路径
	 * @param data 文件数据
	 * @param charset 数据编码
	 * @param append true:附加到末尾; false:覆写
	 * @return true：写入成功; false:写入失败
	 */
	public static boolean write(String filePath, String data, String charset, boolean append) {
		if(StrUtils.isTrimEmpty(filePath)) {
    		return false;
    	}
		return write(new File(filePath), data, charset, append);
	}
	
	/**
	 * <PRE>
	 * 把数据写到指定文件.
	 * </PRE>
	 * @param file 文件
	 * @param data 文件数据
	 * @param charset 数据编码
	 * @param append true:附加到末尾; false:覆写
	 * @return true：写入成功; false:写入失败
	 */
	public static boolean write(File file, String data, String charset, boolean append) {
    	boolean isOk = true;
        try {
        	org.apache.commons.io.FileUtils.write(file, data, charset, append);
        	
		} catch (Exception e) {
			isOk = false;
			log.error("写文件失败: ", (file == null ? "null" : file.getPath()), e);
		}
        return isOk;
	}
	
	/**
	 * 计算文件/目录大小
	 * @param filePath 文件/目录路径
	 * @return 文件/目录大小，单位bytes
	 */
	public static long getSize(String filePath) {
		long size = 0;
		if(StrUtils.isNotTrimEmpty(filePath)) {
			size = getSize(new File(filePath));
		}
		return size;
	}
	
	/**
	 * 计算文件/目录大小
	 * @param file 文件/目录
	 * @return 文件/目录大小，单位bytes
	 */
	public static long getSize(File file) {
		long size = 0;
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				for (File sFile : file.listFiles()) {
					size += getSize(sFile);
				}
			} else {
				size = file.length();
			}
		}
		return size;
	}
	
	/**
	 * 获取文件类型
	 * @param filePath 文件路径
	 * @return 文件类型
	 */
	public static FileType getFileType(String filePath) {
		FileType fileType = FileType.UNKNOW;
		if(StrUtils.isNotTrimEmpty(filePath)) {
			fileType = getFileType(new File(filePath));
		}
		return fileType;
	}
	
	/**
	 * 获取文件类型
	 * @param file 文件
	 * @return 文件类型
	 */
	public static FileType getFileType(File file) {
		FileType fileType = FileType.UNKNOW;
		
		// 基于文件后缀ext与文件头header是正确配对的前提下, 验证猜测文件类型
		// (先用文件后缀获取理论文件头，再用实际文件头匹配理论文件头)
		String ext = getExtension(file);
		if(StrUtils.isNotTrimEmpty(ext)) {
			Integer headLen = HEAD_LENS.get(ext);
			if(headLen != null) {
				String header = _getHeader(file, headLen.intValue());
				fileType = _toFileType(header, ext);
			}
		}
		
		// 基于文件后缀ext与文件头header是不匹配的前提下, 通过文件头猜测文件类型
		if(fileType == FileType.UNKNOW) {
			String fileHeader = _getHeader(file, MAX_HEAD_LEN);
			Iterator<String> headers = FILE_TYPES.keySet().iterator();
			while(headers.hasNext()) {
				String header = headers.next();
				if(StrUtils.equals("", fileHeader, header) || 
						(!"".equals(header) && fileHeader.startsWith(header))) {
					Map<String, FileType> types = FILE_TYPES.get(header);
					if(types.size() == 1) {
						fileType = types.values().iterator().next();
						
					} else {
						log.error("判定文件 [{}] 的文件类型失败: 其文件后缀被篡改 (它可能是 {} 中的一个)", 
								file.getName(), types.values().toString());
					}
					break;
				}
			}
		}
		return fileType;
	}
	
	/**
	 * 根据文件头和文件后缀转换成文件类型对象
	 * @param header 16进制文件头
	 * @param ext 含.的文件后缀
	 * @return 文件类型对象
	 */
	private static FileType _toFileType(String header, String ext) {
		FileType fileType = null;
		if(StrUtils.isNotTrimEmpty(header, ext)) {
			header = header.trim().toUpperCase();
			ext = ext.trim().toLowerCase();
			
			Map<String, FileType> types = FILE_TYPES.get(header);
			if(types != null) {
				fileType = types.get(ext);
			}
		}
		return (fileType == null ? FileType.UNKNOW : fileType);
	}
	
	/**
	 * <pre>
	 * 获取文件头信息.
	 * </pre>
	 * @param file 文件
	 * @param headLen 文件头信息长度
	 * @return 文件头信息
	 */
	private static String _getHeader(File file, int headLen) {
		String header = "";
		if(headLen <= 0) {
			return header;
		}
		
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			byte[] bytes = new byte[headLen];
			is.read(bytes, 0, bytes.length);
			header = BODHUtils.toHex(bytes);
			
		} catch (Exception e) {
			log.error("获取文件 [{}] 的文件头信息失败.", 
					(file == null ? "null" : file.getAbsolutePath()), e);
			
		} finally {
			IOUtils.close(is);
		}
		return header;
	}
	
	/**
	 * 获取文件扩展名(包括[.]符号)
	 * @param file 文件
	 * @return 文件扩展名(全小写, 包括[.]符号)
	 */
	public static String getExtension(File file) {
		return getExtension(file == null ? "" : file.getName());
	}
	
	/**
	 * 获取文件扩展名(包括[.]符号)
	 * @param fileName 文件名
	 * @return 文件扩展名(全小写, 包括[.]符号)
	 */
	public static String getExtension(String fileName) {
		String extension = "";
		if(fileName == null) {
			return extension;
		}
		
		int pos = fileName.lastIndexOf(".");
		if (pos != -1) {
			extension = fileName.substring(pos).toLowerCase();
		}
		return extension;
	}
	
	/**
	 * 列举目录下的文件清单
	 * @param dirPath 目录位置
	 * @param extension 文件后缀
	 * @return 后缀匹配的文件清单
	 */
	public static List<File> listFiles(String dirPath, String extension) {
		return listFiles((dirPath == null ? null : new File(dirPath)), extension);
	}
	
	/**
	 * 列举目录下的文件清单
	 * @param dirPath 目录位置
	 * @param extension 文件后缀
	 * @return 后缀匹配的文件清单
	 */
	public static List<File> listFiles(File dir, String extension) {
		List<File> list = new LinkedList<File>();
		if(dir != null) {
			if(dir.exists()) {
				if(dir.isFile()) {
					if(_match(dir, extension)) {
						list.add(dir);
					}
					
				} else {
					File[] files = dir.listFiles();
					for(File file : files) {
						if(_match(file, extension)) {
							list.add(file);
						}
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 检查文件与后缀是否匹配
	 * @param file 文件
	 * @param extension 后缀
	 * @return true:匹配; false:不匹配
	 */
	private static boolean _match(File file, String extension) {
		return (StrUtils.isEmpty(extension) || 
				file.getName().toLowerCase().endsWith(extension.toLowerCase()));
	}
	
	/**
	 * <PRE>
	 * 隐藏文件/文件夹
	 * 	此方法仅适用于win系统. 
	 * 	linux系统直接在文件名前加.即可实现隐藏
	 * </PRE>
	 * @param filePath 文件路径
	 * @return true:隐藏成功; false:隐藏失败
	 */
	public static boolean hide(String filePath) {
		boolean isOk = false;
		if(StrUtils.isNotEmpty(filePath)) {
			isOk = hide(new File(filePath));
		}
		return isOk;
	}
	
	/**
	 * <PRE>
	 * 隐藏文件/文件夹
	 * 	此方法仅适用于win系统. 
	 * 	linux系统直接在文件名前加.即可实现隐藏
	 * </PRE>
	 * @param filePath
	 * @return
	 */
	public static boolean hide(File file) {
		boolean isOk = false;
		if(file != null && file.exists()) {
			if(OSUtils.isWin()) {
				String cmd = StrUtils.concat("attrib +H \"", 
						file.getAbsolutePath(), "\"");
				isOk = StrUtils.isTrimEmpty(CmdUtils.execute(cmd).getInfo());
				
			} else if(OSUtils.isUnix()) {
				isOk = file.getName().startsWith(".");
			}
		}
		return isOk;
	}
	
	/**
	 * 移除不允许出现在文件名中的特殊字符
	 * @param fileName 文件名
	 * @param symbol 用于替代被移除的特殊字符
	 * @return 移除特殊字符后的文件名
	 */
	public static String delForbidCharInFileName(String fileName, String symbol) {
		String name = (fileName == null ? "" : fileName);
		return name.replaceAll("[/\\\\:\\*\"<>\\|\\?\r\n\t\0]", symbol);
	}
	
}

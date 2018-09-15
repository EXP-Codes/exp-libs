package exp.libs.utils.encode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.Charset;
import exp.libs.envm.FileType;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.num.BODHUtils;
import exp.libs.utils.num.UnitUtils;
import exp.libs.utils.other.PathUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 压缩/解压工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-01-19
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class CompressUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(CompressUtils.class);
	
	/** 默认压缩编码 */
	public final static String DEFAULT_ENCODE = Charset.UTF8;
	
	/** 私有化构造函数 */
	protected CompressUtils() {}
	
	/**
	 * <PRE>
	 * 压缩文件.
	 * 	压缩算法为zip， 得到的压缩文件与filePath同目录.
	 * </PRE>
	 * 
	 * @param filePath 被压缩的文件路径（路径包含文件名）
	 * @return 压缩成功的文件路径 (若失败返回空串)
	 */
	public static String compress(String filePath) {
		return compress(filePath, FileType.ZIP);
	}
	
	/**
	 * <PRE>
	 * 使用指定算法压缩文件.
	 * 	得到的压缩文件与filePath同目录.
	 * </PRE>
	 * 
	 * @param filePath 被压缩的文件路径（路径包含文件名）
	 * @param fileType 期望得到的压缩文件类型(亦即压缩算法, 默认为zip)
	 * @return 压缩成功的文件路径 (若失败返回空串)
	 */
	public static String compress(String filePath, FileType fileType) {
		fileType = (fileType == null ? FileType.ZIP : fileType);
		String snkPath = StrUtils.concat(filePath, fileType.EXT);
		return (compress(filePath, snkPath, fileType) ? snkPath : "");
	}
	
	/**
	 * <PRE>
	 * 压缩文件.
	 * 	压缩算法为zip.
	 * </PRE>
	 * 
	 * @param srcPath 被压缩的文件路径（路径包含文件名）
	 * @param snkPath 得到压缩文件的路径（路径包含文件名）
	 * @return true:压缩成功; false:压缩失败
	 */
	public static boolean compress(String srcPath, String snkPath) {
		return compress(srcPath, snkPath, FileType.ZIP);
	}
	
	/**
	 * 使用指定算法压缩文件.
	 * 
	 * @param srcPath 被压缩的文件路径（路径包含文件名）
	 * @param snkPath 得到压缩文件的路径（路径包含文件名）
	 * @param fileType 期望得到的压缩文件类型(亦即压缩算法, 默认为zip)
	 * @return true:压缩成功; false:压缩失败
	 */
	public static boolean compress(String srcPath, String snkPath, FileType fileType) {
		if(StrUtils.isEmpty(srcPath) || StrUtils.isEmpty(snkPath)){
			log.warn("压缩文件 [{}] 失败：源路径/目标路径为空.");
			return false;
		}
		
		fileType = (fileType == null ? FileType.UNKNOW : fileType);
		boolean isOk = false;
		try{
			if (FileType.ZIP == fileType) {
				isOk = toZip(srcPath, snkPath);
				
			} else if (FileType.TAR == fileType) {
				isOk = toTar(srcPath, snkPath);
				
			} else if (FileType.GZ == fileType) {
				isOk = toGZip(srcPath, snkPath);
				
			} else if (FileType.BZ2 == fileType) {
				isOk = toBZ2(srcPath, snkPath);
				
			} else {
				log.warn("压缩文件 [{}] 失败： 不支持的压缩格式 [{}].", srcPath, fileType.NAME);
			}
		} catch(Exception e){
			log.error("压缩文件 [{}] 到 [{}] 失败.", srcPath, snkPath, e);
		}
		return isOk;
	}

	/**
	 * 使用zip算法压缩文件
	 * 
	 * @param srcPath 被压缩的文件路径（路径包含文件名）
	 * @param zipPath 得到压缩文件的路径（路径包含文件名）
	 * @return true:压缩成功; false:压缩失败
	 */
	public static boolean toZip(String srcPath, String zipPath)  {
		boolean isOk = true;
		File zipFile = FileUtils.createFile(zipPath);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipArchiveOutputStream zos = null;
		
		try {
			fos = new FileOutputStream(zipFile);
			bos = new BufferedOutputStream(fos);
			zos = new ZipArchiveOutputStream(bos);
			addFileToZip(zos, "", new File(srcPath));
			zos.finish();
			
		} catch(Exception e){
			isOk = false;
			log.error("[ERROR-ZIP] 压缩文件 [{}] 到 [{}] 失败.", srcPath, zipPath, e);
			
		} finally {
			if(zos != null){
				try {
					zos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-ZIP] 关闭文件压缩流失败: [{}].", zipPath, e);
				}
			}
			
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-ZIP] 关闭文件压缩流失败: [{}].", zipPath, e);
				}
			}
			
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-ZIP] 关闭文件压缩流失败: [{}].", zipPath, e);
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 使用zip算法压缩多个文件，并打包.
	 * 
	 * @param srcPaths 被压缩的文件路径集（路径包含文件名）
	 * @param zipPath 得到压缩文件的路径（路径包含文件名）
	 * @return true:压缩成功; false:压缩失败
	 */
	public static boolean toZip(String[] srcPaths, String zipPath) {
		if(srcPaths == null || srcPaths.length <= 0 || 
				StrUtils.isEmpty(zipPath)) {
			return false;
		}
		
		if(!FileType.ZIP.EXT.equals(FileUtils.getExtension(zipPath))) {
			return false;
		}
		
		boolean isOk = true;
		File zipFile = FileUtils.createFile(zipPath);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipArchiveOutputStream zos = null;
		
		try {
			fos = new FileOutputStream(zipFile);
			bos = new BufferedOutputStream(fos);
			zos = new ZipArchiveOutputStream(bos);
			for(String srcPath : srcPaths) {
				addFileToZip(zos, "", new File(srcPath));
			}
			zos.finish();
			
		} catch(Exception e){
			isOk = false;
			log.error("[ERROR-ZIP] 压缩文件集到 [{}] 失败.", zipPath, e);
			
		} finally {
			if(zos != null){
				try {
					zos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-ZIP] 关闭文件压缩流失败: [{}].", zipPath, e);
				}
			}
			
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-ZIP] 关闭文件压缩流失败: [{}].", zipPath, e);
				}
			}
			
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-ZIP] 关闭文件压缩流失败: [{}].", zipPath, e);
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 添加文件到zip压缩包
	 * @param zos
	 * @param baseDir
	 * @param file
	 * @throws IOException
	 */
	private static void addFileToZip(ZipArchiveOutputStream zos, 
			String baseDir, File file) throws IOException {
		String entryName = StrUtils.concat(baseDir, file.getName());
		ZipArchiveEntry zipEntry = new ZipArchiveEntry(file, entryName);
		zos.putArchiveEntry(zipEntry);
		
		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			IOUtils.copy(fis, zos);
			zos.closeArchiveEntry();
			fis.close();
			
		} else {
			zos.closeArchiveEntry();
			File[] cFiles = file.listFiles();
			for (File cFile : cFiles) {
				addFileToZip(zos, entryName.concat("/"), cFile);
			}
		}
	}
	
	/**
	 * 使用tar算法压缩文件
	 * 
	 * @param srcPath 被压缩的文件路径（路径包含文件名）
	 * @param tarPath 得到压缩文件的路径（路径包含文件名）
	 * @return true:压缩成功; false:压缩失败
	 */
	public static boolean toTar(String srcPath, String tarPath) {
		boolean isOk = true;
		File srcFile = new File(srcPath);
		File tarFile = FileUtils.createFile(tarPath);
		
		FileOutputStream fos = null;
		TarArchiveOutputStream tos = null;
		try{
			fos = new FileOutputStream(tarFile);
			tos = new TarArchiveOutputStream(fos);
			addFileToTar(tos, srcFile, "");
			tos.flush();
			
		} catch(Exception e){
			isOk = false;
			log.error("[ERROR-TAR] 压缩文件 [{}] 到 [{}] 失败.", srcPath, tarPath, e);
			
		} finally {
			if(tos != null){
				try {
					tos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-TAR] 关闭文件压缩流失败: [{}].", tarPath, e);
				}
			}
			
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-TAR] 关闭文件压缩流失败: [{}].", tarPath, e);
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 使用tar算法压缩多个文件，并打包.
	 * 
	 * @param srcPaths 被压缩的文件路径集（路径包含文件名）
	 * @param tarPath 得到压缩文件的路径（路径包含文件名）
	 * @return true:压缩成功; false:压缩失败
	 */
	public static boolean toTar(String[] srcPaths, String tarPath) {
		if(srcPaths == null || srcPaths.length <= 0 || 
				StrUtils.isEmpty(tarPath)) {
			return false;
		}
		
		if(!FileType.TAR.EXT.equals(FileUtils.getExtension(tarPath))) {
			return false;
		}
		
		boolean isOk = true;
		File tarFile = FileUtils.createFile(tarPath);
		FileOutputStream fos = null;
		TarArchiveOutputStream tos = null;
		try{
			fos = new FileOutputStream(tarFile);
			tos = new TarArchiveOutputStream(fos);
			for(String srcPath : srcPaths) {
				File srcFile = new File(srcPath);
				addFileToTar(tos, srcFile, "");
			}
			tos.flush();
			
		} catch(Exception e){
			isOk = false;
			log.error("[ERROR-TAR] 压缩文件集到 [{}] 失败.", tarPath, e);
			
		} finally {
			if(tos != null){
				try {
					tos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-TAR] 关闭文件压缩流失败: [{}].", tarPath, e);
				}
			}
			
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-TAR] 关闭文件压缩流失败: [{}].", tarPath, e);
				}
			}
		}
		return isOk;
	}
	
	private static void addFileToTar(TarArchiveOutputStream tos, 
			File file, String base) throws IOException {
		String entryName = StrUtils.concat(base, file.getName());
		TarArchiveEntry tarEntry = new TarArchiveEntry(file, entryName);
		tos.putArchiveEntry(tarEntry);
		
		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			IOUtils.copy(fis, tos);
			tos.closeArchiveEntry();
			fis.close();
			
		} else {
			tos.closeArchiveEntry();
			File[] cFiles = file.listFiles();
			for (File cFile : cFiles) {
				addFileToTar(tos, cFile, StrUtils.concat(entryName, "/"));
			}
		}
	}

	/**
	 * 使用gzip算法压缩文件
	 * 
	 * @param srcPath 被压缩的文件路径（路径包含文件名）
	 * @param gzipPath 得到压缩文件的路径（路径包含文件名）
	 * @return true:压缩成功; false:压缩失败
	 */
	public static boolean toGZip(String srcPath, String gzipPath) {
		File srcFile = new File(srcPath);
		if (srcFile.isDirectory()) {
			log.warn("[ERROR-GZIP] GZ不支持对目录压缩: [{}]", srcPath);
			return false;
		}
		
		boolean isOk = true;
		BufferedInputStream bis = null;
		GzipCompressorOutputStream gos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(srcFile));
			gos = new GzipCompressorOutputStream(new FileOutputStream(gzipPath));

			int cnt = -1;
			byte[] buf = new byte[UnitUtils._1_MB];
			while ((cnt = bis.read(buf)) > 0) {
				gos.write(buf, 0, cnt);
			}
			gos.flush();
			
		} catch(Exception e){
			isOk = false;
			log.error("[ERROR-GZIP] 压缩文件 [{}] 到 [{}] 失败.", srcPath, gzipPath, e);
			
		} finally {
			if (gos != null) {
				try {
					gos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-GZIP] 关闭文件输出流失败: [{}].", gzipPath, e);
				}
			}
			
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-GZIP] 关闭文件输入流失败: [{}].", srcFile, e);
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 使用bz2算法压缩文件
	 * 
	 * @param srcPath 被压缩的文件路径（路径包含文件名）
	 * @param bzPath 得到压缩文件的路径（路径包含文件名）
	 * @return true:压缩成功; false:压缩失败
	 */
	public static boolean toBZ2(String srcPath, String bzPath) {
		File srcFile = new File(srcPath);
		if (srcFile.isDirectory()) {
			log.warn("[ERROR-BZ2] BZ2不支持对目录压缩: [{}]", srcPath);
			return false;
		}
		
		boolean isOk = true;
		InputStream is = null;
		OutputStream os = null;
		BZip2CompressorOutputStream bos = null;
		try {
			is = new FileInputStream(srcFile);
			os = new FileOutputStream(new File(bzPath));
			bos = new BZip2CompressorOutputStream(os);

			int count;
			byte data[] = new byte[UnitUtils._1_MB];
			while ((count = is.read(data, 0, UnitUtils._1_MB)) != -1) {
				bos.write(data, 0, count);
			}
			bos.finish();
			bos.flush();
			
		} catch (Exception e) {
			isOk = false;
			log.error("[ERROR-BZ2] 压缩文件 [{}] 到 [{}] 失败.", srcPath, bzPath, e);
			
		} finally {
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-BZ2] 关闭文件压缩流失败: [{}].", bzPath, e);
				}
			}
			
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-BZ2] 关闭文件输出流失败: [{}].", bzPath, e);
				}
			}
			
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-BZ2] 关闭文件输入流失败: [{}].", srcFile, e);
				}
			}
		}
		return isOk;
	}
	
	/**
	 * <PRE>
	 * 解压文件.
	 * 	得到的解压文件与filePath同目录.
	 * </PRE>
	 * 
	 * @param filePath 被解压的文件路径（路径包含文件名）
	 * @return 解压文件所在的目录（若解压失败返回空串）
	 */
	public static String extract(String filePath) {
		String snkDir = PathUtils.getParentDir(filePath);
		return (extract(filePath, snkDir) ? snkDir : "");
	}
	
	/**
	 * 使用指定算法解压文件.
	 * 
	 * @param srcPath 被解压的文件路径（路径包含文件名）
	 * @param snkDir 得到解压文件的目录
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean extract(String srcPath, String snkDir) {
		if(StrUtils.isEmpty(srcPath) || StrUtils.isEmpty(snkDir)){
			log.warn("解压文件 [{}] 失败：源路径/目标路径为空.");
			return false;
		}
		
		FileType fileType = FileUtils.getFileType(srcPath);
		boolean isOk = false;
		try {
			if (FileType.ZIP == fileType) {
				isOk = unZip(srcPath, snkDir);
				
			} else if (FileType.TAR == fileType) {
				isOk = unTar(srcPath, snkDir);
				
			} else if (FileType.GZ == fileType) {
				isOk = unGZip(srcPath, snkDir);
				
			} else if (FileType.BZ2 == fileType) {
				isOk = unBZ2(srcPath, snkDir);
				
			} else {
				log.warn("解压文件 [{}] 失败： 不支持的压缩格式 [{}].", srcPath, fileType.NAME);
			}
		} catch (Exception e) {
			log.error("解压文件 [{}] 到 [{}] 失败.", srcPath, snkDir, e);
		}
		return isOk;
	}
	
	/**
	 * 使用zip算法解压文件到zipPath所在的目录
	 * @param zipPath 被解压的文件路径（路径包含文件名）
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean unZip(String zipPath) {
		String snkDir = PathUtils.getParentDir(zipPath);
		return unZip(zipPath, snkDir);
	}
	
	/**
	 * 使用zip算法解压文件
	 * 
	 * @param zipPath 被解压的文件路径（路径包含文件名）
	 * @param snkDir 得到解压文件的目录
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean unZip(String zipPath, String snkDir) {
		boolean isOk = true;
		FileUtils.createDir(snkDir);
		File zipFile = new File(zipPath);
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipFile);
			
			Enumeration<ZipArchiveEntry> entries = zip.getEntries();
			while (entries.hasMoreElements()) {
				ZipArchiveEntry zipEntry = entries.nextElement();
				String name = zipEntry.getName().trim();
				name = name.replace('\\', '/');
				File destFile = new File(snkDir, name);
				
				// 所解压的是目录
				if ("".equals(name) || name.endsWith("/")) {
					destFile.mkdirs();
					continue;

				// 所解压的是文件, 先创建其所有祖先目录
				} else if (name.indexOf('/') != -1) {
					File parentDir = destFile.getParentFile();
					if (!parentDir.exists() && !parentDir.mkdirs()) {
						isOk = false;
					}
				}

				// 解压文件
				FileOutputStream fos = new FileOutputStream(destFile);
				InputStream is = zip.getInputStream(zipEntry);
				int cnt = -1;
				byte[] buf = new byte[UnitUtils._1_MB];
				while ((cnt = is.read(buf)) != -1) {
					fos.write(buf, 0, cnt);
				}
				fos.close();
			}
		} catch (Exception e) {
			isOk = false;
			log.error("[ERROR-ZIP] 解压文件 [{}] 到 [{}] 失败.", zipPath, snkDir, e);
			
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-ZIP] 关闭文件压缩流失败: [{}].", zipPath, e);
				}
			}
		}
		return isOk;
	}

	/**
	 * 使用tar算法解压文件到tarPath所在的目录
	 * @param tarPath 被解压的文件路径（路径包含文件名）
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean unTar(String tarPath) {
		String snkDir = PathUtils.getParentDir(tarPath);
		return unTar(tarPath, snkDir);
	}
	
	/**
	 * 使用tar算法解压文件
	 * 
	 * @param tarPath 被解压的文件路径（路径包含文件名）
	 * @param snkDir 得到解压文件的目录
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean unTar(String tarPath, String snkDir) {
		boolean isOk = true;
		FileUtils.createDir(snkDir);
		File tarFile = new File(tarPath);
		TarArchiveInputStream tis = null;
		try {
			tis = new TarArchiveInputStream(new FileInputStream(tarFile));
			TarArchiveEntry tarEntry = null;
			while ((tarEntry = tis.getNextTarEntry()) != null) {
				String name = tarEntry.getName().trim();
				name = name.replace('\\', '/');
				File destFile = new File(snkDir, name);
				
				// 所解压的是目录
				if ("".equals(name) || name.endsWith("/")) {
					destFile.mkdirs();
					continue;

				// 所解压的是文件, 先创建其所有祖先目录
				} else if (name.indexOf('/') != -1) {
					File parentDir = destFile.getParentFile();
					if (!parentDir.exists() && !parentDir.mkdirs()) {
						isOk = false;
					}
				}

				// 解压文件
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				int count = -1;
				byte buf[] = new byte[UnitUtils._1_MB];
				while ((count = tis.read(buf)) != -1) {
					bos.write(buf, 0, count);
				}
				bos.close();
				fos.close();
			}
		} catch (Exception e) {
			isOk = false;
			log.error("[ERROR-TAR] 解压文件 [{}] 到 [{}] 失败.", tarPath, snkDir, e);
			
		} finally {
			if (tis != null) {
				try {
					tis.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-TAR] 关闭文件压缩流失败: [{}].", tarPath, e);
				}
			}
		}
		return isOk;
	}

	/**
	 * 使用gzip算法解压文件到gzipPath所在的目录
	 * @param gzipPath 被解压的文件路径（路径包含文件名）
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean unGZip(String gzipPath) {
		String snkDir = PathUtils.getParentDir(gzipPath);
		return unGZip(gzipPath, snkDir);
	}
	
	/**
	 * 使用gzip算法解压文件
	 * 
	 * @param gzipPath 被解压的文件路径（路径包含文件名）
	 * @param snkDir 得到解压文件的目录
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean unGZip(String gzipPath, String snkDir) {
		boolean isOk = true;
		FileUtils.createDir(snkDir);
		GZIPInputStream gis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		try {
			gis = new GZIPInputStream(new FileInputStream(gzipPath));
			bis = new BufferedInputStream(gis);
			fos = new FileOutputStream(snkDir);

			int cnt = 0;
			byte[] buf = new byte[UnitUtils._1_MB];
			while ((cnt = bis.read(buf)) != -1) {
				fos.write(buf, 0, cnt);
			}
		} catch (Exception e) {
			isOk = false;
			log.error("[ERROR-GZIP] 解压文件 [{}] 到 [{}] 失败.", gzipPath, snkDir, e);
			
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-GZIP] 关闭文件输出流失败: [{}].", snkDir, e);
				}
			}
			
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-GZIP] 关闭文件输入流失败: [{}].", gzipPath, e);
				}
			}
			
			if (gis != null) {
				try {
					gis.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-GZIP] 关闭文件输入流失败: [{}].", gzipPath, e);
				}
			}
		}
		return isOk;
	}

	/**
	 * 使用bz2算法解压文件到bzPath所在的目录
	 * @param bzPath 被解压的文件路径（路径包含文件名）
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean unBZ2(String bzPath) {
		String snkDir = PathUtils.getParentDir(bzPath);
		return unBZ2(bzPath, snkDir);
	}
	
	/**
	 * 使用bz2算法解压文件
	 * 
	 * @param bzPath 被解压的文件路径（路径包含文件名）
	 * @param snkDir 得到解压文件的目录
	 * @return true:解压成功; false:解压失败
	 */
	public static boolean unBZ2(String bzPath, String snkDir) {
		boolean isOk = true;
		FileUtils.createDir(snkDir);
		BZip2CompressorInputStream bzis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		try {
			bzis = new BZip2CompressorInputStream(new FileInputStream(bzPath));
			bis = new BufferedInputStream(bzis);
			fos = new FileOutputStream(snkDir);

			int cnt = 0;
			byte[] buf = new byte[UnitUtils._1_MB];
			while ((cnt = bis.read(buf)) != -1) {
				fos.write(buf, 0, cnt);
			}
		} catch (Exception e) {
			isOk = false;
			log.error("[ERROR-BZ2] 解压文件 [{}] 到 [{}] 失败.", bzPath, snkDir, e);
			
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-BZ2] 关闭文件输出流失败: [{}].", snkDir, e);
				}
			}
			
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-BZ2] 关闭文件输入流失败: [{}].", bzPath, e);
				}
			}
			
			if (bzis != null) {
				try {
					bzis.close();
				} catch (IOException e) {
					isOk = false;
					log.error("[ERROR-BZ2] 关闭文件输入流失败: [{}].", bzPath, e);
				}
			}
		}
		return isOk;
	}

	/**
	 * <PRE>
	 * 把字符串以【GZIP方式】进行压缩，并得到【压缩串】的【16进制表示形式】.
	 * 
	 * 被压缩的字符串越长，压缩率越高。
	 * 对于很短的字符串，压缩后可能变得更大，因为GZIP的文件头需要存储压缩字典（约20字节）
	 * 
	 * 返回16进制的表示形式是为了便于对压缩串进行存储、复制等，
	 * 否则一堆乱码是不便于处理的。
	 * 但缺点是16进制显示形式会直接把压缩串的长度在原来基础上扩展1倍（原本1个字节被拆分成高低位两个字符）。
	 * </PRE>
	 * 
	 * @param str 原字符串（默认为UTF-8编码）
	 * @return 【压缩串】的【16进制表示形式】, 压缩失败则返回空串（非null）
	 */
	public static String toGZipString(final String str) {
		return toGZipString(str, DEFAULT_ENCODE);
	}
	
	/**
	 * <PRE>
	 * 把字符串以【GZIP方式】进行压缩，并得到【压缩串】的【16进制表示形式】.
	 * ---------------------------------------------------------
	 * 被压缩的字符串越长，压缩率越高。
	 * 对于很短的字符串，压缩后可能变得更大，因为GZIP的文件头需要存储压缩字典（约20字节）
	 * 
	 * 返回16进制的表示形式是为了便于对压缩串进行存储、复制等，
	 * 否则一堆乱码是不便于处理的。
	 * 但缺点是16进制显示形式会直接把压缩串的长度在原来基础上扩展1倍（原本1个字节被拆分成高低位两个字符）。
	 * </PRE>
	 * 
	 * @param str 原字符串
	 * @param encode 原字符串编码
	 * @return 【压缩串】的【16进制表示形式】, 压缩失败则返回空串（非null）
	 */
	public static String toGZipString(final String str, final String encode) {
		String hex = "";
		if (str != null && str.length() > 0) {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				GZIPOutputStream gos = new GZIPOutputStream(bos);
				gos.write(str.getBytes(encode));
				gos.close();
				bos.close();
				hex = BODHUtils.toHex(bos.toByteArray());
				
			} catch (Exception e) {
				log.error("压缩字符串失败: [{}]", StrUtils.showSummary(str), e);
			}
		}
		return hex;
	}
	
	/**
	 * 把【16进制表示形式】的、以【GZIP方式】压缩的【压缩串】还原为原字符串（默认原字符串的编码方式为UTF-8）
	 * 
	 * @param hex 【16进制表示形式】的、以【GZIP方式】压缩的【压缩串】
	 * @return 原字符串, 还原失败则返回空串（非null）
	 */
	public static String unGZipString(final String hex) {
		return unGZipString(hex, DEFAULT_ENCODE);
	}

	/**
	 * 把【16进制表示形式】的、以【GZIP方式】压缩的【压缩串】还原为原字符串
	 * 
	 * @param hex 【16进制表示形式】的、以【GZIP方式】压缩的【压缩串】
	 * @param encode 原字符串的编码方式
	 * @return 原字符串, 还原失败则返回空串（非null）
	 */
	public static String unGZipString(String hex, final String encode) {
		String str = "";
		if (hex == null) {
			return str;
		}
		
		hex = hex.trim();
		if(hex.length() <= 0 || hex.length() % 2 == 1) {
			return str;
		}
			
		byte[] bytes = BODHUtils.toBytes(hex);
		if(bytes != null) {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				GZIPInputStream gis = new GZIPInputStream(bis);
				byte[] buf = new byte[UnitUtils._1_MB];
				int cnt = -1;
				while ((cnt = gis.read(buf)) >= 0) {
					bos.write(buf, 0, cnt);
				}
				str = bos.toString(encode);
				bos.close();
				gis.close();
				bis.close();
				
			} catch (Exception e) {
				log.error("解压字符串失败: [{}]", StrUtils.showSummary(hex), e);
			}
		}
		return str;
	}
	
}

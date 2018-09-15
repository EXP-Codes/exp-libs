package exp.libs.warp.conf.ini;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dtools.ini.BasicIniFile;
import org.dtools.ini.BasicIniSection;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileReader;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <PRE>
 * INI配置器.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public final class IniUtils {
//
//	/** 日志器 */
//	private Logger log = LoggerFactory.getLogger(PcIdMgr.class);
//	
//	/** 项目部署路径的标识 */
//	private final static String PRO_PATH = "PRO_PATH";
//	
//	private final static String PATH = "path";
//	
//	/** 正常退出程序的标识 */
//	private final static String PROPER_EXIT = "PROPER_EXIT";
//	
//	private final static String CUR_PC_ID = "cur_pc_id";	//inclusive
//	
//	private final static String MAX_PC_ID = "max_pc_id";	//exclusive
//	
//	private final static String SEPARATOR_LINE = "---------------------------";
//	
//	private final static int CACHE_ID_NUM = 1000;
//	
//	private final static String PCID_FILE_PATH = "./conf/pcid.ini";
//	
//	private IniFile iniFile;
//	
//	private IniFileReader reader;
//	
//	private IniFileWriter writer;
//	
//	/** 单例 */
//	private static volatile PcIdMgr instance;
//	
//	/**
//	 * 私有化构造函数
//	 */
//	private PcIdMgr() {
//		this.iniFile = new BasicIniFile();
//		File file = new File(PCID_FILE_PATH);
//		this.reader = new IniFileReader(iniFile, file);
//		this.writer = new IniFileWriter(iniFile, file);
//	}
//	
//	/**
//	 * 获取单例
//	 * @return 单例
//	 */
//	public static PcIdMgr getInstn() {
//		if(instance == null) {
//			synchronized (PcIdMgr.class) {
//				if(instance == null) {
//					instance = new PcIdMgr();
//					instance.init();
//				}
//			}
//		}
//		return instance;
//	}
//	
//	private void init() {
//		read();
//		
//		// 部署路径变更检查
//		String curProPath = PathUtils.getProjectRootPath();
//		IniSection proPath = iniFile.getSection(PRO_PATH);
//		if(proPath == null) {
//			log.warn("程序属全新部属运行, 清空本地pcid缓存文件 [{}] 既有记录.", PCID_FILE_PATH);
//			iniFile.removeAll();
//			
//			addProPathSection(curProPath);
//			write();
//			return;
//			
//		} else {
//			IniItem lastProPath = proPath.getItem(PATH);
//			if(lastProPath == null || !curProPath.equals(lastProPath.getValue())) {
//				log.warn("程序被复制/迁移部署运行, 清空本地pcid缓存文件 [{}] 既有记录.", PCID_FILE_PATH);
//				iniFile.removeAll();
//				
//				addProPathSection(curProPath);
//				write();
//				return;
//			}
//		}
//		
//		// 首次(非正常)运行检查
//		IniSection isProperExit = iniFile.getSection(PROPER_EXIT);
//		if(isProperExit == null) {
//			log.warn("程序属首次运行(或上次非正常退出), 清空本地pcid缓存文件 [{}] 既有记录.", PCID_FILE_PATH);
//			iniFile.removeAll();
//			
//			addProPathSection(curProPath);
//			write();
//			return;
//		}
//		iniFile.removeSection(isProperExit);
//		
//		// 删除某些网管的无效pcid主题(避免人为改动pcid)
//		List<String> delSections = new LinkedList<String>();
//		for(Iterator<IniSection> sections = iniFile.getSections().iterator();
//				sections.hasNext();) {
//			IniSection section = sections.next();
//			if(PRO_PATH.equals(section.getName())) {
//				continue;
//			}
//			
//			IniItem curPcId = section.getItem(CUR_PC_ID);
//			IniItem maxPcId = section.getItem(MAX_PC_ID);
//			
//			if(curPcId == null || maxPcId == null) {
//				delSections.add(section.getName());
//			} else {
//				long iCurPcId = NumUtils.toLong(curPcId.getValue());
//				long iMAxPcId = NumUtils.toLong(maxPcId.getValue());
//				if(iCurPcId < 0 || iMAxPcId < 0 || iCurPcId >= iMAxPcId) {
//					delSections.add(section.getName());
//				}
//			}
//		}
//		
//		for(String section : delSections) {
//			iniFile.removeSection(section);
//		}
//		write();
//	}
//
//	public void destory() {
//		addExitMarkSection();
//		write();
//	}
//	
//	private void addProPathSection(String proPath) {
//		IniSection section = new BasicIniSection(PRO_PATH);
//		IniItem item = new IniItem(PATH);
//		item.setValue(proPath);
//		section.addItem(item);
//		section.addItem(new IniItem(SEPARATOR_LINE));
//		iniFile.addSection(section, 0);
//	}
//	
//	private void addExitMarkSection() {
//		IniSection section = new BasicIniSection(PROPER_EXIT);
//		section.addItem(new IniItem(SEPARATOR_LINE));
//		iniFile.addSection(section, 1);
//	}
//	
//	private void coverSection(IniSection section) {
//		if(section != null) {
//			iniFile.removeSection(section.getName());
//			iniFile.addSection(section);
//			write();
//		}
//	}
//	
//	public String getPcId(EmsInfo emsInfo) {
//		String pcId = "unknow";
//		if(emsInfo != null) {
//			IniSection section = iniFile.getSection(emsInfo.getEmsId());
//			if(section == null) {
//				section = syncPcId(emsInfo);
//				coverSection(section);
//			}
//			
//			String sCurPcId = section.getItem(CUR_PC_ID).getValue();
//			String sMaxPcId = section.getItem(MAX_PC_ID).getValue();
//			long iCurPcId = NumUtils.toLong(sCurPcId);
//			long iMaxPcId = NumUtils.toLong(sMaxPcId);
//			
//			pcId = String.valueOf(iCurPcId);
//			iCurPcId++;
//			section.getItem(CUR_PC_ID).setValue(iCurPcId);
//			
//			// id已耗尽
//			if(iCurPcId >= iMaxPcId) {
//				section = syncPcId(emsInfo);
//				coverSection(section);
//			}
//		}
//		return pcId;
//	}
//	
//	private IniSection syncPcId(EmsInfo emsInfo) {
//		IniSection section = null;
//		
//		final int retryLimt = 100;
//		int cnt = 0;
//		do {
//			ISession session = SessionFactory.getShortOutSession(emsInfo);
//			section = syncPcId(emsInfo.getEmsId(), session);
//			SessionFactory.close(session);
//		} while(section == null && ++cnt < retryLimt);
//		
//		if(section == null) {
//			log.error("连续10次同步pcid失败, 程序退出.");
//			System.err.println("连续10次同步pcid失败, 程序退出.");
//			System.exit(0);
//		}
//		return section;
//	}
//	
//	private IniSection syncPcId(String emsId, ISession session) {
//		IniSection section = null;
//		long iCurPcId = session.getPcId(CACHE_ID_NUM);
//		
//		if(iCurPcId > 0) {
//			section = new BasicIniSection(emsId);
//			long iMaxPcId = iCurPcId + CACHE_ID_NUM;
//			
//			IniItem curPcId = new IniItem(CUR_PC_ID);
//			IniItem maxPcId = new IniItem(MAX_PC_ID);
//			curPcId.setValue(iCurPcId);
//			maxPcId.setValue(iMaxPcId);
//			section.addItem(curPcId);
//			section.addItem(maxPcId);
//			section.addItem(new IniItem(SEPARATOR_LINE));
//		}
//		return section;
//	}
//
//	private void read() {
//		try {
//			reader.read();
//		} catch (Exception e) {
//			log.error("读取 [{}] 文件失败.", PCID_FILE_PATH, e);
//		}
//	}
//	
//	private void write() {
//		try {
//			writer.write();
//		} catch (Exception e) {
//			log.error("更新 [{}] 文件失败.", PCID_FILE_PATH, e);
//		}
//	}
	
}

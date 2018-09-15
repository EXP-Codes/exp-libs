package exp.libs.warp.io.flow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * 文件段读取器 - 需配合[文件流读取器]使用.
 * 
 * 使用限制: 
 *   1.完整的 [段] 不能被拆分到不同的文件中, 否则该[段]会被完全抛弃.
 *   2.由于是流式读取, 只要中途有一 [段] 发生错位, 后面即使还有 [段] 也读取不了.
 *   
 * 使用示例:
 * 	FileFlowReader ffr = new FileFlowReader(FILE_PATH, Charset.UTF8);
 * 	final char LINE_END = '\n';
 * 	final String SEG_HEAD = "== bgn ==";
 *  final String SEG_TAIL = "== end ==";
 * 	FileSegmentReader fsr = new FileSegmentReader(ffr, LINE_END, SEG_HEAD, SEG_TAIL);
 *  while(fsr.hasNextSegment()) {
 *  	String segment = fsr.getSegment();
 *  	// ... do for segment
 *  }
 *  fsr.close();
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class FileSegmentReader {

	/** 文件流读取器对象 */
	private FileFlowReader ffr;
	
	/** 文件流的行终止符 */
	private char lineEnd;
	
	/** 段首标识(凭[段首所在行] endWith 进行匹配) */
	private String segHead;
	
	/** 段尾标识(凭[段尾所在行] endWith 进行匹配) */
	private String segTail;
	
	/** 标记是否存在可读的下一段 */
	private boolean hasNextSegment;
	
	/**
	 * 所有[段]中希望被忽略的[子段]的[首尾标识].
	 * [子段头标识]: 凭[子段首所在行] endWith 进行匹配. 不能为空串.
	 * [子段尾标识]: 凭[子段尾所在行] endWith 进行匹配. 若尾为空串"", 则希望跳过子段是[子段头标识]的[单行].
	 * 
	 * 子段头 -> 子段尾
	 */
	private Map<String, String> skipSubSegmentHTs;
	
	/**
	 * 构造函数
	 * @param ffr 文件流读取器对象
	 * @param lineEnd 文件流的行终止符
	 * @param segmentHead 段首标识
	 * @param segmentTail 段尾标识
	 */
	public FileSegmentReader(FileFlowReader ffr, char lineEnd, 
			String segmentHead, String segmentTail) {
		init(ffr, lineEnd, segmentHead, segmentTail, null, null);
	}
	
	/**
	 * 构造函数
	 * @param ffr 文件流读取器对象
	 * @param lineEnd 文件流的行终止符
	 * @param segmentHead 段首标识
	 * @param segmentTail 段尾标识
	 * @param skipSubSegmentHeads 希望忽略的子段首标识集(必须与子段尾标识集一一顺序对应, 否则全部无效)
	 * @param skipSubSegmentTails 希望忽略的子段尾标识集(必须与子段首标识集一一顺序对应, 否则全部无效)
	 */
	public FileSegmentReader(FileFlowReader ffr, char lineEnd, 
			String segmentHead, String segmentTail, 
			List<String> skipSubSegmentHeads, List<String> skipSubSegmentTails) {
		init(ffr, lineEnd, segmentHead, segmentTail, 
				skipSubSegmentHeads, skipSubSegmentTails);
	}
	
	/**
	 * 初始化
	 * @param ffr 文件流读取器对象
	 * @param lineEnd 文件流的行终止符
	 * @param segmentHead 段首标识
	 * @param segmentTail 段尾标识
	 * @param skipSubSegmentHeads 希望忽略的子段首标识集(必须与子段尾标识集一一顺序对应, 否则全部无效)
	 * @param skipSubSegmentTails 希望忽略的子段尾标识集(必须与子段首标识集一一顺序对应, 否则全部无效)
	 */
	private void init(FileFlowReader ffr, char lineEnd, 
			String segmentHead, String segmentTail, 
			List<String> skipSubSegmentHeads, List<String> skipSubSegmentTails) {
		this.ffr = ffr;
		this.lineEnd = lineEnd;
		this.segHead = segmentHead;
		this.segTail = segmentTail;
		this.hasNextSegment = true;
		
		if(skipSubSegmentHeads != null && skipSubSegmentTails != null && 
				skipSubSegmentHeads.size() == skipSubSegmentTails.size()) {
			this.skipSubSegmentHTs = new HashMap<String, String>();
			
			for(int i = 0; i < skipSubSegmentHeads.size(); i++) {
				String subHead = skipSubSegmentHeads.get(i);
				String subTail = skipSubSegmentTails.get(i);
				if(subHead == null || "".equals(subHead)) {
					continue;
				}
				subTail = (subTail == null ? "" : subTail);
				skipSubSegmentHTs.put(subHead, subTail);
			}
			
			if(skipSubSegmentHTs.isEmpty()) { 
				skipSubSegmentHTs = null;
			}
		}
	}
	
	/**
	 * 当前文件流是否存在下一段
	 * @return true:存在; false:不存在
	 */
	public boolean hasNextSegment() {
		return hasNextSegment;
	}
	
	/**
	 * 读取当前段.
	 * 	此方法需配合 hasNextSegment 方法使用（类似迭代器的使用方式）.
	 * @return 当前段数据（希望被忽略的子段数据不会在段内）
	 */
	public String getSegment() {
		String segment = "";
		if(!hasNextSegment) {
			return segment;
		}
		
		StringBuilder sb = new StringBuilder();
		boolean isFindingHeader = true;
		boolean isFindingTail = false;
		
		while(ffr.hasNextLine()) {
			String line = ffr.readLine(lineEnd);
			
			// 正在找头
			if(isFindingHeader) {
				if(line.endsWith(segHead)) { // 找到头
					sb.append(line);
					
					isFindingHeader = false;
					isFindingTail = true;
				}
				continue;
			}
			
			// 检查是否需要跳过子段
			if(skipSubSegment(line)) {
				continue;
			}
			
			// 正在找尾
			if(isFindingTail) {
				sb.append(line);
				
				if(line.endsWith(segTail)) { // 找到尾
					isFindingHeader = true;
					isFindingTail = false;
					break;
				}
			}
		}
		
		// 直到文件尾终止还找不到尾部，舍弃已找到的部分
		if(isFindingTail) {
			sb.setLength(0);
		}
		
		segment = sb.toString();
		if("".equals(segment)) {
			hasNextSegment = false;
			ffr.close();	// 再也找不到[段]的时候就自动关闭数据流
		}
		return sb.toString();
	}
	
	/**
	 * 检查该行是否会是某个应该被忽略的子段的段首 
	 * @param line 当前行
	 * @return true:当前行是被希望忽略的子段的起点; false:当前行不是被希望忽略的子段的起点
	 */
	private boolean skipSubSegment(final String line) {
		boolean isSkip = false;
		if(skipSubSegmentHTs != null) {
			String subTail = null;
			for(Iterator<String> subHeads = skipSubSegmentHTs.keySet().iterator(); 
					subHeads.hasNext();) {
				String subHead = subHeads.next();
				if(line.endsWith(subHead)) {
					subTail = skipSubSegmentHTs.get(subHead);
					break;
				}
			}
			
			if(subTail != null) {
				isSkip = true;
				
				// 仅跳过本行
				if("".equals(subTail)) {
					//Undo
					
				// 跳过一段
				} else {
					while(ffr.hasNextLine()) {
						String nextLine = ffr.readLine(lineEnd);
						if(nextLine.endsWith(subTail)) {
							break;
						}
					}
				}
			}
		}
		return isSkip;
	}

	/**
	 * 连续读取后续的若干段.
	 * 	此方法需配合 hasNextSegment 方法使用（类似迭代器的使用方式）.
	 * @param num 期望最大得到的段数（若文件已读到末尾，则可能不会达到该数目）
	 * @return 若干段数据（希望被忽略的子段数据不会在段内）
	 */
	public List<String> getSegments(int num) {
		List<String> segments = new LinkedList<String>();
		if(!hasNextSegment) {
			return segments;
		}
		
		for(int i = 0; i < num && hasNextSegment; i++) {
			String segment = getSegment();
			if(!"".equals(segment)) {
				segments.add(segment);
			}
		}
		return segments;
	}
	
	/**
	 * 关闭文件流读取器
	 */
	public void close() {
		ffr.close();
	}
}

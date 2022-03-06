package exp.libs.warp.ui.cpt.tbl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * <PRE>
 * 表单组件
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-07-05
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public abstract class AbstractTable extends _BaseTable {

	/** serialVersionUID */
	private static final long serialVersionUID = 191838444436258900L;

	/**
	 * 
	 * @param headers 表头
	 * @param maxViewRow 最大呈现的行数
	 */
	public AbstractTable(List<String> headers, int maxViewRow) {
		super(headers, maxViewRow);
	}
	
	/**
	 * 
	 * @param headers 表头
	 * @param maxViewRow 最大呈现的行数
	 */
	public AbstractTable(String[] headers, int maxViewRow) {
		super((headers != null ? Arrays.asList(headers) : null), maxViewRow);
	}
	
	/**
	 * 刷新表单
	 */
	public void reflash() {
		super._reflash(null);
	}
	
	/**
	 * 刷新表单数据
	 * @param datas 新的表单数据
	 */
	public void reflash(List<List<String>> datas) {
		if(datas == null) {
			super._reflash(null);
			
		} else {
			Vector<Vector<String>> vDatas = new Vector<Vector<String>>();
			for(List<String> rowData : datas) {
				vDatas.add(new Vector<String>(rowData));
			}
			super._reflash(vDatas);
		}
	}
	
	/**
	 * 新增一行数据
	 * @param rowData 行数据
	 * @param rowIdx 插入位置
	 */
	public void add(List<String> rowData, int rowIdx) {
		if(rowData == null) {
			return;
		}
		super._add(new Vector<String>(rowData), rowIdx);
	}
	
	/**
	 * 插入到顶端
	 * @param rowData 行数据
	 */
	public void addTop(List<String> rowData) {
		add(rowData, 0);
	}
	
	/**
	 * 插入到底部
	 * @param rowData 行数据
	 */
	public void addBtm(List<String> rowData) {
		add(rowData, Integer.MAX_VALUE);
	}
	
	/**
	 * 插入到表单（默认在顶端）
	 * @param rowData 行数据
	 */
	public void add(List<String> rowData) {
		addTop(rowData);
	}
	
	/**
	 * 删除行数据
	 * @param rowIdx 行索引
	 * @return 被删除的行数据
	 */
	public List<String> del(int rowIdx) {
		List<String> rowData = new LinkedList<String>();
		Vector<String> delData = super._del(rowIdx);
		if(delData != null) {
			rowData.addAll(delData);
		}
		return rowData;
	}
	
	/**
	 * 获取行数据
	 * @param rowIdx 行索引
	 * @return 指定行数据
	 */
	public List<String> getRowData(int rowIdx) {
		List<String> rowData = new LinkedList<String>();
		Vector<String> data = super._getRowData(rowIdx);
		if(data != null) {
			rowData.addAll(data);
		}
		return rowData;
	}
	
	/**
	 * 获取当前选择行的行数据
	 * @return 当前选择行的行数据
	 */
	public List<String> getSelectedRowData() {
		return getRowData(getCurSelectRow());
	}
	
}

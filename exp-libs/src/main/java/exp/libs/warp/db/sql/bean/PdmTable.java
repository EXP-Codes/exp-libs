package exp.libs.warp.db.sql.bean;

import java.util.LinkedList;
import java.util.List;

/**
 * <PRE>
 * PDM物理模型 - 表.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class PdmTable {

	private String tableName;

	private List<PdmColumn> columns;

	public PdmTable() {
		this.columns = new LinkedList<PdmColumn>();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<PdmColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<PdmColumn> columns) {
		this.columns = columns;
	}
	
}

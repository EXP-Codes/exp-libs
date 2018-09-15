package exp.libs.bean.test;

import java.sql.Connection;
import java.util.List;

import exp.libs.warp.db.sql.DBUtils;

/**
 * <PRE>
 * Table Name : t_mt_fault
 * Class Name : TMtFault
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-01-08 21:32:06
 * @author    EXP: 272629724@qq.com
 * @since     jdk version : jdk 1.6
 */
public class TMtFault  {
	
    /** insert sql */
    public final static String SQL_INSERT = 
            "INSERT INTO t_mt_fault(id, ems_id, task_id, faultwsif, faultcode, faultstring, detail, D_INSERT_TIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    
    /** delete sql */
    public final static String SQL_DELETE = 
            "DELETE FROM t_mt_fault WHERE 1 = 1 ";
    
    /** update sql */
    public final static String SQL_UPDATE = 
            "UPDATE t_mt_fault SET ems_id = ?, task_id = ?, faultwsif = ?, faultcode = ?, faultstring = ?, detail = ?, D_INSERT_TIME = ? WHERE 1 = 1 ";
    
    /** select sql */
    public final static String SQL_SELECT = 
            "SELECT id AS 'id', ems_id AS 'emsId', task_id AS 'taskId', faultwsif AS 'faultwsif', faultcode AS 'faultcode', faultstring AS 'faultstring', detail AS 'detail', D_INSERT_TIME AS 'insertTime' FROM t_mt_fault WHERE 1 = 1 ";

    /** id */
    private Integer id;

    /** ems_id */
    private Integer emsId;

    /** task_id */
    private Integer taskId;

    /** faultwsif */
    private String faultwsif;

    /** faultcode */
    private String faultcode;

    /** faultstring */
    private String faultstring;

    /** detail */
    private String detail;

    /** D_INSERT_TIME */
    private java.sql.Timestamp insertTime;

    /**
     * insert the bean of TMtFault to db.
     * 
     * @param conn : the connection of db
     * @param bean : a bean of the data
     * @return effect of row number
     */
    public static boolean insert(Connection conn, TMtFault bean) {
        Object[] params = new Object[] {
                bean.getId(),
                bean.getEmsId(),
                bean.getTaskId(),
                bean.getFaultwsif(),
                bean.getFaultcode(),
                bean.getFaultstring(),
                bean.getDetail(),
                bean.getInsertTime()
        };
        return DBUtils.execute(conn, TMtFault.SQL_INSERT, params);
    }
    
    /**
     * delete some bean of TMtFault from db with some conditions.
     * 
     * @param conn : the connection of db
     * @param where : 
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return effect of row number
     */
    public static boolean delete(Connection conn, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TMtFault.SQL_DELETE);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        return DBUtils.execute(conn, sql.toString());
    }
    
    /**
     * update some bean of TMtFault to db with some conditions.
     * 
     * @param conn : the connection of db
     * @param bean : a bean of the data
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return effect of row number
     */
    public static boolean update(Connection conn, TMtFault bean, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TMtFault.SQL_UPDATE);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        Object[] params = new Object[] {
                bean.getEmsId(),
                bean.getTaskId(),
                bean.getFaultwsif(),
                bean.getFaultcode(),
                bean.getFaultstring(),
                bean.getDetail(),
                bean.getInsertTime()
        };
        return DBUtils.execute(conn, sql.toString(), params);
    }    
    
    /**
     * query all beans of TMtFault from db.
     * 
     * @param conn : the connection of db
     * @return all beans of the data set
     */
    public static List<TMtFault> queryAll(Connection conn) {
        return querySome(conn, null);
    }
    
    /**
     * query a bean of TMtFault from db with some conditions.
     * If the conditions <B>can't</B> lock the range of one record,
     * you will get <B>the first record</B> or <B>null</B>.
     *
     * @param conn : the connection of db
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return a beans of the data
     */
    public static TMtFault queryOne(Connection conn, String where) {
        TMtFault bean = null;
        List<TMtFault> beans = querySome(conn, where);
        if(beans != null && beans.size() > 0) {
            bean = beans.get(0);
        }
        return bean;
    }
    
    /**
     * query some beans of TMtFault from db with some conditions.
     * 
     * @param conn : the connection of db
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return some beans of the data set
     */
    public static List<TMtFault> querySome(Connection conn, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TMtFault.SQL_SELECT);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        return DBUtils.query(TMtFault.class, conn, sql.toString());
    }
    
    /**
     * getId
     * @return Integer
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * setId
     * @param id id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * getEmsId
     * @return Integer
     */
    public Integer getEmsId() {
        return this.emsId;
    }

    /**
     * setEmsId
     * @param emsId emsId to set
     */
    public void setEmsId(Integer emsId) {
        this.emsId = emsId;
    }

    /**
     * getTaskId
     * @return Integer
     */
    public Integer getTaskId() {
        return this.taskId;
    }

    /**
     * setTaskId
     * @param taskId taskId to set
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * getFaultwsif
     * @return String
     */
    public String getFaultwsif() {
        return this.faultwsif;
    }

    /**
     * setFaultwsif
     * @param faultwsif faultwsif to set
     */
    public void setFaultwsif(String faultwsif) {
        this.faultwsif = faultwsif;
    }

    /**
     * getFaultcode
     * @return String
     */
    public String getFaultcode() {
        return this.faultcode;
    }

    /**
     * setFaultcode
     * @param faultcode faultcode to set
     */
    public void setFaultcode(String faultcode) {
        this.faultcode = faultcode;
    }

    /**
     * getFaultstring
     * @return String
     */
    public String getFaultstring() {
        return this.faultstring;
    }

    /**
     * setFaultstring
     * @param faultstring faultstring to set
     */
    public void setFaultstring(String faultstring) {
        this.faultstring = faultstring;
    }

    /**
     * getDetail
     * @return String
     */
    public String getDetail() {
        return this.detail;
    }

    /**
     * setDetail
     * @param detail detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * getInsertTime
     * @return java.sql.Timestamp
     */
    public java.sql.Timestamp getInsertTime() {
        return this.insertTime;
    }

    /**
     * setInsertTime
     * @param insertTime insertTime to set
     */
    public void setInsertTime(java.sql.Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * get column name
     * @return id
     */
    public static String getId$CN() {
        return "id";
    }

    /**
     * get column name
     * @return ems_id
     */
    public static String getEmsId$CN() {
        return "ems_id";
    }

    /**
     * get column name
     * @return task_id
     */
    public static String getTaskId$CN() {
        return "task_id";
    }

    /**
     * get column name
     * @return faultwsif
     */
    public static String getFaultwsif$CN() {
        return "faultwsif";
    }

    /**
     * get column name
     * @return faultcode
     */
    public static String getFaultcode$CN() {
        return "faultcode";
    }

    /**
     * get column name
     * @return faultstring
     */
    public static String getFaultstring$CN() {
        return "faultstring";
    }

    /**
     * get column name
     * @return detail
     */
    public static String getDetail$CN() {
        return "detail";
    }

    /**
     * get column name
     * @return D_INSERT_TIME
     */
    public static String getInsertTime$CN() {
        return "D_INSERT_TIME";
    }

    /**
     * get java name
     * @return id
     */
    public static String getId$JN() {
        return "id";
    }

    /**
     * get java name
     * @return emsId
     */
    public static String getEmsId$JN() {
        return "emsId";
    }

    /**
     * get java name
     * @return taskId
     */
    public static String getTaskId$JN() {
        return "taskId";
    }

    /**
     * get java name
     * @return faultwsif
     */
    public static String getFaultwsif$JN() {
        return "faultwsif";
    }

    /**
     * get java name
     * @return faultcode
     */
    public static String getFaultcode$JN() {
        return "faultcode";
    }

    /**
     * get java name
     * @return faultstring
     */
    public static String getFaultstring$JN() {
        return "faultstring";
    }

    /**
     * get java name
     * @return detail
     */
    public static String getDetail$JN() {
        return "detail";
    }

    /**
     * get java name
     * @return insertTime
     */
    public static String getInsertTime$JN() {
        return "insertTime";
    }

    /**
     * get all column names
     * @return String
     */
    public static String getAllColNames() {
        return "id, ems_id, task_id, faultwsif, faultcode, faultstring, detail, D_INSERT_TIME";
    }

    /**
     * get all java names
     * @return String
     */
    public static String getAllJavaNames() {
        return "id, emsId, taskId, faultwsif, faultcode, faultstring, detail, insertTime";
    }

    /**
     * get table name
     * @return String
     */
    public static String getTableName() {
        return "t_mt_fault";
    }

    /**
     * get class name
     * @return String
     */
    public static String getClassName() {
        return "TMtFault";
    }
    
    /**
     * toString
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("t_mt_fault/TMtFault: {\r\n");
        sb.append("\tid/id").append(" = ").append(this.getId()).append("\r\n");
        sb.append("\tems_id/emsId").append(" = ").append(this.getEmsId()).append("\r\n");
        sb.append("\ttask_id/taskId").append(" = ").append(this.getTaskId()).append("\r\n");
        sb.append("\tfaultwsif/faultwsif").append(" = ").append(this.getFaultwsif()).append("\r\n");
        sb.append("\tfaultcode/faultcode").append(" = ").append(this.getFaultcode()).append("\r\n");
        sb.append("\tfaultstring/faultstring").append(" = ").append(this.getFaultstring()).append("\r\n");
        sb.append("\tdetail/detail").append(" = ").append(this.getDetail()).append("\r\n");
        sb.append("\tD_INSERT_TIME/insertTime").append(" = ").append(this.getInsertTime()).append("\r\n");
        sb.append("}\r\n");
        return sb.toString();
    }
}

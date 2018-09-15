package @{package_path}@;

import java.sql.Connection;
import java.util.List;

import exp.libs.warp.db.sql.DBUtils;

/**
 * <PRE>
 * Table Name : @{table_name}@
 * Class Name : @{class_name}@
 * </PRE>
 * <B>PROJECT : </B> exp-libs
 * <B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   @{date}@
 * @author    EXP: 272629724@qq.com
 * @since     jdk version : jdk 1.6
 */
public class @{class_name}@  {
    
    /** insert sql */
    public final static String SQL_INSERT = 
            "INSERT INTO @{table_name}@(@{insert_column}@) VALUES(@{insert_column_placeholder}@)";
    
    /** delete sql */
    public final static String SQL_DELETE = 
            "DELETE FROM @{table_name}@ WHERE 1 = 1 ";
    
    /** update sql */
    public final static String SQL_UPDATE = 
            "UPDATE @{table_name}@ SET @{update_column}@ WHERE 1 = 1 ";
    
    /** select sql */
    public final static String SQL_SELECT = 
            "SELECT @{select_column}@ FROM @{table_name}@ WHERE 1 = 1 ";

@{class_member}@
    /**
     * insert the bean of @{class_name}@ to db.
     * 
     * @param conn : the connection of db
     * @param bean : a bean of the data
     * @return effect of row number
     */
    public static boolean insert(Connection conn, @{class_name}@ bean) {
        Object[] params = new Object[] {
@{insert_params}@
        };
        return DBUtils.execute(conn, @{class_name}@.SQL_INSERT, params);
    }
    
    /**
     * delete some bean of @{class_name}@ from db with some conditions.
     * 
     * @param conn : the connection of db
     * @param where : 
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return effect of row number
     */
    public static boolean delete(Connection conn, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(@{class_name}@.SQL_DELETE);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        return DBUtils.execute(conn, sql.toString());
    }
    
    /**
     * update some bean of @{class_name}@ to db with some conditions.
     * 
     * @param conn : the connection of db
     * @param bean : a bean of the data
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return effect of row number
     */
    public static boolean update(Connection conn, @{class_name}@ bean, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(@{class_name}@.SQL_UPDATE);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        Object[] params = new Object[] {
@{update_params}@
        };
        return DBUtils.execute(conn, sql.toString(), params);
    }    
    
    /**
     * query all beans of @{class_name}@ from db.
     * 
     * @param conn : the connection of db
     * @return all beans of the data set
     */
    public static List<@{class_name}@> queryAll(Connection conn) {
        return querySome(conn, null);
    }
    
    /**
     * query a bean of @{class_name}@ from db with some conditions.
     * If the conditions <B>can't</B> lock the range of one record,
     * you will get <B>the first record</B> or <B>null</B>.
     *
     * @param conn : the connection of db
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return a beans of the data
     */
    public static @{class_name}@ queryOne(Connection conn, String where) {
        @{class_name}@ bean = null;
        List<@{class_name}@> beans = querySome(conn, where);
        if(beans != null && beans.size() > 0) {
            bean = beans.get(0);
        }
        return bean;
    }
    
    /**
     * query some beans of @{class_name}@ from db with some conditions.
     * 
     * @param conn : the connection of db
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return some beans of the data set
     */
    public static List<@{class_name}@> querySome(Connection conn, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(@{class_name}@.SQL_SELECT);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        return DBUtils.query(@{class_name}@.class, conn, sql.toString());
    }
    
@{getter_and_setter}@
@{get_column_name}@
@{get_java_name}@
    /**
     * get all column names
     * @return String
     */
    public final static String ALL_COLUMN_NAMES() {
        return "@{all_column_names}@";
    }

    /**
     * get all java names
     * @return String
     */
    public final static String ALL_JAVA_NAMES() {
        return "@{all_java_names}@";
    }

    /**
     * get table name
     * @return String
     */
    public final static String TABLE_NAME() {
        return "@{table_name}@";
    }

    /**
     * get class name
     * @return String
     */
    public final static String CLASS_NAME() {
        return "@{class_name}@";
    }
    
    /**
     * toString
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("@{table_name}@/@{class_name}@: {\r\n");
@{to_string}@
        sb.append("}\r\n");
        return sb.toString();
    }
}

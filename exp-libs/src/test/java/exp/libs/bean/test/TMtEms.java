package exp.libs.bean.test;

import java.sql.Connection;
import java.util.List;

import exp.libs.warp.db.sql.DBUtils;

/**
 * <PRE>
 * Table Name : t_mt_ems
 * Class Name : TMtEms
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-01-08 21:32:06
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk version : jdk 1.6
 */
public class TMtEms  {
    
    /** insert sql */
    public final static String SQL_INSERT = 
            "INSERT INTO t_mt_ems(id, task_id, name, userLabel, discoveredName, namingOs, aliasNameList, owner, vendorExtensions, source, networkAccessDomain, meiAttributes, resourceState, softwareVersion, productName, manufacturer, resourceFulfillmentState, isSubordinateOs, asapRef, SGUID, I_VER, D_INSERT_TIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    /** delete sql */
    public final static String SQL_DELETE = 
            "DELETE FROM t_mt_ems WHERE 1 = 1 ";
    
    /** update sql */
    public final static String SQL_UPDATE = 
            "UPDATE t_mt_ems SET task_id = ?, name = ?, userLabel = ?, discoveredName = ?, namingOs = ?, aliasNameList = ?, owner = ?, vendorExtensions = ?, source = ?, networkAccessDomain = ?, meiAttributes = ?, resourceState = ?, softwareVersion = ?, productName = ?, manufacturer = ?, resourceFulfillmentState = ?, isSubordinateOs = ?, asapRef = ?, SGUID = ?, I_VER = ?, D_INSERT_TIME = ? WHERE 1 = 1 ";
    
    /** select sql */
    public final static String SQL_SELECT = 
            "SELECT id AS 'id', task_id AS 'taskId', name AS 'name', userLabel AS 'userLabel', discoveredName AS 'discoveredName', namingOs AS 'namingOs', aliasNameList AS 'aliasNameList', owner AS 'owner', vendorExtensions AS 'vendorExtensions', source AS 'source', networkAccessDomain AS 'networkAccessDomain', meiAttributes AS 'meiAttributes', resourceState AS 'resourceState', softwareVersion AS 'softwareVersion', productName AS 'productName', manufacturer AS 'manufacturer', resourceFulfillmentState AS 'resourceFulfillmentState', isSubordinateOs AS 'isSubordinateOs', asapRef AS 'asapRef', SGUID AS 'sGUID', I_VER AS 'ver', D_INSERT_TIME AS 'insertTime' FROM t_mt_ems WHERE 1 = 1 ";

    /** id */
    private Integer id;

    /** task_id */
    private Integer taskId;

    /** name */
    private String name;

    /** userLabel */
    private String userLabel;

    /** discoveredName */
    private String discoveredName;

    /** namingOs */
    private String namingOs;

    /** aliasNameList */
    private String aliasNameList;

    /** owner */
    private String owner;

    /** vendorExtensions */
    private String vendorExtensions;

    /** source */
    private String source;

    /** networkAccessDomain */
    private String networkAccessDomain;

    /** meiAttributes */
    private String meiAttributes;

    /** resourceState */
    private String resourceState;

    /** softwareVersion */
    private String softwareVersion;

    /** productName */
    private String productName;

    /** manufacturer */
    private String manufacturer;

    /** resourceFulfillmentState */
    private String resourceFulfillmentState;

    /** isSubordinateOs */
    private String isSubordinateOs;

    /** asapRef */
    private String asapRef;

    /** SGUID */
    private java.math.BigDecimal sGUID;

    /** I_VER */
    private Integer ver;

    /** D_INSERT_TIME */
    private java.sql.Timestamp insertTime;

    /**
     * insert the bean of TMtEms to db.
     * 
     * @param conn : the connection of db
     * @param bean : a bean of the data
     * @return effect of row number
     */
    public static boolean insert(Connection conn, TMtEms bean) {
        Object[] params = new Object[] {
                bean.getId(),
                bean.getTaskId(),
                bean.getName(),
                bean.getUserLabel(),
                bean.getDiscoveredName(),
                bean.getNamingOs(),
                bean.getAliasNameList(),
                bean.getOwner(),
                bean.getVendorExtensions(),
                bean.getSource(),
                bean.getNetworkAccessDomain(),
                bean.getMeiAttributes(),
                bean.getResourceState(),
                bean.getSoftwareVersion(),
                bean.getProductName(),
                bean.getManufacturer(),
                bean.getResourceFulfillmentState(),
                bean.getIsSubordinateOs(),
                bean.getAsapRef(),
                bean.getSGUID(),
                bean.getVer(),
                bean.getInsertTime()
        };
        return DBUtils.execute(conn, TMtEms.SQL_INSERT, params);
    }
    
    /**
     * delete some bean of TMtEms from db with some conditions.
     * 
     * @param conn : the connection of db
     * @param where : 
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return effect of row number
     */
    public static boolean delete(Connection conn, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TMtEms.SQL_DELETE);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        return DBUtils.execute(conn, sql.toString());
    }
    
    /**
     * update some bean of TMtEms to db with some conditions.
     * 
     * @param conn : the connection of db
     * @param bean : a bean of the data
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return effect of row number
     */
    public static boolean update(Connection conn, TMtEms bean, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TMtEms.SQL_UPDATE);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        Object[] params = new Object[] {
                bean.getTaskId(),
                bean.getName(),
                bean.getUserLabel(),
                bean.getDiscoveredName(),
                bean.getNamingOs(),
                bean.getAliasNameList(),
                bean.getOwner(),
                bean.getVendorExtensions(),
                bean.getSource(),
                bean.getNetworkAccessDomain(),
                bean.getMeiAttributes(),
                bean.getResourceState(),
                bean.getSoftwareVersion(),
                bean.getProductName(),
                bean.getManufacturer(),
                bean.getResourceFulfillmentState(),
                bean.getIsSubordinateOs(),
                bean.getAsapRef(),
                bean.getSGUID(),
                bean.getVer(),
                bean.getInsertTime()
        };
        return DBUtils.execute(conn, sql.toString(), params);
    }    
    
    /**
     * query all beans of TMtEms from db.
     * 
     * @param conn : the connection of db
     * @return all beans of the data set
     */
    public static List<TMtEms> queryAll(Connection conn) {
        return querySome(conn, null);
    }
    
    /**
     * query a bean of TMtEms from db with some conditions.
     * If the conditions <B>can't</B> lock the range of one record,
     * you will get <B>the first record</B> or <B>null</B>.
     *
     * @param conn : the connection of db
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return a beans of the data
     */
    public static TMtEms queryOne(Connection conn, String where) {
        TMtEms bean = null;
        List<TMtEms> beans = querySome(conn, where);
        if(beans != null && beans.size() > 0) {
            bean = beans.get(0);
        }
        return bean;
    }
    
    /**
     * query some beans of TMtEms from db with some conditions.
     * 
     * @param conn : the connection of db
     * @param where :
     *      The conditions of sql, must start with "and" (Ignoring the case).
     *      For example: "and id > 1 and name like Exp%"
     * @return some beans of the data set
     */
    public static List<TMtEms> querySome(Connection conn, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append(TMtEms.SQL_SELECT);
        
        if (where != null && !"".equals(where.trim())) {
            if(false == where.toLowerCase().trim().startsWith("and")) {
                sql.append("AND ");
            }
            sql.append(where);
        }
        return DBUtils.query(TMtEms.class, conn, sql.toString());
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
     * getName
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * setName
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getUserLabel
     * @return String
     */
    public String getUserLabel() {
        return this.userLabel;
    }

    /**
     * setUserLabel
     * @param userLabel userLabel to set
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    /**
     * getDiscoveredName
     * @return String
     */
    public String getDiscoveredName() {
        return this.discoveredName;
    }

    /**
     * setDiscoveredName
     * @param discoveredName discoveredName to set
     */
    public void setDiscoveredName(String discoveredName) {
        this.discoveredName = discoveredName;
    }

    /**
     * getNamingOs
     * @return String
     */
    public String getNamingOs() {
        return this.namingOs;
    }

    /**
     * setNamingOs
     * @param namingOs namingOs to set
     */
    public void setNamingOs(String namingOs) {
        this.namingOs = namingOs;
    }

    /**
     * getAliasNameList
     * @return String
     */
    public String getAliasNameList() {
        return this.aliasNameList;
    }

    /**
     * setAliasNameList
     * @param aliasNameList aliasNameList to set
     */
    public void setAliasNameList(String aliasNameList) {
        this.aliasNameList = aliasNameList;
    }

    /**
     * getOwner
     * @return String
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * setOwner
     * @param owner owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * getVendorExtensions
     * @return String
     */
    public String getVendorExtensions() {
        return this.vendorExtensions;
    }

    /**
     * setVendorExtensions
     * @param vendorExtensions vendorExtensions to set
     */
    public void setVendorExtensions(String vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }

    /**
     * getSource
     * @return String
     */
    public String getSource() {
        return this.source;
    }

    /**
     * setSource
     * @param source source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * getNetworkAccessDomain
     * @return String
     */
    public String getNetworkAccessDomain() {
        return this.networkAccessDomain;
    }

    /**
     * setNetworkAccessDomain
     * @param networkAccessDomain networkAccessDomain to set
     */
    public void setNetworkAccessDomain(String networkAccessDomain) {
        this.networkAccessDomain = networkAccessDomain;
    }

    /**
     * getMeiAttributes
     * @return String
     */
    public String getMeiAttributes() {
        return this.meiAttributes;
    }

    /**
     * setMeiAttributes
     * @param meiAttributes meiAttributes to set
     */
    public void setMeiAttributes(String meiAttributes) {
        this.meiAttributes = meiAttributes;
    }

    /**
     * getResourceState
     * @return String
     */
    public String getResourceState() {
        return this.resourceState;
    }

    /**
     * setResourceState
     * @param resourceState resourceState to set
     */
    public void setResourceState(String resourceState) {
        this.resourceState = resourceState;
    }

    /**
     * getSoftwareVersion
     * @return String
     */
    public String getSoftwareVersion() {
        return this.softwareVersion;
    }

    /**
     * setSoftwareVersion
     * @param softwareVersion softwareVersion to set
     */
    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    /**
     * getProductName
     * @return String
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * setProductName
     * @param productName productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * getManufacturer
     * @return String
     */
    public String getManufacturer() {
        return this.manufacturer;
    }

    /**
     * setManufacturer
     * @param manufacturer manufacturer to set
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * getResourceFulfillmentState
     * @return String
     */
    public String getResourceFulfillmentState() {
        return this.resourceFulfillmentState;
    }

    /**
     * setResourceFulfillmentState
     * @param resourceFulfillmentState resourceFulfillmentState to set
     */
    public void setResourceFulfillmentState(String resourceFulfillmentState) {
        this.resourceFulfillmentState = resourceFulfillmentState;
    }

    /**
     * getIsSubordinateOs
     * @return String
     */
    public String getIsSubordinateOs() {
        return this.isSubordinateOs;
    }

    /**
     * setIsSubordinateOs
     * @param isSubordinateOs isSubordinateOs to set
     */
    public void setIsSubordinateOs(String isSubordinateOs) {
        this.isSubordinateOs = isSubordinateOs;
    }

    /**
     * getAsapRef
     * @return String
     */
    public String getAsapRef() {
        return this.asapRef;
    }

    /**
     * setAsapRef
     * @param asapRef asapRef to set
     */
    public void setAsapRef(String asapRef) {
        this.asapRef = asapRef;
    }

    /**
     * getSGUID
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getSGUID() {
        return this.sGUID;
    }

    /**
     * setSGUID
     * @param sGUID sGUID to set
     */
    public void setSGUID(java.math.BigDecimal sGUID) {
        this.sGUID = sGUID;
    }

    /**
     * getVer
     * @return Integer
     */
    public Integer getVer() {
        return this.ver;
    }

    /**
     * setVer
     * @param ver ver to set
     */
    public void setVer(Integer ver) {
        this.ver = ver;
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
     * @return task_id
     */
    public static String getTaskId$CN() {
        return "task_id";
    }

    /**
     * get column name
     * @return name
     */
    public static String getName$CN() {
        return "name";
    }

    /**
     * get column name
     * @return userLabel
     */
    public static String getUserLabel$CN() {
        return "userLabel";
    }

    /**
     * get column name
     * @return discoveredName
     */
    public static String getDiscoveredName$CN() {
        return "discoveredName";
    }

    /**
     * get column name
     * @return namingOs
     */
    public static String getNamingOs$CN() {
        return "namingOs";
    }

    /**
     * get column name
     * @return aliasNameList
     */
    public static String getAliasNameList$CN() {
        return "aliasNameList";
    }

    /**
     * get column name
     * @return owner
     */
    public static String getOwner$CN() {
        return "owner";
    }

    /**
     * get column name
     * @return vendorExtensions
     */
    public static String getVendorExtensions$CN() {
        return "vendorExtensions";
    }

    /**
     * get column name
     * @return source
     */
    public static String getSource$CN() {
        return "source";
    }

    /**
     * get column name
     * @return networkAccessDomain
     */
    public static String getNetworkAccessDomain$CN() {
        return "networkAccessDomain";
    }

    /**
     * get column name
     * @return meiAttributes
     */
    public static String getMeiAttributes$CN() {
        return "meiAttributes";
    }

    /**
     * get column name
     * @return resourceState
     */
    public static String getResourceState$CN() {
        return "resourceState";
    }

    /**
     * get column name
     * @return softwareVersion
     */
    public static String getSoftwareVersion$CN() {
        return "softwareVersion";
    }

    /**
     * get column name
     * @return productName
     */
    public static String getProductName$CN() {
        return "productName";
    }

    /**
     * get column name
     * @return manufacturer
     */
    public static String getManufacturer$CN() {
        return "manufacturer";
    }

    /**
     * get column name
     * @return resourceFulfillmentState
     */
    public static String getResourceFulfillmentState$CN() {
        return "resourceFulfillmentState";
    }

    /**
     * get column name
     * @return isSubordinateOs
     */
    public static String getIsSubordinateOs$CN() {
        return "isSubordinateOs";
    }

    /**
     * get column name
     * @return asapRef
     */
    public static String getAsapRef$CN() {
        return "asapRef";
    }

    /**
     * get column name
     * @return SGUID
     */
    public static String getSGUID$CN() {
        return "SGUID";
    }

    /**
     * get column name
     * @return I_VER
     */
    public static String getVer$CN() {
        return "I_VER";
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
     * @return taskId
     */
    public static String getTaskId$JN() {
        return "taskId";
    }

    /**
     * get java name
     * @return name
     */
    public static String getName$JN() {
        return "name";
    }

    /**
     * get java name
     * @return userLabel
     */
    public static String getUserLabel$JN() {
        return "userLabel";
    }

    /**
     * get java name
     * @return discoveredName
     */
    public static String getDiscoveredName$JN() {
        return "discoveredName";
    }

    /**
     * get java name
     * @return namingOs
     */
    public static String getNamingOs$JN() {
        return "namingOs";
    }

    /**
     * get java name
     * @return aliasNameList
     */
    public static String getAliasNameList$JN() {
        return "aliasNameList";
    }

    /**
     * get java name
     * @return owner
     */
    public static String getOwner$JN() {
        return "owner";
    }

    /**
     * get java name
     * @return vendorExtensions
     */
    public static String getVendorExtensions$JN() {
        return "vendorExtensions";
    }

    /**
     * get java name
     * @return source
     */
    public static String getSource$JN() {
        return "source";
    }

    /**
     * get java name
     * @return networkAccessDomain
     */
    public static String getNetworkAccessDomain$JN() {
        return "networkAccessDomain";
    }

    /**
     * get java name
     * @return meiAttributes
     */
    public static String getMeiAttributes$JN() {
        return "meiAttributes";
    }

    /**
     * get java name
     * @return resourceState
     */
    public static String getResourceState$JN() {
        return "resourceState";
    }

    /**
     * get java name
     * @return softwareVersion
     */
    public static String getSoftwareVersion$JN() {
        return "softwareVersion";
    }

    /**
     * get java name
     * @return productName
     */
    public static String getProductName$JN() {
        return "productName";
    }

    /**
     * get java name
     * @return manufacturer
     */
    public static String getManufacturer$JN() {
        return "manufacturer";
    }

    /**
     * get java name
     * @return resourceFulfillmentState
     */
    public static String getResourceFulfillmentState$JN() {
        return "resourceFulfillmentState";
    }

    /**
     * get java name
     * @return isSubordinateOs
     */
    public static String getIsSubordinateOs$JN() {
        return "isSubordinateOs";
    }

    /**
     * get java name
     * @return asapRef
     */
    public static String getAsapRef$JN() {
        return "asapRef";
    }

    /**
     * get java name
     * @return sGUID
     */
    public static String getSGUID$JN() {
        return "sGUID";
    }

    /**
     * get java name
     * @return ver
     */
    public static String getVer$JN() {
        return "ver";
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
        return "id, task_id, name, userLabel, discoveredName, namingOs, aliasNameList, owner, vendorExtensions, source, networkAccessDomain, meiAttributes, resourceState, softwareVersion, productName, manufacturer, resourceFulfillmentState, isSubordinateOs, asapRef, SGUID, I_VER, D_INSERT_TIME";
    }

    /**
     * get all java names
     * @return String
     */
    public static String getAllJavaNames() {
        return "id, taskId, name, userLabel, discoveredName, namingOs, aliasNameList, owner, vendorExtensions, source, networkAccessDomain, meiAttributes, resourceState, softwareVersion, productName, manufacturer, resourceFulfillmentState, isSubordinateOs, asapRef, sGUID, ver, insertTime";
    }

    /**
     * get table name
     * @return String
     */
    public static String getTableName() {
        return "t_mt_ems";
    }

    /**
     * get class name
     * @return String
     */
    public static String getClassName() {
        return "TMtEms";
    }
    
    /**
     * toString
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("t_mt_ems/TMtEms: {\r\n");
        sb.append("\tid/id").append(" = ").append(this.getId()).append("\r\n");
        sb.append("\ttask_id/taskId").append(" = ").append(this.getTaskId()).append("\r\n");
        sb.append("\tname/name").append(" = ").append(this.getName()).append("\r\n");
        sb.append("\tuserLabel/userLabel").append(" = ").append(this.getUserLabel()).append("\r\n");
        sb.append("\tdiscoveredName/discoveredName").append(" = ").append(this.getDiscoveredName()).append("\r\n");
        sb.append("\tnamingOs/namingOs").append(" = ").append(this.getNamingOs()).append("\r\n");
        sb.append("\taliasNameList/aliasNameList").append(" = ").append(this.getAliasNameList()).append("\r\n");
        sb.append("\towner/owner").append(" = ").append(this.getOwner()).append("\r\n");
        sb.append("\tvendorExtensions/vendorExtensions").append(" = ").append(this.getVendorExtensions()).append("\r\n");
        sb.append("\tsource/source").append(" = ").append(this.getSource()).append("\r\n");
        sb.append("\tnetworkAccessDomain/networkAccessDomain").append(" = ").append(this.getNetworkAccessDomain()).append("\r\n");
        sb.append("\tmeiAttributes/meiAttributes").append(" = ").append(this.getMeiAttributes()).append("\r\n");
        sb.append("\tresourceState/resourceState").append(" = ").append(this.getResourceState()).append("\r\n");
        sb.append("\tsoftwareVersion/softwareVersion").append(" = ").append(this.getSoftwareVersion()).append("\r\n");
        sb.append("\tproductName/productName").append(" = ").append(this.getProductName()).append("\r\n");
        sb.append("\tmanufacturer/manufacturer").append(" = ").append(this.getManufacturer()).append("\r\n");
        sb.append("\tresourceFulfillmentState/resourceFulfillmentState").append(" = ").append(this.getResourceFulfillmentState()).append("\r\n");
        sb.append("\tisSubordinateOs/isSubordinateOs").append(" = ").append(this.getIsSubordinateOs()).append("\r\n");
        sb.append("\tasapRef/asapRef").append(" = ").append(this.getAsapRef()).append("\r\n");
        sb.append("\tSGUID/sGUID").append(" = ").append(this.getSGUID()).append("\r\n");
        sb.append("\tI_VER/ver").append(" = ").append(this.getVer()).append("\r\n");
        sb.append("\tD_INSERT_TIME/insertTime").append(" = ").append(this.getInsertTime()).append("\r\n");
        sb.append("}\r\n");
        return sb.toString();
    }
}

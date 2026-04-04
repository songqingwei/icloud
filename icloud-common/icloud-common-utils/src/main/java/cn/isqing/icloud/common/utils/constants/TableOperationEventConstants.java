package cn.isqing.icloud.common.utils.constants;

/**
 * 表操作事件常量类
 * 定义事件类型的固定前缀，避免与用户自定义事件冲突
 *
 * @author songqingwei
 * @version 1.0
 */
public class TableOperationEventConstants {

    /**
     * 表操作事件固定前缀
     * 所有表操作事件都以这个前缀开头
     */
    public static final String TABLE_OP_PREFIX = "TABLE_OP";

    /**
     * 路由级别1：表操作
     */
    public static final String R1_TABLE_OPERATION = TABLE_OP_PREFIX;

    /**
     * 路由级别2：操作类型
     */
    public static final String R2_INSERT = "INSERT";
    public static final String R2_UPDATE = "UPDATE";
    public static final String R2_DELETE = "DELETE";
    public static final String R2_QUERY = "QUERY";

    /**
     * 路由级别3：表名或业务标识
     * 由使用者动态指定
     */

    /**
     * 集群模式（用于BaseFactory）
     */
    public static final String CLUSTERING_MODEL = "CLUSTERING";

    /**
     * 广播模式（用于BaseFactory）
     */
    public static final String BROADCAST_MODEL = "BROADCAST";

    /**
     * 构建完整的事件类型key
     *
     * @param operation 操作类型（INSERT/UPDATE/DELETE）
     * @param tableName 表名
     * @return 事件类型key
     */
    public static String buildEventType(String operation, String tableName) {
        return String.format("%s:%s:%s", R1_TABLE_OPERATION, operation, tableName);
    }

    /**
     * 构建插入事件类型
     *
     * @param tableName 表名
     * @return 事件类型
     */
    public static String insertEvent(String tableName) {
        return buildEventType(R2_INSERT, tableName);
    }

    /**
     * 构建更新事件类型
     *
     * @param tableName 表名
     * @return 事件类型
     */
    public static String updateEvent(String tableName) {
        return buildEventType(R2_UPDATE, tableName);
    }

    /**
     * 构建删除事件类型
     *
     * @param tableName 表名
     * @return 事件类型
     */
    public static String deleteEvent(String tableName) {
        return buildEventType(R2_DELETE, tableName);
    }
}

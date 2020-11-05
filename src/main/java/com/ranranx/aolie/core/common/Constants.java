package com.ranranx.aolie.core.common;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/8 16:50
 * @Version V0.0.1
 **/
public class Constants {
    /**
     * 表间关系
     */
    public static class TableRelationType {
        /**
         * 一对一关系
         */
        public static final Integer TYPE_ONE_ONE = 1;
        /**
         * 一对多关系
         */
        public static final Integer TYPE_ONE_MULTI = 2;
        /**
         * 多对一关系
         */
        public static final Integer TYPE_MULTI_ONE = 3;
        /**
         * 多对多关系
         */
        public static final Integer TYPE_MULTI_MULTI = 4;
    }

    /**
     * 请求处理类型
     */
    public static class HandleType {
        /**
         * 查询
         */
        public static final String TYPE_QUERY = "query";
        /**
         * 删除
         */
        public static final String TYPE_DELETE = "delete";
        /**
         * 插入
         */
        public static final String TYPE_INSERT = "insert";
        /**
         * 更新
         */
        public static final String TYPE_UPDATE = "update";
    }

    /**
     * 排序类型
     */
    public static class OrderType {
        /**
         * 无排序
         */
        public static final short NONE = 0;
        /**
         * 升序
         */
        public static final short ASC = 1;
        /**
         * 降序
         */
        public static final short DESC = 2;
    }

    /**
     * 数据库表连接方式
     */
    public static class JoinType {
        public static final String INNER_JOIN = "inner join";
        public static final String LEFT_JOIN = "left join";
        public static final String RIGHT_JOIN = "right join";
        public static final String FULL_JOIN = "full join";
    }

    /**
     * 分组类型
     */
    public static class GroupType {
        /**
         * 不参与分组
         */
        public static final short NONE = 0;
        /**
         * 加和
         */
        public static final short SUM = 1;
        /**
         * 平均
         */
        public static final short AVG = 2;
        /**
         * 计数
         */
        public static final short COUNT = 3;
        /**
         * 最大
         */
        public static final short MAX = 4;
        /**
         * 最小
         */
        public static final short MIN = 5;

    }

    /**
     * 约束的处理方式
     */
    public static class ConstraintHandleType {
        /**
         * 警告
         */
        public static final int WARNING = 1;

        /**
         * 错误
         */
        public static final int ERROR = 0;
    }

    /**
     * 树节点的默认字段
     */
    public static class TreeNodeNames {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CODE = "code";
        public static final String PARENT = "parent";
        public static final String IS_LEAF = "is_leaf";
    }

    /**
     * 默认业务方案ID
     */
    public static final Long DEFAULT_BS_SCHEMA = new Long(2);

    /**
     * 布局
     */
    public static class LayoutType {
        /*边界布局*/
        public static final int BORDER_LAYOUT = 1;
        /*流式*/
        public static final int FLOW_LAYOUT = 3;
        /*绝对定位*/
        public static final int ABSTRACT_LAYOUT = 5;
    }
}

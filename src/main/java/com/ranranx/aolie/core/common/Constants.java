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
    public static final Long DEFAULT_BS_SCHEMA = new Long(3);
    /**
     * 默认系统方案ID
     */
    public static final Long DEFAULT_SYS_SCHEMA = new Long(2);
    /**
     * 基础方案方案
     */
    public static final Long DEFAULT_DM_SCHEMA = new Long(1);
    /**
     * 全局引用方案
     */
    public static final Long DEFAULT_REFERENCE_SCHEMA = new Long(0);

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

    /**
     * 表的固定字段名
     */
    public static class FixColumnName {
        /*版本号*/
        public static final String VERSION_CODE = "version_code";
        public static final String XH = "xh";
        public static final String LVL_CODE = "lvl_code";
    }

    /**
     * 前后端协商的固定字段
     */
    public static class ConstFieldName {
        /**
         * 保存时用于存放变化过的主键数据
         */
        public static final String CHANGE_KEYS_FEILD = "keys";
    }

    /**
     * 字段在树状结构中的显示角色
     */
    public static class TreeRole {
        /*ID字段*/
        public static final int idField = 1;
        /**
         * 编码字段,且是3|3|3编码的
         */
        public static final int codeField = 2;
        /*名称字段*/
        public static final int nameField = 3;
        /*父字段*/
        public static final int parentField = 4;
    }

    /**
     * 页面明细的类型
     */
    public static class PageViewType {
        //界面
        public static final int blockView = 1;
        //引用
        public static final int reference = 2;
        //页面
        public static final int page = 3;
    }

    public static class SystemParamNames {
        public static final long userId = -100;
        public static final long role = -105;
        public static final long belongOrgId = -109;
        public static final long belongOrgCode = -110;
        public static final long userType = -115;
        public static final long versionCode = -120;
    }

}

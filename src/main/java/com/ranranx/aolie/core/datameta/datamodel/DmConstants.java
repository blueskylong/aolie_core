package com.ranranx.aolie.core.datameta.datamodel;

/**
 * @Author xxl
 * @Description 和模型相关的常量
 * @Date 2020/12/28 0028 10:26
 * @Version V0.0.1
 **/
public class DmConstants {
    public static final class FieldType {
        public static final String INT = "int";
        public static final String VARCHAR = "varchar";
        public static final String DATETIME = "datetime";
        public static final String TEXT = "text";
        public static final String BINARY = "binary";
        public static final String DECIMAL = "decimal";
        public static final String SMALLINT = "smallint";
    }

    public static final class DispType {
        //自定义视图的ID
        public static final long CUSTOM_UI_ID = 2L;
        public static final int form = 1;
        public static final int table = 2;
        public static final int tree = 3;
        public static final int card = 4;
        public static final int refTree = 5;
        public static final int custom = 9;//自定义面板
    }

}
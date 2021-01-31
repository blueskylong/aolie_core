package com.ranranx.aolie.core.datameta.datamodel;

/**
 * @Author xxl
 * @Description 和模型相关的常量
 * @Date 2020/12/28 0028 10:26
 * @Version V0.0.1
 **/
public class DmConstants {
    public static final class FieldType {
        public static final String INT = "2";
        public static final String VARCHAR = "1";
        public static final String DATETIME = "10";
        public static final String TEXT = "20";
        public static final String BINARY = "30";
        public static final String DECIMAL = "4";
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


    /**
     * 公式元素类型
     */
    public static final class FormulaElementType {
        public static final int logic = 100;//逻辑运算符,如: and  or
        public static final int compare = 200;//比较符 如:> <
        public static final int mathOperator = 300; //数学符号, 如 +-
        public static final int bracket = 500;//括号
        public static final int column = 800;//字段
        public static final int sysparam = 900;//系统参数
        public static final int constant = 1000;//常量,如"xxx"
        public static final int error = 9999;//错误类型
    }

    /**
     * 固定系统参数
     */
    public static final class GlobalParamsIds {
        public static final int userId = 9991;
        public static final int roleId = 9992;
        public static final int version = 9995;
        public static final int userName = 9999;
        public static final int userBelong = 9998;
        public static final int userAccount = 9996;
    }

}

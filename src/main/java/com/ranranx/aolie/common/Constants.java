package com.ranranx.aolie.common;

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
}

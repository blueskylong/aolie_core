package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.Constants;

/**
 * @Author xxl
 * @Description
 * @Date 2020/11/30 14:39
 * @Version V0.0.1
 **/
public class SchemaTools {
    /**
     * 是不是全局引用方案
     *
     * @param schemaId
     * @return
     */
    public static boolean isReferenceSchema(Long schemaId) {
        if (schemaId == null) {
            return false;
        }
        return schemaId.equals(Constants.DEFAULT_REFERENCE_SCHEMA);
    }
}

package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;

import java.util.List;

/**
 * @author xxl
 * 删除参数载体
 * @version V0.0.1
 * @date 2020/8/6 14:28
 **/
public class DeleteParam extends OperParam<DeleteParam> {
    /**
     * 要删除的ID
     */
    private List<Object> ids;

    public List<Object> getIds() {
        return ids;
    }

    public void setIds(List<Object> ids) {
        this.ids = ids;
    }

    public static DeleteParam deleteById(Long schemaId, Class clazz, Object id, String version) {
        DeleteParam param = new DeleteParam();
        param.setTable(SchemaHolder.findTableByTableName(CommonUtils.getTableName(clazz),
                schemaId, version));
        param.getCriteria().andEqualTo(param.getTable().getTableDto().getTableName(),
                param.getTable().getKeyField(), id);
        return param;
    }
}

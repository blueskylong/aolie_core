package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.SqlExp;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 删除参数载体
 * @version V0.0.1
 * @date 2020/8/6 14:28
 **/
public class DeleteParam {
    /**
     * 复杂条件
     */
    private Criteria criteria;
    /**
     * 表ID
     */
    private long tableId;
    /**
     * 表信息
     */
    private TableInfo table;
    /**
     * 要删除的ID
     */
    private List<Object> ids;

    public List<Object> getIds() {
        return ids;
    }

    /**
     * 直接语句.暂时提供在复杂语句下使用.
     */
    private SqlExp sqlExp;

    /**
     * 增加控制信息,让拦截器使用
     */
    private Map<String, Object> mapControlParam;

    public SqlExp getSqlExp() {
        return sqlExp;
    }

    public void setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
    }

    public void setIds(List<Object> ids) {
        this.ids = ids;
    }

    public Criteria getCriteria() {
        if (this.criteria == null) {
            this.criteria = new Criteria();
        }
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }


    public TableInfo getTable() {
        return table;
    }

    public void setTable(TableInfo table) {
        this.table = table;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public Map<String, Object> getMapControlParam() {
        return mapControlParam;
    }

    public void setMapControlParam(Map<String, Object> mapControlParam) {
        this.mapControlParam = mapControlParam;
    }

    /**
     * 增加一个控制参数
     *
     * @param key
     * @param value
     */
    public void addControlParam(String key, Object value) {
        if (this.mapControlParam == null) {
            this.mapControlParam = new HashMap<>();
        }
        this.mapControlParam.put(key, value);
    }

    public void setTableDto(Long schemaId, Class clazz) {
        this.table = SchemaHolder.findTableByTableName(CommonUtils.getTableName(clazz),
                schemaId, SessionUtils.getLoginVersion());
    }

    public DeleteParam setDeleteDto(Long schemaId, Object obj) {
        if (obj == null) {
            return this;
        }
        if (this.criteria == null) {
            this.criteria = new Criteria();
        }
        this.criteria.andEqualToDto(null, obj);
        this.setTableDto(schemaId, obj.getClass());
        return this;
    }


    /**
     * 取得一个控制参数
     *
     * @param key
     * @return
     */
    public Object getControlParam(String key) {
        if (this.mapControlParam == null) {
            return null;
        }
        return this.mapControlParam.get(key);
    }
}

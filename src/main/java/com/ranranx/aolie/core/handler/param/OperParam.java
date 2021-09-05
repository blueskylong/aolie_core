package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.SqlExp;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/6 0006 9:49
 **/
public abstract class OperParam<T> {
    /**
     * 增加控制信息,让拦截器使用
     */
    protected Map<String, Object> mapControlParam;

    /**
     * 直接语句.暂时提供在复杂语句下使用.
     */
    protected SqlExp sqlExp;

    protected TableInfo[] tableInfos;

    /**
     * 复杂条件
     */
    protected List<Criteria> criterias;

    /**
     * 增加一个控制参数
     *
     * @param key
     * @param value
     */
    public T addControlParam(String key, Object value) {
        if (this.mapControlParam == null) {
            this.mapControlParam = new HashMap<>();
        }
        this.mapControlParam.put(key, value);
        return (T) this;
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

    public Map<String, Object> getMapControlParam() {
        return mapControlParam;
    }

    public T setMapControlParam(Map<String, Object> mapControlParam) {
        this.mapControlParam = mapControlParam;
        return (T) this;
    }


    public SqlExp getSqlExp() {
        return sqlExp;
    }

    public T setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
        return (T) this;
    }

    public TableInfo[] getTables() {
        return tableInfos;
    }

    public T setTables(TableInfo[] tableInfo) {
        this.tableInfos = tableInfo;
        return (T) this;
    }

    public TableInfo getTable() {
        if (tableInfos == null) {
            return null;
        }
        return tableInfos[0];
    }

    public T setTable(TableInfo table) {
        if (this.tableInfos == null) {
            this.tableInfos = new TableInfo[]{table};
        }
        this.tableInfos[0] = table;
        return (T) this;
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public T setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
        return (T) this;
    }

    public T setTableDto(Long schemaId, Class clazz) {
        this.setTable(SchemaHolder.findTableByTableName(CommonUtils.getTableName(clazz),
                schemaId, SessionUtils.getLoginVersion()));
        return (T) this;
    }

    public void setTableDto(Long schemaId, Class clazz, String version) {
        this.setTable(SchemaHolder.findTableByTableName(CommonUtils.getTableName(clazz),
                schemaId, version));
    }

    public T appendTable(TableInfo tableInfo) {
        if (this.tableInfos == null) {
            this.tableInfos = new TableInfo[]{tableInfo};
        } else {
            TableInfo[] tables = new TableInfo[this.tableInfos.length + 1];
            System.arraycopy(this.tableInfos, 0, tables, 0, this.tableInfos.length);
            tables[this.tableInfos.length] = tableInfo;
            this.tableInfos = tables;

        }
        return (T) this;
    }

    public T setOperDto(Long schemaId, Object obj, String version) {
        if (obj == null) {
            return (T) this;
        }
        this.setTableDto(schemaId, obj.getClass(), version);
        String tableName = null;
        if (this.getTable() != null) {
            tableName = this.getTable().getTableDto().getTableName();
        }
        this.getCriteria().andEqualToDto(tableName, obj);

        return (T) this;
    }

    public Criteria getCriteria() {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
            this.criterias.add(new Criteria());
        }
        return criterias.get(0);
    }

    public void setCriteria(Criteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
            this.criterias.add(criteria);
        } else {
            this.criterias.set(0, criteria);
        }

    }


    /**
     * 设置DTO代替表名
     *
     * @param lstClass
     */
    public T setTableDtos(Long schemaId, String version, Class... lstClass) {
        this.tableInfos = new TableInfo[lstClass.length];
        String tableName;
        int index = 0;
        for (Class clazz : lstClass) {
            tableName = CommonUtils.getTableName(clazz);
            if (CommonUtils.isEmpty(tableName)) {
                throw new InvalidException("指定的类没有@Table注解");

            }
            tableInfos[index++] = SchemaHolder.findTableByTableName(tableName, schemaId, version);
        }
        return (T) this;
    }

    /**
     * 增加过滤条件
     * TODO  这里的条件需要支持字段的表示方式 ,比如EQUALSTO第一个参数可以用字段信息
     *
     * @return
     */
    public Criteria appendCriteria() {
        if (criterias == null) {
            criterias = new ArrayList<>();
        }
        Criteria criteria = new Criteria();
        criterias.add(criteria);
        return criteria;
    }

    /**
     * 是不是没有自定义的条件
     *
     * @return
     */
    public boolean isNoFilter() {
        if (this.criterias == null || this.criterias.isEmpty()) {
            return true;
        }
        for (Criteria criteria : this.criterias) {
            if (!criteria.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 增加过滤条件
     *
     * @param criteria
     */
    public T addCriteria(Criteria criteria) {
        if (criterias == null) {
            criterias = new ArrayList<>();
        }
        criterias.add(criteria);
        return (T) this;
    }

    /**
     * 批量增加等于条件
     *
     * @param mapFilter
     */
    public Criteria addMapEqualsFilter(String tableName, Map<String, Object> mapFilter, boolean isSelective) {
        if (mapFilter == null || mapFilter.isEmpty()) {
            return getCriteria();
        }
        Criteria criteria = this.getCriteria();
        mapFilter.forEach((key, value) -> {
            if (CommonUtils.isEmpty(value)) {
                if (isSelective) {
                    return;
                }
                criteria.andIsNull(tableName, key);
            } else {
                criteria.andEqualTo(tableName, key, value);
            }
        });
        return criteria;
    }
}

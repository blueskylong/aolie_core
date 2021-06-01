package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableColumnRelation;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.ds.definition.SqlExp;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 查询参数载体, 可以是多种组合, 如果是单表, 则可以使用table和mapFilter组合, 如果使用视图则需要根据视图的设定拆分
 * @version V0.0.1
 * @date 2020/8/6 14:28
 **/
public class QueryParam {
    /**
     * 视图ID   视图模式条件
     */
    private Long viewId;

    /**
     * 视图展现的方式    视图模式条件
     */
    private Integer displayType;
    /**
     * 复杂条件  表条件
     */
    private List<Criteria> lstCriteria;
    /**
     * 插件扩展条件,前后端配合使用
     */
    private Map<String, Object> plugFilter;
    /**
     * 表信息   表条件
     */
    private TableInfo[] table;
    /**
     * 分页信息  综合条件
     */
    private Page page;

    private Class resultClass;
    /**
     * 是否不需要系统添加版本过滤条件
     */
    private boolean noVersionFilter = false;

    /**
     * 排序字段  综合条件
     */
    @javax.persistence.Transient
    private List<FieldOrder> lstOrder;
    private List<Field> fields;

    /**
     * 表间关系,多表时传入
     * 这个关系一般情况下不需要人为指定,此关系会根据数据表的定义来生成,但如果需要指定特殊的关系,则可以使用
     * 如: 流程管理中,审核表与其它表的ID字段关系,因为不能在数据方案中关联,所以只能手动指定
     */
    private List<TableColumnRelation> lstRelation;

    /**
     * 增加控制信息,让拦截器使用
     */
    private Map<String, Object> mapControlParam;
    /**
     * 直接语句.暂时提供在复杂语句下使用.
     */
    private SqlExp sqlExp;

    public SqlExp getSqlExp() {
        return sqlExp;
    }

    public QueryParam setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
        return this;
    }

    public Long getViewId() {
        return viewId;
    }

    public QueryParam setViewId(Long viewId) {
        this.viewId = viewId;
        return this;
    }


    public Map<String, Object> getPlugFilter() {
        return plugFilter;
    }

    public QueryParam setPlugFilter(Map<String, Object> mapFilter) {
        this.plugFilter = mapFilter;
        return this;
    }

    public List<FieldOrder> getLstOrder() {
        return lstOrder;
    }

    public QueryParam setLstOrder(List<FieldOrder> lstOrder) {
        this.lstOrder = lstOrder;
        return this;
    }

    public Integer getDisplayType() {
        return displayType;
    }

    public QueryParam setDisplayType(Integer displayType) {
        this.displayType = displayType;
        return this;
    }

    public List<Criteria> getLstCriteria() {
        return lstCriteria;
    }

    public QueryParam setLstCriteria(List<Criteria> lstCriteria) {
        this.lstCriteria = lstCriteria;
        return this;
    }

    public TableInfo[] getTable() {
        return table;
    }

    public QueryParam setTable(TableInfo[] table) {
        this.table = table;
        return this;
    }

    public QueryParam appendTable(TableInfo tableInfo) {
        if (this.table == null) {
            this.table = new TableInfo[]{tableInfo};
        } else {
            TableInfo[] tables = new TableInfo[this.table.length + 1];
            System.arraycopy(this.table, 0, tables, 0, this.table.length);
            tables[this.table.length] = tableInfo;
            this.table = tables;

        }
        return this;
    }

    /**
     * 设置DTO代替表名
     *
     * @param lstClass
     */
    public QueryParam setTableDtos(Long schemaId, String version, Class... lstClass) {
        this.table = new TableInfo[lstClass.length];
        String tableName;
        int index = 0;
        for (Class clazz : lstClass) {
            tableName = CommonUtils.getTableName(clazz);
            if (CommonUtils.isEmpty(tableName)) {
                throw new InvalidException("指定的类没有@Table注解");

            }
            table[index++] = SchemaHolder.findTableByTableName(tableName, schemaId, version);
        }
        return this;
    }

    /**
     * 设置查询表和返回类型,他们都是一个DTO类
     *
     * @param schemaId
     * @param version
     * @param clazz
     * @return
     */
    public QueryParam setTableDtoAndResultType(Long schemaId, String version, Class clazz) {
        this.table = new TableInfo[1];
        String tableName;
        int index = 0;

        tableName = CommonUtils.getTableName(clazz);
        if (CommonUtils.isEmpty(tableName)) {
            throw new InvalidException("指定的类没有@Table注解");
        }
        TableInfo tableInfo = SchemaHolder.findTableByTableName(tableName, schemaId, version);
        if (tableInfo == null) {
            throw new NotExistException("没有查询到指定表的定义信息:" + tableName + "[" + schemaId + "]");
        }
        table[0] = tableInfo;
        this.resultClass = clazz;
        return this;
    }

    public QueryParam setFilterObjectAndTableAndResultType(Long schemaId, String version, Object filter) {
        setTableDtoAndResultType(schemaId, version, filter.getClass());
        this.appendCriteria().andEqualToDto(null, filter);
        return this;
    }

    public QueryParam setFilterObjectAndTable(Long schemaId, String version, Object filter) {
        setTableDtos(schemaId, version, filter.getClass())
                .appendCriteria().andEqualToDto(null, filter);
        return this;
    }


    public Page getPage() {
        return page;
    }

    public QueryParam setPage(Page page) {
        this.page = page;
        return this;
    }

    public QueryParam addOrder(FieldOrder order) {
        if (this.lstOrder == null) {
            this.lstOrder = new ArrayList<>();
        }
        order.setOrder(this.lstOrder.size() + 1);
        this.lstOrder.add(order);
        return this;
    }

    public QueryParam addOrders(List<FieldOrder> order) {
        if (order == null || order.isEmpty()) {
            return this;
        }
        if (this.lstOrder == null) {
            this.lstOrder = new ArrayList<>();
        }
        this.lstOrder.addAll(order);
        return this;
    }

    /**
     * 增加过滤条件
     * TODO  这里的条件需要支持字段的表示方式 ,比如EQUALSTO第一个参数可以用字段信息
     *
     * @return
     */
    public Criteria appendCriteria() {
        if (lstCriteria == null) {
            lstCriteria = new ArrayList<>();
        }
        Criteria criteria = new Criteria();
        lstCriteria.add(criteria);
        return criteria;
    }

    /**
     * 增加过滤条件
     *
     * @param criteria
     */
    public QueryParam addCriteria(Criteria criteria) {
        if (lstCriteria == null) {
            lstCriteria = new ArrayList<>();
        }
        lstCriteria.add(criteria);
        return this;
    }


    public List<Field> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    public QueryParam setFields(List<Field> fields) {
        this.fields = fields;
        return this;
    }

    @Transient
    public Class getResultClass() {
        return resultClass;
    }

    public QueryParam setResultClass(Class resultClass) {
        this.resultClass = resultClass;
        return this;
    }


    public Map<String, Object> getMapControlParam() {
        return mapControlParam;
    }

    public QueryParam setMapControlParam(Map<String, Object> mapControlParam) {
        this.mapControlParam = mapControlParam;
        return this;
    }

    /**
     * 增加一个控制参数
     *
     * @param key
     * @param value
     */
    public QueryParam addControlParam(String key, Object value) {
        if (this.mapControlParam == null) {
            this.mapControlParam = new HashMap<>();
        }
        this.mapControlParam.put(key, value);
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


    public boolean isNoVersionFilter() {
        return noVersionFilter;
    }

    public QueryParam setNoVersionFilter(boolean noVersionFilter) {
        this.noVersionFilter = noVersionFilter;
        return this;
    }


    /**
     * 增加插件条件
     *
     * @param field
     * @param value
     */
    public QueryParam addPlugFilter(String field, Object value) {
        if (this.plugFilter == null) {
            this.plugFilter = new HashMap<>();
        }
        this.plugFilter.put(field, value);
        return this;
    }

    public List<TableColumnRelation> getLstRelation() {
        return lstRelation;
    }

    public QueryParam setLstRelation(List<TableColumnRelation> lstRelation) {
        this.lstRelation = lstRelation;
        return this;
    }
}

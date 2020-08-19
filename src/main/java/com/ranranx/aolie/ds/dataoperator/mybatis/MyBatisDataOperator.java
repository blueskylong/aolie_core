package com.ranranx.aolie.ds.dataoperator.mybatis;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.common.SqlTools;
import com.ranranx.aolie.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.ds.dataoperator.IDataOperator;
import com.ranranx.aolie.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.ds.definition.*;
import com.ranranx.aolie.exceptions.InvalidException;
import com.ranranx.aolie.handler.param.condition.Criteria;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/11 10:12
 * @Version V0.0.1
 **/


public class MyBatisDataOperator implements IDataOperator {

    /**
     * 数据库操作接口信息
     */
    private DataOperatorDto dto;

    /**
     * 数据源ID
     */
    private String dsKey;

    @Autowired
    private MyBatisGeneralMapper mapper;

    /**
     * 查询
     *
     * @param queryParamDefinition
     * @RETURN
     */
    @Override
    public List<Map<String, Object>> select(QueryParamDefinition queryParamDefinition) {
        //分析是单表查询还是多表查询
        if (isSingleTable(queryParamDefinition)) {
            return singleTableSelect(queryParamDefinition);
        }
        return multiTableSelect(queryParamDefinition);

    }

    private List<Map<String, Object>> select(Map<String, Object> map) {
        DynamicDataSource.setDataSource(dsKey);
        return mapper.select(map);
    }

    /**
     * 多表查询
     *
     * @param queryParamDefinition
     * @return
     */
    private List<Map<String, Object>> multiTableSelect(QueryParamDefinition queryParamDefinition) {
        Map<String, String> mapAlias = genTableAlias(queryParamDefinition.getTableNames());
        Map<String, Object> mapParamValue = new HashMap<>();
        String sField = SqlBuilder.buildFields(queryParamDefinition.getFields(), mapAlias);
        String sTable = SqlBuilder.buildTables(queryParamDefinition.getLstRelation(),
                mapAlias, queryParamDefinition.getTableNames());
        String sWhere = SqlBuilder.getWhere(mapAlias, queryParamDefinition.getLstCriteria(), mapParamValue);
        String sGroup = "";
        if (queryParamDefinition.isHasGroup()) {
            sGroup = SqlBuilder.genGroupBy(mapAlias, queryParamDefinition.getFields());
        }
        String sOrder = SqlBuilder.genOrder(mapAlias, queryParamDefinition.getLstOrder());
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(sField).append(" from ").append(sTable);
        if (CommonUtils.isNotEmpty(sWhere)) {
            sql.append(" where ").append(sWhere);
        }
        sql.append(sGroup);
        sql.append(" ").append(sOrder);
        mapParamValue.put(SQL_PARAM_NAME, sql.toString());
        return mapper.select(mapParamValue);
    }


    /**
     * 生成多表的别名
     *
     * @param tableNames
     * @return
     */
    private Map<String, String> genTableAlias(List<String> tableNames) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < tableNames.size(); i++) {
            map.put(tableNames.get(i), makeTableAliasString(i));
        }
        return map;
    }

    private String makeTableAliasString(int index) {
        return "table" + index;
    }

    /**
     * 单表查询
     *
     * @param queryParamDefinition
     * @return
     */
    private List<Map<String, Object>> singleTableSelect(QueryParamDefinition queryParamDefinition) {
        String tableName = queryParamDefinition.getTableNames().get(0);
        String field = getFieldsExp((queryParamDefinition.getFields() == null || queryParamDefinition.getFields().isEmpty())
                ? null : queryParamDefinition.getFields());
        Map<String, Object> mapParam = new HashMap<>();
        String where = "";
        if (queryParamDefinition.getCriteria() != null) {
            if (queryParamDefinition.hasCriteria()) {
                where = queryParamDefinition.getCriteria().get(0).getSqlWhere(mapParam, null, 1, false);
                if (!CommonUtils.isEmpty(where)) {
                    where = " where " + where;
                }
            }
        }
        String orderExp = "";
        if (queryParamDefinition.getLstOrder() != null && !queryParamDefinition.getLstOrder().isEmpty()) {
            List<FieldOrder> orders = queryParamDefinition.getLstOrder();
            for (FieldOrder order : orders) {
                orderExp += order.getOrderExp(null) + ",";
            }
            orderExp = " order by " + orderExp.substring(0, orderExp.length() - 1);
        }
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select ").append(field).append(" from ").append(tableName).append(where).append(orderExp);
        mapParam.put(SQL_PARAM_NAME, sbSql.toString());
        return mapper.select(mapParam);
    }


    private String getFieldsExp(List<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            return "*";
        }
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {

            sb.append(field.getFieldName()).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 是不是单表查询
     *
     * @param queryParamDefinition
     * @return
     */
    private boolean isSingleTable(QueryParamDefinition queryParamDefinition) {
        return queryParamDefinition.getTableNames() != null && queryParamDefinition.getTableNames().size() == 1;
    }

    /**
     * 删除
     *
     * @param deleteParamDefinition
     * @return
     */
    @Override
    public int delete(DeleteParamDefinition deleteParamDefinition) {

        //根据ID字段删除优先
        StringBuilder sb = new StringBuilder();
        Map<String, Object> mapParamValue = new HashMap<>();
        if (deleteParamDefinition.getIds() != null && deleteParamDefinition.getIds().size() > 0) {
            if (CommonUtils.isEmpty(deleteParamDefinition.getIdField())) {
                throw new InvalidException("没有指定主键字段");
            }
            sb.append(SqlTools.genInClause(deleteParamDefinition.getIdField(), deleteParamDefinition.getIds(), 1, mapParamValue));
        }
        if (deleteParamDefinition.getCriteria() != null) {
            sb.append(deleteParamDefinition.getCriteria().getSqlWhere(mapParamValue, null,
                    2, sb.length() > 0));
        }
        String sSql = "delete from " + deleteParamDefinition.getTableName() + " ";
        if (sb.length() > 0) {
            sSql += " where " + sb.toString();
        }
        mapParamValue.put(IDataOperator.SQL_PARAM_NAME, sSql);
        return mapper.delete(mapParamValue);
    }

    /**
     * 更新
     *
     * @param updateParamDefinition
     * @return
     */
    @Override
    public int update(UpdateParamDefinition updateParamDefinition) {
        //更新有二种情况,1是一行数据,如果带了ID字段则使用ID过滤 ,如果没有则需要指定复杂条件.
        if (updateParamDefinition.getLstRows() == null || updateParamDefinition.getLstRows().isEmpty()) {
            return -1;
        }
        int count = 0;
        for (Map<String, Object> row : updateParamDefinition.getLstRows()) {
            count += updateRow(row, updateParamDefinition.getCriteria(), updateParamDefinition.getIdField(),
                    updateParamDefinition.getTableName(), updateParamDefinition.isSelective());

        }
        return count;
    }

    private int updateRow(Map<String, Object> row, Criteria criteria,
                          String idField, String tableName, boolean isSelective) {
        //情况1 ,存在id值及字段,则增加ID条件
        StringBuilder sb = new StringBuilder();
        int index = 0;
        String key;
        if (CommonUtils.isNotEmpty(idField) && CommonUtils.isNotEmpty(row.get(idField))) {
            if (criteria == null) {
                criteria = new Criteria();
            }
            criteria.andEqualTo(idField, row.remove(idField));
        }
        Map<String, Object> mapParam = new HashMap<>();
        sb.append("update ").append(tableName).append(" set ").append(genSetSql(row, mapParam, index++, isSelective));
        if (criteria != null && !criteria.isEmpty()) {
            sb.append(" where ").append(criteria.getSqlWhere(mapParam, null, index++, false));
        }
        mapParam.put(SQL_PARAM_NAME, sb.toString());
        return mapper.update(mapParam);
    }

    /**
     * 生成设置信息
     *
     * @param setValues
     * @param mapParamValues
     * @param index
     * @param isSelective
     * @return
     */
    private String genSetSql(Map<String, Object> setValues, Map<String, Object> mapParamValues, int index,
                             boolean isSelective) {
        Iterator<Map.Entry<String, Object>> iterator = setValues.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        int subIndex = 1;
        String key;
        while (iterator.hasNext()) {
            Map.Entry<String, Object> en = iterator.next();
            key = makeSubKey(index, subIndex++);
            if (CommonUtils.isEmpty(en.getValue())) {
                if (isSelective) {
                    continue;
                }
                sb.append(en.getKey()).append("=null,");
            } else if (SQL_PARAM_NAME.equals(en.getKey())) {
                continue;
            } else {
                sb.append(en.getKey()).append("=#{").append(key).append("},");
                mapParamValues.put(key, en.getValue());
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 生成子键
     *
     * @param parIndex
     * @param index
     * @return
     */
    private String makeSubKey(int parIndex, int index) {
        return "P" + (parIndex * 1000 + index);
    }

    /**
     * 插入
     *
     * @param insertParamDefinition
     * @return
     */
    @Override
    public int insert(InsertParamDefinition insertParamDefinition) {
        List<Map<String, Object>> lstRows = insertParamDefinition.getLstRows();
        if (lstRows == null || lstRows.isEmpty()) {
            return -1;
        }
        int count = 0;
        for (Map<String, Object> row : lstRows) {
            count += insertRow(row, insertParamDefinition.getTableName());
        }
        return count;
    }

    private int insertRow(Map<String, Object> row, String tableName) {
        StringBuilder sbValue = new StringBuilder();
        StringBuilder sbPre = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = row.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (CommonUtils.isEmpty(entry.getValue())) {
                continue;
            }
            sbPre.append(entry.getKey()).append(",");
            sbValue.append("#{").append(entry.getKey()).append("},");
        }
        String sSql = "insert into " + tableName + "(" + sbPre.substring(0, sbPre.length() - 1)
                + ")values(" + sbValue.substring(0, sbValue.length() - 1) + ")";
        row.put(SQL_PARAM_NAME, sSql);
        return mapper.insert(row);
    }

    /**
     * 取得连接的唯一标识,用来区分不同的数据源
     *
     * @return
     */
    @Override
    public String getKey() {
        return CommonUtils.makeKey(dto.getName(), dto.getVersionCode());
    }

    /**
     * 取得名称,这是定义此数据源的名字,待用
     *
     * @return
     */
    @Override
    public String getName() {
        return dto.getDsName();
    }

    /**
     * 取得版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return dto.getVersionCode();
    }

    public DataOperatorDto getDto() {
        return dto;
    }

    public void setDto(DataOperatorDto dto) {
        this.dto = dto;
        if (this.dto == null) {
            dsKey = DataSourceUtils.getDefaultDataSourceKey();
        } else {
            dsKey = CommonUtils.makeKey(dto.getName(), dto.getVersionCode());
        }
    }
}
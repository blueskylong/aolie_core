package com.ranranx.aolie.core.ds.dataoperator.mybatis;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SqlTools;
import com.ranranx.aolie.core.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.core.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.core.ds.dataoperator.IDataOperator;
import com.ranranx.aolie.core.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.core.ds.definition.*;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

    public MyBatisDataOperator() {

    }

    public MyBatisDataOperator(DataOperatorDto dto) {
        this.setDto(dto);
    }

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
        DynamicDataSource.setDataSource(this.getKey());
        return select(genSelectParams(queryParamDefinition));
    }

    public static Map<String, Object> genSelectParams(QueryParamDefinition queryParamDefinition) {
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
        return mapParamValue;
    }


    /**
     * 生成多表的别名
     *
     * @param tableNames
     * @return
     */
    private static Map<String, String> genTableAlias(List<String> tableNames) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < tableNames.size(); i++) {
            map.put(tableNames.get(i), makeTableAliasString(i));
        }
        return map;
    }

    private static String makeTableAliasString(int index) {
        return "table" + UUID.randomUUID().toString().replace("-", "");
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
                ? null : queryParamDefinition.getFields(), null);
        Map<String, Object> mapParam = new HashMap<>();
        StringBuilder where = new StringBuilder();

            if (queryParamDefinition.hasCriteria()) {
                List<Criteria> lstCriteria = queryParamDefinition.getCriteria();
                int index = 1;
                for (Criteria criteria : lstCriteria) {
                    String aWhere = criteria.getSqlWhere(mapParam, null, index++, false);
                    if (!CommonUtils.isEmpty(aWhere)) {
                        where.append(criteria.getAndOr()).append("  ").append(aWhere);
                    }
                }
                if (where.length() > 0) {
                    where.replace(0, 4, "");
                    where.insert(0, " where ");
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
        sbSql.append("select ").append(field).append(" from ").append(tableName).append(where.toString()).append(orderExp);
        mapParam.put(SQL_PARAM_NAME, sbSql.toString());

        return select(mapParam);
    }


    private String getFieldsExp(List<Field> fields, String alias) {
        if (fields == null || fields.isEmpty()) {
            return "*";
        }
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {

            sb.append(field.getSelectExp(alias)).append(",");
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
            count += updateRow(row, updateParamDefinition.getCriteria(),
                    updateParamDefinition.getIdField(),
                    updateParamDefinition.getTableName(), updateParamDefinition.isSelective());

        }
        return count;
    }


    private int updateRow(Map<String, Object> row, Criteria criteria,
                          String filterFields, String tableName, boolean isSelective) {
        //情况1 ,存在id值及字段,则增加ID条件
        StringBuilder sb = new StringBuilder();
        int index = 0;
        String key;
        List<String> lstFilterField = Arrays.asList(filterFields.split(","));
        if (CommonUtils.isNotEmpty(filterFields)) {
            if (criteria == null) {
                criteria = new Criteria();
            }
            for (String field : lstFilterField) {
                if (CommonUtils.isEmpty(row.get(field))) {
                    criteria.andIsNull(field);
                } else {
                    criteria.andEqualTo(field, row.remove(field));
                }
            }

        }
        Map<String, Object> mapParam = new HashMap<>();
        sb.append("update ").append(tableName).append(" set ")
                .append(genSetSql(row, mapParam, index++, isSelective, lstFilterField));
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
                             boolean isSelective, List<String> expFields) {
        Iterator<Map.Entry<String, Object>> iterator = setValues.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        int subIndex = 1;
        String key;
        while (iterator.hasNext()) {
            Map.Entry<String, Object> en = iterator.next();
            key = makeSubKey(index, subIndex++);
            if (expFields.indexOf(en.getKey()) > -1) {
                continue;
            }
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

    @Override
    public void setDto(DataOperatorDto dto) {
        this.dto = dto;
        if (this.dto == null) {
            dsKey = DataSourceUtils.getDefaultDataSourceKey();
        } else {
            dsKey = CommonUtils.makeKey(dto.getName(), dto.getVersionCode());
        }
    }

    @Override
    public List<Map<String, Object>> selectDirect(Map<String, Object> mapParam) {
        return mapper.select(mapParam);
    }

    @Override
    public int executeDirect(Map<String, Object> mapParam) {
        String sql = CommonUtils.getStringField(mapParam, IDataOperator.SQL_PARAM_NAME);
        if (CommonUtils.isEmpty(sql)) {
            return -1;
        }
        sql = sql.toUpperCase().trim();
        if (sql.startsWith("delete")) {
            return mapper.delete(mapParam);
        } else if (sql.startsWith("insert")) {
            return mapper.insert(mapParam);
        }
        return mapper.update(mapParam);
    }
}

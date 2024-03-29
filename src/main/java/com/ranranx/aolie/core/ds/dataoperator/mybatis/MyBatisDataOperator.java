package com.ranranx.aolie.core.ds.dataoperator.mybatis;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.common.SqlTools;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.core.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.core.ds.dataoperator.IDataOperator;
import com.ranranx.aolie.core.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.core.ds.definition.*;
import com.ranranx.aolie.core.exceptions.InvalidConfigException;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/11 10:12
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

    private boolean oracleDb = false;

    public MyBatisDataOperator() {

    }

    private boolean isOracle() {
        return this.oracleDb;
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
        if (queryParamDefinition.getSqlExp() != null) {
            DynamicDataSource.setDataSource(dsKey);
            if (isOracle()) {
                return CommonUtils.keyToLowerCase(mapper.select(queryParamDefinition.getSqlExp().getExecuteMap()));
            }
            return mapper.select(queryParamDefinition.getSqlExp().getExecuteMap());
        }
        //分析是单表查询还是多表查询
        if (isSingleTable(queryParamDefinition)) {
            return singleTableSelect(queryParamDefinition);
        }
        return multiTableSelect(queryParamDefinition, new int[]{0});

    }

    private List<Map<String, Object>> select(Map<String, Object> map) {
        DynamicDataSource.setDataSource(dsKey);
        if (isOracle()) {
            return CommonUtils.keyToLowerCase(mapper.select(map));
        }
        return mapper.select(map);
    }

    /**
     * 多表查询
     *
     * @param queryParamDefinition
     * @return
     */
    private List<Map<String, Object>> multiTableSelect(QueryParamDefinition queryParamDefinition, int[] index) {
        DynamicDataSource.setDataSource(this.getKey());
        return select(SqlBuilder.genSelectParams(queryParamDefinition, null, index).getExecuteMap());
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

        Map<String, String> mapAlias = new HashMap<>();
        String mainTableAlias = "T_" + IdGenerator.getNextId("alias");
        mapAlias.put(tableName, mainTableAlias);
        if (queryParamDefinition.hasCriteria()) {
            List<Criteria> lstCriteria = queryParamDefinition.getCriteria();
            int[] index = new int[]{1};
            for (Criteria criteria : lstCriteria) {
                index[0] = index[0] + 1;
                String aWhere = criteria.getSqlWhere(mapParam, mapAlias, index, false);
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
        sbSql.append("select ").append(field).append(" from ").append(tableName)
                .append(" ").append(mainTableAlias).append(" ").append(where.toString()).append(orderExp);
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
        String mainTableAlias = "T_" + IdGenerator.getNextId("alias");
        if (deleteParamDefinition.getSqlExp() != null) {
            DynamicDataSource.setDataSource(dsKey);
            return mapper.delete(deleteParamDefinition.getSqlExp().getExecuteMap());
        }
        //根据ID字段删除优先
        StringBuilder sb = new StringBuilder();
        Map<String, Object> mapParamValue = new HashMap<>();
        if (deleteParamDefinition.getIds() != null && deleteParamDefinition.getIds().size() > 0) {
            if (CommonUtils.isEmpty(deleteParamDefinition.getIdField())) {
                throw new InvalidException("没有指定主键字段");
            }
            sb.append(SqlTools.genInClause(deleteParamDefinition.getIdField(), deleteParamDefinition.getIds(), new int[]{1}, mapParamValue));
        }
        if (deleteParamDefinition.getCriteria() != null) {
            Map<String, String> mapAlias = new HashMap<>();
            mapAlias.put(deleteParamDefinition.getTableName(), mainTableAlias);
            sb.append(deleteParamDefinition.getCriteria().getSqlWhere(mapParamValue, mapAlias,
                    new int[]{2}, sb.length() > 0));
        }
        String sSql = "delete ";
        if (!isOracle()) {
            sSql += mainTableAlias;
        }
        sSql += " from " + deleteParamDefinition.getTableName() + " " + mainTableAlias + " ";
        if (sb.length() > 0) {
            sSql += " where " + sb.toString();
        }
        mapParamValue.put(IDataOperator.SQL_PARAM_NAME, sSql);
        return mapper.delete(mapParamValue);
    }

    /**
     * 更新
     * 更新分为三种形式,
     * 1. sqlExp ,这个直接执行
     * 2. mapSetValues 与 Criteria 配合,前者设置值,后者条件,且条件必须有
     * 3. lstRows 根据KEY字段更新每一行.此不支持批量更新
     *
     * @param updateParamDefinition
     * @return
     */
    @Override
    public int update(UpdateParamDefinition updateParamDefinition) {
        //第一种更新执行
        if (updateParamDefinition.getSqlExp() != null) {
            DynamicDataSource.setDataSource(dsKey);
            return mapper.update(updateParamDefinition.getSqlExp().getExecuteMap());
        }
        //第二种批量更新
        if (updateParamDefinition.getMapSetValues() != null && !updateParamDefinition.getMapSetValues().isEmpty()) {
            return updateBatch(updateParamDefinition);
        }
        return updateByKeyField(updateParamDefinition);

    }

    /**
     * 批量更新
     *
     * @param updateParamDefinition
     * @return
     */
    private int updateBatch(UpdateParamDefinition updateParamDefinition) {
        Criteria criteria = updateParamDefinition.getCriteria();
        Map<String, Object> mapValue = new HashMap<>();
        int[] index = new int[]{0};
        Map<String, String> mapAlias = new HashMap<>();
        String mainTableAlias = "T_" + IdGenerator.getNextId("tableAlias");
        mapAlias.put(updateParamDefinition.getTableName(), mainTableAlias);
        String sqlWhere = criteria.getSqlWhere(mapValue, mapAlias, index, false);
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("update ").append(updateParamDefinition.getTableName())
                .append(" ").append(mainTableAlias).append(" set ")
                .append(genSetSql(updateParamDefinition.getMapSetValues(),
                        mapValue, index, mainTableAlias, updateParamDefinition.isSelective(), new ArrayList<>()))
                .append(" where ").append(sqlWhere);

        mapValue.put(SQL_PARAM_NAME, sbSql.toString());
        return mapper.update(mapValue);
    }

    /**
     * 根据KEY字段 更新
     *
     * @param updateParamDefinition
     * @return
     */
    private int updateByKeyField(UpdateParamDefinition updateParamDefinition) {
        //更新有二种情况,1是一行数据,如果带了ID字段则使用ID过滤 ,如果没有则需要指定复杂条件.
        if (updateParamDefinition.getLstRows() == null || updateParamDefinition.getLstRows().isEmpty()) {
            return -1;
        }
        int count = 0;
        for (Map<String, Object> row : updateParamDefinition.getLstRows()) {
            count += updateRow(row, CommonUtils.deepClone(updateParamDefinition.getCriteria()),
                    updateParamDefinition.getIdField(),
                    updateParamDefinition.getTableName(), updateParamDefinition.isSelective());

        }
        return count;
    }


    /**
     * 根据ID字段去更新
     *
     * @param row
     * @param criteria
     * @param filterFields
     * @param tableName
     * @param isSelective
     * @return
     */
    private int updateRow(Map<String, Object> row, Criteria criteria,
                          String filterFields, String tableName, boolean isSelective) {
        //情况1 ,存在id值及字段,则增加ID条件
        StringBuilder sb = new StringBuilder();
        int[] index = new int[]{0};
        String key;
        List<String> lstFilterField = Arrays.asList(filterFields.split(","));
        if (CommonUtils.isNotEmpty(filterFields)) {
            if (criteria == null) {
                criteria = new Criteria();
            }
            for (String field : lstFilterField) {
                if (CommonUtils.isEmpty(row.get(field))) {
                    criteria.andIsNull(null, field);
                } else {
                    criteria.andEqualTo(null, field, row.remove(field));
                }
            }

        }
        Map<String, Object> mapParam = new HashMap<>();
        sb.append("update ").append(tableName).append(" a set ")
                .append(genSetSql(row, mapParam, index, "a", isSelective, lstFilterField));
        if (criteria != null && !criteria.isEmpty()) {
            sb.append(" where ").append(criteria.getSqlWhere(mapParam, null, index, false));
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
    private String genSetSql(Map<String, Object> setValues, Map<String, Object> mapParamValues, int[] index, String alias,
                             boolean isSelective, List<String> expFields) {
        Iterator<Map.Entry<String, Object>> iterator = setValues.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        int subIndex = 1;
        String key;
        while (iterator.hasNext()) {
            Map.Entry<String, Object> en = iterator.next();
            index[0] = index[0] + 1;
            key = makeSubKey(index[0], subIndex++);
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
                sb.append(alias).append(".").append(en.getKey()).append("=#{").append(key).append("},");
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
        if (insertParamDefinition.getSqlExp() != null) {
            DynamicDataSource.setDataSource(dsKey);
            return mapper.insert(insertParamDefinition.getSqlExp().getExecuteMap());
        }
        List<Map<String, Object>> lstRows = insertParamDefinition.getLstRows();
        if (lstRows == null || lstRows.isEmpty()) {
            return -1;
        }
        return insertRowBatch(lstRows, insertParamDefinition.getTableName());
    }

    /**
     * 这里要注意,第一个数据的列决定了下面的数据哪些列会插入,所以要保持最多的列
     *
     * @param lstData
     * @param tableName
     * @return
     */
    private int insertRowBatch(List<Map<String, Object>> lstData, String tableName) {
        StringBuilder sbPre = new StringBuilder();
        Map<String, Object> row = lstData.get(0);
        Iterator<Map.Entry<String, Object>> iterator = row.entrySet().iterator();
        //取得第一行，并生成前插入语句
        int index = 1;//参数序列
        //将参数直接拼成字符串
        List<String> lstFieldName = new ArrayList<>();
        List<String> lstFieldType = new ArrayList<>();
        List<TableInfo> lstTable = SchemaHolder.findTablesByTableName(tableName, SessionUtils.getDefaultVersion());
        if (lstTable == null || lstTable.isEmpty()) {
            throw new InvalidConfigException("找到了重复的表设置:" + tableName);
        }
        TableInfo info = lstTable.get(0);
        String jdbcType = null;

        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            sbPre.append(entry.getKey()).append(",");
            lstFieldName.add(entry.getKey());
            jdbcType = info.getColumnMybatisType(entry.getKey());
            lstFieldType.add(CommonUtils.isEmpty(jdbcType) ? DmConstants.MyBatisColumnType.VARCHAR : jdbcType);
        }
        Map<String, Object> mapParam = new HashMap<>();
        String sSql = "insert into " + tableName + "(" + sbPre.substring(0, sbPre.length() - 1)
                + ")";
        if (!isOracle()) {
            sSql += "values";
        }
        if (isOracle()) {
            mapParam.put(SQL_PARAM_NAME, sSql + genOracleBatch(lstData, lstFieldName, lstFieldType, mapParam));
        } else {
            mapParam.put(SQL_PARAM_NAME, sSql + genMySqlBatch(lstData, lstFieldName, mapParam));
        }

        return mapper.insert(mapParam);
    }

    private String genMySqlBatch(List<Map<String, Object>> lstData,
                                 List<String> lstFieldName, Map<String, Object> mapParam) {
        StringBuffer sbValues = new StringBuffer();
        String paramPre = "P";
        int index = 1;
        String paramName;
        for (Map<String, Object> aRow : lstData) {
            sbValues.append("(");
            for (String field : lstFieldName) {
                paramName = paramPre + index++;
                sbValues.append("#{").append(paramName).append("},");
                //如果值为空则插入空字符串，让oracle不报错
                mapParam.put(paramName, aRow.get(field));
            }
            sbValues.delete(sbValues.length() - 1, sbValues.length());
            sbValues.append("),");
        }
        sbValues.delete(sbValues.length() - 1, sbValues.length());
        return sbValues.toString();
    }

    private String genOracleBatch(List<Map<String, Object>> lstData, List<String> lstFieldName,
                                  List<String> lstFieldType, Map<String, Object> mapParam) {
        StringBuffer sbValues = new StringBuffer("(");
        String paramPre = "P";
        int index = 1;
        String paramName;
        String field;
        for (Map<String, Object> aRow : lstData) {
            sbValues.append("select ");
            for (int i = 0; i < lstFieldName.size(); i++) {
                field = lstFieldName.get(i);
                paramName = paramPre + index++;
                sbValues.append("#{").append(paramName).append(",jdbcType=").append(lstFieldType.get(i)).append("},");
                //如果值为空则插入空字符串，让oracle不报错
                mapParam.put(paramName, aRow.get(field));
            }
            sbValues.delete(sbValues.length() - 1, sbValues.length());
            sbValues.append(" from dual union all ");
        }
        sbValues.delete(sbValues.length() - 10, sbValues.length()).append(")");
        return sbValues.toString();
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
            this.oracleDb = false;
        } else {
            dsKey = CommonUtils.makeKey(dto.getName(), dto.getVersionCode());
            if (dto.getUrl() == null) {
                this.oracleDb = false;
            } else {
                this.oracleDb = dto.getUrl().contains("oracle");
            }

        }

    }

    @Override
    public List<Map<String, Object>> selectDirect(Map<String, Object> mapParam) {
        if (isOracle()) {
            return CommonUtils.keyToLowerCase(mapper.select(mapParam));
        }
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

    /**
     * 取得数据库的类型
     *
     * @return
     */
    @Override
    public DmConstants.DbType getDbType() {
        return isOracle() ? DmConstants.DbType.ORACLE : DmConstants.DbType.MYSQL;
    }

    @Override
    public String convertColType(String colType) {
        colType = colType.toLowerCase();
        if (mapMysqlFieldTypeRelation.containsKey(colType)) {
            return mapMysqlFieldTypeRelation.get(colType);

        }
        return DmConstants.FieldType.VARCHAR;
    }

    static {
        mapMysqlFieldTypeRelation.put("varchar", DmConstants.FieldType.VARCHAR);
        mapMysqlFieldTypeRelation.put("varchar2", DmConstants.FieldType.VARCHAR);
        mapMysqlFieldTypeRelation.put("char", DmConstants.FieldType.VARCHAR);
        mapMysqlFieldTypeRelation.put("int", DmConstants.FieldType.INT);
        mapMysqlFieldTypeRelation.put("smallint", DmConstants.FieldType.INT);
        mapMysqlFieldTypeRelation.put("tinyint", DmConstants.FieldType.INT);
        mapMysqlFieldTypeRelation.put("bigint", DmConstants.FieldType.INT);
        mapMysqlFieldTypeRelation.put("decimal", DmConstants.FieldType.DECIMAL);
        mapMysqlFieldTypeRelation.put("float", DmConstants.FieldType.DECIMAL);
        mapMysqlFieldTypeRelation.put("number", DmConstants.FieldType.DECIMAL);
        mapMysqlFieldTypeRelation.put("datetime", DmConstants.FieldType.DATETIME);
        mapMysqlFieldTypeRelation.put("date", DmConstants.FieldType.DATETIME);
        mapMysqlFieldTypeRelation.put("text", DmConstants.FieldType.TEXT);
        mapMysqlFieldTypeRelation.put("binary", DmConstants.FieldType.BINARY);
    }
}

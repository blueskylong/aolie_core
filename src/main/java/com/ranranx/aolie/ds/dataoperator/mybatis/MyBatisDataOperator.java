package com.ranranx.aolie.ds.dataoperator.mybatis;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.datamodal.Field;
import com.ranranx.aolie.datameta.datamodal.SchemaHolder;
import com.ranranx.aolie.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.ds.dataoperator.IDataOperator;
import com.ranranx.aolie.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.ds.definition.*;
import com.ranranx.aolie.exceptions.InvalidParamException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Table;
import java.util.HashMap;
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
        return null;
    }

    /**
     * 单表查询
     *
     * @param queryParamDefinition
     * @return
     */
    private List<Map<String, Object>> singleTableSelect(QueryParamDefinition queryParamDefinition) {
        String tableName = getSingleSelectTableName(queryParamDefinition);
        String field = getFieldsExp(queryParamDefinition.getFields(), queryParamDefinition.getVersion());
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
                orderExp += order.getOrderExp() + ",";
            }
            orderExp = " order by " + orderExp.substring(0, orderExp.length() - 1);
        }
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select ").append(field).append(" from ").append(tableName).append(where).append(orderExp);
        mapParam.put("sql", sbSql.toString());
        return mapper.select(mapParam);
    }

    /**
     * 取得单表选择的表名.这里有约定,如果有Table信息,则以其为优先,然后是类名上的注解
     *
     * @param queryParamDefinition
     * @return
     */
    private String getSingleSelectTableName(QueryParamDefinition queryParamDefinition) {
        if (queryParamDefinition.getTable() != null && queryParamDefinition.getTable().length == 1) {
            return queryParamDefinition.getTable()[0].getTableDto().getTableName();
        }
        Class<?> clazz = queryParamDefinition.getClazz();
        if (clazz == null) {
            throw new InvalidParamException("查询操作没有找到指定的表信息");
        }
        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation == null || CommonUtils.isEmpty(annotation.name())) {
            throw new InvalidParamException("查询操作没有找到指定的表信息");
        }
        return annotation.name();
    }

    private String getFieldsExp(Long[] fieldIds, String version) {
        if (fieldIds == null || fieldIds.length == 0) {
            return "*";
        }
        StringBuilder sb = new StringBuilder();
        Field field;
        for (Long id : fieldIds) {
            field = SchemaHolder.getField(id, version);
            sb.append(SchemaHolder.getColumn(field.getFieldDto().getColumnId(), version)
                    .getColumnDto().getFieldName()).append(",");
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
        return queryParamDefinition.getClazz() != null ||
                (queryParamDefinition.getTable() != null && queryParamDefinition.getTable().length == 1);
    }

    /**
     * 删除
     *
     * @param deleteParamDefinition
     * @return
     */
    @Override
    public int delete(DeleteParamDefinition deleteParamDefinition) {
        return 0;
    }

    /**
     * 更新
     *
     * @param updateParamDefinition
     * @return
     */
    @Override
    public int update(UpdateParamDefinition updateParamDefinition) {
        return 0;
    }

    /**
     * 插入
     *
     * @param insertParamDefinition
     * @return
     */
    @Override
    public int insert(InsertParamDefinition insertParamDefinition) {
        return 0;
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

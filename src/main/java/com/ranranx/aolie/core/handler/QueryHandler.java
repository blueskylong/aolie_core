package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableColumnRelation;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.ds.definition.TableRelation;
import com.ranranx.aolie.core.handler.param.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *  数据查询服务
 * @date 2020/8/4 14:33
 * @version V0.0.1
 **/
@Component
public class QueryHandler<T extends QueryParam> extends BaseHandler<T> {
    @Autowired
    private DataOperatorFactory factory;

    /**
     * 查询所有的子查询的数据 //TODO
     *
     * @param param
     * @return
     */
    private HandleResult findAllResult(QueryParam param) {

        //TODO 这里需要将查询分解,合并
        List<Map<String, Object>> lstData = factory.getDataOperatorByKey(null).select(convertParam(param));

        if (param.getResultClass() != null) {
            lstData = CommonUtils.convertCamelAndToObject(lstData, param.getResultClass());
        }


        HandleResult result = new HandleResult();
        result.setSuccess(true);
        result.setData(lstData);
        return result;
    }

    private static Class getGenericClass(QueryParam param) {
        Class<? extends QueryParam> aClass = param.getClass();
        if (!(aClass.getGenericSuperclass() instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType type = (ParameterizedType) aClass.getGenericSuperclass();
        if (type == null) {
            return null;
        }
        Type[] tp = type.getActualTypeArguments();
        if (tp == null || tp.length == 0) {
            return null;
        }
        return tp[0].getClass();

    }


    /**
     * 转换成内部 查询参数
     *
     * @param param
     * @return
     */
    private QueryParamDefinition convertParam(QueryParam param) {
        QueryParamDefinition paramDefinition = new QueryParamDefinition();
        if (param.getSqlExp() != null) {
            paramDefinition.setSqlExp(param.getSqlExp());
            return paramDefinition;
        }
        TableInfo[] table = param.getTable();
        List<String> lstTableName = new ArrayList<>();

        Long tableId[] = new Long[table.length];
        for (int i = 0; i < table.length; i++) {
            lstTableName.add(table[i].getTableDto().getTableName());
            tableId[i] = table[i].getTableDto().getTableId();
        }
        paramDefinition.setTableNames(lstTableName);
        paramDefinition.setLstCriteria(param.getLstCriteria());
        paramDefinition.setLstOrder(param.getLstOrder());
        paramDefinition.setFields(param.getFields());
        paramDefinition.setPage(param.getPage());
        List<TableColumnRelation> tableRelations = SchemaHolder.getTableRelations(table[0].getTableDto().getVersionCode(), tableId);
        paramDefinition.setLstRelation(convertRelation(tableRelations));
        return paramDefinition;
    }

    /**
     * 转换成内部查询的关系
     *
     * @param lstColRelation
     * @return
     */
    private List<TableRelation> convertRelation(List<TableColumnRelation> lstColRelation) {
        if (lstColRelation == null || lstColRelation.isEmpty()) {
            return null;
        }
        String version = lstColRelation.get(0).getDto().getVersionCode();
        TableRelation newRelation;
        List<TableRelation> result = new ArrayList<>();
        for (TableColumnRelation relation : lstColRelation) {
            newRelation = new TableRelation();
            newRelation.setFieldLeft(
                    SchemaHolder.getColumn(relation.getDto()
                            .getFieldFrom(), version).getColumnDto().getFieldName());
            newRelation.setTableLeft(relation.getTableFrom().getTableDto().getTableName());
            newRelation.setFieldRight(SchemaHolder.getColumn(relation.getDto()
                    .getFieldTo(), version).getColumnDto().getFieldName());
            newRelation.setTableRight(relation.getTableTo().getTableDto().getTableName());
            newRelation.setJoinType(Constants.JoinType.INNER_JOIN);
            result.add(newRelation);
        }
        return result;

    }

    /**
     * 合并数据
     *
     * @param param
     * @param mapResult
     * @return
     */
    private HandleResult mergeResults(QueryParam param, Map<String, HandleResult> mapResult) {
        if (mapResult == null || mapResult.isEmpty()) {
            return new HandleResult();
        } else if (mapResult.size() == 1) {
            return mapResult.values().iterator().next();
        }
        //TODO doMerge
        return null;
    }


    /**
     * 默认可以处理的类型
     *
     * @return
     */
    @Override
    String getCanHandleType() {
        return Constants.HandleType.TYPE_QUERY;
    }

    /**
     * 执行的地方
     *
     * @param param
     * @return
     */
    @Override
    protected HandleResult handle(QueryParam param) {
        return findAllResult(param);
    }


    /**
     * 是否需要事务
     *
     * @return
     */
    @Override
    public boolean needTransaction() {
        return false;
    }
}

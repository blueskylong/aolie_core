package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableColumnRelation;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.ds.definition.TableRelation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 21:39
 **/
public class ParamConverter {

    /**
     * 转换成内部 查询参数
     *
     * @param param
     * @param outTables 外部使用的表
     * @return
     */
    public static QueryParamDefinition convertQueryParam(QueryParam param, List<String> outTables) {
        QueryParamDefinition paramDefinition = new QueryParamDefinition();
        if (param.getSqlExp() != null) {
            paramDefinition.setSqlExp(param.getSqlExp());
            return paramDefinition;
        }
        TableInfo[] table = param.getTables();
        List<String> lstTableName = new ArrayList<>();

        Long tableId[] = new Long[table.length];
        for (int i = 0; i < table.length; i++) {
            lstTableName.add(table[i].getTableDto().getTableName());
            tableId[i] = table[i].getTableDto().getTableId();
        }
        paramDefinition.setTableNames(lstTableName);
        paramDefinition.setLstCriteria(param.getCriterias());
        paramDefinition.setLstOrder(param.getLstOrder());
        paramDefinition.setFields(param.getFields());
        paramDefinition.setPage(param.getPage());
        List<TableColumnRelation> tableRelations = SchemaHolder.getTableRelations(table[0].getTableDto().getVersionCode(), tableId);


        //过滤多余的关联
        List<String> lstAllTable = new ArrayList<>();
        if (outTables != null) {
            lstAllTable.addAll(outTables);
        }
        lstAllTable.addAll(lstTableName);
        tableRelations = findValidRelation(lstAllTable, tableRelations);

        if (param.getLstRelation() != null) {
            if (tableRelations == null) {
                tableRelations = new ArrayList<>();
            }
            //只留下与查询表有直接关联的联系
            tableRelations.addAll(param.getLstRelation());
        }
        paramDefinition.setLstRelation(convertRelation(tableRelations));
        return paramDefinition;
    }

    static List<TableColumnRelation> findValidRelation(List<String> tables, List<TableColumnRelation> tableRelations) {
        if (tableRelations == null || tableRelations.isEmpty()) {
            return tableRelations;
        }
        //关联二边的表，都要出现的查询需要的表中
        List<TableColumnRelation> result = new ArrayList<>();
        for (TableColumnRelation relation : tableRelations) {
            if (tables.indexOf(relation.getTableTo().getTableDto().getTableName()) != -1
                    && tables.indexOf(relation.getTableFrom().getTableDto().getTableName()) != -1) {
                result.add(relation);
            }
        }
        return result;
    }


    /**
     * 转换成内部查询的关系
     *
     * @param lstColRelation
     * @return
     */
    private static List<TableRelation> convertRelation(List<TableColumnRelation> lstColRelation) {
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
            newRelation.setLstCriteria(relation.getLstCriteria());
            if (relation.getDto().getRelationType() <= 9) {

                newRelation.setJoinType(Constants.JoinType.INNER_JOIN);
            } else {
                newRelation.setJoinType(Constants.JoinType.LEFT_JOIN);
            }
            result.add(newRelation);
        }
        return result;

    }
}

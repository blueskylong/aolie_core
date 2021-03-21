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
     * @return
     */
    public static QueryParamDefinition convertQueryParam(QueryParam param) {
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

        if (param.getLstRelation() != null) {
            if (tableRelations == null) {
                tableRelations = new ArrayList<>();
            }
            tableRelations.addAll(param.getLstRelation());
        }
        paramDefinition.setLstRelation(convertRelation(tableRelations));
        return paramDefinition;
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
            newRelation.setJoinType(Constants.JoinType.INNER_JOIN);
            result.add(newRelation);
        }
        return result;

    }
}

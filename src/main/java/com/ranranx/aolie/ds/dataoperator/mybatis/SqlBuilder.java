package com.ranranx.aolie.ds.dataoperator.mybatis;

import com.ranranx.aolie.common.Constants;
import com.ranranx.aolie.common.SqlTools;
import com.ranranx.aolie.ds.definition.Field;
import com.ranranx.aolie.ds.definition.FieldOrder;
import com.ranranx.aolie.ds.definition.TableRelation;
import com.ranranx.aolie.handler.param.condition.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/19 11:16
 * @Version V0.0.1
 **/
public class SqlBuilder {
    /**
     * 生成查询字段
     *
     * @param lstField
     * @param mapAlias
     * @return
     */
    public static String buildFields(List<Field> lstField, Map<String, String> mapAlias) {
        StringBuilder sb = new StringBuilder();
        if (lstField == null || lstField.isEmpty()) {
            return " * ";
        }
        for (Field field : lstField) {
            if (field.isShow()) {
                sb.append(field.getSelectExp(mapAlias.get(field.getTableName()))).append(",");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 生成表信息  如    tabl1 a left join tbl2 b on a.field1=b.field1,tbl3 c
     *
     * @param lstRelation
     * @param mapTableAlias
     * @param tableNames
     * @return
     */
    public static String buildTables(List<TableRelation> lstRelation, Map<String, String> mapTableAlias, List<String> tableNames) {
        List<String> hasAddedTable = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (lstRelation != null && !lstRelation.isEmpty()) {
            for (TableRelation relation : lstRelation) {
                sb.append(addTable(relation, mapTableAlias, lstRelation,
                        relation == lstRelation.get(0), hasAddedTable));
            }
        }
        //如果还存在没有关联的表,则直接加到后面
        List<String> notUsedTable = findNotUsedTable(tableNames, hasAddedTable);
        if (!notUsedTable.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            for (String tableName : notUsedTable) {
                sb.append(tableName).append(SqlTools.roundSpace(mapTableAlias.get(tableName))).append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    private static List<String> findNotUsedTable(List<String> allTable, List<String> usedTable) {
        List<String> remainTable = new ArrayList<>();
        for (String aTable : allTable) {
            if (usedTable.indexOf(aTable) == -1) {
                remainTable.add(aTable);
            }
        }
        return remainTable;
    }

    private static List<TableRelation> findRelationTables(String[] tableName, List<TableRelation> lstRelation) {
        List<TableRelation> lstResult = new ArrayList<>();
        for (TableRelation relation : lstRelation) {
            for (int i = 0; i < tableName.length; i++) {
                if (tableName[i].equals(relation.getTableRight()) || tableName[i].equals(relation.getTableLeft())) {
                    if (lstResult.indexOf(relation) != -1) {
                        lstResult.add(relation);
                    }
                }
            }

        }
        return lstResult;
    }

    /**
     * 添加右表,左边已添加过
     *
     * @param relation
     * @param tableAlias
     * @return
     */
    private static String addRightTable(TableRelation relation,
                                        Map<String, String> tableAlias, List<String> hadAddedTables) {
        StringBuilder sb = new StringBuilder();
        String aliasLeft = tableAlias.get(relation.getTableLeft());
        String aliasRight = tableAlias.get(relation.getTableRight());
        sb.append(" " + relation.getJoinType() + " ").append(relation.getTableRight())
                .append(SqlTools.roundSpace(aliasRight));
        hadAddedTables.add(relation.getTableRight());
        //添加关联
        if (relation.getFieldLeft() != null && relation.getFieldLeft().length > 0) {

            int len = relation.getFieldLeft().length;
            for (int i = 0; i < len; i++) {
                sb.append(" on ").append(aliasLeft).append(".").append(relation.getFieldLeft()[i]).append("=")
                        .append(aliasRight).append(".").append(relation.getFieldRight()[i]).append(" and ");
            }
            sb.delete(sb.length() - 4, sb.length());
        }
        return sb.toString();
    }

    /**
     * 增加剩下的表
     */
    private static String addRemainTable(TableRelation relation, boolean isLeftTable, Map<String, String> tableAlias, List<String> hadAddedTables) {
        if (!isLeftTable) {
            return addRightTable(relation, tableAlias, hadAddedTables);
        }
        //增加剩下的表是左表,则关系要反转
        StringBuilder sb = new StringBuilder();
        String aliasLeft = tableAlias.get(relation.getTableLeft());
        String aliasRight = tableAlias.get(relation.getTableRight());
        sb.append(" " + relation.getReverseJoinType() + " ").append(relation.getTableLeft())
                .append(SqlTools.roundSpace(tableAlias.get(aliasLeft)));
        hadAddedTables.add(relation.getTableLeft());
        //添加关联
        if (relation.getFieldLeft() != null && relation.getFieldLeft().length > 0) {

            int len = relation.getFieldLeft().length;
            for (int i = 0; i < len; i++) {
                sb.append(" on ").append(aliasRight).append(".").append(relation.getFieldRight()[i]).append("=")
                        .append(aliasLeft).append(".").append(relation.getFieldLeft()[i]).append(" and ");
            }
            sb.delete(sb.length() - 4, sb.length());
        }
        return sb.toString();
    }

    /**
     * 添加主表
     *
     * @param relation
     * @param tableAlias
     * @param lstRelation
     * @param isFirstTable
     * @param hadAddedTables
     * @return
     */
    private static String addTable(TableRelation relation, Map<String, String> tableAlias, List<TableRelation> lstRelation,
                                   boolean isFirstTable, List<String> hadAddedTables) {
        if (isAllDone(relation, hadAddedTables)) {
            return "";
        }
        //先找这二个表,是不是有前置的表关系
        StringBuilder sb = new StringBuilder();
        //如果是第一个
        if (isFirstTable) {
            sb.append(relation.getTableLeft()).append(SqlTools.roundSpace(tableAlias.get(relation.getTableLeft())));
            hadAddedTables.add(relation.getTableLeft());
            sb.append(addRightTable(relation, tableAlias, hadAddedTables));
            return sb.toString();
        } else {
            //寻找左表有关系的
            List<TableRelation> relationTables =
                    findRelationTables(new String[]{relation.getTableLeft(), relation.getTableRight()}, lstRelation);
            if (relationTables.size() > 1) {
                //去了本身
                relationTables.remove(relation);
                for (TableRelation superRelation : relationTables) {
                    sb.append(addTable(superRelation, tableAlias, lstRelation, false, hadAddedTables));
                }
            }
            //再次判断,是不是表都已经存在
            if (isAllDone(relation, hadAddedTables)) {
                return sb.toString();
            }
            if (hadAddedTables.indexOf(relation.getTableRight()) != -1 &&
                    hadAddedTables.indexOf(relation.getTableLeft()) == -1) {
                //如果右表已增加,左表没有增加,则增加左表
                sb.append(addRemainTable(relation, true, tableAlias, hadAddedTables));
            } else if (hadAddedTables.indexOf(relation.getTableRight()) == -1 &&
                    hadAddedTables.indexOf(relation.getTableLeft()) != -1) {
                //如果左表没有增加,则增加右表
                sb.append(addRemainTable(relation, true, tableAlias, hadAddedTables));
            } else {
                //如果二张表都没有增加
                //先增加左表
                sb.append(addNotFirstLeftTable(relation, tableAlias, hadAddedTables))
                        .append(addRemainTable(relation, false, tableAlias, hadAddedTables));
            }
            return sb.toString();

        }
    }

    private static String addNotFirstLeftTable(TableRelation relation,
                                               Map<String, String> tableAlias, List<String> hadAddedTables) {
        StringBuilder sb = new StringBuilder();
        String aliasLeft = tableAlias.get(relation.getTableLeft());
        sb.append(",").append(relation.getTableLeft())
                .append(SqlTools.roundSpace(aliasLeft)).append(" ");
        hadAddedTables.add(relation.getTableLeft());
        return sb.toString();

    }

    /**
     * 是否表都已处理过
     *
     * @param relation
     * @param hadAddedTables
     * @return
     */
    private static boolean isAllDone(TableRelation relation, List<String> hadAddedTables) {
        return hadAddedTables.indexOf(relation.getTableLeft()) != -1
                && hadAddedTables.indexOf(relation.getTableRight()) != -1;
    }

    /**
     * 取得表相关联的字段表达式
     *
     * @param tableAliasMaster
     * @param tableAliasSlave
     * @param fieldMaster
     * @param fieldSlave
     * @return
     */
    private static String getTableJoinFieldExp(String tableAliasMaster, String tableAliasSlave,
                                               String[] fieldMaster, String[] fieldSlave) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldMaster.length; i++) {
            sb.append(tableAliasMaster).append(".").append(fieldMaster[i]).append("=").append(tableAliasSlave)
                    .append(".").append(fieldSlave[i]).append(" and ");
        }
        return sb.substring(0, sb.length() - 4);
    }

    /**
     * 生成查询条件
     *
     * @param tableAlias
     * @param lstCriteria
     * @param paramValues
     * @return
     */
    public static String getWhere(Map<String, String> tableAlias, List<Criteria> lstCriteria, Map<String, Object> paramValues) {
        if (lstCriteria == null || lstCriteria.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Criteria criteria : lstCriteria) {
            sb.append(criteria.getSqlWhere(paramValues, tableAlias.get(criteria.getTableName()), index++, criteria != lstCriteria.get(0)));
        }
        return sb.toString();
    }

    /**
     * 生成分组信息
     *
     * @param tableAlias
     * @param fields
     * @return
     */
    public static String genGroupBy(Map<String, String> tableAlias, List<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            if (field.getGroupType() == Constants.GroupType.NONE) {
                sb.append(tableAlias.get(field.getTableName())).append(".").append(field.getFieldName()).append(",");
            }
        }
        if (sb.length() > 0) {
            return " group by " + sb.delete(sb.length() - 1, sb.length());
        }
        return "";
    }

    /**
     * 取得排序语句
     *
     * @param tableAlias
     * @param lstOrder
     * @return
     */
    public static String genOrder(Map<String, String> tableAlias, List<FieldOrder> lstOrder) {
        if (lstOrder == null || lstOrder.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (FieldOrder order : lstOrder) {
            sb.append(order.getOrderExp(tableAlias.get(order.getTableName()))).append(",");
        }
        return " order by " + sb.substring(0, sb.length() - 1);
    }
}

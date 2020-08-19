package com.ranranx.aolie.testobj;

import com.ranranx.aolie.common.Constants;
import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.ds.definition.*;
import com.ranranx.aolie.handler.param.condition.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/11 13:10
 * @Version V0.0.1
 **/
@RestController
public class TestController {
    @Autowired
    private DataOperatorFactory factory;

    @RequestMapping("/findContries")
    public List<Map<String, Object>> findContries() {
        return factory.getDataOperatorByName("mybatisDbOperator", "1").select(null);
    }

    @RequestMapping("/testQuery")
    public List<Map<String, Object>> query() {
        String mainTable = "aolie_dm_table";
        String subTable = "aolie_dm_column";
        QueryParamDefinition definition = new QueryParamDefinition();
        List<String> lstTableName = new ArrayList<>();
        lstTableName.add(mainTable);
        lstTableName.add(subTable);
        definition.setTableNames(lstTableName);
        Criteria criteria = definition.appendCriteria();
        criteria.setTableName(mainTable);
        criteria.andEqualTo("table_id", 1);
        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.setFieldName("table_id");
        field.setTableName(mainTable);
        fields.add(field);
        field = new Field();
        field.setTableName(mainTable);
        field.setFieldName("table_name");
        fields.add(field);

        field = new Field();
        field.setTableName(subTable);
        field.setFieldName("title");
        fields.add(field);
        field = new Field();
        field.setTableName(subTable);
        field.setFieldName("field_index");
        fields.add(field);
        field = new Field();
        field.setTableName(subTable);
        field.setFieldName("field_name");
        fields.add(field);
        definition.setFields(fields);

        List<TableRelation> lstRelation = new ArrayList<>();
        TableRelation relation = new TableRelation();
        relation.setTableLeft(mainTable);
        relation.setTableRight(subTable);
        relation.setFieldLeft(new String[]{"table_id"});
        relation.setFieldRight(new String[]{"table_id"});
        lstRelation.add(relation);
        definition.setLstRelation(lstRelation);

        List<FieldOrder> lstOrder = new ArrayList<>();
        FieldOrder order = new FieldOrder();
        order.setTableName(subTable);
        order.setField("field_index");
        order.setAsc(false);
        lstOrder.add(order);
        definition.setLstOrder(lstOrder);

        List<Map<String, Object>> lstData = factory.getDefaultDataOperator().select(definition);
        //模拟分组查询
        field.setGroupType(Constants.GroupType.COUNT);
        List<Map<String, Object>> lstData2 = factory.getDefaultDataOperator().select(definition);
        lstData.addAll(lstData2);
        return lstData;

    }

    @RequestMapping("/test")
    @Transactional
    public int otherTest() {
        String tableName = "aolie_dm_table";
        String idField = "table_id";
        //增加
        Map<String, Object> mapRow = new HashMap<>();
        mapRow.put(idField, 2);
        mapRow.put("table_name", "手动增加的表");
        List<Map<String, Object>> lstRows = new ArrayList<>();
        lstRows.add(mapRow);
        InsertParamDefinition insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setLstRows(lstRows);
        insertParamDefinition.setTableName(tableName);
        factory.getDefaultDataOperator().insert(insertParamDefinition);

        mapRow.put("table_name", "这是不真的");
        UpdateParamDefinition updateParamDefinition = new UpdateParamDefinition();
        updateParamDefinition.setTableName(tableName);
        updateParamDefinition.setIdField(idField);
        updateParamDefinition.setLstRows(lstRows);
        factory.getDefaultDataOperator().update(updateParamDefinition);

        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        List<String> lstTableName = new ArrayList<>();
        lstTableName.add("aolie_dm_table");
        queryParamDefinition.setTableNames(lstTableName);
        queryParamDefinition.appendCriteria().andEqualTo("table_id", 2);

        List<Map<String, Object>> lst = factory.getDefaultDataOperator().select(queryParamDefinition);
        System.out.println(lst);

        DeleteParamDefinition deleteParamDefinition = new DeleteParamDefinition();
        deleteParamDefinition.setTableName(tableName);
        deleteParamDefinition.setIdField(idField);
        ArrayList<Object> lstId = new ArrayList<>();
        lstId.add(2);
        deleteParamDefinition.setIds(lstId);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);
        return 0;
    }
}
package com.ranranx.aolie.testobj;

import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.ds.definition.DeleteParamDefinition;
import com.ranranx.aolie.ds.definition.InsertParamDefinition;
import com.ranranx.aolie.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.ds.definition.UpdateParamDefinition;
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
        queryParamDefinition.setTableName("aolie_dm_table");
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

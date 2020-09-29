package com.ranranx.aolie.controller;

import com.ranranx.aolie.common.SessionUtils;
import com.ranranx.aolie.datameta.datamodel.Schema;
import com.ranranx.aolie.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.datameta.dto.ColumnDto;
import com.ranranx.aolie.datameta.dto.ConstraintDto;
import com.ranranx.aolie.datameta.dto.FormulaDto;
import com.ranranx.aolie.service.DataModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/29 20:59
 * @Version V0.0.1
 **/
@RestController
@RequestMapping("/dm")
public class DataModelController {
    @Autowired
    private DataModelService service;

    @Autowired
    private SchemaHolder schemaHolder;

    @GetMapping("/findSchemaFormulas/{schemaId}/{version}")
    public List<FormulaDto> findSchemaFormulas(@PathVariable Long schemaId, @PathVariable String version) {
        return service.findSchemaFormula(schemaId, version);
    }

    @GetMapping("/findSchemaConstraints/{schemaId}/{version}")
    public List<ConstraintDto> findSchemaConstraints(@PathVariable Long schemaId, @PathVariable String version) {
        return service.findSchemaConstraints(schemaId, version);
    }

    @GetMapping("/findSchemaInfo/{schemaId}/{version}")
    public Schema findSchemaInfo(@PathVariable Long schemaId, @PathVariable String version) {
        return schemaHolder.getSchema(schemaId, version);
    }

    /**
     * 查询默认的数据连接的表，并且还没有增到方案中。
     *
     * @param schemaId
     * @return
     */
    @GetMapping("/findDefaultDBTablesNotInSchema/{schemaId}")
    public List<String> findDefaultDBTablesNotInSchema(@PathVariable long schemaId) {
        return service.findDefaultDBTablesNotInSchema(schemaId, SessionUtils.getLoginVersion());
    }

    @GetMapping("/findTableFieldAsColumnDto/{tableName}")
    public List<ColumnDto> findTableFieldAsColumnDto(@PathVariable String tableName) {
        return service.findTableFieldAsColumnDto(tableName);
    }

    /**
     * 保存方案
     *
     * @param schema
     * @return
     */

    @PostMapping("/saveSchema")
    public String saveSchema(@RequestBody Schema schema) {
        return schemaHolder.saveSchema(schema);
    }

    @RequestMapping("/refreshCache/{schemaId}/{version}")
    public boolean refreshCache(@PathVariable long schemaId, @PathVariable String version) {
        schemaHolder.initSchema(schemaId, version);
        return true;
    }

}

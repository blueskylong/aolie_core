package com.ranranx.aolie.core.controller;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.Schema;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.dto.*;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.service.DataModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 取得所有的方案信息
     *
     * @return
     */
    @GetMapping("/findAllSchemaDto")
    public List<SchemaDto> findAllSchemaDto() {
        return service.findAllSchemaDto(false);
    }

    /**
     * 取得所有的方案信息
     *
     * @return
     */
    @GetMapping("/findSchemaIds")
    public List<Long> findSchemaIds() {
        List<SchemaDto> allSchemaDto = service.findAllSchemaDto(true);
        if (allSchemaDto == null) {
            return null;
        }
        List<Long> lstResult = new ArrayList<>(allSchemaDto.size());
        for (SchemaDto dto : allSchemaDto) {
            lstResult.add(dto.getSchemaId());
        }
        return lstResult;
    }

    /**
     * 增加方案
     *
     * @return
     */
    @PostMapping("/addSchema")
    public long addSchema(@RequestBody Map<String, Object> params) {
        String schemaName = CommonUtils.getStringField(params, "schemaName");
        return service.addSchema(schemaName);
    }

    /**
     * 用于公开 使用的删除
     *
     * @param schemaId
     */
    @RequestMapping("/deleteSchema/{schemaId}")
    public HandleResult deleteSchema(@PathVariable Long schemaId) {
        schemaHolder.deleteSchema(schemaId, SessionUtils.getLoginVersion());
        return HandleResult.success(1);
    }

    /**
     * 同步更新表列信息
     *
     * @param tableId
     */
    @RequestMapping("/getSyncTableCols/{tableId}")
    public List<ColumnDto> getSyncTableCols(@PathVariable long tableId) {
        return service.getSyncTableCols(tableId, SessionUtils.getLoginVersion());
    }

    /**
     * 查询引用数据
     *
     * @return
     */
    @GetMapping("/findReferenceInfo")
    public List<ReferenceDto> findReferenceData() {
        return schemaHolder.getReferenceDtos();
    }

    /**
     * 保存引用信息
     *
     * @param lstDto
     */
    @PostMapping("/saveReference")
    public HandleResult saveReference(@RequestBody List<ReferenceDto> lstDto) {
        service.saveReference(lstDto);
        return HandleResult.success(1);
    }

}

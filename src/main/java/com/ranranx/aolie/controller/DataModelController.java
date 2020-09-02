package com.ranranx.aolie.controller;

import com.ranranx.aolie.datameta.dto.ConstraintDto;
import com.ranranx.aolie.datameta.dto.FormulaDto;
import com.ranranx.aolie.service.DataModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/findSchemaFormulas/{schemaId}/{version}")
    public List<FormulaDto> findSchemaFormulas(@PathVariable Long schemaId, @PathVariable String version) {
        return service.findSchemaFormula(schemaId, version);
    }

    @GetMapping("/findSchemaConstraints/{schemaId}/{version}")
    public List<ConstraintDto> findSchemaConstraints(@PathVariable Long schemaId, @PathVariable String version) {
        return service.findSchemaConstraints(schemaId, version);
    }


}

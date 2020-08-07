package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.datameta.dto.SchemaDto;
import com.ranranx.aolie.interfaces.SchemaMessageReceiveHandler;
import com.ranranx.aolie.interfaces.SchemaMessageSendHandler;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:35
 * @Version V0.0.1
 **/
public class Schema {

    private SchemaDto schemaDto;
    /**
     * 下属表
     */
    private List<Table> lstTable;
    /**
     * 表间约束
     */
    private List<Constraint> lstConstraint;
    /**
     * 外部消息算是器
     */
    private List<SchemaMessageReceiveHandler> lstReceiveHandler;
    /**
     * 通知外面消息
     */
    private List<SchemaMessageSendHandler> lstSendHandler;

    public List<Constraint> getLstConstraint() {
        return lstConstraint;
    }

    public void setLstConstraint(List<Constraint> lstConstraint) {
        this.lstConstraint = lstConstraint;
    }

    public List<Table> getLstTable() {
        return lstTable;
    }

    public void setLstTable(List<Table> lstTable) {
        this.lstTable = lstTable;
    }

    public SchemaDto getSchemaDto() {
        return schemaDto;
    }

    public void setSchemaDto(SchemaDto schemaDto) {
        this.schemaDto = schemaDto;
    }

    public List<SchemaMessageReceiveHandler> getLstReceiveHandler() {
        return lstReceiveHandler;
    }

    public void setLstReceiveHandler(List<SchemaMessageReceiveHandler> lstReceiveHandler) {
        this.lstReceiveHandler = lstReceiveHandler;
    }

    public List<SchemaMessageSendHandler> getLstSendHandler() {
        return lstSendHandler;
    }

    public void setLstSendHandler(List<SchemaMessageSendHandler> lstSendHandler) {
        this.lstSendHandler = lstSendHandler;
    }
}

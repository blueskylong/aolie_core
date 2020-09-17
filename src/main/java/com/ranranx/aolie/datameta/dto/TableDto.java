package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

import javax.persistence.Table;
import java.util.List;

/**
 * @Author xxl
 * @Description 表信息
 * @Date 2020/8/4 16:33
 * @Version V0.0.1
 **/
@Table(name = "AOLIE_DM_TABLE")
public class TableDto extends BaseDto {
    /**
     * 设计ID
     */
    private Long schemaId;
    /**
     * 表ID
     */
    private Long tableId;
    /**
     * 表英文名
     */
    private String tableName;
    /**
     * 表中文名
     */
    private String title;

    /**
     * 是否只读
     */
    private Integer readOnly;

    /**
     * 数据源名
     */
    private Long dataOperId;
    /**
     * 设计的界面元素的顶部位置
     */
    private Integer posTop;
    /**
     * 设计的界面元素的左边位置
     */
    private Integer posLeft;
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;

    private List<ColumnDto> lstColumn;
    private List<ConstraintDto> lstConstraint;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Long schemaId) {
        this.schemaId = schemaId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Integer getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Integer readOnly) {
        this.readOnly = readOnly;
    }

    public Long getDataOperId() {
        return dataOperId;
    }

    public void setDataOperId(Long dataOperId) {
        this.dataOperId = dataOperId;
    }

    public Integer getPosTop() {
        return posTop;
    }

    public void setPosTop(Integer posTop) {
        this.posTop = posTop;
    }

    public Integer getPosLeft() {
        return posLeft;
    }

    public void setPosLeft(Integer posLeft) {
        this.posLeft = posLeft;
    }
}


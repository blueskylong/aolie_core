package com.ranranx.aolie.core.datameta.dto;

/**
 * @author xxl
 *  二张表的外键关系
 * @date 2020/8/8 16:29
 * @version V0.0.1
 **/

import javax.persistence.Table;

@Table(name = "aolie_dm_column_relation")
public class TableColumnRelationDto extends SchemaBaseDto {
    /**
     * 关系类型,值 参考Constants.TableRelationType.
     */
    private Integer relationType;
    private Long fieldFrom;
    private Long fieldTo;
    private Long id;

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFieldFrom() {
        return fieldFrom;
    }

    public void setFieldFrom(Long fieldFrom) {
        this.fieldFrom = fieldFrom;
    }

    public Long getFieldTo() {
        return fieldTo;
    }

    public void setFieldTo(Long fieldTo) {
        this.fieldTo = fieldTo;
    }
}

package com.ranranx.aolie.datameta.dto;

/**
 * @Author xxl
 * @Description 二张表的外键关系
 * @Date 2020/8/8 16:29
 * @Version V0.0.1
 **/
public class TableColumnRelationDto {
    /**
     * 关系类型,值 参考Constants.TableRelationType.
     */
    private Integer relationType;
    private long fieldFrom;
    private long fieldTo;

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public long getFieldFrom() {
        return fieldFrom;
    }

    public void setFieldFrom(long fieldFrom) {
        this.fieldFrom = fieldFrom;
    }

    public long getFieldTo() {
        return fieldTo;
    }

    public void setFieldTo(long fieldTo) {
        this.fieldTo = fieldTo;
    }
}

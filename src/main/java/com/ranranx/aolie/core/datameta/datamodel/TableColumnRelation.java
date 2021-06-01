package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.dto.TableColumnRelationDto;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 表关系模型数据
 * @version V0.0.1
 * @date 2020/8/8 17:00
 **/
public class TableColumnRelation {
    /**
     * 表关系原始数据
     */
    private TableColumnRelationDto dto;
    /**
     * 起始表信息
     */
    private TableInfo tableFrom;
    /**
     * 终止表信息
     */
    private TableInfo tableTo;
    /**
     * 连接额外条件,放到on 后面
     */
    private List<Criteria> lstCriteria;

    /**
     * 这里主要是为了更新公式
     *
     * @param columnIds
     */
    public void columnIdChanged(Map<Long, Long> columnIds) {
        if (columnIds.containsKey(dto.getFieldFrom())) {
            dto.setFieldFrom(columnIds.get(dto.getFieldFrom()));
        }
        if (columnIds.containsKey(dto.getFieldTo())) {
            dto.setFieldTo(columnIds.get(dto.getFieldTo()));
        }
    }

    public TableColumnRelationDto getDto() {
        return dto;
    }

    public void setDto(TableColumnRelationDto dto) {
        this.dto = dto;
    }

    @Transient
    public TableInfo getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(TableInfo tableFrom) {
        this.tableFrom = tableFrom;
    }

    @Transient
    public TableInfo getTableTo() {
        return tableTo;
    }

    public void setTableTo(TableInfo tableTo) {
        this.tableTo = tableTo;
    }

    @Transient
    public List<Criteria> getLstCriteria() {
        return lstCriteria;
    }

    public void setLstCriteria(List<Criteria> lstCriteria) {
        this.lstCriteria = lstCriteria;
    }

    @Transient
    public Criteria appendCriteria() {
        if (this.lstCriteria == null) {
            this.lstCriteria = new ArrayList<>();
        }
        Criteria criteria = new Criteria();
        this.lstCriteria.add(criteria);
        return criteria;
    }
}

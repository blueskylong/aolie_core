package com.ranranx.aolie.core.fixrow.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-08-10 17:52:37
 */
@Table(name = "aolie_s_fix_relation")
public class FixRelation extends BaseDto implements java.io.Serializable {


    private static final long serialVersionUID = 1L;
    private Long detailId;
    private Long fixId;
    private Long destColumnId;
    private Long sourceColumnId;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getFixId() {
        return fixId;
    }

    public void setFixId(Long fixId) {
        this.fixId = fixId;
    }

    public Long getDestColumnId() {
        return destColumnId;
    }

    public void setDestColumnId(Long destColumnId) {
        this.destColumnId = destColumnId;
    }

    public Long getSourceColumnId() {
        return sourceColumnId;
    }

    public void setSourceColumnId(Long sourceColumnId) {
        this.sourceColumnId = sourceColumnId;
    }
}

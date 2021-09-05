package com.ranranx.aolie.core.fixrow.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-08-10 17:52:37
 */
@Table(name = "aolie_s_fix")
public class FixMain extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long fixId;
    private String title;
    private String memo;
    private Long tableId;

    public void setFixId(Long fixId) {
        this.fixId = fixId;
    }

    public Long getFixId() {
        return this.fixId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return this.memo;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
}
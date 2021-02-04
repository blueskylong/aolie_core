package com.ranranx.aolie.core.datameta.dto;

import javax.persistence.Table;

/**
 * @author xxl
 *
 * @date 2020/8/19 17:33
 * @version V0.0.1
 **/
@Table(name = "aolie_dm_layout")
public class LayoutDto extends SchemaBaseDto {

    /**
     * 布局ID
     */
    private Long layoutId;
    /**
     * 布局名
     */
    private String layoutName;
    /**
     * 级次编码
     */
    private String lvlCode;


    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getLvlCode() {
        return lvlCode;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
    }
}

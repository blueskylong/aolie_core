package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/19 17:33
 * @Version V0.0.1
 **/
public class LayoutDto extends BaseDto {

    /**
     * 方案ID
     */
    private Long schemaId;
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

    public Long getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Long schemaId) {
        this.schemaId = schemaId;
    }

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

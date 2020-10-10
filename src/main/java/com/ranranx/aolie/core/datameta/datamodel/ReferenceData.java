package com.ranranx.aolie.core.datameta.datamodel;

/**
 * @Author xxl
 * @Description
 * @Date 2020/9/4 15:21
 * @Version V0.0.1
 **/
public class ReferenceData {
    private String id;
    private String name;
    private String parentId;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

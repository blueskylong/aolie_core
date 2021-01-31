package com.ranranx.aolie.core.common;

/**
 * @Author xxl
 * @Description
 * @Date 2021/1/28 0028 14:36
 * @Version V0.0.1
 **/
public class SystemParam {
    private String name;
    private String dataType;
    private Object value;
    private long id;

    public SystemParam(String name, String dataType, Object value, long id) {
        this.name = name;
        this.dataType = dataType;
        this.value = value;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

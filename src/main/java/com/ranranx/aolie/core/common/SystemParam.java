package com.ranranx.aolie.core.common;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/1/28 0028 14:36
 **/
public class SystemParam {
    private String name;
    private String dataType;
    private Object value;
    private long id;
    private String versionCode;


    public SystemParam() {

    }

    public SystemParam(String name, String dataType, Object value, long id, String versionCode) {
        this.name = name;
        this.dataType = dataType;
        this.value = value;
        this.id = id;
        this.versionCode = versionCode;
    }

    /**
     * 缓存的主键
     *
     * @return
     */
    public String getKey() {
        return CommonUtils.makeKey(String.valueOf(id), versionCode);
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

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}

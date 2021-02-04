package com.ranranx.aolie.core.runtime;

/**
 * @author xxl
 *  全局参数,
 * @date 2020/12/28 0028 14:17
 * @version V0.0.1
 **/
public class GlobalParam {
    /**
     * 唯一ID
     */
    private Long paramId;
    /**
     * 参数名
     */
    private String paramName;
    /**
     * 参数值
     */
    private Object value;
    /**
     * 是不是字符串类型,这在替换表达式时,确定需不需要添加引号
     */
    private boolean isString;

    public GlobalParam() {
    }

    public GlobalParam(Long paramId, String paramName, Object value, boolean isString) {
        this.paramId = paramId;
        this.paramName = paramName;
        this.value = value;
        this.isString = isString;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean string) {
        isString = string;
    }
}

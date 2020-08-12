package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

/**
 * @Author xxl
 * @Description 类似于数据源的配置, TODO 这里可以扩展成分布式,读写分离等
 * @Date 2020/8/10 18:49
 * @Version V0.0.1
 **/
public class DataOperatorDto extends BaseDto {

    /**
     * 数据源名
     */
    private String name;
    /**
     * ID
     */
    private Long id;
    /**
     * 是不是默认
     */
    private Short isDefault;
    /**
     * 连接串
     */
    private String url;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 说明
     */
    private String memo;
    /**
     * 是否只读
     */
    private Short isReadOnly;
    /**
     * 由Spring 管理的数据源名称
     */
    private String dsName;
    /**
     * 驱动类名
     */
    private String driverClassName;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Short isDefault) {
        this.isDefault = isDefault;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Short getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(Short isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }
}

package com.ranranx.aolie.common;

import java.util.Date;

/**
 * @Author xxl
 * @Description 基础数据库表对象
 * @Date 2020/8/5 9:03
 * @Version V0.0.1
 **/
public class BaseDto {
    protected Date createDate;
    protected Date lastUpdateDate;
    protected Double createUser;
    protected Double lastUpdateUser;
    /**
     * 这里的version是为了做一个区分用的,可能是年度,可能是区划,也可以是租户
     */
    protected String versionCode;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Double getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Double createUser) {
        this.createUser = createUser;
    }

    public Double getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Double lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}

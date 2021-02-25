package com.ranranx.aolie.application.user.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-02-10 13:10:17
 */
@Table(name = "aolie_s_user")
public class UserDto extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long userId;
    private String userName;
    private String phone;
    private String addr;
    private String eMail;
    private String password;
    private String avatar;
    private String accountCode;
    private java.util.Date lockoutTime;
    private Integer state;
    private Long belongOrg;

    private Integer userType;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getEMail() {
        return this.eMail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountCode() {
        return this.accountCode;
    }

    public void setLockoutTime(java.util.Date lockoutTime) {
        this.lockoutTime = lockoutTime;
    }

    public java.util.Date getLockoutTime() {
        return this.lockoutTime;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
    }

    public void setBelongOrg(Long belongOrg) {
        this.belongOrg = belongOrg;
    }

    public Long getBelongOrg() {
        return this.belongOrg;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
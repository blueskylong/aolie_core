package com.ranranx.aolie.core.runtime;

/**
 * @Author xxl
 * @Description
 * @Date 2020/12/28 0028 14:14
 * @Version V0.0.1
 **/
public class LoginUser {
    private Long userId;
    private String accountCode;
    private String userName;
    private String versionCode;
    private Long roleId;
    private String loginTime;
    private Integer userType;
    private Long belongOrg;
    private String belongOrgCode;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public Long getBelongOrg() {
        return belongOrg;
    }

    public void setBelongOrg(Long belongOrg) {
        this.belongOrg = belongOrg;
    }

    public String getBelongOrgCode() {
        return belongOrgCode;
    }

    public void setBelongOrgCode(String belongOrgCode) {
        this.belongOrgCode = belongOrgCode;
    }
}

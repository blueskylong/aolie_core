package com.ranranx.aolie.core.runtime;

import com.ranranx.aolie.core.common.SystemParam;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.beans.Transient;
import java.util.Collection;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/12/28 0028 14:14
 **/
public class LoginUser extends User {
    private Long userId;
    private String accountCode;
    private String userName;
    private String versionCode;
    private Long roleId;
    private String loginTime;
    private Integer userType;
    private Long belongOrg;
    private String belongOrgCode;

    private Map<String, SystemParam> params;

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public LoginUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

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

    @Transient
    public Map<String, SystemParam> getParams() {
        return params;
    }

    public void setParams(Map<String, SystemParam> params) {
        this.params = params;
    }
}

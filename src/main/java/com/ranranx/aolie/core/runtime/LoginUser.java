package com.ranranx.aolie.core.runtime;

import com.ranranx.aolie.application.user.dto.UserDto;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SystemParam;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.beans.Transient;
import java.util.*;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/12/28 0028 14:14
 **/
public class LoginUser extends UserDto implements UserDetails, CredentialsContainer {
    private Map<Long, SystemParam> params;
    /**
     * 当前登录的角色
     */
    private Long roleId = 1L;

    /**
     * 所有权限信息 key 权限资源ID 参考Constants.DefaultRsIds
     */
    public Map<Long, Set<Long>> mapRights;

    @Transient
    public Map<Long, SystemParam> getParams() {
        return params;
    }


    /**
     * 取得所有参数值
     *
     * @return
     */
    @Transient
    public Map<Long, Object> getParamValues() {
        Map<Long, Object> values = new HashMap<>();
        Iterator<SystemParam> iterator = params.values().iterator();
        while (iterator.hasNext()) {
            SystemParam param = iterator.next();
            values.put(param.getId(), param.getValue());
        }
        return values;
    }

    public void setParams(Map<Long, SystemParam> params) {
        this.params = params;
    }

    @Override
    public void eraseCredentials() {
        this.setPassword(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return getAccountCode();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getState() != null && this.getState().equals(Constants.UserState.expired);
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getState() != null && this.getState().equals(Constants.UserState.locked);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.getState() != null && this.getState().equals(Constants.UserState.credentialsExpired);
    }

    @Override
    public boolean isEnabled() {
        return this.getState() == null || this.getState().equals(Constants.UserState.normal);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof LoginUser)) {
            return false;
        }
        return this.getUserId().equals(((LoginUser) o).getUserId());
    }


    public Map<Long, Set<Long>> getMapRights() {
        return mapRights;
    }

    public void setMapRights(Map<Long, Set<Long>> mapRights) {
        if (mapRights == null) {
            mapRights = new HashMap<>();
        }
        this.mapRights = mapRights;

    }
}

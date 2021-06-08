package com.ranranx.aolie.application.user.service;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/6 0006 22:08
 **/

import com.ranranx.aolie.application.right.dto.Role;
import com.ranranx.aolie.application.right.service.RightService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.config.authentication.NamePassVersionScodeAuthenticationToken;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.runtime.GlobalParameterService;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component

public class LoginValidateAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ILoginService loginService;

    //解密用的
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GlobalParameterService parameterService;

    @Autowired
    private RightService rightService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        NamePassVersionScodeAuthenticationToken token = (NamePassVersionScodeAuthenticationToken) authentication;
        //获取输入的用户名
        String username = token.getName();
        //获取输入的明文
        String rawPassword = (String) token.getCredentials();
        String sCode = token.getsCode();
        String version = token.getVersion();

        //查询用户是否存在
        LoginUser user = (LoginUser) loginService.loadUserByUserNameAndVersion(username, version);

        if (!user.isEnabled()) {
            throw new DisabledException("该账户已被禁用，请联系管理员");

        } else if (user.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定");

        } else if (user.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期，请联系管理员");

        } else if (user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("该账户的登录凭证已过期，请重新登录");
        }
        // 验证 验证码 TODO ,如果需要 这里增加验证
        if (CommonUtils.isEmpty(sCode)) {

        }

        //验证密码
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("输入密码错误!");
        }
        initUserParams(user);
        //这里将角色信息当作授权信息,供前端做是否选择角色的判断
        List<Role> userRoles = rightService.findUserRoles(user.getUserId(), user.getVersionCode());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (userRoles != null && !userRoles.isEmpty()) {
            userRoles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRoleId().toString()));
            });
        }
//        if (userRoles != null && userRoles.size() == 1) {
//            initSingleRoleRight(authorities, userRoles.get(0).getRoleId(), user);
//        } else {
        //更新登录的权限
        loginService.initUserRight(user, null);
//        }

        return new NamePassVersionScodeAuthenticationToken(user, rawPassword, authorities);
    }


    private Collection<? extends GrantedAuthority> initSingleRoleRight(Collection<? extends GrantedAuthority> authorities, Long roleId
            , LoginUser user) {
        if (authorities == null) {

            authorities = new ArrayList<>();
        }
        String sRoleId = roleId.toString();
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            if (sRoleId.equals(authority.getAuthority())) {
                hasRole = true;
            }
        }
        if (!hasRole) {
            throw new NotExistException("用户没有指定的角色权限");
        }
        //更新登录的权限
        loginService.initUserRight(user, roleId);
        return authorities;
    }


    private void initUserParams(LoginUser user) {
        user.setParams(parameterService.getUserParam(user));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //确保authentication能转成该类
        return authentication.equals(NamePassVersionScodeAuthenticationToken.class);
    }

}

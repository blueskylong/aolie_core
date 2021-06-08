package com.ranranx.aolie.application.user.service;

import com.ranranx.aolie.application.right.dto.Role;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/16 0016 18:46
 **/
public interface ILoginService {
    /**
     * 查询用户
     *
     * @param username
     * @param version
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByUserNameAndVersion(String username, String version) throws UsernameNotFoundException;

    /**
     * 初始化用户的权限信息,如果此用户只有一个角色,可以直接查询,但如果是多个角色,则需要选择角色后查询
     *
     * @param user
     */
    Role initUserRight(LoginUser user, Long roleId);
}

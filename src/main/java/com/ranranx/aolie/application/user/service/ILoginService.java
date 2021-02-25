package com.ranranx.aolie.application.user.service;

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
    public UserDetails loadUserByUserNameAndVersion(String username, String version) throws UsernameNotFoundException;
}

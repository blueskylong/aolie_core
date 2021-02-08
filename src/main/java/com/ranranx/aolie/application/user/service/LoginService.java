package com.ranranx.aolie.application.user.service;

import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/6 0006 21:16
 **/
@Service
public class LoginService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new LoginUser(username, "xxl", new ArrayList<>(), 1L);
    }
}

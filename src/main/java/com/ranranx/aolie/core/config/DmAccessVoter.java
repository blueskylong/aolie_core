package com.ranranx.aolie.core.config;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/9 0009 19:45
 **/
public class DmAccessVoter implements AccessDecisionVoter {
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {

        if (object instanceof FilterInvocation) {
            FilterInvocation invocation = (FilterInvocation) object;
            return ACCESS_GRANTED;
        }
        return ACCESS_ABSTAIN;
    }
}

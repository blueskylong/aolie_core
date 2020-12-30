package com.ranranx.aolie.core.common;

import com.ranranx.aolie.core.runtime.LoginUser;

/**
 * @Author xxl
 * @Description
 * @Date 2020/9/11 10:34
 * @Version V0.0.1
 **/
public class SessionUtils {
    private static LoginUser user = new LoginUser();

    public static String getLoginVersion() {
        return "000000";
    }

    public static LoginUser getLoginUser() {
        return user;
    }

    static {
        user.setAccountCode("999");
        user.setUserId(1L);
        user.setBelongOrg(1L);
        user.setBelongOrgCode("001001");
        user.setRoleId(1L);
        user.setUserName("admin");
        user.setVersionCode("000000");
        user.setUserType(1);
    }
}

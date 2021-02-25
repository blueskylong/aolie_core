package com.ranranx.aolie.core.interfaces;

import com.ranranx.aolie.core.common.SystemParam;
import com.ranranx.aolie.core.runtime.LoginUser;

import java.util.List;

/**
 * @author xxl
 * 生成全局参数值
 * @version V0.0.1
 * @date 2020/12/28 0028 13:35
 **/
public interface ISystemParamGenerator {
    /**
     * 生成与当前登录用户相关的参数值
     *
     * @param user
     * @return
     */
    List<SystemParam> getUserParams(LoginUser user);

    /**
     * 生成不与用户相关的参数值
     *
     * @return
     */
    List<SystemParam> getConstParams();
}

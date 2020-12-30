package com.ranranx.aolie.core.interfaces;

import com.ranranx.aolie.core.runtime.GlobalParam;
import com.ranranx.aolie.core.runtime.LoginUser;

import java.util.List;

/**
 * @Author xxl
 * @Description 生成全局参数值
 * @Date 2020/12/28 0028 13:35
 * @Version V0.0.1
 **/
public interface IGlobalParamGenerator {
    /**
     * 生成与用户相关的参数值
     *
     * @param user
     * @return
     */
    List<GlobalParam> getParams(LoginUser user);

    /**
     * 生成不与用户相关的参数值
     *
     * @return
     */
    List<GlobalParam> getConstParams();
}

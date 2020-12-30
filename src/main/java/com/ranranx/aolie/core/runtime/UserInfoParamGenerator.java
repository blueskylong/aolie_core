package com.ranranx.aolie.core.runtime;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.interfaces.IGlobalParamGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/12/28 0028 15:28
 * @Version V0.0.1
 **/
@Component
public class UserInfoParamGenerator implements IGlobalParamGenerator {
    @Override
    public List<GlobalParam> getParams(LoginUser user) {
        List<GlobalParam> lstResult = new ArrayList<>();
        lstResult.add(new GlobalParam(Constants.SystemParamNames.userId,
                "用户ID", user.getUserId(), false));
        lstResult.add(new GlobalParam(Constants.SystemParamNames.userType,
                "用户类型", user.getUserType(), false));
        lstResult.add(new GlobalParam(Constants.SystemParamNames.belongOrgId,
                "用户所属机构ID", user.getBelongOrg(), false));
        lstResult.add(new GlobalParam(Constants.SystemParamNames.belongOrgCode,
                "用户所属机构编码", user.getBelongOrgCode(), true));
        lstResult.add(new GlobalParam(Constants.SystemParamNames.versionCode,
                "版本号", user.getVersionCode(), true));
        lstResult.add(new GlobalParam(Constants.SystemParamNames.role,
                "登录角色", user.getRoleId(), false));
        return lstResult;
    }

    @Override
    public List<GlobalParam> getConstParams() {
        return null;
    }
}

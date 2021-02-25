package com.ranranx.aolie.core.runtime;

import com.ranranx.aolie.core.common.SystemParam;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.interfaces.ISystemParamGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/12/28 0028 15:28
 **/
@Component
public class UserInfoParamGenerator implements ISystemParamGenerator {
    @Override
    public List<SystemParam> getUserParams(LoginUser user) {
        List<SystemParam> lstResult = new ArrayList<>();
        lstResult.add(
                new SystemParam("登录用户ID", DmConstants.FieldType.INT, user.getUserId(),
                        DmConstants.GlobalParamsIds.userId, user.getVersionCode()));
        lstResult.add(
                new SystemParam("登录角色ID", DmConstants.FieldType.INT, user.getRoleId(),
                        DmConstants.GlobalParamsIds.roleId, user.getVersionCode()));
        lstResult.add(
                new SystemParam("版本号", DmConstants.FieldType.VARCHAR, user.getVersionCode(),
                        DmConstants.GlobalParamsIds.version, user.getVersionCode()));
        lstResult.add(
                new SystemParam("登录用户名", DmConstants.FieldType.VARCHAR, user.getUserName(),
                        DmConstants.GlobalParamsIds.userName, user.getVersionCode()));
        lstResult.add(
                new SystemParam("登录用户所属机构", DmConstants.FieldType.INT, user.getUserId(),
                        DmConstants.GlobalParamsIds.userBelong, user.getVersionCode()));
        lstResult.add(
                new SystemParam("登录帐号", DmConstants.FieldType.VARCHAR, user.getAccountCode(),
                        DmConstants.GlobalParamsIds.userAccount, user.getVersionCode()));

        return lstResult;
    }

    @Override
    public List<SystemParam> getConstParams() {
        return null;
    }
}

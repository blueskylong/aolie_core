package com.ranranx.aolie.application.user.service;

import com.ranranx.aolie.application.user.dto.UserDto;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/6 0006 21:16
 **/
//
@Service
//@ConditionalOnMissingBean(LoginService.class)
public class LoginService implements ILoginService {
    @Autowired
    private HandlerFactory factory;

    @Override
    public UserDetails loadUserByUserNameAndVersion(String username, String version) throws UsernameNotFoundException {
        if (CommonUtils.isEmpty(version) || CommonUtils.isEmpty(username)) {
            return null;
        }
        QueryParam param = new QueryParam();
        param.setTableDtos(Constants.DEFAULT_SYS_SCHEMA, version, UserDto.class);
        param.appendCriteria().andEqualTo(null,
                Constants.FixColumnName.ACCOUNT_CODE, username);
        param.setResultClass(LoginUser.class);

        HandleResult result = factory.handleQuery(param);
        if (!result.isSuccess()) {
            throw new UsernameNotFoundException("查询出错");
        }
        List<LoginUser> data = (List<LoginUser>) result.getData();
        if (data == null || data.isEmpty()) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        if (data.size() > 1) {
            throw new UsernameNotFoundException("用户账号重复");
        }
        return data.get(0);
    }


}

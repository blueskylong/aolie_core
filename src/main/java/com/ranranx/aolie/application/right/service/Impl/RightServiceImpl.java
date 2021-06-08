package com.ranranx.aolie.application.right.service.Impl;

import com.ranranx.aolie.application.right.dto.Role;
import com.ranranx.aolie.application.right.service.RightService;
import com.ranranx.aolie.application.user.service.UserService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/6/7 0007 9:27
 **/
@Service
@Transactional(readOnly = false)
public class RightServiceImpl implements RightService {
    @Autowired
    private HandlerFactory factory;

    @Autowired
    private UserService userService;

    /**
     * 根据角色名查询角色
     *
     * @param roleName
     * @return
     */
    @Override
    public List<Role> findRoleByName(String roleName) {
        QueryParam param = new QueryParam();
        param.setTableDtos(Constants.DEFAULT_SYS_SCHEMA, SessionUtils.getLoginVersion(), Role.class);
        if (!CommonUtils.isEmpty(roleName)) {
            param.appendCriteria().andInclude(param.getTable()[0].getTableDto().getTableName(), "role_name", roleName);
            param.addOrder(new FieldOrder(Role.class, "role_name", false, 1));
        }
        param.setResultClass(Role.class);
        return (List<Role>) factory.handleQuery(param).getData();

    }

    /**
     * 查询用户的角色
     *
     * @return
     */
    @Override
    public List<Role> findUserRoles(Long userId, String version) {

        Set<Long> lstRoles = userService.findUserDirectRights(userId, Constants.DefaultRsIds.role
                , version);
        List<Long> lstRole = new ArrayList<>();

        if (lstRoles == null || lstRoles.isEmpty()) {
            return null;
        }
        lstRoles.forEach(el -> lstRole.add(el));
        return findRoleByIds(lstRole, version);
    }

    /**
     * 根据角色ID取得角色全信息
     *
     * @param lstRoles
     * @return
     */
    private List<Role> findRoleByIds(List<Long> lstRoles, String version) {
        QueryParam param = new QueryParam();
        Role role = new Role();
        String tableName = CommonUtils.getTableName(Role.class);
        param.setFilterObjectAndTableAndResultType(Constants.DEFAULT_SYS_SCHEMA, version, role);
        param.appendCriteria().andIn(tableName, "role_id", lstRoles);
        param.addOrder(new FieldOrder(tableName, "role_name", false, 1));
        param.setMaskDataRight(true);
        HandleResult result = factory.handleQuery(param);
        return (List<Role>) result.getData();
    }

    /**
     * 根据角色名查询角色
     *
     * @param roleId
     * @return
     */
    @Override
    public Role findRoleById(Long roleId, String version) {
        QueryParam param = new QueryParam();
        Role role = new Role();
        role.setRoleId(roleId);
        param.setFilterObjectAndTableAndResultType(Constants.DEFAULT_SYS_SCHEMA, SessionUtils.getLoginVersion(), role);

        return (Role) factory.handleQuery(param).singleValue();
    }
}

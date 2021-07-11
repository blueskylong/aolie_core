package com.ranranx.aolie.application.user.service.impl;

import com.ranranx.aolie.application.menu.dto.MenuButtonDto;
import com.ranranx.aolie.application.menu.service.MenuService;
import com.ranranx.aolie.application.right.RightNode;
import com.ranranx.aolie.application.right.dto.Role;
import com.ranranx.aolie.application.right.service.RightService;
import com.ranranx.aolie.application.user.dto.UserDto;
import com.ranranx.aolie.application.user.service.ILoginService;
import com.ranranx.aolie.application.user.service.UserService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.interfaces.ICacheRefTableChanged;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/6 0006 21:16
 **/
//
@Service
//@ConditionalOnMissingBean(LoginService.class)
public class LoginServiceImpl implements ILoginService, ICacheRefTableChanged, CommandLineRunner {
    @Autowired
    private HandlerFactory factory;

    @Autowired
    private UserService userService;
    @Autowired
    private RightService rightService;

    @Autowired
    private MenuService menuService;

    private Map<String, String> mapBtnRights;

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

    /**
     * 设置选择的角色
     *
     * @param roleId
     */
    @Override
    public void setSelectRole(Long roleId) {
        //检查当前用户是不是存在这个角色
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return;
        }
        SessionUtils.getLoginUser().setRoleId(roleId);

    }

    /**
     * 初始化用户的权限信息,如果此用户只有一个角色,可以直接查询,但如果是多个角色,则需要选择角色后查询
     *
     * @param user
     */
    @Override
    public Role initUserRight(LoginUser user, Long roleId) {
        try {
            //取得权限结构
            RightNode rightNodeRoot = SessionUtils.getMapRightStruct().get(user.getVersionCode());
            //查询所有权限相关信息,生成Map
            Map<Long, Set<Long>> mapRights = populateNodeStruct(rightNodeRoot, user, roleId);
            //收集自定义权限
            user.setCustomRights(findUserCustomRight(mapBtnRights,
                    mapRights.get(Constants.DefaultRsIds.menuButton), user.getVersionCode()));

            user.setMapRights(mapRights);
            setSelectRole(roleId);
            if (roleId == null) {
                return null;
            }
            return rightService.findRoleById(roleId, user.getVersionCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 取得用户自定义权限
     */

    private Set<String> findUserCustomRight(Map<String, String> mapBtnRights, Set<Long> lstBtns, String version) {
        Set<String> result = new HashSet<>();
        if (lstBtns == null || lstBtns.isEmpty() || mapBtnRights == null || mapBtnRights.isEmpty()) {
            return result;
        }
        lstBtns.forEach(btnId -> {
            result.add(mapBtnRights.get(version + "_" + btnId));
        });
        return result;
    }

    /**
     * 查询具体的权限ID,这里需要思考一个平衡问题,如果权限数据过多,是不是可以采用临时查询的方式处理,否则内存占用会较多
     *
     * @param root
     * @return key: rsId,value:array of ids
     */
    private Map<Long, Set<Long>> populateNodeStruct(RightNode root, LoginUser user, Long roleId) {
        Map<Long, Set<Long>> mapResult = userService.findUserDirectAllRights(user.getUserId(),
                user.getVersionCode(), roleId);
        List<RightNode> lstSub = root.getLstSub();
        if (lstSub == null || lstSub.isEmpty()) {
            return mapResult;
        }
        if (mapResult == null || mapResult.isEmpty()) {
            return mapResult;
        }

        lstSub.forEach(el -> {
            //如果有下级,则处理下级
            //这里使用了深度优先的方式,如果采用广度优先的方式,一定情况下会理有效率
            List<RightNode> subNodes = el.getLstSub();
            if (subNodes != null && !subNodes.isEmpty()) {
                subNodes.forEach(node -> {
                    findSubRights(el, node, mapResult, user);
                });
            }
        });
        return mapResult;
    }


    /**
     * 处理下级的权限传递;
     * 此函数顺便查询出按钮自定义权限信息
     *
     * @param fromNode
     * @param toNode
     * @param mapRights
     */
    private void findSubRights(RightNode fromNode, RightNode toNode, Map<Long, Set<Long>> mapRights, LoginUser user) {
        Set<Long> rights = findRight(fromNode, toNode, mapRights.get(fromNode.getRightId()), user);
        Set<Long> lstExists = mapRights.computeIfAbsent(toNode.getRightId(), key -> {
            return new HashSet<Long>();
        });
        //添加到结果集中
        if (rights != null) {
            lstExists.addAll(rights);
        }

        //检查下级
        List<RightNode> lstSub = toNode.getLstSub();
        if (lstSub != null && !lstSub.isEmpty()) {
            //执行递归
            lstSub.forEach(el -> {
                findSubRights(toNode, el, mapRights, user);
            });
        }
    }


    /**
     * 查询TO节点的权限数据ID
     *
     * @param fromNode
     * @param toNode
     * @param lstFormIds
     * @param user
     * @return
     */
    private Set<Long> findRight(RightNode fromNode, RightNode toNode, Set<Long> lstFormIds, LoginUser user) {
        //如果是根节点,则不再查询,因前面已统一查询用户的直接权限
        if (fromNode.getLstParent().isEmpty()) {
            return null;
        } else {
            //否则查询资源关联表
            return userService.findNextRights(fromNode.getRightId(),
                    toNode.getRightId(), lstFormIds, user.getVersionCode());
        }
    }

    @Override
    public List<String> getCareTables() {
        return Arrays.asList(CommonUtils.getTableName(MenuButtonDto.class));
    }

    @Override
    public void refresh(String tableName) {
        this.mapBtnRights = menuService.findCustomPermission();
    }

    @Override
    public void run(String... args) throws Exception {
        this.refresh(null);
    }
}

package com.ranranx.aolie.application.user.service;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/6 0006 22:08
 **/

import com.ranranx.aolie.application.right.RightNode;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.config.authentication.NamePassVersionScodeAuthenticationToken;
import com.ranranx.aolie.core.runtime.GlobalParameterService;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginValidateAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ILoginService loginService;

    //解密用的
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GlobalParameterService parameterService;

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        NamePassVersionScodeAuthenticationToken token = (NamePassVersionScodeAuthenticationToken) authentication;
        //获取输入的用户名
        String username = token.getName();
        //获取输入的明文
        String rawPassword = (String) token.getCredentials();
        String sCode = token.getsCode();
        String version = token.getVersion();

        //查询用户是否存在
        LoginUser user = (LoginUser) loginService.loadUserByUserNameAndVersion(username, version);

        if (!user.isEnabled()) {
            throw new DisabledException("该账户已被禁用，请联系管理员");

        } else if (user.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定");

        } else if (user.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期，请联系管理员");

        } else if (user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("该账户的登录凭证已过期，请重新登录");
        }
        // 验证 验证码 TODO ,如果需要 这里增加验证
        if (CommonUtils.isEmpty(sCode)) {

        }

        //验证密码
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("输入密码错误!");
        }

        initUserRight(user);
        initUserParams(user);
        return new NamePassVersionScodeAuthenticationToken(user, rawPassword, user.getAuthorities());
    }

    /**
     * 初始化用户的权限信息,如果此用户只有一个角色,可以直接查询,但如果是多个角色,则需要选择角色后查询
     *
     * @param user
     */
    private void initUserRight(LoginUser user) {
        try {
            //取得权限结构
            RightNode rightNodeRoot = SessionUtils.getMapRightStruct().get(user.getVersionCode());
            //查询所有权限相关信息,生成Map
            Map<Long, Set<Long>> mapRights = populateNodeStruct(rightNodeRoot, user);
            user.setMapRights(mapRights);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询具体的权限ID,这里需要思考一个平衡问题,如果权限数据过多,是不是可以采用临时查询的方式处理,否则内存占用会较多
     *
     * @param root
     * @return key: rsId,value:array of ids
     */
    private Map<Long, Set<Long>> populateNodeStruct(RightNode root, LoginUser user) {
        Map<Long, Set<Long>> mapResult = new HashMap<>();
        List<RightNode> lstSub = root.getLstSub();
        if (lstSub == null || lstSub.isEmpty()) {
            return mapResult;
        }

        lstSub.forEach(el -> {
            //第一层子节点,是没有对应关系传递的
            Set<Long> rights = findRight(root, el, null, user);
            //如果此节点用户没有被授权,则下级关系不再处理
            if (rights == null || rights.isEmpty()) {
                return;
            }
            mapResult.put(el.getRightId(), rights);
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
     * 处理下级的权限传递
     *
     * @param fromNode
     * @param toNode
     * @param mapRights
     */
    private void findSubRights(RightNode fromNode, RightNode toNode, Map<Long, Set<Long>> mapRights, LoginUser user) {
        Set<Long> rights = findRight(fromNode, toNode, mapRights.get(fromNode.getRightId()), user);
        if (rights == null || rights.isEmpty()) {
            return;
        }
        Set<Long> lstExists = mapRights.computeIfAbsent(toNode.getRightId(), key -> {
            return new HashSet<Long>();
        });
        //添加到结果集中
        lstExists.addAll(rights);
        //检查下级
        List<RightNode> lstSub = toNode.getLstSub();
        if (lstSub != null && !lstSub.isEmpty()) {
            //执行递归
            lstSub.forEach(el -> {
                findSubRights(toNode, el, mapRights, user);
            });
        }
    }

    private Set<Long> findRight(RightNode fromNode, RightNode toNode, Set<Long> lstFormIds, LoginUser user) {
        //如果是根节点,则查询用户对资源表
        if (fromNode.getLstParent().isEmpty()) {
            try {
                return userService.findUserDirectRights(user.getUserId(),
                        toNode.getRightId(), user.getVersionCode());
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } else {
            //否则查询资源关联表
            return userService.findNextRights(fromNode.getRightId(),
                    toNode.getRightId(), lstFormIds, user.getVersionCode());
        }
    }


    private void initUserParams(LoginUser user) {
        user.setParams(parameterService.getUserParam(user));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //确保authentication能转成该类
        return authentication.equals(NamePassVersionScodeAuthenticationToken.class);
    }

}

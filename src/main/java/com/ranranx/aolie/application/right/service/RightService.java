package com.ranranx.aolie.application.right.service;

import com.ranranx.aolie.application.right.dto.Role;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/6/7 0007 9:27
 **/
public interface RightService {
    /**
     * 根据角色名查询角色
     *
     * @param roleName
     * @return
     */
    List<Role> findRoleByName(String roleName);
    /**
     * 根据角色名查询角色
     *
     * @param roleId
     * @return
     */
    Role findRoleById(Long roleId,String version);

    /**
     * 查询用户的角色
     *
     * @return
     */
    List<Role> findUserRoles(Long userId, String version);
}

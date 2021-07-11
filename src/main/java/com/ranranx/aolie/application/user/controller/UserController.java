package com.ranranx.aolie.application.user.controller;

import com.ranranx.aolie.application.right.dto.Role;
import com.ranranx.aolie.application.right.service.RightService;
import com.ranranx.aolie.application.user.dto.RightRelationDto;
import com.ranranx.aolie.application.user.dto.RightResourceDto;
import com.ranranx.aolie.application.user.service.ILoginService;
import com.ranranx.aolie.application.user.service.UserService;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/1/6 0006 15:29
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private RightService rightService;

    @Autowired
    private ILoginService loginService;

    /**
     * 查询用户的所有数据权限,
     *
     * @param userId
     * @return 数据块形式为:   rs_id:List<UserToResource>
     */
    @RequestMapping("/getUserRights/{userId}")
    public HandleResult getUserRights(@PathVariable Long userId) {
        return service.getUserRights(userId, SessionUtils.getLoginVersion());
    }

    /**
     * 保存用户权限
     *
     * @param userId
     * @param mapNewUserRight
     * @return
     */
    @PostMapping("/saveUserRight/{userId}")
    public HandleResult saveUserRight(@PathVariable long userId, @RequestBody Map<Long, List<Long>> mapNewUserRight) {
        try {
            return service.saveUserRight(userId, SessionUtils.getLoginVersion(), mapNewUserRight);
        } catch (Exception e) {
            return HandleResult.failure(e.getMessage());
        }

    }

    /**
     * 查询菜单和按钮,组成一个树
     *
     * @return
     */
    @GetMapping("/findMenuAndButton")
    public HandleResult findMenuAndButton() {
        return service.findMenuAndButton();
    }

    /**
     * 查询一权限关系数据
     *
     * @param rrId     权限关系ID
     * @param sourceId 主权限的ID
     * @return
     */
    @GetMapping("/findRightRelationDetail/{rrId}/{sourceId}")
    public HandleResult findRightRelationDetail(@PathVariable long rrId, @PathVariable long sourceId) {
        return service.findRightRelationDetail(rrId, sourceId, SessionUtils.getLoginVersion());
    }

    /**
     * 查询一权限关系数据
     *
     * @param sourceRsId 源权限ID
     * @param destRsId   目标权限 ID
     * @param sourceId   源权限的ID值
     * @return
     */
    @GetMapping("/findRsDetail/{sourceRsId}/{destRsId}/{sourceId}")
    public HandleResult findRightRelationDetail(@PathVariable long sourceRsId,
                                                @PathVariable long destRsId,
                                                @PathVariable long sourceId) {
        return service.findRightRelationDetail(sourceRsId, destRsId, sourceId, SessionUtils.getLoginVersion());
    }

    /**
     * @param rsSource                主资源
     * @param sourceId                主权限定义ID
     * @param destNewRsIdAndDetailIds 从权限定义ID 及权限数据ID
     * @return
     */
    @PostMapping("/saveRightRelationDetails/{rsSource}/{sourceId}")
    public HandleResult saveRightRelationDetails(@PathVariable long rsSource, @PathVariable long sourceId,
                                                 @RequestBody Map<Long, List<Long>> destNewRsIdAndDetailIds) {
        return service.saveRightRelationDetails(rsSource, sourceId,
                destNewRsIdAndDetailIds, SessionUtils.getLoginVersion());
    }

    /**
     * 根据权限资源ID,查询权限资源全信息
     *
     * @param lstId
     * @return
     */
    @PostMapping("/findRightResources")
    public HandleResult findRightResources(@RequestBody List<Long> lstId) {
        return service.findRightResources(lstId, SessionUtils.getLoginVersion());
    }

    /**
     * 保存权限关系
     *
     * @param rrId
     * @param destNewIds
     * @return
     */
    @PostMapping("/saveRightRelationDetailsByRrId/{rrId}")
    public HandleResult saveRightRelationDetailsByRrId(@PathVariable long rrId,
                                                       @RequestBody Map<Long, List<Long>> destNewIds) {
        return service.saveRightRelationDetailsByRrId(rrId, destNewIds, SessionUtils.getLoginVersion());
    }

    /**
     * 查询角色对应的其它资源信息
     *
     * @return
     */
    @GetMapping("/findRoleRightOtherRelation")
    public HandleResult findRoleRightOtherRelation() {
        return service.findRoleRightOtherRelation();
    }

    /**
     * 查询所有权限关系
     *
     * @return
     */
    @GetMapping("/findAllRightSourceDto/{version}")
    public List<RightResourceDto> findAllRightSourceDto(@PathVariable String version) {
        return service.findAllRightSourceDto(version);
    }

    /**
     * 查询所有权限关系
     *
     * @return
     */
    @GetMapping("/findAllRelationDto/{version}")
    public List<RightRelationDto> findAllRelationDto(@PathVariable String version) {
        return service.findAllRelationDto(version);
    }

    /**
     * 取得用户所有角色
     *
     * @return
     */
    @GetMapping("/findUserRoles")
    public HandleResult findUserRoles() {
        List<Role> userRoles = rightService.findUserRoles(SessionUtils.getLoginUser().getUserId(), SessionUtils.getLoginVersion());
        int length = userRoles == null ? 0 : userRoles.size();
        HandleResult result = HandleResult.success(length);
        result.setData(userRoles);
        return result;
    }

    @PutMapping("/selectUserRole/{roleId}")
    public HandleResult selectUserRole(@PathVariable Long roleId) {
        Role role = loginService.initUserRight(SessionUtils.getLoginUser(), roleId);
        HandleResult result = HandleResult.success(1);
        result.setData(role);
        return result;
    }

    /**
     * 重置用户密码
     *
     * @param userId
     * @return
     */
    @PutMapping("/resetUserPassword/{userId}")
    public HandleResult resetUserPassword(@PathVariable Long userId) {
        return service.resetUserPassword(userId);
    }

    /**
     * 启用账号
     *
     * @param userId
     * @return
     */
    @PutMapping("/enableUser/{userId}")
    public HandleResult enableUser(@PathVariable Long userId) {
        return service.enableUser(userId);
    }

    /**
     * 禁用用户账号
     *
     * @param userId
     * @return
     */
    @PutMapping("/disableUser/{userId}")
    public HandleResult disableUser(@PathVariable Long userId) {
        return service.disableUser(userId);
    }

}

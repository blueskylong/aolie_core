package com.ranranx.aolie.application.user.controller;

import com.ranranx.aolie.application.user.service.UserService;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2021/1/6 0006 15:29
 * @Version V0.0.1
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

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
        return service.saveUserRight(userId, SessionUtils.getLoginVersion(), mapNewUserRight);
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
}

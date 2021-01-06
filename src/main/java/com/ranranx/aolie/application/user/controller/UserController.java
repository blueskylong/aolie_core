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
}

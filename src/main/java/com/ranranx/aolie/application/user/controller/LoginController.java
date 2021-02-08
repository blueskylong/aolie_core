package com.ranranx.aolie.application.user.controller;

import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/6 0006 21:09
 **/
@RestController
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginController {
    @RequestMapping("/loginExpired")
    public HandleResult loginExpired() {
        return HandleResult.failure("登录过期,请重新登录");
    }
}

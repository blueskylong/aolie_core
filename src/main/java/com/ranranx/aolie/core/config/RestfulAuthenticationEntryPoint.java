package com.ranranx.aolie.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/7 0007 13:11
 **/
@Component
public class RestfulAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException, ServletException {
        HandleResult result = HandleResult.failure("当前用户没登录");
        result.setCode(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("text/json;charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        // 序列化
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        httpServletResponse.getWriter().write(jsonString);
    }

}

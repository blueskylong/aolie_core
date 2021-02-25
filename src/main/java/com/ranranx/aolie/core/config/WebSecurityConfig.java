package com.ranranx.aolie.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranranx.aolie.core.config.authentication.NamePassVersionScodeAuthenticationFilter;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/6 0006 20:32
 **/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestfulAuthenticationEntryPoint point;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/loginExpired",
                "/logoutSuccess").permitAll()
                .anyRequest().authenticated()
//                .accessDecisionManager(accessDecisionManager())
                .and().exceptionHandling().
                authenticationEntryPoint(point)
                .and()
                .formLogin()
                .and().csrf().disable();
        http.cors();
        http.addFilterAt(createJSONAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().invalidSessionUrl("/loginExpired");
//        http.sessionManagement().maximumSessions(1).expiredUrl("/loginExpired");
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logoutSuccess")
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    /**
     * 如果有自定义的鉴权器,则使用此代码
     *
     * @return
     */
//    @Bean
//    public AccessDecisionManager accessDecisionManager() {
//        List<AccessDecisionVoter<? extends Object>> decisionVoters
//                = Arrays.asList(
//                new WebExpressionVoter(),
//                new RoleVoter(),
//                new AuthenticatedVoter(),
//                new DmAccessVoter());
//        return new UnanimousBased(decisionVoters);
//    }
    @Bean
    NamePassVersionScodeAuthenticationFilter createJSONAuthenticationFilter() throws Exception {
        NamePassVersionScodeAuthenticationFilter filter = new NamePassVersionScodeAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp,
                                                Authentication authentication) throws IOException, ServletException {
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                HandleResult result = HandleResult.success(1);
                result.setData(authentication);
                out.write(new ObjectMapper().writeValueAsString(result));
                out.flush();
                out.close();
            }
        });
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                HandleResult result = HandleResult.failure("登录失败:" + e.getMessage());
                out.write(new ObjectMapper().writeValueAsString(result));
                out.flush();
                out.close();
            }
        });
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }


    /**
     * BCrypt加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
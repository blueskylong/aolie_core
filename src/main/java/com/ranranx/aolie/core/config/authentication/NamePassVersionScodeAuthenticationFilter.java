package com.ranranx.aolie.core.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/7 0007 10:03
 **/
public class NamePassVersionScodeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String SCODE = "scode";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()) {
                Map<String, String> authenticationBean = mapper.readValue(is, Map.class);
                Object version = authenticationBean.get(Constants.FixColumnName.VERSION_CODE);
                if (CommonUtils.isEmpty(version)) {
                    version = SessionUtils.getDefaultVersion();
                }
                Object scode = authenticationBean.get(SCODE);
                if (scode == null) {
                    scode = "";
                }

                authRequest = new NamePassVersionScodeAuthenticationToken(
                        authenticationBean.get(USERNAME),
                        authenticationBean.get(PASSWORD),
                        scode.toString(),
                        version.toString());
            } catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            } finally {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

}

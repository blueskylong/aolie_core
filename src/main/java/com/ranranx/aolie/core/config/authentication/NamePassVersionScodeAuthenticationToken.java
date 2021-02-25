package com.ranranx.aolie.core.config.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/16 0016 17:07
 **/
public class NamePassVersionScodeAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * 验证码
     */
    private String sCode;

    /**
     * 版本
     */
    private String version;

    public NamePassVersionScodeAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public NamePassVersionScodeAuthenticationToken(Object principal, Object credentials,
                                                   String sCode, String version) {
        super(principal, credentials);
        this.sCode = sCode;
        this.version = version;
    }

    public NamePassVersionScodeAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NamePassVersionScodeAuthenticationToken)) {
            return false;
        }
        return this.getPrincipal().equals(((NamePassVersionScodeAuthenticationToken) o).getPrincipal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sCode, version);
    }
}

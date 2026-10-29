package com.hulkhiretech.payment.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class HmacAuthenticationToken
        extends AbstractAuthenticationToken {

    private final Object principal;
    private final String credential;

    public HmacAuthenticationToken(
            Object principal,
            String credential,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credential = credential;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credential;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
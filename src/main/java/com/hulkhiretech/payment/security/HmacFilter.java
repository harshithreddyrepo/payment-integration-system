package com.hulkhiretech.payment.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hulkhiretech.payment.service.interfaces.HmacSHA256;
import com.hulkhiretech.payment.util.JsonUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HmacFilter extends OncePerRequestFilter {

    private final HmacSHA256 hmacSHA256Util;

    private final JsonUtil jsonUtil;

    public HmacFilter(HmacSHA256 hmacSHA256Util, JsonUtil jsonUtil) {
        this.hmacSHA256Util = hmacSHA256Util;
        this.jsonUtil = jsonUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        WrappedRequest wrappedRequest = new WrappedRequest(request);

        String incomingSignature = wrappedRequest.getHeader("HMAC-signature");

        log.info("Incoming HMAC-Signature:{}", incomingSignature);

        // Check incomingSignature with generatedSignature

        // 1. Extract HTTP method
        String method = request.getMethod();

        // 2. Extract request path including query parameters
        String path = request.getRequestURI();

        String query = request.getQueryString();
        if (query != null && !query.isEmpty()) {
            path += "?" + query;
        }

        // 3. Extract canonical request body
        String reqJson = jsonUtil.getCanonicalString(wrappedRequest.getBody());

        // 4. Combine all components
        String canonicalRequest = method + path + reqJson;
        // the signature.

        log.info("extracted request body: {}", reqJson);

        hmacSHA256Util.verifySignature(canonicalRequest, incomingSignature);

        log.info("HMAC-Signature validation successful, proceeding with the payment processing");

        setAuthentication(incomingSignature);

        log.info("set Authentication in SecurityContextHolder for request to {}", wrappedRequest.getRequestURI());

        filterChain.doFilter(wrappedRequest, response);

    }

    private void setAuthentication(String incomingSignature) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        HmacAuthenticationToken authentication = new HmacAuthenticationToken(
                "MERCHANT",
                null,
                Collections.emptyList());

        authentication.setDetails(incomingSignature);

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

}

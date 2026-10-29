package com.hulkhiretech.payment.config;

import com.hulkhiretech.payment.util.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.hulkhiretech.payment.security.HmacFilter;
import com.hulkhiretech.payment.service.interfaces.HmacSHA256;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JsonUtil jsonUtil;
    private final HmacSHA256 hmacSHA256Util;

	public SecurityConfig(HmacSHA256 hmacSHA256Util, JsonUtil jsonUtil) {
		this.hmacSHA256Util = hmacSHA256Util;
        this.jsonUtil = jsonUtil;
	}
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())

           // hmac authentication filter
		   .addFilterAfter(new HmacFilter(hmacSHA256Util, jsonUtil), LogoutFilter.class)

			.authorizeHttpRequests(authorize -> authorize
					.anyRequest().authenticated()
			)
			.sessionManagement(session -> session
					.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
			);

		return http.build();
	}

}

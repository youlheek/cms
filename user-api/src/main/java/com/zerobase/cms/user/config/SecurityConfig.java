package com.zerobase.cms.user.config;

import com.zerobase.cms.user.config.filter.CustomerFilter;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // SpringSecurity 활성화
public class SecurityConfig {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;

	public SecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider) {
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
	}

	@Bean
	@Order(2)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable() // csrf 비활성화 (API 호출용), Jwt 사용시 필요 없음
				// CSRF 보호 : 세션 기반 인증에서는 필요하지만 JWT는 상태를 저장하지 않는 무상태 방식이므로 불필요
				.authorizeRequests(
						auth -> auth.requestMatchers(
										"/signup/**",
										"/signIn/**",
										"/auth/**",
										"/api/auth/**",
										"/swagger-ui/**",
										"/v3/api-docs/**",
										"/hello/**"
								).permitAll()
								.anyRequest().authenticated() // 그 외 요청은 인증 필요
				)
				.sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)));
		// 세션 사용 안 함
		return http.build();
	}

	@Bean
	@Order(1)
	SecurityFilterChain customerSecurityChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/customer/**")
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				.addFilterBefore(new CustomerFilter(jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
				.csrf(csrf -> csrf.disable());

		return http.build();
	}
}

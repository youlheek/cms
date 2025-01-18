package com.zerobase.cms.user.config.filter;

import com.zerobase.cms.user.service.customer.CustomerService;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@WebFilter(urlPatterns = "/customer/*") // 로그인 후 모든 customer 요청마다
@RequiredArgsConstructor
public class CustomerFilter implements Filter {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;

	@Override
	public void doFilter(ServletRequest request,
						 ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String token = req.getHeader("X-AUTH-TOKEN");
		// 1. 토큰이 유효한지 검증

		if (!jwtAuthenticationProvider.validateToken(token)) {
			throw new ServletException("Invalid token");
			// 강사님은 "Invalid Access" 처리함
		}

		// 2. 토큰에서 사용자 정보 추출 -> Authentication 객체 생성
		Authentication auth = jwtAuthenticationProvider.getAuthentication(token);
		// 3. SecurityContext에 auth 저장
		SecurityContextHolder.getContext().setAuthentication(auth);

//		UserVo user = jwtAuthenticationProvider.getUserVo(token);
//		customerService.findByIdAndEmail(user.getId(), user.getEmail())
//				.orElseThrow( () -> new ServletException("Customer not found") );
//				// 강사님은 "Invalid Access" 처리함

		chain.doFilter(request, response);
	}
}

/**
 * getUserVo와 findByIdAndEmail 코드가 필요없어지는 이유 :
 * 1. 토큰 검증 : Jwt 검증은 이미 validateToken 에서 처리됨
 * 2. 인증 정보 관리 : 인증된 사용자 정보는 SecurityContextHolder를 통해 접근 가능
 * 3. 불필요한 DB 조회 제거 : Spring Security가 사용자를 인증한 상태이므로, 추가적인 DB 검증 로직이 필요 없음
 */

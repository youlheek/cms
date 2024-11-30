package com.zerobase.cms.user.config.filter;

import com.zerobase.cms.user.service.CustomerService;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebFilter(urlPatterns = "/customer/*") // 모든 customer 요청마다
@RequiredArgsConstructor
public class CustomerFilter implements Filter {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final CustomerService customerService;

	@Override
	public void doFilter(ServletRequest request,
						 ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String token = req.getHeader("X-AUTH-TOKEN");

		if (!jwtAuthenticationProvider.validateToken(token)) {
			throw new ServletException("Invalid token");
			// 강사님은 "Invalid Access" 처리함
		}

		UserVo vo = jwtAuthenticationProvider.getUserVo(token);
		customerService.findByIdAndEmail(vo.getId(), vo.getEmail())
				.orElseThrow( () -> new ServletException("Customer not found") );
				// 강사님은 "Invalid Access" 처리함

		chain.doFilter(request, response);
	}
}

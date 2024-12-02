package com.zerobase.cms.user.config.filter;


import com.zerobase.cms.user.service.seller.SellerService;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebFilter(urlPatterns = "/seller/*")
@RequiredArgsConstructor
public class SellerFilter implements Filter {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private SellerService sellerService;

	@Override
	public void doFilter(ServletRequest request,
						 ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) request;
		String token = httpReq.getHeader("X-AUTH-TOKEN");

		if (!jwtAuthenticationProvider.validateToken(token)) {
			throw new ServletException("Invalid token");
		}

		UserVo vo = jwtAuthenticationProvider.getUserVo(token);
		sellerService.findByIdAndEmail(vo.getId(), vo.getEmail())
						.orElseThrow( () -> new ServletException("Seller not found")) ;

		chain.doFilter(request, response);
	}
}
/*
* ServletReqeust Vs HttpServletRequest
* */
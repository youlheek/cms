package com.zerobase.cms.user.domain;

import lombok.Getter;

@Getter
public class AuthResponse {
	private String tokenType;

	private String accessToken;
	private long expiresIn;

	private String refreshToken;
	private long refreshTokenExpiresIn;


	public AuthResponse(String accessToken) {
		this.accessToken = accessToken;
	}
}

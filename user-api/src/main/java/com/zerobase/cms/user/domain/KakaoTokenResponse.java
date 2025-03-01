package com.zerobase.cms.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter
public class KakaoTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("id_token")
	private String idToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private int expiresIn;

	@JsonProperty("refresh_token_expires_in")
	private int refreshTokenExpiresIn;

	@JsonProperty("scope")
	private String scope;

}

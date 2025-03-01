package com.zerobase.cms.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter	@Setter
public class KakaoUserInfoResponse {

	@JsonProperty("aud")
	private String aud;

	@JsonProperty("sub")
	private String sub;

	@JsonProperty("auth_time")
	private int authTime;

	@JsonProperty("iss")
	private String iss;

	@JsonProperty("exp")
	private int exp;

	@JsonProperty("iat")
	private int iat;

	@JsonProperty("nickname")
	private String nickname;

	@JsonProperty("picture")
	private String picture;

	@JsonProperty("email")
	private String email;
}

package com.zerobase.domain.config;

import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.common.UserType;
import com.zerobase.domain.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class JwtAuthenticationProvider {

//	private String secretKey = "secretKey";
	// jwt 토큰의 서명 키가 너무 짧아서 보안 요구 사항을 충족시키지 못한다며 에러가 남
	// -> HS256 알고리즘은 256비트 이상의 서명 키를 요구함
	private String secretKey = "testSecretKey20230327testSecretKey20230327testSecretKey20230327";
//	private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private long tokenValidTime = 1000L * 60 * 60 * 24; // 1일
	private long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000; // 7일


	// Access Token 생성
	public String createAccessToken(String userPK, Long id, UserType userType) {

		Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPK)).setId(Aes256Util.encrypt(id.toString()));
		claims.put("roles", userType);
		Date now = new Date();

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + tokenValidTime))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
		// HS256 은 userPK 에 대해서 암호화를 하기 위해 씀
	}

	// Refresh Token 생성 & Redis와 연동
	public String createRefreshToken(String userPK, Long id, UserType userType) {

		Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPK)).setId(Aes256Util.encrypt(id.toString()));
		claims.put("roles", userType);
		Date now = new Date();

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + refreshTokenValidTime))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	// Token 에 대한 Validation
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);

			return !claimsJws.getBody().getExpiration().before(new Date());

		} catch (Exception e) {
			return false;
		}
	}

	// 토큰으로 UserVo 객체 생성
	public UserVo getUserVo(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

		return new UserVo(
				Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(claims.getId()))),
				Aes256Util.decrypt(claims.getSubject()));
	}

	// SpringSecurity에 등록할
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

		// Spring Security의 인증 객체로 변환하기 위해 필요한 Authentication 객체를 만들기위한 코드
		UserDetails userDetails = new User(claims.getSubject(), "", Collections.emptyList());
		// claims.getSubject() : 사용자 이름
		// Collections.emptyList() : 권한(roles)이 비어있음

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
		// UsernamePasswordAuthenticationToken은 Spring Security의 인증 객체입니다.
		// 이 객체는 SecurityContext에 저장되어 Spring Security가 "이 요청은 인증된 사용자"라고 인식하게 만듭니다.
	}
}

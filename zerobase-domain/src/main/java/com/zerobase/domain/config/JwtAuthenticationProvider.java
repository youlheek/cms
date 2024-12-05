package com.zerobase.domain.config;

import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.common.UserType;
import com.zerobase.domain.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

public class JwtAuthenticationProvider {

//	private String secretKey = "secretKey";
	// jwt 토큰의 서명 키가 너무 짧아서 보안 요구 사항을 충족시키지 못한다며 에러가 남
	// -> HS256 알고리즘은 256비트 이상의 서명 키를 요구함
	private String secretKey = "testSecretKey20230327testSecretKey20230327testSecretKey20230327";
//	private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private long tokenValidTime = 1000L * 60 * 60 * 24;

	// Token 생성
	public String createToken(String userPK, Long id, UserType userType) {

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
		// TODO : 코드 확인

		return new UserVo(
				Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(claims.getId()))),
				Aes256Util.decrypt(claims.getSubject()));
	}
}

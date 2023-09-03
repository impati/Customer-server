package com.example.customerserver.web.token;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeGenerator implements TokenGenerator {

	private static final String AUTHORITIES_KEY = "auth";

	@Value("${app.auth.codeExpiry}")
	private long codeValidityInSeconds;

	@Value("${jwt.code}")
	private String secret;

	private Key key;

	@Override
	public void afterPropertiesSet() {
		final byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String createToken(final String name) {
		final long now = new Date().getTime();
		final Date validity = new Date(now + codeValidityInSeconds);
		return Jwts.builder()
			.setSubject(name)
			.claim(AUTHORITIES_KEY, name)
			.signWith(key, SignatureAlgorithm.HS256)
			.setIssuedAt(new Date())
			.setExpiration(validity)
			.compact();
	}

	@Override
	public String getPrincipal(final String token) {
		final Claims claims = Jwts
			.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.getSubject();
	}

	public boolean validateToken(final String code) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(code);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다");
		}
		return false;
	}
}

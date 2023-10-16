package com.auth.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

	private static final String key = "345ev4sf094rjdn49ru49oifdsnf4e49tr8e9jvonfv0ew9eur04uoijd3049";

	public String generteToken(String userName) {
		log.info("*********inside generateToken");
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().issuer("Thiru").claims(claims).subject(userName)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5))).signWith(getSignKey())
				.compact();

	}

	public static SecretKey getSignKey() {
		// TODO Auto-generated method stub
		byte[] keybytes = Decoders.BASE64.decode(key);
		return Keys.hmacShaKeyFor(keybytes);
	}

	public String extractEmployeeId(String token) {

		return extractClaims(token).getSubject();
	}

	public Claims extractClaims(String token) {
		Claims claims = Jwts.parser().verifyWith(getSignKey()).build().parseUnsecuredClaims(token).getPayload();
		return claims;
	}
}

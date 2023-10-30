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

import com.auth.config.UserPrinciples;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

	private static final String key = "345ev4sf094rjdn49ru49oifdsnf4e49tr8e9jvonfv0ew9eur04uoijd3049";

	public String generteToken(UserPrinciples user) {
		log.info("*********inside generateToken");
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", user.getEmployeeId());
		claims.put("role",user.getAccessCode());
		
		return Jwts.builder().setClaims(claims)
				
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(50))).signWith(getSignKey())
				.compact();

	}

	public static SecretKey getSignKey() {
		// TODO Auto-generated method stub
		byte[] keybytes = Decoders.BASE64.decode(key);
		return Keys.hmacShaKeyFor(keybytes);
	}

	public int extractEmployeeId(String token) {
		log.info("*******inside extractEmployeeId JwtService ");
		return   (int) extractClaims(token).get("username");
	}

	public Claims extractClaims(String token) {
		log.info("********inside extractclaims JwtService ");
//		Claims claims = Jwts.parser().verifyWith(getSignKey()).build().parseUnsecuredClaims(token).getPayload();
//		log.info("********after ExtractClaims ");
//		return claims;
		Claims clims= Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		log.info(clims.toString());
		return clims;
	}
}

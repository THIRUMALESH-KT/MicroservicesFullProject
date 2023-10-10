package com.gateway.configuration;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
	private static String Key="vavavavavasesfsfasdfsvandfdfndskfndsfnamcsfksnDfdfmsd";
	public String generateToken(UserDetails userDetails) {
		log.info("inside generatTokn JwtService");
		return Jwts.builder().setIssuer("Thiru").setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+TimeUnit.MINUTES.toMillis(1))).signWith(getSignKey()).compact();
	}
	public String extractUserName(String token)  {
		log.info("inside extractUserName JwtService");
		return extractClaims(token).getSubject();
		
	}
	public Date getExpertion(String token) {
		log.info("inside getExpertion JwtService");
		return extractClaims(token).getExpiration();
	}
	public boolean isTokenValid(String token) {
		log.info("inside isTokenvalid");
		return getExpertion(token).after(new Date(System.currentTimeMillis()));
	}
	private Claims extractClaims(String token) {
		log.info("inside extractClaims JwtService");
		Claims clims= Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		return clims;


		}
	public static Key getSignKey() {
		log.info("inside getsingKey JwtServiec");
		byte[] keybytes=Decoders.BASE64.decode(Key);
		return Keys.hmacShaKeyFor(keybytes);
	}
}

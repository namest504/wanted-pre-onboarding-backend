package com.lim1t.board.service;

import com.lim1t.board.entity.Member;
import com.lim1t.board.exception.CustomException;
import com.lim1t.board.repository.MemberRepository;
import com.lim1t.board.security.MemberPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

	private final Key secretKey;
	private MemberRepository memberRepository;

	private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60L * 30L;           // n(30)분
	private static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60L * 60L * 24L;	 // n(1)일

	public JwtService(@Value("${jwt.secret}") String secretKey, MemberRepository memberRepository) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
		this.memberRepository = memberRepository;
	}

	public String createAccessToken(Long uid) {
		Claims claims = Jwts.claims().setSubject("access_token");
		claims.put("uid", uid);
		Date currentTime = new Date();

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(currentTime)
				.setExpiration(new Date(currentTime.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public UsernamePasswordAuthenticationToken getAuthentication(String token) {
		Long uid = getUidFromToken(token);
		Member member = memberRepository.findById(uid)
				.orElseThrow(() -> new RuntimeException("Member 를 찾지 못했습니다."));
		MemberPrincipal memberPrincipal = new MemberPrincipal(member);
		return new UsernamePasswordAuthenticationToken(memberPrincipal, token,
				member.getAuthorities());
	}

	public Long getUidFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
				.get("uid",
						Long.class);
	}

	public Boolean validateAccessToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build()
					.parseClaimsJws(token).getBody();

			return true;
		} catch (Exception e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다.");
		}
	}
}

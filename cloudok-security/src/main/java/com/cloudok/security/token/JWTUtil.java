package com.cloudok.security.token;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.User;
import com.cloudok.security.exception.SecurityExceptionMessage;
import com.cloudok.util.AESUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author xiazhijian
 * @date Jun 11, 2019 3:29:07 PM
 * 
 */
public class JWTUtil {


	/**
	 * 解算jwt token
	 * 
	 * @param token
	 * @return
	 */
	public static JWTTokenInfo decodeToken(String token) {
		JWTTokenInfo tokenInfo = null;
		try {
			final Jws<Claims> claims = getClaimsFromToken(token);
			try {
				if (claims.getBody().getExpiration().toInstant().isBefore(Instant.now())) {
					throw new SystemException(SecurityExceptionMessage.ACCESS_TOKEN_EXP);
				}
			} catch (Exception e) {
				throw new SystemException(SecurityExceptionMessage.ACCESS_TOKEN_EXP);
			}
			tokenInfo = new JWTTokenInfo(claims.getBody().getSubject(), claims.getBody().get("username").toString(),claims.getBody().get("tokenType").toString().equals("0")?TokenType.ACCESS:TokenType.REFRESH);
		} catch (Exception e) {
			if (e instanceof SystemException) {
				throw e;
			}
			throw new SystemException(CoreExceptionMessage.UNDEFINED_ERROR);
		}
		return tokenInfo;
	}

	private static Jws<Claims> getClaimsFromToken(String token) {
		Jws<Claims> claims;
		try {
			claims = Jwts.parser().setSigningKey(SpringApplicationContext.getBean(TokenProperties.class).getSecret()).parseClaimsJws(token);
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	
	private static final String ISSUER="cloudok.com";

	/**
	 * 生成jwt token
	 * 
	 * @param user
	 * @return
	 */
	public static String genToken(User user, TokenType tokenType) {
		TokenProperties tokenProperties = SpringApplicationContext.getBean(TokenProperties.class);
		int expored=tokenProperties.getExpired();
		String token = Jwts.builder().setId(UUID.randomUUID().toString())
				.setExpiration(Date.from(Instant.now().plusSeconds(tokenType == TokenType.ACCESS ? expored : expored*2)))
				.claim("tokenType", tokenType.getType())
				.claim("username", user.getUsername())
				.setSubject(user.getId().toString())
				.setIssuer(ISSUER)
				.signWith(SignatureAlgorithm.HS256, tokenProperties.getSecret())
				.compact();
		return token;
	}

	/**
	 * 临时通行证
	 * 
	 * @param timeout
	 * @return
	 */
	public static String getProvisionalPass(int timeout) {
		User user = SecurityContextHelper.getCurrentUser();
		if(user!=null) {
		TokenProperties tokenProperties = SpringApplicationContext.getBean(TokenProperties.class);
		SpringApplicationContext.getBean(TokenProperties.class).getSecret();
		try {
			return URLEncoder
					.encode(Base64
							.getEncoder().encodeToString(
									AESUtil.encoder(
											Jwts.builder().setId(UUID.randomUUID().toString())
													.setExpiration(Date.from(Instant.now().plusSeconds(timeout)))
													.claim("tokenType", TokenType.ACCESS.getType())
													.claim("username", user.getUsername())
													.setSubject(user.getId().toString()).setIssuer(ISSUER)
													.signWith(SignatureAlgorithm.HS256, tokenProperties.getSecret()).compact().getBytes(),
													tokenProperties.getSignatureSecret().getBytes())),
							"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		}else {
			return "";
		}
	}

	/**
	 * 解密临时token
	 * 
	 * @param k
	 * @return
	 */
	public static String getJWTTokenFromProvisionalPass(String k) {
		return new String(AESUtil.decoder(Base64.getDecoder().decode(k), SpringApplicationContext.getBean(TokenProperties.class).getSignatureSecret().getBytes()));
	}
}

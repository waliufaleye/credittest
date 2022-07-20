/**
 * 
 */
package com.walex.credit.config;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walex.credit.model.User;
import com.walex.credit.repo.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * @author waliu.faleye
 *
 */
@Component
@Slf4j
public class JwtProvider {
	private CreditProperties creditProp;
	private String secretKey;
	private UserRepository userRepo;

	@Autowired
	public JwtProvider(CreditProperties creditProp, UserRepository userRepo) {
		this.creditProp = creditProp;
		this.secretKey = Base64.getEncoder().encodeToString(creditProp.getJwtSecret().getBytes());
		// this.validityInMilliseconds = milliseconds;
		this.userRepo = userRepo;
		// this.transferValidityInMilliseconds = transferValidityInMilliseconds;
	}

	public String createToken(String username, String password) {
		// Add the username to the payload
		Claims claims = Jwts.claims().setSubject(username);
		Date now = new Date();
		Date expiration = new Date(now.getTime() + creditProp.getJwtExpiration());
		log.info("" + expiration);
		User userDetails = userRepo.findUserByUserName(username);
		userDetails.setExpiration(expiration.toString());
		userRepo.save(userDetails);
		// Build the Token
		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		// secret = Base64.getEncoder().encodeToString(secret.getBytes());
		final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}

	public Boolean isJwtExpired(String token) {
		Date expirationDate = getClaimFromToken(token, Claims::getExpiration);
		return expirationDate.before(new Date());
	}
}

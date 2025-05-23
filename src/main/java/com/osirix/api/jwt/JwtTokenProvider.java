package com.osirix.api.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.osirix.api.entity.Publisher;
import com.osirix.api.entity.Staff;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.repository.PublisherRepository;
import com.osirix.api.repository.StaffRepository;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.utils.UserType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${security.jwt.key.private}")
	private String privateKey;
	
	private static final long JWT_EXPIRATION_DATE = 180000000;
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	PublisherRepository publisherRepository;
	@Autowired
	StaffRepository staffRepository;
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION_DATE);
		User user = userRepository.findUserByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found."));
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", user.getId());
		
		UserType usertype;
		Optional<Publisher> publisher = publisherRepository.findById(user.getId());
		if (publisher.isEmpty()) {
			Optional<Staff> staff = staffRepository.findById(user.getId());
			if (staff.isEmpty()) {
				usertype = UserType.USER;
			} else {
				usertype = UserType.STAFF;
				claims.put("roles", staff.get().getRoles());
			}
		} else {
			usertype = UserType.PUBLISHER;
		}
		
		claims.put("userType", usertype);
		
		
		return Jwts.builder()
				   .subject(username)
				   .issuedAt(currentDate)
				   .expiration(expireDate)
				   .signWith(getSignInKey(), Jwts.SIG.HS256)
				   .claims(claims)
				   .compact();
	}
	
	private SecretKey getSignInKey() {
		byte[] keyBites = Decoders.BASE64.decode(privateKey);		
		return Keys.hmacShaKeyFor(keyBites);
	}
	
	public String getSubjectFromToken(String token) {
		return extraClaim(token, Claims::getSubject);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extraClaim(token, Claims::getExpiration);
	}
	
	public boolean validateToken(String token) {
		Jwts.parser()
			.verifyWith(getSignInKey())
			.build()
			.parseSignedClaims(token);
		
		return true;
	}
	
	private <T> T extraClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				   .verifyWith(getSignInKey())
				   .build()
				   .parseSignedClaims(token)
				   .getPayload();
	}
	
}

package com.osirix.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.osirix.api.dto.auth.AuthLoginRequestDto;
import com.osirix.api.dto.auth.AuthResponseDto;
import com.osirix.api.entity.Role;
import com.osirix.api.entity.Staff;
import com.osirix.api.entity.User;
import com.osirix.api.jwt.JwtTokenProvider;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.security.CustomUserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Collection<GrantedAuthority> mapToAuthorities(Set<Role> roles) {
		return roles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName()))).collect(Collectors.toList());
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User userEntity = userRepository.findUserByUsername(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

	    Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

	    if (userEntity instanceof Staff staff) {
	        authorities = this.mapToAuthorities(staff.getRoles());
	    }

	    return new CustomUserDetails(userEntity, authorities);
	}
	
	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = this.loadUserByUsername(username);
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(username, 
													   userDetails.getPassword(), 
													   userDetails.getAuthorities()
													   );
	}
	
	public AuthResponseDto login(AuthLoginRequestDto authLoginRequest) {
        Authentication authentication = this.authenticate(authLoginRequest.getEmail(),
                                                          authLoginRequest.getPassword());
        
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication);
        
        return new AuthResponseDto(accessToken);
                
    }

}


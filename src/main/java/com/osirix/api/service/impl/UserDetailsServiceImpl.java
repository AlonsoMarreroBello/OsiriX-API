package com.osirix.api.service.impl;

import java.time.LocalDate;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.osirix.api.dto.auth.AuthLoginRequestDto;
import com.osirix.api.dto.auth.AuthResponseDto;
import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.dto.user.UserResponseDto;
import com.osirix.api.entity.Role;
import com.osirix.api.entity.Staff;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.jwt.JwtTokenProvider;
import com.osirix.api.mapper.UserMapper;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.security.CustomUserDetails;
import com.osirix.api.service.AuthService;

@Service
public class UserDetailsServiceImpl implements AuthService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Collection<GrantedAuthority> mapToAuthorities(Set<Role> roles) {
		return roles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName()))).collect(Collectors.toList());
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    User userEntity = userRepository.findUserByEmail(email)
	        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
	    
	    if (!userEntity.getIsEnabled()) {
	    	throw new IllegalAccessError("account is not enabled");
	    }
	    
	    userEntity.setLastLogin(LocalDate.now());
	    userRepository.save(userEntity);

	    Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

	    if (userEntity instanceof Staff staff) {
	        authorities = this.mapToAuthorities(staff.getRoles());
	    }

	    return new CustomUserDetails(userEntity, authorities);
	}
	
	public Authentication authenticate(String email, String password) {
		UserDetails userDetails = this.loadUserByUsername(email);
		
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(email, 
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
	
	@Override
	public AuthResponseDto register(UserRequestDto request) {
	
		User user = userMapper.ToEntity(request);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		user.setRegisterDate(LocalDate.now());
		user.setLastLogin(LocalDate.now());
		user.setIsEnabled(true);
		user.setAccountNotLocked(true);
		
		userRepository.save(user);
		
		return this.login(new AuthLoginRequestDto(user.getEmail(), request.getPassword(), "desktop"));
		
	}

	@Override
	public boolean canUserUploadToApp(Long appId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUserDownloadApp(Long appId) {
		// TODO Auto-generated method stub
		return false;
	}

}


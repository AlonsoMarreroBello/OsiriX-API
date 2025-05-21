package com.osirix.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.dto.user.UserResponseDto;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.UserMapper;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserMapper userMapper;

	@Override
	public List<UserResponseDto> getAll() {
		return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public List<UserResponseDto> getNormalUsers() {
		return userRepository.findAllNormalUsers().stream().map(userMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public UserResponseDto getUserById(Long userId) {
		return userMapper.toDto(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found")));
	}

	@Override
	public UserResponseDto updateUser(Long userId, UserRequestDto request) {
		System.out.println(request);
		
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		if (!request.getUsername().isBlank() && !request.getUsername().isEmpty()) {
			user.setUsername(request.getUsername());
		}
		
		if (!request.getEmail().isBlank() && !request.getEmail().isEmpty()) {
			user.setEmail(request.getEmail());
		}
		
		if (!request.getPassword().isBlank() && !request.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		
		return userMapper.toDto(userRepository.save(user));
		
	}

	@Override
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		userRepository.delete(user);
	}

	@Override
	public UserResponseDto toggleEnable(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		user.setIsEnabled(!user.getIsEnabled());
		return userMapper.toDto(userRepository.save(user));
	}

	@Override
	public UserResponseDto toggleLockAccount(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		user.setAccountNotLocked(!user.getAccountNotLocked());
		return userMapper.toDto(userRepository.save(user));
	}

}

package com.osirix.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.osirix.api.dto.friendship.FriendshipResponseDto;
import com.osirix.api.entity.Friendship;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.FriendshipMapper;
import com.osirix.api.repository.FriendshipRepository;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.service.FriendshipService;

public class FriendshipServiceimpl implements FriendshipService {
	
	@Autowired
	FriendshipRepository friendshipRepository;
	
	@Autowired
	FriendshipMapper friendshipMapper;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public FriendshipResponseDto getById(Long id) {
		Friendship friendship = friendshipRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("friendship not found"));
		
		return friendshipMapper.toResponse(friendship);
	}

	@Override
	public List<FriendshipResponseDto> getByUserId(Long userId) {
		return friendshipRepository.findByUserId(userId).stream().map(friendshipMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public FriendshipResponseDto sendFriendshipRequest(Long senderId, String username) {
	
		User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		User receiver = userRepository.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		Friendship friendship = Friendship.builder().user1(sender).user2(receiver).friendshipDate(null).isAccepted(false).build();
		
		return friendshipMapper.toResponse(friendshipRepository.save(friendship));
	}

	@Override
	public void deleteFriendship(Long friendshipId) {
		Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow(() -> new ResourceNotFoundException("friendship not found"));
		
		friendshipRepository.delete(friendship);
	}

}

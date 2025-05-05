package com.osirix.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.osirix.api.dto.friendship.FriendshipResponseDto;
import com.osirix.api.entity.Friendship;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.FriendshipMapper;
import com.osirix.api.repository.FriendshipRepository;

public class FriendshipServiceimpl implements FriendshipService {
	
	@Autowired
	FriendshipRepository friendshipRepository;
	
	@Autowired
	FriendshipMapper friendshipMapper;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFriendship(Long friendshipId) {
		// TODO Auto-generated method stub

	}

}

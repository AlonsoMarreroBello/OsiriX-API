package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.friendship.FriendshipResponseDto;

public interface FriendshipService {
	
	FriendshipResponseDto getById(Long id);
	List<FriendshipResponseDto> getByUserId(Long userId);
	
	FriendshipResponseDto sendFriendshipRequest(Long senderId, String username);
	
	void acceptFriendship(Long friendshipId);
	
	void deleteFriendship(Long friendshipId);
	
}

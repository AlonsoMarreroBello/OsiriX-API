package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.FriendshipResponseDto;

public interface FriendshipService {
	
	FriendshipResponseDto getById(Long id);
	List<FriendshipResponseDto> getByUserId(Long userId);
	
	FriendshipResponseDto sendFriendshipRequest(String username);

	void deleteFriendship(Long friendshipId);
}

package com.osirix.api.mapper;

import org.mapstruct.Mapper;

import com.osirix.api.dto.friendship.FriendshipResponseDto;
import com.osirix.api.entity.Friendship;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {
	
	FriendshipResponseDto toResponse(Friendship friendship);

}

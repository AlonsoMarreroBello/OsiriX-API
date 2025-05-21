package com.osirix.api.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osirix.api.dto.friendship.FriendshipResponseDto;
import com.osirix.api.dto.notification.NotificationRequestDto;
import com.osirix.api.entity.Friendship;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceAlreadyExistingException;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.FriendshipMapper;
import com.osirix.api.mapper.UserMapper;
import com.osirix.api.repository.FriendshipRepository;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.service.FriendshipService;
import com.osirix.api.service.NotificationService;

@Service
public class FriendshipServiceimpl implements FriendshipService {
	
	@Autowired
	FriendshipRepository friendshipRepository;
	
	@Autowired
	FriendshipMapper friendshipMapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	NotificationService notificationService;

	@Override
	public FriendshipResponseDto getById(Long id) {
		Friendship friendship = friendshipRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("friendship not found"));
		FriendshipResponseDto fResponse = friendshipMapper.toResponse(friendship);
		
		fResponse.setUser1(userMapper.toSimpleDto(friendship.getUser1()));
		fResponse.setUser2(userMapper.toSimpleDto(friendship.getUser2()));
		
		return fResponse;
	}

	@Override
	public List<FriendshipResponseDto> getByUserId(Long userId) {
		 List<Friendship> friendships = friendshipRepository.findByUserId(userId).stream().collect(Collectors.toList());
		 List<FriendshipResponseDto> response = new ArrayList<>();
		 
		 for (Friendship friendship : friendships) {
			FriendshipResponseDto fResponse = friendshipMapper.toResponse(friendship);
			
			fResponse.setUser1(userMapper.toSimpleDto(friendship.getUser1()));
			fResponse.setUser2(userMapper.toSimpleDto(friendship.getUser2()));
			response.add(fResponse);
		}
		
		return response;
	}

	@Override
	public FriendshipResponseDto sendFriendshipRequest(Long senderId, String username) {
	
		User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		User receiver = userRepository.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		Optional<Friendship> friendshipTest = friendshipRepository.findByUser1IdAndUser2Id(sender.getId(), receiver.getId());
		
		if (friendshipTest.isPresent()) {
			throw new ResourceAlreadyExistingException("Friendship or friendship requests between users already exists");
		}
		
		Friendship friendship = Friendship.builder().user1(sender).user2(receiver).friendshipDate(null).isAccepted(false).build();

		FriendshipResponseDto fResponse = friendshipMapper.toResponse(friendship);
		
		fResponse.setUser1(userMapper.toSimpleDto(friendship.getUser1()));
		fResponse.setUser2(userMapper.toSimpleDto(friendship.getUser2()));
		
		
		NotificationRequestDto notification = new NotificationRequestDto(receiver.getId(), "new frienship request from {}" + sender.getUsername() );
		
		notificationService.sendNotification(notification);
		
		return fResponse;
	}

	@Override
	public void acceptFriendship(Long friendshipId) {
		Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow(() -> new ResourceNotFoundException("friendship not found"));
		
		friendship.setIsAccepted(true);
		friendship.setFriendshipDate(LocalDate.now());
		
		NotificationRequestDto notification = new NotificationRequestDto(
				friendship.getUser1().getId(), "You and " + friendship.getUser2().getUsername() + " are now friends"
				);
		
		NotificationRequestDto notification2 = new NotificationRequestDto(
				friendship.getUser2().getId(), "You and " + friendship.getUser1().getUsername() + " are now friends"
				);
		
		notificationService.sendNotification(notification);
		notificationService.sendNotification(notification2);
		
		friendshipRepository.save(friendship);
	}
	
	@Override
	public void deleteFriendship(Long friendshipId) {
		Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow(() -> new ResourceNotFoundException("friendship not found"));
		
		friendshipRepository.delete(friendship);
	}
	
	

}

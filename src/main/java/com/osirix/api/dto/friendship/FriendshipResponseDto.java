package com.osirix.api.dto.friendship;

import java.time.LocalDate;

import com.osirix.api.dto.user.UserSimpleResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipResponseDto {
	
	private Long id;
	private UserSimpleResponseDto user1;
	private UserSimpleResponseDto user2;
	private LocalDate friendshipDate;
    private Boolean isAccepted = false;

}

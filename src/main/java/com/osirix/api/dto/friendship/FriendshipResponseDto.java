package com.osirix.api.dto.friendship;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipResponseDto {
	
	private Long id;
	private Long user1Id;
	private Long user2Id;
	private LocalDate friendshipDate;
    private Boolean isAccepted = false;

}

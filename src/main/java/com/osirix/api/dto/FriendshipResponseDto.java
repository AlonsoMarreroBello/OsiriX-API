package com.osirix.api.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipResponseDto {
	
	private Long id;
	private Long userOneId;
	private Long userTwoId;
	private LocalDate friendshipDate;
    private Boolean isAccepted = false;

}

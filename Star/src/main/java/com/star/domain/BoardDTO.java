package com.star.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
	
	private Long userNumber;
	
	private String userNickname;
	
	private String userId;
	
	private String userPassword;
	
	private String userPhoneNumber;
	
	private String userRegion;
	
	private LocalDateTime signInDate;
	
//	mysql에서 tinyint 자동으로 mybatis에서는 boolean으로 인식 가능 -> boolean으로 할지 int로 할지 선택하면 될듯
	private boolean withdrawalYn;
	
	private LocalDateTime withdrawalDate;
	
	private boolean adminYn;
	

}


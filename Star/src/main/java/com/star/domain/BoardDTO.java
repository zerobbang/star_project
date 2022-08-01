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
	
//	mysql���� tinyint �ڵ����� mybatis������ boolean���� �ν� ���� -> boolean���� ���� int�� ���� �����ϸ� �ɵ�
	private boolean withdrawalYn;
	
	private LocalDateTime withdrawalDate;
	
	private boolean adminYn;
	
	
	

}


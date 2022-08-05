package com.star.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class UserDTO {
	 
	
//	users_table
	private Long userNumber;
	
	private String userNickname;
	
	private String userId;
	
	private String userPassword;
	
	private String userEmail;
	
	private String userRegion;
	
	private LocalDateTime signUpDate;
	
//	mysql tinyint - 0,1 mybatis에서는 tinyint를 자동적으로 boolean 인식한다.
	private boolean withdrawalYn;
	 
	private LocalDateTime withdrawalDate;
	
	private boolean adminYn;
	
	
//	default 값 생성
	public UserDTO() {
		this.userRegion = "전국";
	}


	@Override
	public String toString() {

		return "UserDTO [userNumber=" + userNumber + ", userNickname=" + userNickname + ", userId=" + userId
				+ ", userPassword=" + userPassword + ", userEmail=" + userEmail + ", userRegion=" + userRegion
				+ ", signUpDate=" + signUpDate + ", withdrawalYn=" + withdrawalYn + ", withdrawalDate=" + withdrawalDate
				+ ", adminYn=" + adminYn + "]";

//		StringBuilder builder = new StringBuilder();
//		builder.append("UserDTO [userNumber=").append(userNumber).append(", userNickname=").append(userNickname)
//				.append(", userId=").append(userId).append(", userPassword=").append(userPassword)
//				.append(", userEmail=").append(userEmail).append(", userRegion=").append(userRegion)
//				.append(", signInDate=").append(signUpDate).append(", withdrawalYn=").append(withdrawalYn)
//				.append(", withdrawalDate=").append(withdrawalDate).append(", adminYn=").append(adminYn).append("]");
//		return builder.toString();
 
	}



}
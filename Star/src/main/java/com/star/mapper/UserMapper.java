package com.star.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.UserDTO;

@Mapper
public interface UserMapper {
	
	public int insertUser(UserDTO params);
	
	public String testImg(Long userNumber);
	
	public UserDTO detailUser(Long userNumber);
	
	// 닉네임 조회
	public String getNickname(Long userNumber);
	
	//로그인체크
	public UserDTO loginCheck(UserDTO userDTO);

	public String idCheck(UserDTO userDto);
	
	public String nicknameCheck(UserDTO userDto);

	public String emailCheck(UserDTO userDto);

	public int updateNickRegion(UserDTO userDto);
	
	public int updatePassNickRegion(UserDTO userDto);

	// 회원 탈퇴
	public void pagedown(Long userNumber);

	public String findIdFromEmail(UserDTO userDTO);

	public int changePassword(UserDTO userDto);

} 

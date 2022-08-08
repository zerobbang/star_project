package com.star.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.UserDTO;

@Mapper
public interface UserMapper {
	
	public int insertUser(UserDTO params);
	
	public String testImg(Long userNumber);
	
	public UserDTO detailUser(Long userNumber);
	
	//로그인체크
	public UserDTO loginCheck(UserDTO userDTO);

	public String idCheck(UserDTO userDto);
	

} 

package com.star.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.star.domain.UserDTO;
import com.star.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public boolean registerUser(UserDTO params) {
		
		return false;
	}

//	유저 정보 조회
	@Override
	public UserDTO getUser(Long userNumber) {
		return userMapper.detailUser(userNumber);
	}

	
	
	
	
}

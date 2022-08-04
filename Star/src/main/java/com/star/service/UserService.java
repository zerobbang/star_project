package com.star.service;

import com.star.domain.MailDTO;
import com.star.domain.UserDTO;

public interface UserService {
	
	public boolean registerUser(UserDTO params);
	
	public UserDTO getUser(Long userNumber);
	
	public void sendSimpleMessage(MailDTO mailDto);
	
	public UserDTO loginUser(UserDTO userDTO); 
	
}

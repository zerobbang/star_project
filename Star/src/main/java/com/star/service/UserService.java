package com.star.service;


import org.springframework.stereotype.Service;

//import javax.servlet.http.HttpServletRequest;

import com.star.domain.MailDTO;
import com.star.domain.UserDTO;

@Service
public interface UserService {
	
	public boolean registerUser(UserDTO params);
	
	public UserDTO getUser(Long userNumber);

	public String sendSimpleMessage(MailDTO mailDto);
	
	public UserDTO loginUser(UserDTO userDTO);
	
	public int doSignUp(UserDTO userDTO);

	public String[] idCheck(UserDTO userDto);
	
	public String[] nicknameCheck(UserDTO userDto);
	
	public String[] emailCheck(UserDTO userDto);
	
}

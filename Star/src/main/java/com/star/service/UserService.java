package com.star.service;


//import javax.servlet.http.HttpServletRequest;


import com.star.domain.MailDTO;
import com.star.domain.UserDTO;

public interface UserService {
	
	public boolean registerUser(UserDTO params);
	
	public UserDTO getUser(Long userNumber);
	

	public String sendSimpleMessage(MailDTO mailDto);
	/* void */
	
	public UserDTO loginUser(UserDTO userDTO); 

	
}

package com.star.service;


import java.util.List;

import com.star.domain.DustDTO;

//import javax.servlet.http.HttpServletRequest;

import com.star.domain.MailDTO;
import com.star.domain.UserDTO;
 
public interface UserService {
	
	public boolean registerUser(UserDTO params);
	
	public UserDTO getUser(Long userNumber);
	

	public String sendSimpleMessage(MailDTO mailDto);
	/* void */
	
	public UserDTO loginUser(UserDTO userDTO);
	
	public int doSignUp(UserDTO userDTO);

	public String idCheck(UserDTO userDto);
	
	public List<DustDTO> getPrediction(DustDTO params);
	
}

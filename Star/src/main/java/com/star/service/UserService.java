package com.star.service;


<<<<<<< HEAD
import java.util.List;

import com.star.domain.DustDTO;
=======
import org.springframework.stereotype.Service;
>>>>>>> 661026da716e1efc6e17f5a2ce5bf796ca43cb40

//import javax.servlet.http.HttpServletRequest;

import com.star.domain.MailDTO;
import com.star.domain.UserDTO;
<<<<<<< HEAD
 
=======

@Service
>>>>>>> 661026da716e1efc6e17f5a2ce5bf796ca43cb40
public interface UserService {
	
	public boolean registerUser(UserDTO params);
	
	public UserDTO getUser(Long userNumber);

	public String sendSimpleMessage(MailDTO mailDto);
	
	public UserDTO loginUser(UserDTO userDTO);
	
	public int doSignUp(UserDTO userDTO);

	public String[] idCheck(UserDTO userDto);
	
	public String[] nicknameCheck(UserDTO userDto);
	
	public String[] emailCheck(UserDTO userDto);
	
	public List<DustDTO> getPrediction(DustDTO params);
	
}

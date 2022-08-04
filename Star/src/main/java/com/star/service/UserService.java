package com.star.service;


<<<<<<< HEAD
=======
//import javax.servlet.http.HttpServletRequest;


>>>>>>> 0f164a7917499bccd9a929571bd595cf6de04458
import com.star.domain.MailDTO;
import com.star.domain.UserDTO;

public interface UserService {
	
	public boolean registerUser(UserDTO params);
	
	public UserDTO getUser(Long userNumber);
	
<<<<<<< HEAD
	public String sendSimpleMessage(MailDTO mailDto);
=======

	public String sendSimpleMessage(MailDTO mailDto);
	/* void */
	
	public UserDTO loginUser(UserDTO userDTO); 

>>>>>>> 0f164a7917499bccd9a929571bd595cf6de04458
	
}

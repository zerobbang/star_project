package com.star.service;

<<<<<<< HEAD
=======
//import javax.servlet.http.HttpServletRequest;

>>>>>>> 1941fcd9176c3abbe2cc4f5aa7d53e95e95b1088
import com.star.domain.MailDTO;
import com.star.domain.UserDTO;

public interface UserService {
	
	public boolean registerUser(UserDTO params);
	
	public UserDTO getUser(Long userNumber);
	
<<<<<<< HEAD
	public void sendSimpleMessage(MailDTO mailDto);
	
	public UserDTO loginUser(UserDTO userDTO); 
=======
	public String sendSimpleMessage(MailDTO mailDto);
>>>>>>> 1941fcd9176c3abbe2cc4f5aa7d53e95e95b1088
	
}

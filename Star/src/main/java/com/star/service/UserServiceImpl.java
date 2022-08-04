package com.star.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
//메일
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.star.domain.MailDTO;
import com.star.domain.UserDTO;
import com.star.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
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
	
	

	private JavaMailSender emailSender;

	
	/* void */
    public String sendSimpleMessage(MailDTO mailDto) {
    	Random random = new Random();
		int rdNum = random.nextInt(10);
		String certifyNum = Integer.toString(rdNum);

 
    	mailDto.setTitle("인증번호입니다.");
    	mailDto.setContent(certifyNum);
 

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gdqqdq05@gmail.com");
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());
        
//        session.setAttribute("emailNum", mailDto.getContent());
        emailSender.send(message);
        return certifyNum;
    }
    
    @Override 
    public UserDTO loginUser(UserDTO userDTO) {
//    	이제 같은지 안 같은지 판단
//    	어떻게 판단하지 
//    	db id = 읿력한 id
    	
    	return userMapper.loginCheck(userDTO);
    	
    }
    	
}

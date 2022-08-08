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
    	// 회원 정보 이메일과 일치하는지 확인 하기
    	
    	Random random = new Random();
		String certifyNum = ""; 
				
				for(int i=0; i<6; i++) {
					int rdNum = random.nextInt(10);
					
					certifyNum += Integer.toString(rdNum); 
				}
				 
    	mailDto.setTitle("인증번호입니다.");
    	mailDto.setContent(certifyNum);
 

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gdqqdq05@gmail.com");
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());
        
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

	@Override
	public int doSignUp(UserDTO userDTO) {
		
		userMapper.insertUser(userDTO);
		
		return 0;
	}

	// id 중복검사
	@Override
	public String[] idCheck(UserDTO userDto) {
		
		String resultId = userMapper.idCheck(userDto);
		
		String result;
        
        if(resultId == null) {
        	result = "사용 가능한 닉네임입니다.";
        }else {
        	result = "이미 사용중입니다.";
        }
        String[] returndata = {result};
        
        return returndata;
		
	}


	// 닉네임 중복검사
	@Override
	public String[] nicknameCheck(UserDTO userDto) {
		
		String resultNickname = userMapper.nicknameCheck(userDto);
		
		String result;
        
        if(resultNickname == null) {
        	result = "사용 가능한 닉네임입니다.";
        }else {
        	result = "이미 사용중입니다.";
        }
        String[] returndata = {result};
        
        return returndata;
	}


	// 이메일 중복검사
	@Override
	public String[] emailCheck(UserDTO userDto) {
		// TODO Auto-generated method stub
		String resultEmail = userMapper.emailCheck(userDto);
		
		String result;
        
        if(resultEmail == null) {
        	result = "사용가능한 이메일입니다.";
        }else {
        	result = "이미 사용중입니다.";
        }
        String[] returndata = {result};
        
        return returndata;
		
		
	}
    	
	
}

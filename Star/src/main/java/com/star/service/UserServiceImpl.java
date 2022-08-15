package com.star.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
//메일
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.star.domain.DustDTO;
import com.star.domain.MailDTO;
import com.star.domain.UserDTO;
import com.star.mapper.DustMapper;
import com.star.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private DustMapper dustMapper;
	
	private JavaMailSender emailSender;

	@Override
	public boolean registerUser(UserDTO params) {
		
		return false;
	}
	  
	//	유저 정보 조회
	@Override
	public UserDTO getUser(Long userNumber) {             
		return userMapper.detailUser(userNumber);
	}
	
	/* void */
    public String sendSimpleMessage(MailDTO mailDto) {
    	
    	System.out.println("서비스 - 메일");
    	// 회원 정보 이메일과 일치하는지 확인 하기
    	Random random = new Random();
		String certifyNum = ""; 
				
				for(int i=0; i<6; i++) {
					int rdNum = random.nextInt(10);
					
					certifyNum += Integer.toString(rdNum); 
				}
				 
    	mailDto.setTitle("인증번호입니다.");
    	mailDto.setContent(certifyNum);
    	System.out.println("111111111111");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gdqqdq05@gmail.com");
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());
        System.out.println("22222222222");
        emailSender.send(message);
        System.out.println("333333333333");
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

	@Override
	public String changeInfo(UserDTO userDto) {
		// TODO Auto-generated method stub
		
		String userPassword = userDto.getUserPassword();
		System.out.println(userPassword);
		try {
			if (userPassword == "") {
				userMapper.updateNickRegion(userDto);
			} else {
				userMapper.updatePassNickRegion(userDto);
			}
			return "success";
		}catch (Exception e) {
			return "fail";
		}
	}

	// 회원탈퇴
	@Override
	public void pagedown(Long userNumber) {
		// TODO Auto-generated method stub
		System.out.println("유저 impl 확인");
		System.out.println(userNumber);
//		Long userNumber = userDto.getUserNumber();
		userMapper.pagedown(userNumber);
	}
	
	// 예측 리스트 불러오기 (임시)
	@Override
	public List<DustDTO> getPrediction(DustDTO params) {
		
		// List<DustDTO> boardList = Collections.emptyList();
		List<DustDTO> boardList;
		System.out.printf("이건 테스트용 : " + params.getRegion() + "\n");
		
		if (params.getRegion() == null || (params.getRegion().equals("전국"))) {
			boardList = dustMapper.getPredictionList2(params);
		}else {
			boardList = dustMapper.getPredictionList1(params);
		}
		return boardList;
	}

	@Override
	public String findId(UserDTO userDTO) {
		String result = userMapper.findIdFromEmail(userDTO);
		
		return result;
	}

	@Override
	public int changePassword(UserDTO userDTO) {
    	int result = userMapper.changePassword(userDTO);
    	return result;
	}

	// 닉네임 조회
	@Override
	public String getNickname(Long userNumber) {
		String result = userMapper.getNickname(userNumber);
		return result;
	}
	
}

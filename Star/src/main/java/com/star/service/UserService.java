package com.star.service;



import java.util.List;

import org.springframework.stereotype.Service;

import com.star.domain.DustDTO;
import com.star.domain.ImgDTO;

//import javax.servlet.http.HttpServletRequest;

import com.star.domain.MailDTO;
import com.star.domain.UserDTO;

@Service
public interface UserService {
	
	public boolean registerUser(UserDTO params);
	
	public UserDTO getUser(Long userNumber);
	
	// 닉네임 조회
	public String getNickname(Long userNumber);

	public String sendSimpleMessage(MailDTO mailDto);
	
	public UserDTO loginUser(UserDTO userDTO);
	
	public int doSignUp(UserDTO userDTO);

	public String[] idCheck(UserDTO userDto);
	
	public String[] nicknameCheck(UserDTO userDto);
	
	public String[] emailCheck(UserDTO userDto);

	public String changeInfo(UserDTO userDto);

	public void pagedown(Long userNumber);
	
	public List<DustDTO> getPrediction(DustDTO params);

	public String findId(UserDTO userDTO);

	public int changePassword(UserDTO userDto);
	
//	// 닉네임으로 회원번호 찾기
//	public long getUserNumber(String userNickname);
	
	
}

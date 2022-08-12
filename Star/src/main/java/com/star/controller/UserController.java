package com.star.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.star.domain.DustDTO;
import com.star.domain.MailDTO;
import com.star.domain.UserDTO;
import com.star.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

//	메인 페이지
	@GetMapping(value = "/star/mainpage")
	public String openUser(UserDTO userDTO) {
		return "star/main";
	} 
	
	// ajax 중간 main
	@GetMapping(value = "/star/main2")
	public String openMap() {
		return "star/main2";
	}
	
	// 메인페이지 ( with. 중간에 있는 테이블 )
	@GetMapping(value = "/star/main3")
	public String openPredictionList(DustDTO params, RedirectAttributes rttr) {
		List<DustDTO> dustList = userService.getPrediction(params);
		rttr.addFlashAttribute(dustList);

		System.out.println(params);
		System.out.println(params.getRegion());
		rttr.getFlashAttributes();
		
		System.out.println(dustList.get(0).getHumidity());
		System.out.println(dustList.get(1).getHumidity());
		return "star/main3";
		
	}

	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage() {
		return "star/sendmail";

	}
 
    @PostMapping("/mail/send")
    public String sendMail(MailDTO mailDto) {
        userService.sendSimpleMessage(mailDto);
        System.out.println("메일 전송 완료");
        return "star/sendmail";
    }
    
    // 로그인 페이지
    @GetMapping(value = "/star/login")
    public String logIn() { 
    	System.out.println("test!");
    	return "star/login";
    } 
    
    // 로그인 체크
    @PostMapping(value = "/star/login") 
    public String logInCheck(UserDTO userDTO, RedirectAttributes rttr) {
    	
    	System.out.println(userDTO);
    	
		try {
			
			System.out.println("do action!");
	    	System.out.println(userDTO);
	    	System.out.println(userDTO.toString());
			 
	    	userDTO = userService.loginUser(userDTO);
	    	
	    	System.out.println(rttr.getFlashAttributes());
	    	rttr.addFlashAttribute(userDTO);
	    	System.out.println(rttr.getFlashAttributes());
			 
			if (userDTO.getUserId() != null) {
		        return "redirect:/star/mainpage";
			}else {
				return "star/login";
			}
		} catch (DataAccessException e) {
			return "star/login"; 
		} catch (Exception e) {
			return "star/login";  
		}
 
    };
    
    // 회원가입 페이지 (임시로 sendmail) -> (signUp으로 변경)
    @GetMapping(value = "/star/signup")
    public String singUp() {
    	return "star/signUp";
	}  

    // 계정찾기 페이지
    @GetMapping(value = "/star/findUser")
    public String findAccount() {
    	return "star/findUser";
    }

    // 입력한 이메일로 메일 보내기
	@PostMapping(value = "/mailSend")
	@ResponseBody
	public String[] dataSend(MailDTO mailDto){
		
		System.out.println("메일 전송 시작");
		userService.sendSimpleMessage(mailDto);
		System.out.println("메일 전송 완료");
		String certifyNum = mailDto.getContent();
		
		System.out.println(certifyNum);
		 
        String[] returndata = {certifyNum};
        return returndata;
    };
    
    // 회원가입 기능
    @PostMapping(value = "/signup.do")
    @ResponseBody
    public String doSingUp(UserDTO userDTO) {
    	 
    	System.out.println(userDTO.toString());
    	
    	try {
    		userService.doSignUp(userDTO);
    	} catch(Exception e) {
    		System.out.println("fail...");
    		return "fail";
    	}
    	
    	return "success";
	};
	
    
    // id 유일성 체크
    @PostMapping(value = "/inputIDCheck")
	@ResponseBody
    public String[] inputCheck(UserDTO userDto){
		
        String[] returndata = userService.idCheck(userDto);
        
        return returndata;
    };
    
    // 닉네임 유일성 체크
    @PostMapping(value = "/inputNicknameCheck")
    @ResponseBody
    public String[] inputNicknameCheck(UserDTO userDto){
		
        String[] returndata = userService.nicknameCheck(userDto);
        
        return returndata;
    };
    
    // 이메일 유일성 체크
    @PostMapping(value = "/inputEmailCheck")
    @ResponseBody
    public String[] inputEmailCheck(UserDTO userDto){
		
		String[] returndata = userService.emailCheck(userDto);
		
        return returndata;
    };
    
    // 정보 변경 페이지 이동
    @PostMapping(value = "/star/changeInfo")
    public String changeInfoPage(UserDTO userDto, RedirectAttributes rttr) {
    	
    	rttr.addFlashAttribute(userDto);
    	
		return "star/changeInfo";
	}
    
    // 정보 변경 실행
    @PostMapping(value = "/changeInfo.do")
    public String changeInfo(UserDTO userDto, RedirectAttributes rttr){
		
		userService.changeInfo(userDto);
		
		rttr.addFlashAttribute(userDto);
		
        return "/star/main";
    };
    
	// 마이 페이지 이동
    @GetMapping(value = "/star/gayeong/mypage")
    public String mypage() {
    	return "star/gayeong/mypage";
    }
    
    // 회원탈퇴
    @GetMapping(value = "/star/signdown")
    public String mypage2() {
    	
    	System.out.println("컨트롤러 확인");
    	
    	userService.pagedown();
    	
    	System.out.println("회원탈퇴 완료됨!");
    	
    	return "redirect:/star/mainpage";
    }
    
    // id 찾기
    @PostMapping(value = "/findId")
	@ResponseBody
    public String findId(UserDTO userDTO){
		
        String returndata = userService.findId(userDTO);
        
        return returndata;
    };
    
    // 비밀번호 변경 페이지
    @PostMapping(value = "/star/changePassword")
    public String changePasswordPage(UserDTO userDto, RedirectAttributes rttr){
		
		rttr.addFlashAttribute(userDto);
		System.out.println(rttr.getFlashAttributes());
		
        return "/star/changePassword";
    };
    
    // 비밀번호 변경 실행
    @PostMapping(value = "/changePassword.do")
    public String changePassword(UserDTO userDTO){
		
		int result = userService.changePassword(userDTO);
		System.out.println(result);
		
		return "redirect:/star/login";

    };
    
}
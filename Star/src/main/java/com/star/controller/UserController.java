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
	public String openUser(Model model) {
		return "star/main";
	} 
	
	// ajax 중간 main
	@GetMapping(value = "/star/main2")
	public String openMap() {
		return "star/main2";
	}
	
	// 메인페이지 ( with. 중간에 있는 테이블 )
	@GetMapping(value = "/star/main3")
	public String openPredictionList(@ModelAttribute("params") DustDTO params, Model model) {
		List<DustDTO> dustList = userService.getPrediction(params);
		model.addAttribute("dustList", dustList);
		System.out.println("dd");
		System.out.println(model);
		System.out.println(params);
		System.out.println(params.getRegion());
		model.addAttribute("selectRegion", params.getRegion());
		
		System.out.println(dustList.get(0).getHumidity());
		System.out.println(dustList.get(1).getHumidity());
		return "star/main3"; 
	}

	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage(Model model) {
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
    public String logIn(Model model) { 
    	return "star/login";
    } 
    
    // 로그인 체크  /star/logInCheck
    @PostMapping(value = "/star/login") 
    public String logInCheck(UserDTO userDTO, Model model) {
//    public String logInCheck(Model model) {
  	
//    	여기에서 이제 service에 정의한 함수를 사용할거임
//    	그거로 db에 있는 id,password와 일치하면 메인페이지로 이동시킬거임
//    	근데 db에 접근해야 되니까 mapper.xml에도 관련 코드를 작성해줘야 됨
    	
    	System.out.println(userDTO);
    	System.out.println(model);
    	
		try {
			
			System.out.println("do action!");
	    	System.out.println(userDTO);
	    	System.out.println(userDTO.toString());
			 
	    	userDTO = userService.loginUser(userDTO);

	    	model.addAttribute("userDTO",userDTO);
	    	System.out.println(model);


			 
			if (userDTO
					.getUserId() != null) {
				return "star/main";
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
    public String singUp(Model model) {
    	return "star/signUp";
	}  

    // 계정찾기 페이지
    @GetMapping(value = "/star/findUser")
    public String findAccount() {
    	return "star/findUser";
    }

	@RequestMapping(value = "/dataSend",method = RequestMethod.POST)
	@ResponseBody
    public String[] dataSend(Model model,MailDTO mailDto){
		
		System.out.println("메일 전송 시작");
		userService.sendSimpleMessage(mailDto);
		System.out.println("메일 전송 완료");
		String certifyNum = mailDto.getContent();
		
		System.out.println(certifyNum);
		 
        String[] returndata = {certifyNum};
        return returndata;
    };
    
    // 회원가입 기능
    @RequestMapping(value = "/signup.do",method = RequestMethod.POST)
    @ResponseBody
    public String doSingUp(UserDTO userDTO, Model model) {
    	 
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
    @RequestMapping(value = "/inputIDCheck",method = RequestMethod.POST)
	@ResponseBody
    public String[] inputCheck(Model model,UserDTO userDto){
		
        String[] returndata = userService.idCheck(userDto);
        
        return returndata;
    };
    
    // 닉네임 유일성 체크
    @RequestMapping(value = "/inputNicknameCheck",method = RequestMethod.POST)
	@ResponseBody
    public String[] inputNicknameCheck(Model model,UserDTO userDto){
		
        String[] returndata = userService.nicknameCheck(userDto);
        
        return returndata;
    };
    
    // 이메일 유일성 체크
    @RequestMapping(value = "/inputEmailCheck",method = RequestMethod.POST)
	@ResponseBody
    public String[] inputEmailCheck(Model model,UserDTO userDto){
		
		String[] returndata = userService.emailCheck(userDto);
		
        return returndata;
    };
    
    @PostMapping(value = "/star/changeInfo")
	public String changeInfo(UserDTO userDto, Model model) {
    	System.out.println("--------------");
    	System.out.println(model);
    	System.out.println(model.getAttribute("userDTO"));
    	
		return "star/changeInfo";
	}
    
    // 정보 변경 실행
    @PostMapping(value = "/changeInfo.do")
    public String changeInfo(Model model, UserDTO userDto){
		
//		String returndata;
		
		userService.changeInfo(userDto);
		
		model.addAttribute(userDto);
		
        return "/star/main";
    };
    
	// 마이 페이지 테스트
    @GetMapping(value = "/star/gayeong/mypage")
    public String mypage() {
    	return "star/gayeong/mypage";
    }
    
    //회원탈퇴
    @GetMapping(value = "/star/signdown")
    public String mypage2() {
    	
    	System.out.println("컨트롤러 확인");
    	
    	userService.pagedown();
    	
    	System.out.println("회원탈퇴 완료됨!");
    	
    	return "star/main";
    }
    
}
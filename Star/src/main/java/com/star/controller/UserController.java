package com.star.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	public String openUser(){ 
		
		return "star/main";
	}
	
//	메인 페이지
//	@PostMapping(value = "/star/mainpage")
//	public String openMain(UserDTO userDTO, RedirectAttributes rttr
//			, HttpServletRequest request) {
//		
//		rttr.addFlashAttribute(userDTO);
//		
//		HttpSession session = request.getSession();
//		System.out.println("세션 테스트");
//        System.out.println(session.getAttribute("loginUser"));
//        
//        System.out.println("세션 테스트끝!@");
//		
//		return "star/main";
//	}
	
	// ajax 중간 main
	@GetMapping(value = "/star/main2")
	public String openMap() {
		return "star/main2";
	}	
	
	// 메인페이지 ( with. 중간에 있는 테이블 )
	@GetMapping(value = "/star/main3")
	public String openPredictionList(DustDTO params, Model model) {
		
		// 임시로 일단 서울만 계속 보여주도록
		params.setRegion("서울");
		
		List<DustDTO> dustList = userService.getPrediction(params) ;
		model.addAttribute("dustList", dustList); 
		System.out.println(params);
		 
		System.out.println(dustList.get(0).getHumidity());

		return "star/main3"; 
	}

	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage() {
		return "star/sendmail";

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
    
    // 로그인 페이지 (logIn안에 값은 임시적으로 네비바 띄우기 위해서 적어놓음)
    @GetMapping(value = "/star/login")
    public String logIn(HttpServletRequest request) { 
    	System.out.println("로그인 페이지로 이동");
    	
    	HttpSession session = request.getSession();
    	if (session.getAttribute("userDTO") != null) {
    		System.out.println("이미 로그인중입니다.");
    		return "redirect:/star/mainpage";
    	};
    	return "star/login";
    };
    
    // 로그인 체크
    @PostMapping(value = "/star/login") 
    public String logInCheck(UserDTO userDTO
    		, HttpServletRequest request) {
    	
    	System.out.println(userDTO);
    	
		try {
			
			System.out.println("do action!");
	    	System.out.println(userDTO);
			 
	    	userDTO = userService.loginUser(userDTO);
	    	 
	    	// 로그인 여부 처리
			if (userDTO.getUserId() != null) {
				
                // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
                HttpSession session = request.getSession();
                // 세션에 로그인 회원 정보 보관
                session.setAttribute("userDTO", userDTO);

				System.out.println("로그인 성공!");
		        return "redirect:/star/mainpage";
			}else {
				System.out.println("로그인 실패1...");
				return "star/login";
			}
		} catch (DataAccessException e) {
			System.out.println("로그인 실패2...");
			return "star/login"; 
		} catch (Exception e) {
			System.out.println("로그인 실패3...");
			return "star/login";  
		}
    };
    
    // 회원가입 페이지
    @GetMapping(value = "/star/signup")
    public String singUp() {
    	return "star/signUp";
	}  

    // 계정찾기 페이지
    @GetMapping(value = "/star/findUser")
    public String findAccount() {
    	return "star/findUser";
    }

//    // 입력한 이메일로 메일 보내기
//	@PostMapping(value = "/mailSend")
//	@ResponseBody
//	public String[] dataSend(MailDTO mailDto){
//		
//		System.out.println("메일 전송 시작");
//		userService.sendSimpleMessage(mailDto);
//		System.out.println("메일 전송 완료");
//		String certifyNum = mailDto.getContent();
//		
//		System.out.println(certifyNum);
//		 
//        String[] returndata = {certifyNum};
//        return returndata;
//    };
    
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
    @GetMapping(value = "/star/changeInfo")
    public String changeInfoPage(HttpServletRequest request) {
    	
        HttpSession session = request.getSession();
        
        if (session.getAttribute("userDTO") == null) {
    		System.out.println("로그인을 진행해주세요.");
    		return "redirect:/star/login";
    	};
		return "star/changeInfo";
	}
    
    // 정보 변경 실행
    @PostMapping(value = "/changeInfo.do")
    public String changeInfo(UserDTO userDto){
		
		userService.changeInfo(userDto);
		
		return "redirect:/star/mainpage";
    };
    
    
    // 회원탈퇴
    @PostMapping(value = "/star/signdown")
    public String deleteUser(UserDTO userDto, HttpServletRequest request) {
    	
    	System.out.println(userDto);
    	
    	Long userNumber = userDto.getUserNumber();
    	userService.pagedown(userNumber);
    	
        HttpSession session = request.getSession();    	
        session.removeAttribute("userDTO");

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
    public String changePasswordPage(UserDTO userDto, Model model){
		
		model.addAttribute(userDto); 
		
		System.out.println(model.getAttribute("userDto"));
		
        return "/star/changePassword";
    };
    
    // 비밀번호 변경 실행
    @PostMapping(value = "/changePassword.do")
    public String changePassword(UserDTO userDTO){
		
		int result = userService.changePassword(userDTO);
		System.out.println(result);
		
		return "redirect:/star/login";

    };
    
//    // 유저 닉네임
//    @ResponseBody
//    @PostMapping(value = "/star/getUserNumber")
//    public long getusernumber(UserDTO userDTO){
//    	// System.out.println("userDTO : "+userDTO);
//		long result = userService.getUserNumber(userDTO.getUserNickname());
//		System.out.println("검색 한 닉네임의 유저 번호 : "+result);
//		return result;
//    };
        
    // 로그아웃
    @GetMapping(value = "/star/logout.do")
    public String doLogout(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        
        session.removeAttribute("userDTO");
    	
		return "redirect:/star/mainpage";
    };
    
    // 테스트 할 때 쓰는 용도
    @GetMapping(value = "/star/woobin")
    public String doTest(HttpServletRequest request) {
    	
    	HttpSession session = request.getSession();
    	
        session.removeAttribute("userDTO");
    	
        System.out.println("~~~~~~~~~~~~~~~~~");
        System.out.println(session.getAttribute("userDTO"));
        System.out.println("~~~~~~~~~~~~~~~~~");
    	System.out.println("회원탈퇴 완료됨!");
    	
    	return "redirect:/star/login";
    }
    
    
    
    
}
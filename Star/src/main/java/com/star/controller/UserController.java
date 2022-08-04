package com.star.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.star.domain.MailDTO;
import com.star.domain.UserDTO;
import com.star.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/star/main2.do")
	public String openMap(Model model) {
		return "star/main3";
	}
	
	// 메일 
	public UserController(UserService userService) {
        this.userService = userService;
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
    @GetMapping(value = "/star/login.do")
    public String logIn(Model model) { 
//    	UserDTO userdto = new UserDTO(); 
//    	model.addAttribute("Account",userdto );
    	return "star/login";
    } 
    
    @PostMapping(value = "/star/logInCheck")
    public String logInCheck(UserDTO userDTO, Model model) {

    	System.out.println("do action!"); 
//    	System.out.println(userDTO.toString());
    	
    	String temp_id = userDTO.getUserId();
    	
//    	여기에서 이제 service에 정의한 함수를 사용할거임
//    	그거로 db에 있는 id,password와 일치하면 메인페이지로 이동시킬거임
//    	근데 db에 접근해야 되니까 mapper.xml에도 관련 코드를 작성해줘야 됨
//    	근데 board 때는 컨트롤러가 2개 있었고 Uiutils 라는 컨트롤러에서 함수를 가져왔었네?
    	
    	
    	UserDTO userDto = userService.loginUser(userDTO);
    	System.out.println(userDto);

    	
    	
//    	if(userDto.getUserId() == temp) {
//    		return "star/main3";
//    	}else(userDto.getUserId() == userDto.getUserPassword()){
//    		return "star/sendmail"; 
//    	}
    	return "dd";
    	
    	
//    	if (isLogin == 1) {
//    		return "star/main3";
//    	}else if (isLogin ==0) {
//    		return "star/login";
//    	}else {
//    		return "star/sendmail";
//    	} 
    
    	
//		try {
//			boolean isRegistered = UserService.registerUser(params);
//			if (isRegistered == false) {
//				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
//			}
//		} catch (DataAccessException e) {
//			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
//
//		} catch (Exception e) {
//			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
//		}
//
//		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);

    }
   
    
    
    // 회원가입 페이지 (임시로 sendmail)
    @GetMapping(value = "/star/signin.do")
    public String singIn(Model model) {
    	return "star/sendmail";
    }
}

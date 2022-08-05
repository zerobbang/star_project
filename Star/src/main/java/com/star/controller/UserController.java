package com.star.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.star.domain.MailDTO;
import com.star.domain.UserDTO;
import com.star.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping(value = "/star/main.do")
	public String openUser(Model model) {
		return "star/main";
	} 


	
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
    @GetMapping(value = "/star/login")
    public String logIn(Model model) { 
//    	UserDTO userdto = new UserDTO(); 
//    	model.addAttribute("Account",userdto );
    	return "star/login";
    } 
    
    // 로그인 체크  /star/logInCheck
    @PostMapping(value = "/star/login") 
    public String logInCheck(UserDTO userDTO, Model model) {

//    	System.out.println(userDTO.toString());
  	
//    	여기에서 이제 service에 정의한 함수를 사용할거임
//    	그거로 db에 있는 id,password와 일치하면 메인페이지로 이동시킬거임
//    	근데 db에 접근해야 되니까 mapper.xml에도 관련 코드를 작성해줘야 됨
    	
		try {
			System.out.println("do action!"); 
	    	UserDTO userDto = userService.loginUser(userDTO);
	    	model.addAttribute("UserInfo",userDTO);
	    	
	    	System.out.println(userDto);
	    	System.out.println(userDto.toString());
	    	System.out.println(model);
			 
			if (userDto.getUserId() != null) {
				return "star/findUser";
			}else {
				return "star/login";
			}
		} catch (DataAccessException e) {
			return "star/login"; 
		} catch (Exception e) {
			return "star/login";  
		} 


//    	if(userDto.getUserId() != null) {
//    		return "star/findUser"; 
//    	}
//    	else if(userDto.getUserId() == null){
//    		return "star/login"; 
//    	}
//    	else { 
//    		return "star/sendmail";
//    	} 
    	
    }
     
    // 회원가입 페이지 (임시로 sendmail)
    @GetMapping(value = "/star/signin.do")
    public String singIn(Model model) {
    	return "star/sendmail";
	}  
    
    @GetMapping(value = "/star/findUser.do")
    public String findAccount() {
    	return "star/findUser";
    }
    
    
//    @PostMapping(value="/dataSend")
//    public String dataSend(Model model,MailDto dto){
//    	System.out.println("메일 전송 시작");
//    	userService.sendSimpleMessage(dto);
//        System.out.println("메일 전송 완료");
//        System.out.println(dto.getContent());
//        model.addAttribute("msg",dto.getAddress()+"입니다.");
//        model.addAttribute("emailNumber", dto.getContent());
//        System.out.println(model.getAttribute("emailNumber"));
//        
//        return "/star/findUser :: #resultDiv";
//    }	<<-- 필요없다고 판단되면 지울것.

	
	@RequestMapping(value = "/dataSend",method = RequestMethod.POST)
	@ResponseBody
    public String[] dataSend(Model model,MailDTO mailDto){
		
		System.out.println("메일 전송 시작");
		userService.sendSimpleMessage(mailDto);
		System.out.println("메일 전송 완료");
		String certifyNum = mailDto.getContent();
		System.out.println("testing");
		System.out.println(certifyNum);
		
        String firstdata = "<div id=resultDiv>메일을 확인해주세요!</div>"; 
        String[] returndata = {firstdata, certifyNum};
        return returndata;
    };
	
}

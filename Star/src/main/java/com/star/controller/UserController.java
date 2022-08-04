package com.star.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.star.domain.MailDTO;
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
	


	
//	// 메일 
//	public UserController(UserService userService) {
//        this.userService = userService;
//    }



	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage(Model model) {
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

package com.star.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



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
	
	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage(Model model) {
		return "star/sendmail";
	}  

    
    @GetMapping(value = "/star/findUser.do")
    public String findAccount() {
    	return "star/findUser";
    }
    
    
    @PostMapping(value="/dataSend")
    public String dataSend(Model model,MailDTO dto ){
    	userService.sendSimpleMessage(dto);
        System.out.println("메일 전송 완료");
        System.out.println(dto.getContent());
        model.addAttribute("msg",dto.getAddress()+"입니다.");
        model.addAttribute("emailNumber", dto.getContent());
        System.out.println(model.getAttribute("emailNumber"));
        
        return "/star/findUser :: #resultDiv";
    }
    
   


}

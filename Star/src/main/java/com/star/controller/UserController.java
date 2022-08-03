package com.star.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PostMapping;

=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
>>>>>>> f2fd8d5784b92edf21dc42ae237ceb303564cfaa

import com.star.domain.MailDto;
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
	
<<<<<<< HEAD
=======

	
	// 메일 
	public UserController(UserService userService) {
        this.userService = userService;
    }


>>>>>>> f2fd8d5784b92edf21dc42ae237ceb303564cfaa
	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage(Model model) {
		return "star/sendmail";
	}  
 
<<<<<<< HEAD
    
    @GetMapping(value = "/star/findUser.do")
    public String findAccount() {
    	return "star/findUser";
    }
    
    
    @PostMapping(value="/dataSend")
    public String dataSend(Model model,MailDto dto ){
    	userService.sendSimpleMessage(dto);
        System.out.println("메일 전송 완료");
        System.out.println(dto.getContent());
        model.addAttribute("msg",dto.getAddress()+"입니다.");
        model.addAttribute("emailNumber", dto.getContent());
        System.out.println(model.getAttribute("emailNumber"));
        
        return "/star/findUser :: #resultDiv";
    }
    
   
=======
	
//	@PostMapping("/mail/send")
//    @ResponseBody
//    public String sendMail(MailDto mailDto) {
//		
//		userService.sendSimpleMessage(mailDto); System.out.println("메일 전송 완료");
//		return "star/sendmail"; 
//	}
	
	@RequestMapping(value = "/dataSend",method = RequestMethod.POST)
    public String dataSend(Model model,MailDto mailDto){
		
		userService.sendSimpleMessage(mailDto);
		System.out.println("메일 전송 완료");
        model.addAttribute("msg",mailDto.getAddress());
        return "/star/sendmail :: #resultDiv";
    };
	
>>>>>>> f2fd8d5784b92edf21dc42ae237ceb303564cfaa

}

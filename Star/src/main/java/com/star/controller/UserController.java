package com.star.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.ResponseBody;
=======
import org.springframework.web.bind.annotation.PostMapping;
>>>>>>> 761b2fe9b6dab4f763cb40e9b02f5962cdf60406

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
	
<<<<<<< HEAD
=======
	public UserController(UserService userService) {
        this.userService = userService;
    }
	
	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage(Model model) {
		return "star/sendmail";
	}
 
    @PostMapping("/mail/send")
    public String sendMail(MailDto mailDto) {
        userService.sendSimpleMessage(mailDto);
        System.out.println("메일 전송 완료");
        return "star/sendmail";
    }
	
>>>>>>> 761b2fe9b6dab4f763cb40e9b02f5962cdf60406
}

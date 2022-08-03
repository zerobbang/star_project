package com.star.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
<<<<<<< HEAD

=======
 
>>>>>>> 6a87356ae86685edcc6c06a5dfe0b5d084810982

import com.star.domain.MailDto;
import com.star.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/star/main2.do")
	public String openMap(Model model) {
		return "star/main3";
	}
	
<<<<<<< HEAD
	
	// 메일 
	public UserController(UserService userService) {
        this.userService = userService;
    }
=======
//	필요없어보여서 삭제
//	public UserController(UserService userService) {
//        this.userService = userService;
//    }
>>>>>>> 6a87356ae86685edcc6c06a5dfe0b5d084810982
	
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
<<<<<<< HEAD
	
=======

>>>>>>> 6a87356ae86685edcc6c06a5dfe0b5d084810982
}

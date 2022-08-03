package com.star.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

//	필요없어보여서 삭제
//	public UserController(UserService userService) {
//        this.userService = userService;
//    }

	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage(Model model) {
		return "star/sendmail";
	} 
 
	
	@PostMapping("/mail/send")
    @ResponseBody
	/*public String sendMail(MailDto mailDto) {
		MailDto mailDto = new MailDto();*/
    public String sendMail(MailDto mailDto) {
		
		userService.sendSimpleMessage(mailDto); System.out.println("메일 전송 완료");
		return "star/sendmail"; 
	}
	
	@RequestMapping(value = "/dataSend",method = RequestMethod.POST)
    public String dataSend(Model model,MailDto mailDto){
		
		userService.sendSimpleMessage(mailDto);
		System.out.println("메일 전송 완료");
        model.addAttribute("msg",mailDto.getAddress());
        return "/star/sendmail :: #resultDiv";
    };
	
}

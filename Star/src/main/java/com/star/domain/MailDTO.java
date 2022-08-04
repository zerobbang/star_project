package com.star.domain;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
<<<<<<< HEAD
=======

//@NoArgsConstructor

>>>>>>> 0f164a7917499bccd9a929571bd595cf6de04458
public class MailDTO {
    private String address;
    private String title;
    private String content;
    
<<<<<<< HEAD
    

//    public MailDto() {
//    	Random random = new Random();
//		int rdNum = random.nextInt(10);
//		String certifyNum = Integer.toString(rdNum);
//
//    	this.emailNum = certifyNum;
//    	this.title = "인증번호입니다.";
//    	this.content = certifyNum;
//    	
//    }

    
=======

    public MailDTO() {
    	Random random = new Random();
		int rdNum = random.nextInt(10);
		String certifyNum = Integer.toString(rdNum);
    	
    	this.title = "인증번호입니다.";
    	this.content = certifyNum;
    	
    }

>>>>>>> 0f164a7917499bccd9a929571bd595cf6de04458
}
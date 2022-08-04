package com.star.domain;

<<<<<<< HEAD
import java.util.Random;

import lombok.Getter;
import lombok.NoArgsConstructor;
//import lombok.NoArgsConstructor;
=======
import lombok.Getter;
>>>>>>> 1941fcd9176c3abbe2cc4f5aa7d53e95e95b1088
import lombok.Setter;

@Getter
@Setter
<<<<<<< HEAD
//@NoArgsConstructor
=======
>>>>>>> 1941fcd9176c3abbe2cc4f5aa7d53e95e95b1088
public class MailDTO {
    private String address;
    private String title;
    private String content;
    
<<<<<<< HEAD
    
    public MailDTO() {
    	Random random = new Random();
		int rdNum = random.nextInt(10);
		String certifyNum = Integer.toString(rdNum);
    	
    	this.title = "인증번호입니다.";
    	this.content = certifyNum;
    	
    }
    
=======
>>>>>>> 1941fcd9176c3abbe2cc4f5aa7d53e95e95b1088
}
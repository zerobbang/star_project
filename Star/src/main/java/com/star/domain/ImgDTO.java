package com.star.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImgDTO {
	
	// 이미지 테이블
	private int imgNumber;
	
	private long bno;
	
	private String imgPath;
	
	private boolean imgShowYn;
	
	private String imgName;

	@Override
	public String toString() {
		return "ImgDTO [imgNumber=" + imgNumber + ", bno=" + bno + ", imgPath=" + imgPath + ", imgShowYn=" + imgShowYn
				+ ", imgName=" + imgName + "]";
	}
	
	

}

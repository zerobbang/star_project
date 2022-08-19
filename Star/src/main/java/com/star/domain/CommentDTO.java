package com.star.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
	
	// 댓글 테이블
	private int cmtNum;
	
	private int bno;		// 게시글 테이블의 bno와 연결 
	
	private int userNumber;		// 유저 테이블의 유저 넘버와 연결
	
	private String cmtContent;
	
	private LocalDateTime writeDate;
	
	private LocalDateTime cmtUpdate;

	private boolean cmtDeleteYn;

	@Override
	public String toString() {
		return "CommentDTO [cmtNum=" + cmtNum + ", bno=" + bno + ", userNumber=" + userNumber + ", cmtContent="
				+ cmtContent + ", writeDate=" + writeDate + ", cmtUpdate=" + cmtUpdate + ", cmtDeleteYn=" + cmtDeleteYn
				+ "]";
	}	
	
}

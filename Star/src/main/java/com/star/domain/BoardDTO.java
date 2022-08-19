package com.star.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO  {
	
	// 카테고리 테이블
	private String category;
	
	// 게시글 테이블
	private Long bno;
	
//	privat String category;		// 게시글 테이블의 카테고리도 카테고리 연결
	
	private Long userNumber;		// 유저 테이블의 유저 넘버와 연결
	
	private String userNickname;

//	private String writerNickname;	// 미리보기에서 필요 할 수 있어 첨부함. - wb
	
	private String title;
	
	private String content;
	
	private LocalDateTime regDate;
	
	private LocalDateTime updateDate;
	
	private boolean deleteYn;
	
	// 신고 테이블
//	private int bno;		// 게시글 테이블의 bno와 연결 *게시글 테이블 정보를 끌어올 생각이기에 삭제해도 될듯(외래)
	
//	private int userNumber;		// 유저 테이블의 유저 넘버와 연결 *게시글 테이블 정보를 끌어올 생각이기에 삭제해도 될듯(외래)
	
	private String reportContent;
	
	private boolean manageYn;
	
	// 댓글 테이블
//	private int cmtNum;
//	
////	private int bno;		// 게시글 테이블의 bno와 연결 
//	
////	private int userNumber;		// 유저 테이블의 유저 넘버와 연결
//	
//	private String cmtContent;
//	
//	private LocalDateTime writeDate;
//	
//	private LocalDateTime cmtUpdate;
//	
//	private boolean cmtDeleteYn;
//	
	private String imgName;

	@Override
	public String toString() {
		return "BoardDTO [category=" + category + ", bno=" + bno + ", userNumber=" + userNumber + ", userNickname="
				+ userNickname + ", title=" + title + ", content=" + content + ", regDate=" + regDate + ", updateDate="
				+ updateDate + ", deleteYn=" + deleteYn + ", reportContent=" + reportContent + ", manageYn=" + manageYn
				+ ", imgName=" + imgName + "]";
	}


}

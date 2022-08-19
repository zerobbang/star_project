package com.star.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {
	
	// 카테고리
	private String category;
	
	// 유저 넘버
	// private long userNumber;
	
	/* 현제 페이지 */
	private int pageNum;
	
	/* 한 페이지 당 보여질 게시물 갯수 */
    private int amount;
    
    /* 스킵 할 게시물 수( (pageNum-1) * amount ) */
    private int skip;
    
	/* 검색 키워드 */
    private String keyword;
    
	/* 검색 타입 */
    private String searchType;
    
	/* 회원 번호 */
	private int bno;
    
    /* 기본 생성자 -> 기봅 세팅 : pageNum = 1, amount = 10 */
    public Criteria() {
        this(1,10);
        this.skip = 0;
    }
    
    /* 생성자 => 원하는 pageNum, 원하는 amount */
    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
        this.skip = (pageNum-1)*amount;
    }
    
    public void setPageNum(int pageNum) {
    	this.skip = (pageNum - 1)*this.amount;
    	this.pageNum = pageNum;
    }

    public void setAmount(int amount) {
    	this.skip = (this.pageNum - 1)*amount;
    	this.amount = amount;
    }

	@Override
	public String toString() {
		return "Criteria [category=" + category + ", pageNum=" + pageNum + ", amount=" + amount + ", skip=" + skip
				+ ", keyword=" + keyword + ", searchType=" + searchType + ", bno=" + bno + "]";
	}



}

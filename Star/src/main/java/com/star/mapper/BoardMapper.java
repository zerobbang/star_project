package com.star.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.BoardDTO;

@Mapper
public interface BoardMapper {
	
	// 조회 테스트용 함수들
	public BoardDTO detailBoard(BoardDTO boardDto);
	
	public BoardDTO detailReport(BoardDTO boardDto);
	
	public BoardDTO detailComment(BoardDTO boardDto);
	
	public BoardDTO detailImg(BoardDTO boardDto);
	
	public BoardDTO detailCategory(BoardDTO boardDto);
	
	
	// 진짜 사용하는 함수들
	public int insertBoard(BoardDTO params);

	
	
}

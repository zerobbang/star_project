package com.star.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.BoardDTO;

@Mapper
public interface BoardMapper {
	
	public BoardDTO detailBoard(BoardDTO boardDto);
	
	public BoardDTO detailReport(BoardDTO boardDto);
	
	public BoardDTO detailComment(BoardDTO boardDto);
	
	public BoardDTO detailImg(BoardDTO boardDto);
	
	public BoardDTO detailCategory(BoardDTO boardDto);

	
	
}

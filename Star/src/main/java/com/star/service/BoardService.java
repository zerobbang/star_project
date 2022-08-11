package com.star.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.star.domain.BoardDTO;

@Service
public interface BoardService {
	
	// 게시글 리스트 조회
	public List<BoardDTO> getBoardList(BoardDTO params);
	
	// 게시글 등록
	public boolean registerBoard(BoardDTO params);
	
	// 게시글 상세 조회
	public BoardDTO getBoardDetail(Long bno);

	// 상세글 조회 신고하기	
//	public void report();
	


}


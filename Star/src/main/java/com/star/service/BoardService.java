package com.star.service;

import org.springframework.stereotype.Service;

import com.star.domain.BoardDTO;

@Service
public interface BoardService {
	
	// 게시글 등록
	public boolean registerBoard(BoardDTO params);
	

}

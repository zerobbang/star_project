package com.star.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.star.domain.BoardDTO;
import com.star.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;

	@Override
	public boolean registerBoard(BoardDTO params) {
		int queryResult = 0;
		
		queryResult = boardMapper.insertBoard(params);
		
//		// 글 번호가 널값이면 새로 글을 생성
//		if(params.getBno() == null) {
//			queryResult = boardMapper.insertBoard(params);
//		}else {
//			// 글 번호 존재하면 수정 페이지로 이동 -> 임시로 main 페이지로 이동
//		}
		
		return (queryResult == 1) ? true:false ;
	}
	

}

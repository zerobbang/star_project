package com.star.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.star.domain.BoardDTO;
import com.star.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;

	// 게시글 쓰기
	@Override
	public boolean registerBoard(BoardDTO params) {
		int queryResult = 0;
		
		queryResult = boardMapper.insertBoard(params);
		System.out.println(queryResult);
		
		// 글 번호가 널값이면 새로 글을 생성
		if(params.getBno() == null) {
			queryResult = boardMapper.insertBoard(params);
		}else {
//			queryResult = boardMapper.insertBoard(params);
		}
		
		return (queryResult == 1) ? true:false ;
	}

	// 게시글 상세 조회
	@Override
	public BoardDTO getBoardDetail(Long bno) {
		return boardMapper.selectDetail(bno);
	}

	// 게시글 리스트 조회
	@Override
	public List<BoardDTO> getBoardList(BoardDTO params) {
		// 데이터 타입 BoardDTO로 빈 리스트 생성
		List<BoardDTO> boardList = Collections.emptyList();
		
		// 페이징 기능 아직 미구현
		boardList = boardMapper.selectList(params);
		return boardList;
	}

//	신고하기 8월 11일에 마무리 짓게
//	@Override
//	public void report() {
//		// TODO Auto-generated method stub
//		
//		System.out.println("보드 impl 확인");
//		
//	}


}

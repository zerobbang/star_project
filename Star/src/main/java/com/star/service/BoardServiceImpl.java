package com.star.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.star.domain.BoardDTO;
import com.star.mapper.BoardMapper;
import com.star.paging.Criteria;

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
		
//		// 글 번호가 널값이면 새로 글을 생성
//		if(params.getBno() == null) {
//			queryResult = boardMapper.insertBoard(params);
//		}else {
////			queryResult = boardMapper.insertBoard(params);
//		}
		
		return (queryResult == 1) ? true:false ;
	}

	// 게시글 상세 조회
	@Override
	public BoardDTO getBoardDetail(Long bno) {
		return boardMapper.selectDetail(bno);
	}
	
	

//	신고하기
	@Override
	public void report(BoardDTO boardDTO) {
		// TODO Auto-generated method stub
		
		System.out.println("보드 impl 확인");
		
		System.out.println(boardDTO);
		
		boardMapper.report(boardDTO);
		
		System.out.println("서비스 끝!");
		 	
	}
	
	// 카테고리별 게시글 리스트 조회	
	@Override
	public List<BoardDTO> getBoardList(Criteria cri) {
		// 데이터 타입 BoardDTO로 빈 리스트 생성 > 조회된 결과 값을 받기 위해 준비
		List<BoardDTO> boardList = Collections.emptyList();
		
		boardList = boardMapper.selectList(cri);
		
		return boardList;
	}

	// 내 글 조회
	@Override
	public List<BoardDTO> getMyListBoard(Criteria cri) {
		List<BoardDTO> myList = Collections.emptyList();
		
		myList = boardMapper.getMyListBoard(cri);
		
		return myList;
	}

	// 카테고리별 총 게시글 개수
	@Override
	public int getCount(String category) {
		return boardMapper.getCount(category);
	}

	// 내 글 총 수
	@Override
	public int getMyCount(Long userNumber) {
		return boardMapper.getMyCount(userNumber);
	}
	
}

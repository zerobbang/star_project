package com.star.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.star.domain.BoardDTO;
import com.star.domain.ImgDTO;
import com.star.paging.Criteria;

@Service
public interface BoardService {
	
	// 게시글 리스트 조회
	public List<BoardDTO> getBoardList(Criteria cri);
	
	// 카테고리별 게시글 총 개수
	public int getCount(Criteria cri);
	
	// 게시글 등록
	public boolean registerBoard(BoardDTO params, List<MultipartFile> file) throws Exception;
	
	// 게시글 상세 조회
	public BoardDTO getBoardDetail(Long bno);

	public void report(BoardDTO boardDTO);
	
	// 내 글 조회
	public List<BoardDTO> getMyListBoard(Criteria cri);
	
	// 내 글 총 수
	public int getMyCount(Criteria cri);

	public String getWriter(Long writerNumber);

	public List<ImgDTO> getImgsFromBno(Long boardBno);

	public void deleteBoard(BoardDTO boardDto);

}


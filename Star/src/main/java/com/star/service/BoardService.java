package com.star.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.star.domain.BoardDTO;
import com.star.domain.CommentDTO;
// import com.star.domain.CommentDTO; 
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
	public List<BoardDTO> getMyListBoard(Criteria cri,Long userNumber);
	
	// 댓글 조회
	public List<CommentDTO> getCommentList(Long bno); 
	
	// 내 글 총 수
	public int getMyCount(Long userNumber);
	
	// 댓글 수
	public int getCommentCount(Criteria cri);

	public String getWriter(Long writerNumber);

	public List<ImgDTO> getImgsFromBno(Long boardBno);

	public void deleteBoard(BoardDTO boardDto);
	
	// 게시글 수정
	public int updateBoard(BoardDTO boardDTO);
	
	// 이미지 수정
	public int updateImg(Map map);
	
	// 이미지 추가
	public boolean addImgList(List<MultipartFile> file, Long bno) throws Exception;

	public List<BoardDTO> getReportBoard();

	public void managingComplete(BoardDTO boardDto);

}


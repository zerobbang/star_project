package com.star.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.BoardDTO;
import com.star.domain.CommentDTO;
import com.star.domain.ImgDTO;
import com.star.paging.Criteria;

@Mapper
public interface BoardMapper {
	
	// 조회 테스트용 함수들
	public BoardDTO detailBoard(BoardDTO boardDto);
	
	public BoardDTO detailReport(BoardDTO boardDto);
	
	public BoardDTO detailComment(BoardDTO boardDto);
	
	public BoardDTO detailImg(BoardDTO boardDto);
	
	public BoardDTO detailCategory(BoardDTO boardDto);
	
	
	// 진짜 사용하는 함수들

	// 글 데이터 삽입
	public int insertBoard(BoardDTO params);
	
	// 글 데이터 상세 조회 -> 글 번호 값을 가져와서 처리한다.
	public BoardDTO selectDetail(Long bno);
	
	// 게시글 전체 조회
	public List<BoardDTO> selectList(Criteria cri);
	
	// 카테고리별 게시글 총 개수
	public int getCount(Criteria cri);
	
	// 상세글 조회 신고하기	
	public void report(BoardDTO boardDTO);
	
	// 내 글 조회
	public List<BoardDTO> getMyListBoard(Map map);
	
	// 댓글 조회
	public List<CommentDTO> getCommentList(Long bno);
	
	// 내가 쓴 글 게시글 총 수
	public int getMyCount(Long userNumber);
	
	// 댓글 총 수
	public int getCommentCount(Criteria cri);
	
	// 이미지 저장
	public int insertImg(ImgDTO imgDTO);
	
	// 최신 글 1개 불러오기
	public BoardDTO getLastBoard();

	public String findWriterFromUserNumber(Long writerNumber);

	public List<ImgDTO> findImgsFromBno(Long boardBno);

	public void deleteBoardFromBno(Long boardBno);

	// 게시글 수정 
	public int updateBoard(BoardDTO boardDTO);
	
}

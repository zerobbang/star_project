package com.star.service;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.star.domain.BoardDTO;
import com.star.domain.ImgDTO;
import com.star.mapper.BoardMapper;
import com.star.paging.Criteria;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;

	// 게시글 쓰기
	@Override
	public boolean registerBoard(BoardDTO params, List<MultipartFile> fileList) throws Exception {
		
		int queryResult = boardMapper.insertBoard(params);
		
		if(queryResult == 1) {
			System.out.println("글 정상적으로 저장 성공."+queryResult);
		}else {
			System.out.println("글 정상적으로 저장 실패."+queryResult);
		}
		
			
		Iterator<MultipartFile> files = fileList.iterator();
		
		while(files.hasNext()) {
			MultipartFile file = files.next();

			// 이미지 저장
			String filePath = System.getProperty("user.dir")+"/src/main/resources/static/imgfiles";
			// 이미지 고유 번호 생성
			UUID uuid = UUID.randomUUID();
			// 이미지 이름 = UUID_파일원래이름
			String fileName = uuid+"_"+ file.getOriginalFilename();
			// 이미지 저장한다.
			File saveFile = new File(filePath,fileName);
			file.transferTo(saveFile);
			
			BoardDTO newBoard = boardMapper.getLastBoard();
			System.out.println("저장한 게시글의 글 번호 : "+newBoard.getBno());
			System.out.println("저장한 게시글의 정보 : "+newBoard);
			
			ImgDTO imgDTO = new ImgDTO();
			
			imgDTO.setImgName(fileName);
			imgDTO.setImgPath(filePath);
			imgDTO.setBno(newBoard.getBno());
			
			int imgResult = boardMapper.insertImg(imgDTO);
			
			if(imgResult == 1) {
				System.out.println("글 이미지 정상적으로 저장 성공."+imgResult);
			}else {
				System.out.println("글 이미지 정상적으로 저장 실패."+imgResult);
			}
			
		}	
		
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
		
		try {
			boardMapper.report(boardDTO);
		} catch (Exception e) {
			// TODO: handle exception
			
			System.out.println("신고하는중에 에러 발생");
		}
		 	
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
	public int getCount(Criteria cri) {
		return boardMapper.getCount(cri);
	}

	// 내 글 총 수
	@Override
	public int getMyCount(Criteria cri) {
		return boardMapper.getMyCount(cri);
	}
	
	public String getWriter(Long writerNumber) {
		
		String resultData = boardMapper.findWriterFromUserNumber(writerNumber);
		System.out.println("----------------------");
		System.out.println(resultData);
		System.out.println("----------------------");
		
		return resultData;
	};
	
	
	public List<ImgDTO> getImgsFromBno(Long boardBno) {
		
		List<ImgDTO> resultData = boardMapper.findImgsFromBno(boardBno);
		return resultData;
	}

	@Override
	public void deleteBoard(BoardDTO boardDto) {
		// TODO Auto-generated method stub
		
		Long boardBno = boardDto.getBno();
		boardMapper.deleteBoardFromBno(boardBno);
		
	};
	
}

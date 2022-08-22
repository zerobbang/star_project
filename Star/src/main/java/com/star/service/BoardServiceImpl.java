package com.star.service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.star.domain.BoardDTO;
import com.star.domain.CommentDTO;
// import com.star.domain.CommentDTO;
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
		
		// 사진 첨부가 없으면
		if(fileList == null ) {
			// 글 저장 종료
			return (queryResult == 1) ? true:false ;
		}else {
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
		return (queryResult == 1) ? true:false ;
		}
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
	public List<BoardDTO> getMyListBoard(Criteria cri,Long userNumber) {
		
		List<BoardDTO> myList = Collections.emptyList();
		
//		Map<Criteria, Long> map = new HashMap<Criteria,Long>();
		Map map = new HashMap<>();
		
		System.out.println("서비스 hashMap Before : "+map);
		
//		map.put(cri,userNumber);
		int skip = cri.getSkip();
		int amount = cri.getAmount();
		
		map.put("skip", skip);
		map.put("amount", amount);
		map.put("userNumber", userNumber);
		
		System.out.println("서비스 hashMap After : "+map);
		
		// myList = boardMapper.getMyListBoard(cri);
		myList = boardMapper.getMyListBoard(map);
		
		return myList;
	}
	
	// 댓글 조회
	@Override
	public List<CommentDTO> getCommentList(Long bno) {
		List<CommentDTO> commentList = Collections.emptyList();
		
		commentList = boardMapper.getCommentList(bno);
		
		return commentList;
	}

	// 카테고리별 총 게시글 개수
	@Override
	public int getCount(Criteria cri) {
		return boardMapper.getCount(cri);
	}

	// 내 글 총 수
	@Override
	public int getMyCount(Long userNumber) {
		return boardMapper.getMyCount(userNumber);
	}
	
	// 내 글 총 수
	@Override
	public int getCommentCount(Criteria cri) {
		return boardMapper.getCommentCount(cri);
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
		
	}

	// 게시글 수정
	@Override
	public int updateBoard(BoardDTO boardDTO) {
		return boardMapper.updateBoard(boardDTO);
	}


	// 이미지 수정
	@Override
	public int updateImg(Map map) {
		
		System.out.println(map);
		return boardMapper.updateImg(map);
	}

	// 이미지 추가
	@Override
	public boolean addImgList(List<MultipartFile> fileList, Long bno) throws Exception {
		int queryResult = 0;
		
		// 사진 첨부가 없으면
		if(fileList == null ) {
			// 글 저장 종료
			queryResult = 0;
		}else {
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

				System.out.println("저장한 게시글의 정보 : "+bno);
				
				ImgDTO imgDTO = new ImgDTO();
				
				imgDTO.setImgName(fileName);
				imgDTO.setImgPath(filePath);
				imgDTO.setBno(bno);
				
				int imgResult = boardMapper.insertImg(imgDTO);
				
				if(imgResult == 1) {
					System.out.println("글 이미지 정상적으로 저장 성공."+imgResult);
				}else {
					System.out.println("글 이미지 정상적으로 저장 실패."+imgResult);
				}
				
			}		
			queryResult = 1;;
			}
			return (queryResult == 1) ? true:false ;
	}

	@Override
	public List<BoardDTO> getReportBoard() {
		// TODO Auto-generated method stub
		List<BoardDTO> boardDto = boardMapper.getReportBoardList();
		
		return boardDto;
	}
	
	
	@Override
	public void managingComplete(BoardDTO boardDto) {
		// TODO Auto-generated method stub
		
		Long boardBno = boardDto.getBno();
		
		boardMapper.managingComplete(boardBno);
		
	}
	
}

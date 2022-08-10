package com.star.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.star.domain.BoardDTO;
import com.star.domain.UserDTO;
import com.star.service.BoardService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	
	// 게시글 조회
	@GetMapping(value="/board/list")
	public String listBoard(@RequestParam(value="params") BoardDTO params, Model model) {
		List<BoardDTO> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);
		return "board/boardLlist";
	}
	
	
	
	// 게시글 쓰기 화면으로 이동
	@GetMapping(value="/board/write")
//	public String writeBoard(@RequestParam(value = "bno", required = false) final Long bno ,Model model) {
	public String writeBoard(Model model, UserDTO userDto) {
//		// 글 번호를 뷰에서 받아오는데
//		if(bno==null) {
//			// 새로 생성하는 글인 경우 새로운 보드 DTO 객체 생성
//			model.addAttribute("board", new BoardDTO());
//		} else {
//			// 기존에 존재하는 글인 경우 해당 글의 글 번호 bno를 넘겨받아 상세 조회
//			BoardDTO boardDTO = boardService.getBoardDetail(bno);
//			// 만약 글 번호를 통해 넘겨받은 boardDTO가 없다면? -?> 해야하나? > 나중
//			
//			// service를 통해 실행된 mapper 결과값을 "board" 속성?에 저장
//			model.addAttribute("board", boardDTO);
//		}
		// String writer = userDto.getUserNickname();
		String writer = "코신황";
		System.out.println("유저 닉네임"+writer);
		model.addAttribute("writer", writer);
		
		return "/board/boardWrite";
	}
	
	// 게시글 쓰기가 완료 되면
	@PostMapping(value="/board/registerBoard")
	public String registerBoard() {
		return "" ;
	}
	
    // 상세글조회페이지
    @GetMapping(value = "/star/detailed_check")
    public String detailedCheck() {
    	return "star/detailed_check";
    }
	
    // 신고하기
//    @GetMapping(value = "/star/report")
//    public String report() {
//    	
//    	System.out.println("확인");
//    	
//    	boardService.report();    	
//    	
//    	System.out.println("신고완료확인");
//    	
//    	return "star/detailed_check";
//    }
	
	
	
}


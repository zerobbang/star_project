package com.star.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.star.domain.BoardDTO;
import com.star.domain.UserDTO;
import com.star.service.BoardService;
import com.star.service.UserService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private UserService userService;
	
	
	// 게시글 조회
	@GetMapping(value="/board/list")
	public String listBoard(@RequestParam(value="category") String category, Model model, UserDTO userDto) {
		System.out.println("list list list : "+category);
//		System.out.println(userDto);
		model.addAttribute("selectCategory", category);
		
		List<BoardDTO> boardList = boardService.getBoardList(category);
		model.addAttribute("boardList", boardList);
		
		// 로그인 한 유저 정보도 함께
//		Long userNumber = userDto.getUserNumber();
//		UserDTO userDTO = userService.getUser(userNumber);
//		model.addAttribute("userDTO", userDTO);
		
		// 각 게시글의 유저 넘버로 조회해서 해당 닉네임을 가져오고 list에 저장한다.
		List<String> userNickname = new ArrayList<>();
		
		Iterator<BoardDTO> list = boardList.iterator();

		while(list.hasNext()) {
			BoardDTO boardOne = list.next();
			Long userNum = (long) boardOne.getUserNumber();
			userNickname.add(userService.getNickname(userNum));
//			System.out.println(boardOne.getUserNumber());
//			System.out.println(userNickname);
		}
		
		model.addAttribute("userNickname", userNickname);
//		System.out.println("zzzzzzzzzzzzzzzzzzz"+boardList);
//		System.out.println(userNickname);
		return "board/list";
	}
	
	
	
	// 게시글 쓰기 화면으로 이동
	@PostMapping(value="/board/write")
//	public String writeBoard(@RequestParam(value = "bno", required = false) final Long bno ,Model model) {
	public String writeBoard(Model model, UserDTO userDto) {
//	public String writeBoard(@ModelAttribute("params") final UserDTO params, Model model) {
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
		

		System.out.println("글쓰기 페이지 들어가기 전 userDto : "+userDto);
		Long userNumber = userDto.getUserNumber();
		System.out.println(userNumber);
		
		// 해당 user 정보 다 넘겨주기
		UserDTO userDTO = userService.getUser(userNumber);
		// String userNickname = userDTO.getUserNickname();
		model.addAttribute("userDTO", userDTO);
		
		return "/board/write";
	}
	
	// 게시글 쓰기가 완료 되면
	@PostMapping(value="/board/registerBoard")
	public String registerBoard(BoardDTO params) {
		System.out.println(params.toString());
		boardService.registerBoard(params);
		return "/board/list" ;
	}
	

	// 게시글 쓰기 화면으로 이동
	@GetMapping(value="/star/test")
	public String testBoard(Model model, UserDTO userDto) {
		
		userDto.setUserRegion("전국");
		
		model.addAttribute("region", userDto.getUserRegion());
		
		return "/star/test";
	}

    // 상세글조회 페이지
    @GetMapping(value = "/star/detailed_check")
    public String detailedCheck() {
    	return "/star/detailed_check";
    }
    
    // 신고사유 페이지
    @GetMapping(value = "/star/userReport")
    public String reportpage() {
    	return "/star/userReport";
    }
	
    // 신고하기
    @PostMapping(value = "/star/report")
    @ResponseBody
    public String report(BoardDTO boardDTO) {
    	
    	System.out.println("확인33");
    	System.out.println(boardDTO);
    	
    	boardService.report(boardDTO); // 문제발생
    	
    	System.out.println("컨트롤러 끝!");
//    	
//    	System.out.println("신고완료확인");
    	
//    	return "star/detailed_check";
    	return "good";
    }
    	
}


package com.star.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	/*
	 * @RequestMapping(value="/board/list") public String
	 * listBoard(@RequestParam(value="category") String category, Model model,
	 * UserDTO userDto, RedirectAttributes rttr) {
	 * System.out.println("선택한 게시글 카테고리 : "+category);
	 * //model.addAttribute("selectCategory", category);
	 * model.addAttribute("category", category);
	 * 
	 * // 유저 넘버만 존재 rttr.addFlashAttribute(userDto);
	 * 
	 * List<BoardDTO> boardList = boardService.getBoardList(category);
	 * model.addAttribute("boardList", boardList);
	 * 
	 * // 각 게시글의 유저 넘버로 조회해서 해당 닉네임을 가져오고 list에 저장한다. List<String> nicknameList =
	 * new ArrayList<>();
	 * 
	 * Iterator<BoardDTO> list = boardList.iterator();
	 * 
	 * while(list.hasNext()) { BoardDTO boardOne = list.next(); Long userNum =
	 * (long) boardOne.getUserNumber();
	 * nicknameList.add(userService.getNickname(userNum)); }
	 * 
	 * model.addAttribute("nicknameList", nicknameList); return "board/list"; }
	 */

	@RequestMapping(value="/board/list")
	public String listBoard(BoardDTO boardDto, Model model, RedirectAttributes rttr) {
		System.out.println("선택한 게시글 : "+boardDto);
		//rttr.addFlashAttribute("boardDTO", boardDto);
		// System.out.println("다시"+rttr.getFlashAttributes());
		
		// 선택된 카테고리 뽑기
		String category = boardDto.getCategory();
		
		// 선택된 글 리스트 뽑기
		List<BoardDTO> boardList = boardService.getBoardList(category);
		model.addAttribute("boardList", boardList);
		
		// 각 게시글의 유저 넘버로 조회해서 해당 닉네임을 가져오고 list에 저장한다.
		List<String> nicknameList = new ArrayList<>();
		
		Iterator<BoardDTO> list = boardList.iterator();

		while(list.hasNext()) {
			BoardDTO boardOne = list.next();
			Long userNum = (long) boardOne.getUserNumber();
			nicknameList.add(userService.getNickname(userNum));
		}
		
		model.addAttribute("nicknameList", nicknameList);
		return "board/list";
	}
	
	
	
	// 게시글 쓰기 화면으로 이동
	@PostMapping(value="/board/write")
//	public String writeBoard(@RequestParam(value = "bno", required = false) final Long bno ,Model model) {
	public String writeBoard(BoardDTO boardDTO, Model model) {
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
		
		// 유저 넘버랑 category 가져옴.
		System.out.println("글쓰기 페이지 BoardDto : "+boardDTO);

		// 가입 안한 사람도 볼 수 있기에 null이라면 로그인 페이지로 이동
		Long userNumber = boardDTO.getUserNumber();
		if (userNumber == null) {
			return "redirect:/star/login";
		}
		//////
		
		// 해당 user 정보 다 넘겨주기
		UserDTO userDTO = userService.getUser(userNumber);
		model.addAttribute("userDTO", userDTO);
		System.out.println("--------"+userDTO);
		
		return "/board/write";
	}
	
	
	// 게시글 쓰기가 완료 되면
	@PostMapping(value="/board/registerBoard")
	public String registerBoard(BoardDTO boardDTO, RedirectAttributes rttr) {
		System.out.println("글 저장 "+boardDTO.toString());
		
		// 이렇게 넘기는 거 아님..?
		// 유저 넘버랑 카테고리만 넘어가면된다.
		// 넘길때는 boardDto였는데 list.html에서 받을때는 boardDTO였어서 찾지 못했던것.
		// --> boardDTO로 변경함.
		// rttr.addFlashAttribute("boardDto", boardDTO);
		
		rttr.addFlashAttribute("boardDTO", boardDTO);
		
		
		boardService.registerBoard(boardDTO);
		
		return "redirect:/board/list" ;
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
    
    
    // 마이페이지
    @PostMapping(value="/board/mypage")
    public String openMypage(BoardDTO boardDTO, Model model) {
    	System.out.println("마이 페이지로 이동");
    	// 유저 넘저 존재
    	System.out.println(boardDTO);
    	
    	Long userNumber = (long) boardDTO.getUserNumber();
    	
    	String userNickname = userService.getNickname(userNumber);
    	System.out.println(userNickname);
    	model.addAttribute("userNickname",userNickname);
    	
    	System.out.println("---------------------");
    	
    	List<BoardDTO> myList = boardService.getMyListBoard(userNumber);
    	model.addAttribute("myList", myList);
    	
    	return "/board/mypage";
    }
    	
}


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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.star.domain.BoardDTO;
import com.star.domain.PageMakeDTO;
import com.star.domain.UserDTO;
import com.star.paging.Criteria;
import com.star.service.BoardService;
import com.star.service.UserService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private UserService userService;
	
	// 게시글 조회
	@RequestMapping(value="/board/list")
	public String listBoard(Criteria cri, Model model, BoardDTO boardDTO, RedirectAttributes rttr) {
		System.out.println("리스트 카테고리 : "+cri.getCategory());
		
		// 선택된 글 리스트 뽑기
		List<BoardDTO> boardList = boardService.getBoardList(cri);
		model.addAttribute("boardList", boardList);
		
		// 각 게시글의 유저 넘버로 조회해서 해당 닉네임을 가져오고 list에 저장한다.
		// > 우리 boardDTO에는 유저 번호가 있고 유저 닉네임이 없기 때문에
		List<String> nicknameList = new ArrayList<>();
		
		Iterator<BoardDTO> list = boardList.iterator();

		while(list.hasNext()) {
			BoardDTO boardOne = list.next();
			Long userNum = (long) boardOne.getUserNumber();
			nicknameList.add(userService.getNickname(userNum));
		}
		
		model.addAttribute("nicknameList", nicknameList);
		
		// 페이징 처리
		int total = boardService.getCount(cri.getCategory());
		
		PageMakeDTO pageMake = new PageMakeDTO(cri,total);
		
		model.addAttribute("pageMaker", pageMake);
		model.addAttribute("criteria",cri);
		
		System.out.println(pageMake);
		
		return "board/list";
	}
	
	
	
	// 게시글 쓰기 화면으로 이동
	@PostMapping(value="/board/write")
	public String writeBoard(Model model,Criteria cri) {
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
		
		System.out.println("글쓰기 페이지 이동 ");
		// 이전 값 존재
		model.addAttribute("criteria", cri);
		
		Long userNumber = (long) cri.getUserNumber();
		
		// 로그인 유저만 가능
		if (userNumber == null) {
			return "redirect:/star/login";
		}
		
		// 해당 user 정보 다 넘겨주기
		// 로그인 된 유저의 닉네임 보여주기 위해
		UserDTO userDTO = userService.getUser(userNumber);
		model.addAttribute("userDTO", userDTO);;
		
		return "/board/write";
	}
	
	
	// 게시글 쓰기가 완료 되면
	@PostMapping(value="/board/registerBoard")
	public String registerBoard(BoardDTO boardDTO, Criteria cri, @RequestParam(value="img",required=false) List<MultipartFile> file, RedirectAttributes rttr) throws Exception {
		// 글 등록
		System.out.println("file 여러개로가져오면   "+file);
		boardService.registerBoard(boardDTO,file);
		
		// 글 쓰기 전 cri 값 잘 받아 왔나 확인
		// System.out.println(cri);
		rttr.addFlashAttribute("criteria",cri);	
		
		return "redirect:/board/list" ;
	}
	

//	// 게시글 쓰기 화면으로 이동
//	@GetMapping(value="/star/test")
//	public String testBoard(Model model, UserDTO userDto) {
//		
//		userDto.setUserRegion("전국");
//		
//		model.addAttribute("region", userDto.getUserRegion());
//		
//		return "/star/test";
//	}

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
    
    
	// 마이 페이지
	@RequestMapping(value="/board/mypage")
	public String openMypage2(Criteria cri, Model model, BoardDTO boardDTO, RedirectAttributes rttr) {
		System.out.println("마이 페이지로 이동");
		
		// System.out.println(cri);
		
		// 유저 번호 추출
		Long userNumber = (long) cri.getUserNumber();
		
		// 선택된 글 리스트 뽑기
		List<BoardDTO> myList = boardService.getMyListBoard(cri);
    	model.addAttribute("myList", myList);
		
    	// 유저 닉네임
    	String userNickname = userService.getNickname(userNumber);
    	// System.out.println(userNickname);
    	model.addAttribute("userNickname",userNickname);
		
		// 페이징 처리
		int total = boardService.getMyCount(userNumber);
		
		PageMakeDTO pageMake = new PageMakeDTO(cri,total);
		
		model.addAttribute("pageMaker", pageMake);
		model.addAttribute("criteria",cri);
		
		System.out.println(pageMake);
		
		return "board/mypage";
	}
    
    // 상세글 테스트용 : get방식 채택 고려중  
    @GetMapping(value = "/star/view.do")
    public String viewContent() {
    	return "/star/detailed_check";
    }
    
    	
}
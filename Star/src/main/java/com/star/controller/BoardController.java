package com.star.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.star.domain.ImgDTO;
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
	public String listBoard(Criteria cri, Model model, BoardDTO boardDTO) {
		
		// 선택된 글 리스트 뽑기
		List<String> nicknameList = new ArrayList<>();
		List<BoardDTO> boardList = boardService.getBoardList(cri);
		
		for(BoardDTO board : boardList)
		{

			System.out.println("bno: " + board.getBno());
			
			List<ImgDTO> img = boardService.getImgsFromBno(board.getBno());

			Long userNum = (long) board.getUserNumber();
			nicknameList.add(userService.getNickname(userNum));
			
			String firstImg = "";
			try {
				firstImg = img.get(0).getImgName() ;
			} catch (Exception e) {
			}
			board.setImgName(firstImg);
			
		}
		
		model.addAttribute("nicknameList", nicknameList);
		model.addAttribute("boardList", boardList);
		
		// 페이징 처리
		int total = boardService.getCount(cri);
		PageMakeDTO pageMake = new PageMakeDTO(cri,total);
		
		model.addAttribute("pageMaker", pageMake);
		model.addAttribute("criteria",cri);
		model.addAttribute("boardDTO",boardDTO);
		
		System.out.println("리스트 카테고리 : "+cri.getCategory());
		System.out.println("cri: "+cri);
		System.out.println("길이: "+ boardList.size());
		System.out.println("borad: "+boardDTO);
		System.out.println("pageMake: " + pageMake);
		
		System.out.println("--------------");
		System.out.println(model);
		System.out.println("--------------");
		
		return "board/list";
	}
	
	
	
	// 게시글 쓰기 화면으로 이동
	@PostMapping(value="/board/write")
	public String writeBoard(Model model,Criteria cri
			, HttpServletRequest request) {
		
        HttpSession session = request.getSession();
        
        if (session.getAttribute("userDTO") == null) {
    		System.out.println("로그인을 진행해주세요.");
    		return "redirect:/star/login";
    	};
        
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

    // 상세글조회 페이지
    @GetMapping(value = "/star/detailed_check")
    public String detailedCheck(@RequestParam(value="bno",required=true) Long bno, UserDTO userDto, Model model) {
    	
    	BoardDTO boardDto = boardService.getBoardDetail(bno);
    	model.addAttribute(boardDto);
    	
    	Long writerNumber = boardDto.getUserNumber();
    	String writer = boardService.getWriter(writerNumber);
    	model.addAttribute("writer", writer);
    	System.out.println(writer);
    	
    	Long boardBno = boardDto.getBno();
    	List<ImgDTO> imgs = boardService.getImgsFromBno(boardBno);
    	model.addAttribute("imgDto", imgs);
    	System.out.println(imgs);
    	
    	System.out.println(model);
    	
    	return "star/detailed_check";
    }
    
    
    // 신고사유 페이지
    @GetMapping(value = "/star/userReport")
    public String reportpage(HttpServletRequest request) {
    	
    	HttpSession session = request.getSession();
    	System.out.println(session.getAttribute("userDTO"));
    	
    	if (session.getAttribute("userDTO") == null) {
    		System.out.println("로그인을 진행해주세요.");
    		
    		return "redirect:/star/login";
    	};
    	
    	
    	return "/star/userReport";
    }
	
    // 신고하기
    @PostMapping(value = "/star/report")
    @ResponseBody
    public String report(BoardDTO boardDTO
    		, HttpServletRequest request) {
    	
    	System.out.println(boardDTO);
    	
        HttpSession session = request.getSession();
    	System.out.println(session.getAttribute("userDTO"));
    	
    	boardService.report(boardDTO);
    	
    	System.out.println("접수되었습니다.");
    	
    	return "good";
    }
    
    
	// 마이 페이지
	@RequestMapping(value="/board/mypage")
	public String openMypage(Criteria cri, Model model, BoardDTO boardDTO, HttpServletRequest request, UserDTO userDTO) {
		System.out.println("마이 페이지로 이동");
		
		HttpSession session = request.getSession();
		System.out.println(session.getAttribute("userDTO"));
		
		UserDTO user = (UserDTO) session.getAttribute("userDTO");
		
		cri.setUserNumber(user.getUserNumber());
		
		 System.out.println(cri);
		
		// 선택된 글 리스트 뽑기
		List<BoardDTO> myList = boardService.getMyListBoard(cri);
    	model.addAttribute("myList", myList);
		
		
		// 페이징 처리
		int total = boardService.getMyCount(cri);
		
		PageMakeDTO pageMake = new PageMakeDTO(cri,total);
		
		model.addAttribute("pageMaker", pageMake);
		model.addAttribute("criteria",cri);
		
		System.out.println(pageMake);
		
		return "board/mypage";
	}
	
	// 게시글 삭제
    @PostMapping(value = "/board/delete.do")
    public String deleteBoard(BoardDTO boardDto) {
    	
    	boardService.deleteBoard(boardDto);
    	
    	// 끝나면 메인 페이지로 이동
    	return "redirect:/star/mainpage";
    };
    
    // 게시글 수정 화면으로 이동
 	@PostMapping(value="/board/modify")
 	public String modifyBoard(Model model, BoardDTO boardDto
 			, HttpServletRequest request) {
 		
 		// 로그인 안된사람 탈출시키기
         HttpSession session = request.getSession();
         if (session.getAttribute("userDTO") == null) {
     		System.out.println("로그인을 진행해주세요.");
     		return "redirect:/star/login";
     	};
 		
 		System.out.println("글수정 페이지 이동 ");
 		// 이전 값 존재
 		
 		boardDto = boardService.getBoardDetail(boardDto.getBno());
 		
 		model.addAttribute("boardDto", boardDto);
 		
 		System.out.println(model);
 		
 		return "/board/write";
 	}
    
    	
}
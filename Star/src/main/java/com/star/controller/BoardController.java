package com.star.controller;

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
import com.star.domain.CommentDTO;
import com.star.domain.ImgDTO;
import com.star.domain.PageMakeDTO;
import com.star.domain.UserDTO;
import com.star.paging.Criteria;
import com.star.service.BoardService;

@Controller
public class BoardController {
	
	
	@Autowired
	private BoardService boardService;
	
	// 게시글 조회
	@RequestMapping(value="/board/list")
	public String listBoard(Criteria cri, Model model, HttpServletRequest request) {
		
		System.out.println("cri는요 : "+ cri.getPageNum());
		
		// 유저 정보 받아옴
        HttpSession session = request.getSession();
        
        // ajax를 사용하여 페이지 이동을 한 경우
		if (cri.getAjaxYn().equals("y")) {
			// session.removeAttribute("criteria");
			// 페이징 처리 시 카테고리가 변경되는 오류 처리
			System.out.println("페이징 처리나 검색했을 경우"+session.getAttribute("criteria"));
			System.out.println("그냥 크리 "+cri);
			Criteria prevCri = (Criteria) session.getAttribute("criteria");
			session.removeAttribute("criteria");
			session.setAttribute("criteria", prevCri);
			cri.setCategory(prevCri.getCategory());
			System.out.println("----------------------------------");
			System.out.println("페이징 처리나 검색했을 경우"+session.getAttribute("criteria"));
			System.out.println("그냥 크리 "+cri);
			
			
		} else {
			// 페이지로 그냥 넘어오는 경우
			
			try {
				Criteria prevCri = (Criteria) session.getAttribute("criteria");
				System.out.println("bbbbbbbbbb");
				System.out.println(cri.getCategory());
				System.out.println(prevCri.getCategory());
				System.out.println("cccccccccccc");
				try {
					// 새로 들어온 카테고리가 이전 카테고리와 다른 문자일경우
					if (cri.getCategory().equals(prevCri.getCategory()) == false) {
						System.out.println("카테고리가 변경됐네요222");
						
						System.out.println("해드렸을겁니다~");
						
					}
				} catch (Exception e) {
					// 새로 들어온 카테고리가 null인경우
					if (cri.getCategory() != (prevCri.getCategory())) {
						System.out.println("카테고리가 변경됐네요333");

						System.out.println("aaaaaaaa");
						cri.setCategory(prevCri.getCategory());
						cri.setPageNum(prevCri.getPageNum());
						cri.setAmount(prevCri.getAmount());
						System.out.println("해드렸습니다~");
						
					}
				}								
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("안해드렸습니다~");
			}
			
			
			// 세션 제거
			session.removeAttribute("criteria");
			System.out.println("third");
			System.out.println(session.getAttribute("criteria"));
			
			System.out.println("!!!!...");
			
		}
		
		// 페이징 처리
		if(cri.getCategory().equals("작품전")){
			// 한 페이지 보여줄 amount 수정
			cri.setAmount(9);
		}
		
		int total = boardService.getCount(cri);
		PageMakeDTO pageMake = new PageMakeDTO(cri,total);
		
		model.addAttribute("pageMaker", pageMake);
		System.out.println("pageMake: " + pageMake);
		
		System.out.println("아아아아아ㅏ아아아아아아 cri : "+cri);
		
		// 세션에 다시 저장
		session.setAttribute("criteria", cri);
		System.out.println("fourth");
		System.out.println(session.getAttribute("criteria"));
		
		// 선택된 글 리스트 뽑기
		List<BoardDTO> boardList = boardService.getBoardList(cri);
		
		for(BoardDTO board : boardList)
		{
			System.out.println("bno: " + board.getBno());
			
			List<ImgDTO> img = boardService.getImgsFromBno(board.getBno());
			
			String firstImg = "";
			try {
				firstImg = img.get(0).getImgName() ;
			} catch (Exception e) {
			}
			board.setImgName(firstImg);	
		}
		
		model.addAttribute("boardList", boardList);
		// model.addAttribute("boardDTO",boardDTO);
		
		System.out.println("리스트 카테고리 : "+cri.getCategory());
		System.out.println("cri: "+cri);
		System.out.println("길이: "+ boardList.size());
		// System.out.println("borad: "+boardDTO);
		// System.out.println("pageMake: " + pageMake);
		
		System.out.println("--------------");
		System.out.println(model);
		System.out.println("--------------");
		
		return "board/list";
	}
	
	
	
	// 게시글 쓰기 화면으로 이동
	@PostMapping(value="/board/write")
	public String writeBoard(Model model,Criteria cri
			, HttpServletRequest request) {
		
		System.out.println("글쓰기 페이지 이동 ");
		
		// 유저 정보 받아옴
        HttpSession session = request.getSession();
        
        if (session.getAttribute("userDTO") == null) {
    		System.out.println("로그인을 진행해주세요.");
    		return "redirect:/star/login";
    	};
        
		// 이전 값 존재
		model.addAttribute("criteria", cri);
		
		return "/board/write";
	}
	
	
	// 게시글 쓰기가 완료 되면
	@PostMapping(value="/board/registerBoard")
	public String registerBoard(BoardDTO boardDTO, Criteria cri
			, @RequestParam(value="img",required=false) List<MultipartFile> file
//			, RedirectAttributes rttr
			, Model model) throws Exception {
		// 글 등록
		System.out.println("file 여러개로가져오면   "+file);
		
		boardService.registerBoard(boardDTO,file);
		
//		HttpSession session = request.getSession();
//		session.setAttribute("criteria", cri);
		
		// 글 쓰기 전 cri 값 잘 받아 왔나 확인
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.println(cri);
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
//		rttr.addFlashAttribute("criteria",cri);
		model.addAttribute(cri);
//		 model.addAttribute("criteria",cri);
		
		
		return "redirect:/board/list" ;
	}

    // 상세글조회 페이지
    @GetMapping(value = "/board/detailed_check")
    public String detailedCheck(@RequestParam(value="bno",required=false) Long bno, Model model,HttpServletRequest request) {
    	
    	HttpSession session = request.getSession();
    	System.out.println(session.getAttribute("criteria"));
    	
    	System.out.println("상세 조회로 이동");
    	// .out.println(cri);
    	
    	System.out.println(bno);
    	
    	BoardDTO boardDto = boardService.getBoardDetail(bno);
    	model.addAttribute(boardDto);

    	
    	Long boardBno = boardDto.getBno();
    	System.out.println("상세 조회할 글 번호 : "+boardBno);
    	List<ImgDTO> imgs = boardService.getImgsFromBno(boardBno);
    	model.addAttribute("imgDto", imgs);
    	System.out.println(imgs);

    	
//    	댓글	
//		UserDTO user = (UserDTO) session.getAttribute("userDTO");
//		cri.setUserNumber(user.getUserNumber());
//		System.out.println(cri);
		
		// 선택된 글의 댓글 리스트 뽑기
		List<CommentDTO> commentList = boardService.getCommentList(boardBno);
		System.out.println("-----------------");
    	System.out.println(commentList);
    	System.out.println("-----------------");
		
    	model.addAttribute("commentList", commentList);
		
		    	
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
		Long userNumber = user.getUserNumber();
		
		// cri.setUserNumber(user.getUserNumber());
		
		 System.out.println(cri);
		
		// 선택된 글 리스트 뽑기
		List<BoardDTO> myList = boardService.getMyListBoard(cri, userNumber);
    	model.addAttribute("myList", myList);
		
		
		// 페이징 처리
		int total = boardService.getMyCount(userNumber);
		
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
 	public String modifyBoard(@RequestParam(value="bno",required=true) Long bno, Model model, BoardDTO boardDto
 			, HttpServletRequest request) {
 		
 		// 로그인 안된사람 탈출시키기
		HttpSession session = request.getSession();
         if (session.getAttribute("userDTO") == null) {
     		System.out.println("로그인을 진행해주세요.");
     		return "redirect:/star/login";
     	};
 		
 		System.out.println("글수정 페이지 이동 ");
 		// 이전 값 존재
 		
 		boardDto = boardService.getBoardDetail(bno);
 		
 		model.addAttribute("boardDto", boardDto);
 		
 		System.out.println(model);
 		
 		return "/board/update";
 	}
 	
 	// 게시글 수정하기
 	@PostMapping(value="/board/updateBoard")
 	public String updateBoard(@RequestParam(value="bno",required=true) Long bno, BoardDTO boardDTO, RedirectAttributes re) {
 		System.out.println("게시글 수정 시작");
 		System.out.println("수정할 게시글 내용"+boardDTO);
 		
 		int result = boardService.updateBoard(boardDTO);
 		
 		if(result == 1) {
 			System.out.println("게시글 수정 완료");
 		}else {
 			System.out.println("게시글 수정 실패");
 		}
 		
 		re.addAttribute("bno", boardDTO.getBno());
 		
 		return "redirect:/board/detailed_check";
 	}
 	
 	
	// 댓글 입력
	@PostMapping(value="/star/writeComment")
	public String writeComment(BoardDTO boardDTO, Criteria cri
			, @RequestParam(value="img",required=false) List<MultipartFile> file
//			, RedirectAttributes rttr
			, Model model) throws Exception {
		// 글 등록
		System.out.println("file 여러개로가져오면   "+file);
		
		boardService.registerBoard(boardDTO,file);
		
//		HttpSession session = request.getSession();
//		session.setAttribute("criteria", cri);
		
		// 글 쓰기 전 cri 값 잘 받아 왔나 확인
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.println(cri);
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
//		rttr.addFlashAttribute("criteria",cri);
		model.addAttribute(cri);
//		 model.addAttribute("criteria",cri);
		
		
		return "redirect:/board/detailed_check" ;
	}
 	
    
 	
 	// 관리자 페이지
 	@RequestMapping(value="/board/managerpage")
 	public String managerpage(Model model, BoardDTO boardDTO, HttpServletRequest request, UserDTO userDTO) {
 		System.out.println("마이 페이지로 이동");
 		
 		HttpSession session = request.getSession();
 		UserDTO userDto = (UserDTO) session.getAttribute("userDTO");
 		try {
 			if (userDto.isAdminYn()==true){
 	 			System.out.println("hello admin!");
 	 		} else {
 	 			System.out.println("you are member!");
 	 			return "redirect:/star/mainpage";
 	 		}
		} catch (Exception e) {
			// TODO: handle exception
 			System.out.println("you are guest!");
 			return "redirect:/star/mainpage";
		}
 		
 		List<BoardDTO> reportList = boardService.getReportBoard();
 		System.out.println(reportList);
 		
 		model.addAttribute("reportList", reportList);
 		
 		return "board/managerpage";
 	}
 	
	// 관리자 - 게시글 삭제 판단
    @PostMapping(value = "/manage/delete.do")
    public String managerDeleteBoard(BoardDTO boardDto) {
    	
    	boardService.deleteBoard(boardDto);
    	boardService.managingComplete(boardDto);
    	
    	// 끝나면 메인 페이지로 이동
    	return "redirect:/board/managerpage";
    };
 	
	// 관리자 - 게시글 통과 판단
    @PostMapping(value = "/manage/pass.do")
    public String managerPassBoard(BoardDTO boardDto) {
    	
    	boardService.managingComplete(boardDto);
    	
    	// 끝나면 메인 페이지로 이동
    	return "redirect:/board/managerpage";
    };    
    	
}
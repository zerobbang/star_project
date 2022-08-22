package com.star.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.star.domain.DustDTO;
import com.star.domain.MailDTO;
import com.star.domain.UserDTO;
import com.star.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	//	메인 페이지
	@GetMapping(value = "/star/mainpage")
	public String openUser(){ 
		
		return "star/main";
	}
	
//	메인 페이지
//	@PostMapping(value = "/star/mainpage")
//	public String openMain(UserDTO userDTO, RedirectAttributes rttr
//			, HttpServletRequest request) {
//		
//		rttr.addFlashAttribute(userDTO);
//		
//		HttpSession session = request.getSession();
//		System.out.println("세션 테스트");
//        System.out.println(session.getAttribute("loginUser"));
//        
//        System.out.println("세션 테스트끝!@");
//		
//		return "star/main";
//	}
	
	// ajax 중간 main - map
	@GetMapping(value = "/star/main2")
	public String openMap() throws IOException {
		// 현재 대기 정보 open API
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=2OLJexa7ycFuyZomTegF0Z1G5ar6arjzMfEcvT%2B3ewmnNYnDdOfXG0FJFy3%2BzA%2F7spRHr%2BRTraC%2Fnxzak73dFg%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20220822", "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0600", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("59", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/
        
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
        JSONParser parser;
//        근데 보통 파싱을
//        JSONParser parser = new JSONParser();
//        JSONObject obj = (JSONObject)parser.parse(인자들어감);
//        이런 형식이더라고 근데 어쨌든 둘중 하나에 sb가 들어가야 할것같은데 돌다 안됌...끝
//        // 2. 문자열을 JSON 형태로 JSONObject 객체에 저장. 	
        try {
			JSONObject obj = (JSONObject)parser.parse;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        // 3. 필요한 리스트 데이터 부분만 가져와 JSONArray로 저장.
//        JSONArray dataArr = (JSONArray) obj.get("response");
        // 4. model에 담아준다.
        // model.addAttribute("data",dataArr);
        System.out.println(sb.toString());
//        > 이게 sb가 우리가 받아오는 값인데 type인 stringBuilder고 이걸 json이랑 파싱해야하거든??
        System.out.println(sb); 
        String data = sb.toString();
        // JSONParser parser = new JSONParser();
//        // JSONObject obj = (JSONObject) parser.parse(data);
//        JSONObject objData = (JSONObject)new JSONParser().parse(data);
//        JSONObject result = (JSONObject) new JSONParser().parse(sb.toString());
//
//        String response =  obj.get("response");
//        System.out.println(response);

		return "star/main2";
	}	
	
	// 메인페이지 ( with. 중간에 있는 테이블 )
	@GetMapping(value = "/star/main3")
	public String openPredictionList(DustDTO params, Model model) {
		
		// 임시로 일단 서울만 계속 보여주도록
		params.setRegion("서울");
		
		List<DustDTO> dustList = userService.getPrediction(params) ;
		model.addAttribute("dustList", dustList); 
		System.out.println(params);
		 
		System.out.println(dustList.get(0).getHumidity());

		return "star/main3"; 
	}

	@GetMapping(value = "/star/sendmail.do")
	public String openMailPage() {
		return "star/sendmail";

	}
 
	// 입력한 이메일로 메일 보내기
	@PostMapping(value = "/mailSend")
	@ResponseBody
	public String[] dataSend(MailDTO mailDto){
		
		System.out.println("메일 전송 시작");
		userService.sendSimpleMessage(mailDto);
		System.out.println("메일 전송 완료");
		String certifyNum = mailDto.getContent();
		
		System.out.println(certifyNum);
		 
        String[] returndata = {certifyNum};
        return returndata;
    };
    
    // 로그인 페이지 (logIn안에 값은 임시적으로 네비바 띄우기 위해서 적어놓음)
    @GetMapping(value = "/star/login")
    public String logIn(HttpServletRequest request) { 
    	System.out.println("로그인 페이지로 이동");
    	
    	HttpSession session = request.getSession();
    	if (session.getAttribute("userDTO") != null) {
    		System.out.println("이미 로그인중입니다.");
    		return "redirect:/star/mainpage";
    	};
    	return "star/login";
    };
    
    // 로그인 체크
    @PostMapping(value = "/star/login") 
    public String logInCheck(UserDTO userDTO
    		, HttpServletRequest request) {
    	
    	System.out.println(userDTO);
    	
		try {
			
			System.out.println("do action!");
	    	System.out.println(userDTO);
			 
	    	userDTO = userService.loginUser(userDTO);
	    	 
	    	// 로그인 여부 처리
			if (userDTO.getUserId() != null) {
				
                // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
                HttpSession session = request.getSession();
                // 세션에 로그인 회원 정보 보관
                session.setAttribute("userDTO", userDTO);

				System.out.println("로그인 성공!");
		        return "redirect:/star/mainpage";
			}else {
				System.out.println("로그인 실패1...");
				return "star/login";
			}
		} catch (DataAccessException e) {
			System.out.println("로그인 실패2...");
			return "star/login"; 
		} catch (Exception e) {
			System.out.println("로그인 실패3...");
			return "star/login";  
		}
    };
    
    // 회원가입 페이지
    @GetMapping(value = "/star/signup")
    public String singUp() {
    	return "star/signUp";
	}  

    // 계정찾기 페이지
    @GetMapping(value = "/star/findUser")
    public String findAccount() {
    	return "star/findUser";
    }

//    // 입력한 이메일로 메일 보내기
//	@PostMapping(value = "/mailSend")
//	@ResponseBody
//	public String[] dataSend(MailDTO mailDto){
//		
//		System.out.println("메일 전송 시작");
//		userService.sendSimpleMessage(mailDto);
//		System.out.println("메일 전송 완료");
//		String certifyNum = mailDto.getContent();
//		
//		System.out.println(certifyNum);
//		 
//        String[] returndata = {certifyNum};
//        return returndata;
//    };
    
    // 회원가입 기능
    @PostMapping(value = "/signup.do")
    @ResponseBody
    public String doSingUp(UserDTO userDTO) {
    	 
    	System.out.println(userDTO.toString());
    	
    	try {
    		userService.doSignUp(userDTO);
    	} catch(Exception e) {
    		System.out.println("fail...");
    		return "fail";
    	}
    	
    	return "success";
	};
	
    
    // id 유일성 체크
    @PostMapping(value = "/inputIDCheck")
	@ResponseBody
    public String[] inputCheck(UserDTO userDto){
		
        String[] returndata = userService.idCheck(userDto);
        
        return returndata;
    };
    
    // 닉네임 유일성 체크
    @PostMapping(value = "/inputNicknameCheck")
    @ResponseBody
    public String[] inputNicknameCheck(UserDTO userDto){
		
        String[] returndata = userService.nicknameCheck(userDto);
        
        return returndata;
    };
    
    // 이메일 유일성 체크
    @PostMapping(value = "/inputEmailCheck")
    @ResponseBody
    public String[] inputEmailCheck(UserDTO userDto){
		
		String[] returndata = userService.emailCheck(userDto);
		
        return returndata;
    };
    
    // 정보 변경 페이지 이동
    @GetMapping(value = "/star/changeInfo")
    public String changeInfoPage(HttpServletRequest request) {
    	
        HttpSession session = request.getSession();
        
        if (session.getAttribute("userDTO") == null) {
    		System.out.println("로그인을 진행해주세요.");
    		return "redirect:/star/login";
    	};
		return "star/changeInfo";
	}
    
    // 정보 변경 실행
    @PostMapping(value = "/changeInfo.do")
    public String changeInfo(UserDTO userDto
    		, HttpServletRequest request){
		
    	System.out.println("aaaaaa"+userDto);
    	HttpSession session = request.getSession();
    	
    	UserDTO user = (UserDTO) session.getAttribute("userDTO");
    	String Id = user.getUserId();
    	
    	String Pw;
    	if (userDto.getUserPassword() == "") {
    		Pw = user.getUserPassword(); 
    	}else {
    		Pw = userDto.getUserPassword();
    	}
    	
    	System.out.println("지우기 전 : "+Id);
    	System.out.println("지우기 전 비밀번호 486 : "+Pw);
    	
    	session.removeAttribute("userDTO");
    	userDto.setUserId(Id);
    	userDto.setUserPassword(Pw);
    	
    	System.out.println("지운 후 : "+Id);
    	System.out.println("지운 후 비밀 번호 : "+Pw);
    	
    	userDto = userService.changeInfo(userDto);
		
		session.setAttribute("userDTO", userDto);
		System.out.println("컨트롤러 유저 정보 변경 "+session.getAttribute("userDTO"));
		
		return "redirect:/star/mainpage";
    };
    
    
    // 회원탈퇴
    @PostMapping(value = "/star/signdown")
    public String deleteUser(UserDTO userDto, HttpServletRequest request) {
    	
    	System.out.println(userDto);
    	
    	Long userNumber = userDto.getUserNumber();
    	userService.pagedown(userNumber);
    	
        HttpSession session = request.getSession();    	
        session.removeAttribute("userDTO");

    	System.out.println("회원탈퇴 완료됨!");
    	
    	return "redirect:/star/mainpage";
    }
    
    // id 찾기
    @PostMapping(value = "/findId")
	@ResponseBody
    public String findId(UserDTO userDTO){
		
        String returndata = userService.findId(userDTO);
        
        return returndata;
    };
    
    // 비밀번호 변경 페이지
    @PostMapping(value = "/star/changePassword")
    public String changePasswordPage(UserDTO userDto, Model model){
		
		model.addAttribute(userDto); 
		
		System.out.println(model.getAttribute("userDto"));
		
        return "/star/changePassword";
    };
    
    // 비밀번호 변경 실행
    @PostMapping(value = "/changePassword.do")
    public String changePassword(UserDTO userDTO){
		
		int result = userService.changePassword(userDTO);
		System.out.println(result);
		
		return "redirect:/star/login";

    };
    
//    // 유저 닉네임
//    @ResponseBody
//    @PostMapping(value = "/star/getUserNumber")
//    public long getusernumber(UserDTO userDTO){
//    	// System.out.println("userDTO : "+userDTO);
//		long result = userService.getUserNumber(userDTO.getUserNickname());
//		System.out.println("검색 한 닉네임의 유저 번호 : "+result);
//		return result;
//    };
        
    // 로그아웃
    @GetMapping(value = "/star/logout.do")
    public String doLogout(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        
        session.removeAttribute("userDTO");
    	
		return "redirect:/star/mainpage";
    };
    
    // 테스트 할 때 쓰는 용도
    @GetMapping(value = "/star/woobin")
    public String doTest(HttpServletRequest request) {
    	
    	HttpSession session = request.getSession();
    	
        session.removeAttribute("userDTO");
    	
        System.out.println("~~~~~~~~~~~~~~~~~");
        System.out.println(session.getAttribute("userDTO"));
        System.out.println("~~~~~~~~~~~~~~~~~");
    	System.out.println("회원탈퇴 완료됨!");
    	
    	return "redirect:/star/login";
    }
    
 // 지역 추천 페이지(서울)
    @GetMapping(value = "/star/goodPlaces")
    public String goodPlaces(){
		
		System.out.println();
		
        return "/star/goodPlace";
    };
    
    
    
}
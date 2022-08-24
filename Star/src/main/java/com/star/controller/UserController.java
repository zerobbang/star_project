package com.star.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
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
	public String openMap(Model model) throws IOException {
		
		// 서울 메타데이타
		Map<String,List<Integer>> weather = new HashMap<>();
		ArrayList<Integer> junglanggu = new ArrayList<Integer>(Arrays.asList(62,128));
		ArrayList<Integer> gwangjingu= new ArrayList<Integer>(Arrays.asList(62,126));		
		ArrayList<Integer> dobonggu = new ArrayList<Integer>(Arrays.asList(61,129));
		ArrayList<Integer> dongdaemongu = new ArrayList<Integer>(Arrays.asList(61,128));
		ArrayList<Integer> seongdonggu = new ArrayList<Integer>(Arrays.asList(61,127));
		ArrayList<Integer> gangnamgu = new ArrayList<Integer>(Arrays.asList(61,126));
		ArrayList<Integer> seochogu = new ArrayList<Integer>(Arrays.asList(61,125));
		ArrayList<Integer> jonglogu = new ArrayList<Integer>(Arrays.asList(60,127));
		ArrayList<Integer> yongsangu = new ArrayList<Integer>(Arrays.asList(60,126));
		ArrayList<Integer> mapogu = new ArrayList<Integer>(Arrays.asList(59,127));
		ArrayList<Integer> dongjakgu = new ArrayList<Integer>(Arrays.asList(59,125));
		ArrayList<Integer> geumcheongu = new ArrayList<Integer>(Arrays.asList(59,124));
		ArrayList<Integer> gangseogu = new ArrayList<Integer>(Arrays.asList(58,126));
		ArrayList<Integer> gulogu = new ArrayList<Integer>(Arrays.asList(58,125));
		
		weather.put("종로구", jonglogu);
//		weather.put("중구", jonglogu);
//		weather.put("용산구", yongsangu);
//		weather.put("성동구", seongdonggu);
//		weather.put("광진구", gwangjingu);
//		weather.put("동대문구", dongdaemongu);
//		weather.put("중랑구", junglanggu);
//		weather.put("성북구", seongdonggu);
//		weather.put("강북구", dongdaemongu);
//		weather.put("도봉구", dobonggu);
//		weather.put("노원구", dobonggu);
//		weather.put("은평구", mapogu);
//		weather.put("서대문구", mapogu);
//		weather.put("마포구", mapogu);
//		weather.put("양천구", gangseogu);
//		weather.put("강서구", gangseogu);
//		weather.put("구로구", gulogu);
//		weather.put("금천구", geumcheongu);
//		weather.put("영등포구", gangseogu);
//		weather.put("동작구", dongjakgu);
//		weather.put("관악구", dongjakgu);
//		weather.put("서초구", seochogu);
//		weather.put("강남구", gangnamgu);
//		weather.put("송파구", gwangjingu);
//		weather.put("강동구", gwangjingu);
		
		// 현재 날짜
		LocalDate now = LocalDate.now();
		String formatedNow = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//		String formatedNow = now.format(formatter);
//		System.out.println(formatedNow);
		
		// 현재 시간
		LocalTime nowTime = LocalTime.now();
		String formatedNowTime = "";
		
		// 단기 예보 (SKY 값 존재 , 기준 시간 부터 72시간 값 )
		// - Base_time : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
		// - API 제공 시간(~이후) : 02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10
		
		ArrayList<Integer> baseTime = new ArrayList<>(Arrays.asList(2,5,8,11,14,17,20,23));	
		
		// 시간 비교하기
		for(int i : baseTime) {
			LocalTime criTime = LocalTime.of(i, 10, 30);
			if(nowTime.isAfter(criTime) == true) {
				// DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmm");
				// String formatedNowTime = nowTime.format(formatterTime);
				if(i<10) {
					formatedNow = now.format(formatter);
					formatedNowTime = "0"+i+"00";
					break;
				}else {
					formatedNow = now.format(formatter);
					formatedNowTime = i+"00";
					break;
				}					
			}else {
				// 23시 이후인 경우
				formatedNow = now.minusDays(1).format(formatter);
				formatedNowTime = "2300";
				break;
			}
		}
		
		// 람다식을 사용할 때 로컬 변수를 사용하려면 final 특성이어야 한다. (불가변성)  > 그래서 재 선언
		String formatedNowFinal = formatedNow;
		String formatedNowTimeFinal = formatedNowTime;
//		System.out.println(formatedNowFinal);
//		System.out.println(formatedNowTimeFinal);
			
		// 메인 페이지로 넘겨줄 list data
		Map<String,List> mapWeather = new HashMap<>();
		
		weather.forEach((key,value)->{
	        try {
				int x =value.get(0);
				int y = value.get(1);

				// 현재 대기 정보 open API
				StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
				urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=2OLJexa7ycFuyZomTegF0Z1G5ar6arjzMfEcvT%2B3ewmnNYnDdOfXG0FJFy3%2BzA%2F7spRHr%2BRTraC%2Fnxzak73dFg%3D%3D");
				urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
		        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
		        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
		        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(formatedNowFinal, "UTF-8")); 
		        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(formatedNowTimeFinal, "UTF-8"));      
	        	urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(x+"", "UTF-8")); /*예보지점의 X 좌표값*/
		        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(y+"", "UTF-8")); /*예보지점의 Y 좌표값*/
		        
		        
		        // URL 객체 생성
		        URL url = new URL(urlBuilder.toString());
		        // 연결
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        System.out.println("Response code: " + conn.getResponseCode());
		        
		        BufferedReader rd;
		        // 서버 연결 코드에 상관없이 rd로 데이터를 받아온다.
		        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        } else {
		            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		        }
		        
		        // StringBuilder로 생성
		        StringBuilder sb = new StringBuilder();
		        String line;
		        // 서버로부터 받아온 데이터 rd를 line으로 변수명 지정
		        // 널값이 아니면 stringbuilder에 데이터 추가
		        while ((line = rd.readLine()) != null) {
		            sb.append(line);
		        }
		        // 연결 종료
		        rd.close();
		        conn.disconnect();
		        
		        String data = sb.toString();
		        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
		        JSONParser parser = new JSONParser();
		        // 2. 문자열을 JSON 형태로 JSONObject 객체에 저장. 	
		        try {
					JSONObject obj = (JSONObject)parser.parse(data);
					JSONObject response = (JSONObject) obj.get("response");
					JSONObject body = (JSONObject) response.get("body");
					JSONObject items = (JSONObject) body.get("items");
					JSONArray item = (JSONArray) items.get("item");
					// System.out.println(item.size());
					Map<String,Float> air = new HashMap<>();
					ArrayList<Map> airList = new ArrayList<>();
					for(int i=0;i<item.size();i++) {
						System.out.println(i);
						
						JSONObject itemList = (JSONObject) item.get(i);
						String category = (String) itemList.get("category");
						if(category.equals("PTY") || category.equals("SKY") || category.equals("TMP")) {
							System.out.println(category);
							// System.out.println(String.valueOf(itemList.get("fcstValue")));
							float airVal = Float.parseFloat(String.valueOf(itemList.get("fcstValue")));
							// System.out.println("airVal"+airVal);
							System.out.println(itemList);
							air.put(category,airVal);
							System.out.println("-"+air);

							
						}
						// 예보시간
						// air.put("fcstTime", (Float) itemList.get("fcstTime"));
						// air.put("fcstDate", (Float) itemList.get("fcstDate"));
//						airList.add(air);
//						System.out.println(air.size());
//						System.out.println("아");
//						if(air.size()==3) {
//							System.out.println("한시간 지남");
//							
//							air.clear();
//						}
						if(air.size()==3) {
							System.out.println("한시간 지남");
							System.out.println(air);
							airList.add(air);
							System.out.println(airList);
							air.clear();
						}
						
						
						
					}
					
					
					System.out.println("-----"+airList);
					mapWeather.put(key, airList);
					System.out.println(mapWeather);
					
					// System.out.println(cate);
//					System.out.println(item);
//					JSONObject tempData = (JSONObject) item.get(3);
//					float temp = Float.parseFloat(String.valueOf(tempData.get("obsrValue")));
//					mapWeather.put(key, temp);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
	       
		});
        
		model.addAttribute("mapWeather", mapWeather);
		
		//System.out.println(model);
        
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
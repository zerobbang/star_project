package com.star.controller;

import java.io.BufferedReader;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.star.domain.WeatherDTO;
import com.star.service.WeatherService;

@Controller
public class WeatherController {
	
	@Autowired
	private WeatherService weatherService;
	
	@GetMapping(value="/weather")
	public String getWeather(Model model) {
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
				weather.put("중구", jonglogu);
				weather.put("용산구", yongsangu);
				weather.put("성동구", seongdonggu);
				weather.put("광진구", gwangjingu);
				weather.put("동대문구", dongdaemongu);
				weather.put("중랑구", junglanggu);
				weather.put("성북구", seongdonggu);
				weather.put("강북구", dongdaemongu);
				weather.put("도봉구", dobonggu);
				weather.put("노원구", dobonggu);
				weather.put("은평구", mapogu);
				weather.put("서대문구", mapogu);
				weather.put("마포구", mapogu);
				weather.put("양천구", gangseogu);
				weather.put("강서구", gangseogu);
				weather.put("구로구", gulogu);
				weather.put("금천구", geumcheongu);
				weather.put("영등포구", gangseogu);
				weather.put("동작구", dongjakgu);
				weather.put("관악구", dongjakgu);
				weather.put("서초구", seochogu);
				weather.put("강남구", gangnamgu);
				weather.put("송파구", gwangjingu);
				weather.put("강동구", gwangjingu);
				
				// 현재 날짜
				LocalDate now = LocalDate.now();
				String formatedNow = "";
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				
				// 현재 시간
				LocalTime nowTime = LocalTime.now();
				String formatedNowTime = "";
				
				// 단기 예보 (SKY 값 존재 , 기준 시간 부터 72시간 값 )
				// - Base_time : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
				// - API 제공 시간(~이후) : 02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10
				
				ArrayList<Integer> baseTime = new ArrayList<>(Arrays.asList(2,5,8,11,14,17,20,23));	
				// 시간 비교하기
				for(int i = 0; i<baseTime.size(); i++) {
					int criTime = baseTime.get(i);
					if( criTime > nowTime.getHour() || (criTime == nowTime.getHour() && nowTime.getMinute() <= 20 )) {
						if(criTime==2) {
							// 이전 날 23시
							formatedNow = now.minusDays(1).format(formatter);
							formatedNowTime = "2300";
							break;
						}else if(criTime<12) {
							int preTime = baseTime.get(i-1);
							formatedNow = now.format(formatter);
							formatedNowTime = "0"+preTime+"00";
							break;
						}else {
							int preTime = baseTime.get(i-1);
							formatedNow = now.format(formatter);
							formatedNowTime = preTime+"00";
							break;
						}
					}else {
						// 23시 이후 ~ 24시 이전
						formatedNow = now.format(formatter);
						formatedNowTime = "2300";
					}
				}

				// 람다식을 사용할 때 로컬 변수를 사용하려면 final 특성이어야 한다. (불가변성)  > 그래서 재 선언
				String formatedNowFinal = formatedNow;
				String formatedNowTimeFinal = formatedNowTime;
				
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
				        // System.out.println("Response code: " + conn.getResponseCode());
				        
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
							Map<String,Object> air = new HashMap<>();
							ArrayList<Map> airList = new ArrayList<>();
							
							WeatherDTO weatherDTO = new WeatherDTO();
							int index = 0;
							for(int i=0;i<item.size();i++) {
								JSONObject itemList = (JSONObject) item.get(i);
								String category = (String) itemList.get("category");
								System.out.println("index : "+index);
								if(category.equals("TMP")) {
//									System.out.println("TMP : "+itemList.get("fcstValue").getClass().getTypeName());
									weatherDTO.setTmp(Integer.parseInt((String) itemList.get("fcstValue")));
									index++;
								}else if(category.equals("VEC")) {
//									System.out.println("VEC : "+itemList.get("fcstValue").getClass().getTypeName());
									weatherDTO.setVec(Integer.parseInt((String) itemList.get("fcstValue")));
									index++;
								}else if(category.equals("WSD")) {
//									System.out.println("WSD : "+itemList.get("fcstValue").getClass().getTypeName());
									weatherDTO.setWsd(Float.parseFloat((String) itemList.get("fcstValue")));
									index++;
								}else if(category.equals("SKY")) {
//									System.out.println("SKY : "+itemList.get("fcstValue").getClass().getTypeName());
									weatherDTO.setSky(Integer.parseInt((String) itemList.get("fcstValue")));
									index++;
								}else if(category.equals("PTY")) {
//									System.out.println("PTY : "+itemList.get("fcstValue").getClass().getTypeName());
									weatherDTO.setPty(Integer.parseInt((String) itemList.get("fcstValue")));
									index++;
								}else if(category.equals("PCP")) {
//									System.out.println("PCP : "+itemList.get("fcstValue").getClass().getTypeName());
									weatherDTO.setPcp((String) itemList.get("fcstValue"));
									index++;
								}else if(category.equals("REH")) {
//									System.out.println("REH : "+itemList.get("fcstValue").getClass().getTypeName());
									weatherDTO.setReh(Integer.parseInt((String) itemList.get("fcstValue")));
									index++;
								}else {
									// 없음.이래도 꺼지네
								}
								if(index == 7) {
									weatherDTO.setBaseDate((String) itemList.get("baseDate"));
									weatherDTO.setBaseTime((String) itemList.get("baseTime"));
									weatherDTO.setFcstDate((String) itemList.get("fcstDate"));
									weatherDTO.setFcstTime((String) itemList.get("fcstTime"));
									System.out.println("한 개 데이터 완성 : "+weatherDTO.toString());
									
									// boolean result = weatherService.insertWeather(weatherDTO);
//									if(result == true) {
//										System.out.println("한 개 데이터 DB 저장 완료");
//									}else {
//										System.out.println("한 개 데이터 DB 저장 실패 ㅜㅠ");
//									}
									
									index = 0;
								}
								
//								if(category.equals("PTY") || category.equals("SKY") || category.equals("TMP") || category.equals("WSD") || 
//										category.equals("VEC") || category.equals("PCP") || category.equals("REH") ) {
//									// System.out.println(category);
//									// System.out.println(String.valueOf(itemList.get("fcstValue")));
//									float airVal = Float.parseFloat(String.valueOf(itemList.get("fcstValue")));
//									// System.out.println("airVal"+airVal);
//									// System.out.println(itemList);
//									air.put(category,airVal);
//									// System.out.println("-"+air);
//									if(air.size() == 7) {
//										air.put("fcstDate", itemList.get("fcstDate"));
//										air.put("fcstTime", itemList.get("fcstTime"));
//										// 새로운 Map 객체 airClone을 air 사이즈가 3일 때마다 생성 (덮어씌우기) > 주소가 매번 달라짐.
//										// air는 for문 밖에 존재하기 때문에 값이 덮어씌우기가 안된다. 그래서 add(air)하면 같은 주소값만 들어감.
//										// add는 주소값 저장
//										// clone은 public 메소드가 아니어서? 형변환 하고 clone해야 한다.
//										Map<String,Object> airClone = new HashMap<>(air);
//										//a로 바꿔도 air바뀌고    air로 바꿔도 a바뀐다.
//										// System.out.println("air : "+air);
//										// System.out.println("airClone : "+airClone);
//										airList.add(airClone);	//air를 쓰면 마지막 air만 반복해서 들어간다.
//										air.clear();
//									}							
//								}						
							}
							// mapWeather.put(key, airList);
						} catch (ParseException e) {
							e.printStackTrace();					
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
			       
				});
		        
				// model.addAttribute("mapWeather", mapWeather);
				System.out.println(formatedNowFinal);
				System.out.println(formatedNowTimeFinal);
				
				//System.out.println(model);
				
				return "/board/test";
	}
	
	
}

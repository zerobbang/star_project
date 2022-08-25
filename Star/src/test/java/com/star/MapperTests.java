package com.star;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.star.domain.BoardDTO;
import com.star.domain.DustDTO;
import com.star.domain.RegionDTO;
import com.star.domain.UserDTO;
import com.star.domain.WeatherDTO;
import com.star.mapper.BoardMapper;
import com.star.mapper.DustMapper;
import com.star.mapper.RegionMapper;
import com.star.mapper.UserMapper;
import com.star.mapper.WeatherMapper;

@SpringBootTest
public class MapperTests {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private DustMapper dustMapper;
	
	@Autowired
	private RegionMapper regionMapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private WeatherMapper weatherMapper;

//	유저 정보 입력 test
	@Test
	public void testOfInsert() {
		UserDTO params = new UserDTO();
		

		 params.setUserNickname("가영"); 
		 params.setUserId("zerobbang");
		 params.setUserEmail("gydiane1008@gmail.com");
		 params.setUserPassword("zero123");
		 params.setUserRegion("서울");


		int result = userMapper.insertUser(params);
		System.out.println(params.toString());
		System.out.println("result" + result + "입니다.");
	}
	
	
	//	유저 정보 조회
	@Test
	public void testUser() {
		UserDTO result = userMapper.detailUser((long) 1);
		
		try {
            String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(result);

			System.out.println("=========================");
			System.out.println(boardJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}
	
	
	
	// 훈련 / 예측 test
	@Test
	public void testMl() {
		DustDTO dustDto = new DustDTO();
		// 훈련
		DustDTO result = dustMapper.detailInfo(dustDto);
		
		// 예측
		// DustDTO result = dustMapper.detailInfo2(dustDto);
		
		try {
            String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(result);

			System.out.println("=========================");
			System.out.println(boardJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	
	// RegionMapper test
	@Test
	public void testRegion() {
		RegionDTO regionDto = new RegionDTO();
		
		// region test
		 RegionDTO result = regionMapper.detailRegion(regionDto);
		// region detail test
//		 RegionDTO result = regionMapper.detailDetail(regionDto);
		// region test
//		RegionDTO result = regionMapper.detailObservatory(regionDto);
		
		
		try {
            String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(result);

			System.out.println("=========================");
			System.out.println(boardJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	
	// Board Test
	@Test
	public void testBoard() {
		BoardDTO boardDto  = new BoardDTO();
		
		// board test
		 BoardDTO result = boardMapper.detailBoard(boardDto);
		// report test
//		 BoardDTO result = boardMapper.detailReport(boardDto);
		// Comment test
//		BoardDTO result = boardMapper.detailComment(boardDto);
		// img test
//		BoardDTO result = boardMapper.detailImg(boardDto);
		// category
//		BoardDTO result = boardMapper.detailCategory(boardDto);
		
		
		try {
           String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(result);

			System.out.println("=========================");
			System.out.println(boardJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
	
	// 예보 날씨 test
	@Test
	public void testWeather() {
		WeatherDTO weatherDTO = new WeatherDTO();
		
//		weatherDTO.setDataNum(1);
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//		time = LocalDateTime.now();
//		System.out.println("현재 시간"+time);
		weatherDTO.setFcstDate("20220822");
		weatherDTO.setFcstTime(String.valueOf(LocalDateTime.now().getHour()));
		weatherDTO.setWsd(0);
		weatherDTO.setVec(0);
		weatherDTO.setPcp(null);
		weatherDTO.setReh(0);
		weatherDTO.setPty(0);
		weatherDTO.setSky(0);
		weatherDTO.setTmp(0);
		weatherDTO.setBaseDate("20220822");
		weatherDTO.setBaseTime(String.valueOf(LocalDateTime.now().getHour()));
		
		
		
		
		int result = weatherMapper.insertWeather(weatherDTO);
		System.out.println("result" + result);
		System.out.println(weatherDTO.toString());
	}

	

}

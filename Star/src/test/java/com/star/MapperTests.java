package com.star;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.star.domain.UserDTO;
import com.star.mapper.UserMapper;

@SpringBootTest
public class MapperTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void testOfInsert() {
		UserDTO params = new UserDTO();
		
		 params.setUserNickname("가영"); 
		 params.setUserId("zerobbang");
		 params.setUserEmail("zerobban123");
		 params.setUserPassword("01022223333");
		 params.setUserRegion("경기");

		int result = userMapper.insertUser(params);
		System.out.println(params.toString());
		System.out.println("result" + result + "입니다.");
	}
	
	@Test
	public void testMap() {
		String result = userMapper.testImg((long) 6);
		
		try {
            String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(result);

			System.out.println("=========================");
			System.out.println(boardJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
	
//	유저 정보 조회
	@Test
	public void testUser() {
		UserDTO result = userMapper.detailUser((long) 6);
		
		try {
            String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(result);

			System.out.println("=========================");
			System.out.println(boardJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

}

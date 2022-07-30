package com.star;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.star.domain.BoardDTO;
import com.star.mapper.BoardMapper;

@SpringBootTest
public class MapperTests {

	@Autowired
	private BoardMapper boardMapper;

	@Test
	public void testOfInsert() {
		BoardDTO params = new BoardDTO();
		
		 params.setUserNickname("가영"); 
		 params.setUserId("zerobbang");
		 params.setUserPhoneNumber("zerobban123");
		 params.setUserPassword("01022223333");
		 params.setUserRegion("서울");

		int result = boardMapper.insertUser(params);
		System.out.println("결과는 " + result + "입니다.");
	}

}

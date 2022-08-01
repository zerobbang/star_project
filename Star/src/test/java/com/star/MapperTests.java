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
		
		 params.setUserNickname("test"); 
		 params.setUserId("zerobbang");
		 params.setUserPhoneNumber("zerobban123");
		 params.setUserPassword("01022223333");
		 params.setUserRegion("test");

		int result = boardMapper.insertUser(params);
		System.out.println("testetst" + result + "tete.");
	}

}

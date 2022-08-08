package com.star;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.star.service.BoardService;

@SpringBootTest
public class ServiceTests {
	
	@Autowired
	BoardService boardService;
	
	@Test
	private void save() {
		
	}

}

package com.star.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.BoardDTO;

@Mapper
public interface BoardMapper {
	
	public int insertUser(BoardDTO params);

}

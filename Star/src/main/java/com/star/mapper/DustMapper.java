package com.star.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.DustDTO;

@Mapper
public interface DustMapper {
	
	// test 훈련
	public DustDTO detailInfo(DustDTO dustDto);
	
	// test 예측
	public DustDTO detailInfo2(DustDTO dustDto);
	
	// 예측 리스트 불러오기 (임시)
	public List<DustDTO> getPredictionList(DustDTO params);

}

package com.star.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.RegionDTO;

@Mapper
public interface RegionMapper {
	
	// 지역 테이블 조회
	public RegionDTO detailRegion(RegionDTO regionDto);
	
	// 지역 상세 테이블 조회
	public RegionDTO detailDetail(RegionDTO regionDto);
		
	// 관측지 테이블 조회
	public RegionDTO detailObservatory(RegionDTO regionDto);

}

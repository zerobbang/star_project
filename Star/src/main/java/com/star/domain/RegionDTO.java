package com.star.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionDTO {
	
	// 지역 테이블
	private String region;
	
	// 지역 상세 테이블
	private String lowerRegion;
	
	private String upperRegion;
	
	private float latitude;
	
	private float longitude;
	
	// 관측지 테이블
	private String observatory;
	
	private String placedRegion;
	
	private String observatoryAddress;
	
	private String observatoryImgUrl;
	
	private String observatoryPhone;
	
	private String observatoryHomepage;
	
	
	

}

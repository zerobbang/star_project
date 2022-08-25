package com.star.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.star.domain.WeatherDTO;

@Mapper
public interface WeatherMapper {

	// 예보 날씨 데이터 입력
	public int insertWeather(WeatherDTO weaterDTO);
}

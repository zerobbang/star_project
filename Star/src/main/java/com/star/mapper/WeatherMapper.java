package com.star.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.star.domain.WeatherDTO;

@Mapper
public interface WeatherMapper {

	// 예보 날씨 데이터 입력
	public int insertWeather(WeatherDTO weaterDTO);
	
	// 예보 날씨 불러오기
	public List<WeatherDTO> getWeather(@Param("fcstDate") String fcstDate, @Param("fcstTime")String fcstTime);
}

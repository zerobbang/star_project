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
	
	// 최근 24시간 까지의 기상 데이터 추출
	public List<WeatherDTO> getWeatherFromTime();

	// 선택지역의 모든 하위지역 추출
	public List<String> getLowerRegion(String region);

	public List<String> getAllLowerRegion();
}

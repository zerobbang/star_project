package com.star.service;

import org.springframework.stereotype.Service;

import com.star.domain.WeatherDTO;

@Service
public interface WeatherService {

	// 예보 데이터 저장
	public boolean insertWeather(WeatherDTO weatherDTO);
}

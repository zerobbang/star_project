package com.star.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.star.domain.WeatherDTO;
import com.star.mapper.WeatherMapper;

public class WeatherServiceImpl implements WeatherService {
	
	@Autowired
	private WeatherMapper weatherMapper;

	@Override
	public boolean insertWeather(WeatherDTO weatherDTO) {
		int result =weatherMapper.insertWeather(weatherDTO); 
		return (result == 1) ? true:false ;
	}

}

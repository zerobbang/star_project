package com.star.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.star.domain.WeatherDTO;
import com.star.mapper.WeatherMapper;

@Service
public class WeatherServiceImpl implements WeatherService {

	@Autowired
	private WeatherMapper weatherMapper;
	
	@Override
	public boolean insertWeather(WeatherDTO weatherDTO) {
		int result = weatherMapper.insertWeather(weatherDTO);
		
//		int result = 1;
		return (result == 1) ? true:false ;
	}

	@Override
	public List<WeatherDTO> getWeather(String fcstDate, String fcstTime) {
		List<WeatherDTO> weatherForcast = Collections.emptyList();
		weatherForcast = weatherMapper.getWeather(fcstDate, fcstTime);
		return weatherForcast;
	}

}

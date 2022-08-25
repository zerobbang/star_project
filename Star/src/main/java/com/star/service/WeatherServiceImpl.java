package com.star.service;

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
		// TODO Auto-generated method stub
		int result =weatherMapper.insertWeather(weatherDTO);
		
//		int result = 1;
		return (result == 1) ? true:false ;
	}

}

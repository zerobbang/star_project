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
//		weatherForcast = weatherMapper.getWeather(fcstDate, fcstTime);
		weatherForcast = weatherMapper.getWeatherFromTime();
		return weatherForcast;
	}

	@Override
	public List<String> getLowerRegion(String region) {
		
		List<String> lowerRegion = null;
		
		// TODO Auto-generated method stub
		if( (region == null) || (region == "전국") ) {
			lowerRegion = weatherMapper.getAllLowerRegion();
		} else {
			lowerRegion = weatherMapper.getLowerRegion(region);
		}
		
		
		return lowerRegion;
	}

}

package com.star.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DustDTO {
	
	private String region;
	
	private LocalDateTime date;
	
	private String weather;
	
	private float humidity;
	
	private float windSpeed;
	
	private String korWind;
	
	private int cnPm10;
	
	private int korPm10;

}

package com.star.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDTO {
	
	private int dataNum;
	
	private String fcstDate;
	
	private String fcstTime;
	
	private float wsd;
	
	private int vec;
	
	private String pcp;
	
	private int reh;
	
	private int pty;
	
	private int sky;
	
	private int tmp;
	
	private String baseDate;
	
	private String baseTime;
	
	@Override
	public String toString() {
		return "WeatherDTO [dataNum=" + dataNum + ", fcstDate=" + fcstDate + ", fcstTime=" + fcstTime + ", wsd=" + wsd
				+ ", vec=" + vec + ", pcp=" + pcp + ", reh=" + reh + ", pty=" + pty + ", sky=" + sky + ", tmp=" + tmp
				+ ", baseDate=" + baseDate + ", baseTime=" + baseTime + "]";
	}
	
}

package com.star.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {
	
	private String uuid;
	
	private String fileName;
	
	private String contentType;
	
	public FileDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public FileDTO(String uuid, String fileName, String contentType) {
		this.uuid = uuid;
		this.fileName = fileName;
		this.contentType = contentType;
		
		System.out.println(uuid);
		System.out.println(contentType);
	}

}

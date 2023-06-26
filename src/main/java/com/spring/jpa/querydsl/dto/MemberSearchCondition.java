package com.spring.jpa.querydsl.dto;

import lombok.Getter;

@Getter
public class MemberSearchCondition {

	private String username;
	private String teanName;
	private Integer ageGoe;
	private Integer ageLoe;
	
}

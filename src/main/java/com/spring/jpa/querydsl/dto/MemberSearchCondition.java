package com.spring.jpa.querydsl.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchCondition {

	private String username;
	private String teanName;
	private Integer ageGoe;
	private Integer ageLoe;
	
}

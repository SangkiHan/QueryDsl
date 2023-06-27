package com.spring.jpa.querydsl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString(of= {"id","username","age"})
public class Member extends BaseEntity {
	@Id @GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String username;
	private int age;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;
	
	public Member() {
	}
	
	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
	
	public Member(String username, int age) {
		this.username = username;
		this.age = age;
	}

	public Member(Long id, String username, int age) {
		this.id = id;
		this.username = username;
		this.age = age;
	}
}

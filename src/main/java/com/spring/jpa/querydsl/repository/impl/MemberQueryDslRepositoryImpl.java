package com.spring.jpa.querydsl.repository.impl;

import static com.spring.jpa.querydsl.entity.QMember.member;
import static com.spring.jpa.querydsl.entity.QTeam.team;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpa.querydsl.dto.MemberSearchCondition;
import com.spring.jpa.querydsl.dto.MemberTeamDto;
import com.spring.jpa.querydsl.dto.QMemberTeamDto;
import com.spring.jpa.querydsl.entity.Member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl{
	
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public void save(Member member) {
		em.persist(member);
	}

	public Optional<Member> findById(Long id) {
		return Optional.ofNullable(queryFactory
				.select(member)
				.from(member)
				.where(member.id.eq(id))
				.fetchOne());
	}

	public List<Member> findAll() {
		return queryFactory
				.select(member)
				.from(member)
				.fetch();
	}

	public List<Member> findByUsername(String username) {
		return queryFactory
				.select(member)
				.from(member)
				.where(member.username.eq(username))
				.fetch();
	}
	
	public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition){
		
		BooleanBuilder builder = new BooleanBuilder();
		if(StringUtils.hasText(condition.getUsername())) {
			builder.and(member.username.eq(condition.getUsername()));
		}
		if(StringUtils.hasText(condition.getTeanName())) {
			builder.and(team.name.eq(condition.getTeanName()));
		}
		if(condition.getAgeGoe()!=null) {
			builder.and(member.age.goe(condition.getAgeGoe()));
		}
		if(condition.getAgeLoe()!=null) {
			builder.and(member.age.goe(condition.getAgeLoe()));
		}
		
		return queryFactory
				.select(new QMemberTeamDto(
						member.id.as("memberId"),
						member.username,
						member.age,
						team.id.as("teamId"),
						team.name.as("teamName")
				))
				.from(member)
				.leftJoin(member.team, team)
				.where(builder)
				.fetch();
	}
	
	public List<MemberTeamDto> search(MemberSearchCondition condition){
		return queryFactory
				.select(new QMemberTeamDto(
						member.id.as("memberId"),
						member.username,
						member.age,
						team.id.as("teamId"),
						team.name.as("teamName")
				))
				.from(member)
				.leftJoin(member.team, team)
				.where(usernameEq(condition.getUsername()), 
						teamanameEq(condition.getTeanName()), 
						ageGoe(condition.getAgeGoe()), 
						ageLoe(condition.getAgeLoe()))
				.fetch();
	}
	
	private BooleanExpression usernameEq(String username) {
		return StringUtils.hasText(username) ? member.username.eq(username) : null;
	}
	
	private BooleanExpression teamanameEq(String teamname) {
		return StringUtils.hasText(teamname) ? team.name.eq(teamname) : null;
	}
	
	private BooleanExpression ageGoe(Integer age) {
		return age != null ? member.age.goe(age) : null;
	}
	
	private BooleanExpression ageLoe(Integer age) {
		return age != null ? member.age.loe(age) : null;
	}
}

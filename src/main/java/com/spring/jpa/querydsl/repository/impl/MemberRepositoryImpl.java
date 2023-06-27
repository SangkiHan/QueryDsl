package com.spring.jpa.querydsl.repository.impl;

import static com.spring.jpa.querydsl.entity.QMember.member;
import static com.spring.jpa.querydsl.entity.QTeam.team;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpa.querydsl.dto.MemberSearchCondition;
import com.spring.jpa.querydsl.dto.MemberTeamDto;
import com.spring.jpa.querydsl.dto.QMemberTeamDto;
import com.spring.jpa.querydsl.repository.MemberRepositoryCustom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
	
	private final JPAQueryFactory queryFactory;
	
	@Override
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

	@Override
	public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
		List<MemberTeamDto> results = queryFactory
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
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long count = queryFactory
				.select(member.count())
				.from(member)
				.leftJoin(member.team, team)
				.where(usernameEq(condition.getUsername()), 
						teamanameEq(condition.getTeanName()), 
						ageGoe(condition.getAgeGoe()), 
						ageLoe(condition.getAgeLoe()))
				.fetchOne();
		
		return new PageImpl<>(results, pageable, count);
	}

	@Override
	public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
		List<MemberTeamDto> results = queryFactory
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
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		JPAQuery<Long>  countQuery = queryFactory
				.select(member.count())
				.from(member)
				.leftJoin(member.team, team)
				.where(usernameEq(condition.getUsername()), 
						teamanameEq(condition.getTeanName()), 
						ageGoe(condition.getAgeGoe()), 
						ageLoe(condition.getAgeLoe()));
		
		return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
	}
	
}

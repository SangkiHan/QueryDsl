package com.spring.jpa.querydsl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.jpa.querydsl.dto.MemberSearchCondition;
import com.spring.jpa.querydsl.dto.MemberTeamDto;

public interface MemberRepositoryCustom {
	public List<MemberTeamDto> search(MemberSearchCondition condition);
	public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
	public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
}

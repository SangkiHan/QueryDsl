package com.spring.jpa.querydsl.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.querydsl.dto.MemberSearchCondition;
import com.spring.jpa.querydsl.dto.MemberTeamDto;
import com.spring.jpa.querydsl.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	
	private final MemberRepository memberRepository;
	
	@GetMapping("/v1/search")
	public Page<MemberTeamDto> searchMemberV1(MemberSearchCondition condition, Pageable pageable){
		return memberRepository.searchPageSimple(condition, pageable);
	}
	
	@GetMapping("/v2/search")
	public Page<MemberTeamDto> searchMemberV2(MemberSearchCondition condition, Pageable pageable){
		return memberRepository.searchPageComplex(condition, pageable);
	}

}

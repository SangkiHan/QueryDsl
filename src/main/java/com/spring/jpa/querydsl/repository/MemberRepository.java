package com.spring.jpa.querydsl.repository;

import java.util.List;
import java.util.Optional;

import com.spring.jpa.querydsl.entity.Member;

public interface MemberRepository {
	
	public void save(Member member);
	
	public Optional<Member> findById(Long id);
	
	public List<Member> findAll();
	
	public List<Member> findByUsername(String username);
}

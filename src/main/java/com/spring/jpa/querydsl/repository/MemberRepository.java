package com.spring.jpa.querydsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.jpa.querydsl.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{
	
	//select m from Member m where m username = :username
	List<Member> findByUsername(String username);
	
}

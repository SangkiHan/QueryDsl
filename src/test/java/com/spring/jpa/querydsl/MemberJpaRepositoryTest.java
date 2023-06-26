package com.spring.jpa.querydsl;

import java.util.List;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.spring.jpa.querydsl.entity.Member;
import com.spring.jpa.querydsl.repository.impl.MemberJpaRepositoryImpl;
import com.spring.jpa.querydsl.repository.impl.MemberQueryDslRepositoryImpl;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
	
	@Autowired private MemberJpaRepositoryImpl memberJpaRepository;
	@Autowired private MemberQueryDslRepositoryImpl memberQueryDslRepository;
	
	@Test
	public void JpaTest() {
		Member member = new Member("테스트1", 28);
		memberJpaRepository.save(member);
		
		Member findMember = memberJpaRepository.findById(member.getId()).get();
		
		Assertions.assertThat(findMember).isEqualTo(member);
		
		List<Member> members = memberJpaRepository.findAll();
		
		Assertions.assertThat(members).containsExactly(member);
	}
	
	@Test
	@Rollback(value = false)
	public void QueryDslTest() {
		Member member = new Member("테스트1", 28);
		memberQueryDslRepository.save(member);
		
		Member findMember = memberQueryDslRepository.findById(member.getId()).get();
		
		Assertions.assertThat(findMember).isEqualTo(member);
		
		List<Member> members = memberQueryDslRepository.findAll();
		
		Assertions.assertThat(members).containsExactly(member);
	}
}

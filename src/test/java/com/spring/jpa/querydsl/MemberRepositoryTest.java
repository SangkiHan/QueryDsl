package com.spring.jpa.querydsl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.spring.jpa.querydsl.dto.MemberSearchCondition;
import com.spring.jpa.querydsl.dto.MemberTeamDto;
import com.spring.jpa.querydsl.entity.Member;
import com.spring.jpa.querydsl.entity.Team;
import com.spring.jpa.querydsl.repository.MemberRepository;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	@Rollback(value = false)
	public void insert() {
		Team teamA = new Team("TeamA");
		Team teamB = new Team("TeamB");
		
		em.persist(teamA);
		em.persist(teamB);
		
		for(int i=0; i<100; i++) {
			Member member = new Member("member"+i, 28);
			member.changeTeam((i%2==0) ? teamB : teamA); 
			em.persist(member);
		}
	}
	
	@Test
	public void JpaTest() {
		Member member = new Member("테스트1", 28);
		memberRepository.save(member);
		
		Member findMember = memberRepository.findById(member.getId()).get();
		
		Assertions.assertThat(findMember).isEqualTo(member);
		
		List<Member> members = memberRepository.findAll();
		
		Assertions.assertThat(members).containsExactly(member);
	}
	
	@Test
	public void searchTest() {
		Team teamA = new Team("TeamA");
		Team teamB = new Team("TeamB");
		
		em.persist(teamA);
		em.persist(teamB);
		
		for(int i=0; i<10; i++) {
			Member member = new Member("member"+i, 28);
			member.changeTeam((i%2==0) ? teamB : teamA); 
			em.persist(member);
		}
		
		MemberSearchCondition condition = new MemberSearchCondition();
		PageRequest pageRequest = PageRequest.of(0, 3);
		
		Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition, pageRequest);
		
		Assertions.assertThat(result.getSize()).isEqualTo(3);
		
	}
}

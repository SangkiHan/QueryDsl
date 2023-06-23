package com.spring.jpa.querydsl;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpa.querydsl.entity.Member;

import static com.spring.jpa.querydsl.entity.QTeam.team;
import static com.spring.jpa.querydsl.entity.QMember.member;

import java.util.List;

@SpringBootTest
@Transactional
class QueryDslApplicationTests {
	
	@Autowired EntityManager em;
	JPAQueryFactory queryFactory;
	
	@BeforeEach
	public void before() {
		queryFactory = new JPAQueryFactory(em);
	}
	
	@Test
	@Rollback(value = false)
	public void insertMember() {
		for(int i=0; i<5; i++) {
			Member member = new Member("�׽�Ʈ"+i, 28);
			em.persist(member);
		}
	}

	@Test
	public void startQueryDsl() {
		Member findMember = queryFactory
				.select(member)
				.from(member)
				.where(member.username.eq("�׽�Ʈ1"))
				.fetchOne();
		
		System.out.println(findMember);
	}
	
	@Test
	public void search() {
		Member findMember = queryFactory
				.select(member)
				.from(member)
				.where(member.username.eq("�׽�Ʈ1")
						.and(member.age.eq(28)))
				.fetchOne();
		
		System.out.println(findMember);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void resultFetch() {
		List<Member> members = queryFactory
				.select(member)
				.from(member)
				.fetch();
		
		Member memberOne = queryFactory
				.selectFrom(member)
				.fetchOne();
		
		QueryResults<Member> results = queryFactory
				.selectFrom(member)
				.fetchResults();
		
	}
	
	@Test
	public void sort() {
		List<Member> results = queryFactory
					.selectFrom(member)
					.orderBy(member.age.desc(), member.username.asc().nullsLast())
					.fetch();
		
		System.out.println(results);
	}
	
	@Test
	public void paging1() {
		List<Member> results = queryFactory
					.selectFrom(member)
					.orderBy(member.age.desc())
					.offset(1)
					.limit(2)
					.fetch();
		
		System.out.println(results);
	}
	
	@Test
	public void aggregation() {
		Tuple tuple = queryFactory
				.select(
						member.count(),
						member.age.sum(),
						member.age.avg(),
						member.age.max(),
						member.age.min()
						)
				.from(member)
				.fetchOne();
		
		Long count = tuple.get(member.count());
		int sum = tuple.get(member.age.sum());
		Double avg = tuple.get(member.age.avg());
		int max = tuple.get(member.age.max());
		int min = tuple.get(member.age.min());
	}
	
	@Test
	public void TeamAvg() {
		List<Tuple> tuples = queryFactory
				.select(
						team.name, member.age.avg()
						)
				.from(member.team, team)
				.groupBy(team.id)
				.fetch();
		Tuple tuple = tuples.get(0);
		
		String count = tuple.get(team.name);
		double avg = tuple.get(member.age.avg());
	}
}

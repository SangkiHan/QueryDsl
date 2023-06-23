package com.spring.jpa.querydsl;

import static com.spring.jpa.querydsl.entity.QMember.member;
import static com.spring.jpa.querydsl.entity.QTeam.team;
import static com.querydsl.jpa.JPAExpressions.select;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpa.querydsl.entity.Member;
import com.spring.jpa.querydsl.entity.QMember;
import com.spring.jpa.querydsl.entity.Team;

@SpringBootTest
@Transactional
@SuppressWarnings("unused")
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
			Member member = new Member("테스트"+i, 28);
			em.persist(member);
		}
	}
	
	@Test
	@Rollback(value = false)
	public void insertTeam() {
		for(int i=0; i<5; i++) {
			Team team = new Team("Team"+i);
			em.persist(team);
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
				.from(member)
				.join(member.team, team)
				.groupBy(team.id)
				.fetch();
		Tuple tuple = tuples.get(0);
		
		String count = tuple.get(team.name);
		double avg = tuple.get(member.age.avg());
	}
	
	@Test
	public void JoinOn() {
		List<Tuple> results = queryFactory
				.select(member, team)
				.from(member)
				.join(member.team, team)
				.on(team.name.eq("TeamA"))
				.fetch();
		
		Tuple tuple = results.get(0);
		Member memberOne = tuple.get(member);
		Team teamOne = tuple.get(team);
	}
	
	@Test
	public void JoinOnNoRelation() {
		List<Tuple> results = queryFactory
				.select(member,team)
				.from(member)
				.leftJoin(member.team, team )
				.on(member.username.eq(team.name))
				.fetch();
	}		
	
	@Test
	public void FetchJoin() {
		Member findMember = queryFactory
				.select(member)
				.from(member)
				.join(member.team, team).fetchJoin()
				.where(member.username.eq("테스트1"))
				.fetchOne();
		
		System.out.println(findMember.getTeam());
	}
	
	@Test
	public void subQuery() {
		QMember memberSub = new QMember("subQuery");
		Member findMember = queryFactory
				.select(member)
				.from(member)
				.where(member.age.eq(
							select(memberSub.age.max())
							.from(memberSub)
						))
				.fetchOne();
	}
	
	/*
	 * From절에서의 서브쿼리를 지원하지 않는다.
	 * */
	@Test
	public void subQueryIn() {
		QMember memberSub = new QMember("subQuery");
		Member findMember = queryFactory
				.select(member)
				.from(member)
				.where(member.age.in(
							select(memberSub.age.max())
							.from(memberSub)
							.where(memberSub.age.gt(10))
						))
				.fetchOne();
	}
}

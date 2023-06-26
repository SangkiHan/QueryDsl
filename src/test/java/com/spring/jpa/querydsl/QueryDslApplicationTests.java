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

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpa.querydsl.dto.MemberDto;
import com.spring.jpa.querydsl.dto.QMemberDto;
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
	
	/*
	 * case when then
	 * */
	@Test
	public void basicCase() {
		List<String> results = queryFactory
				.select(member.age
						.when(10).then("열살")
						.when(20).then("스무살")
						.otherwise("기타"))
				.from(member)
				.fetch();
	}
	
	
	/*
	 * case when then
	 * */
	@Test
	public void complexCase() {
		List<String> results = queryFactory
				.select(
						new CaseBuilder()
						.when(member.age.between(0, 10)).then("0~10살")
						.when(member.age.between(10, 20)).then("10~20살")
						.otherwise("기타")
						)
				.from(member)
				.fetch();
		
		System.out.println(results);
	}
	
	/*
	 * 고정값들 지정해서 반환
	 * */
	@Test
	public void constant() {
		List<Tuple> results = queryFactory
				.select(member.username, Expressions.constant("HI"))
				.from(member)
				.fetch();
		
		System.out.println(results);
	}
	
	/*
	 * 문자열 합치기
	 * */
	@Test
	public void concat() {
		List<String> results = queryFactory
				.select(member.username.concat("_").concat(member.age.stringValue()))
				.from(member)
				.fetch();
		
		System.out.println(results);
	}
	
	/*
	 * DTO의 Setter메소드를 감지하여 반환
	 * */
	@Test
	public void projectionDtobySetter() {
		List<MemberDto> results = queryFactory
				.select(Projections.bean(MemberDto.class, member.username, member.age))
				.from(member)
				.fetch();
		
		System.out.println(results.toString());
	}
	
	/*
	 * 필드명으로 반환
	 * Dto 필드명과 Entity의 필드가 같아야한다.
	 * */
	@Test
	public void projectionDtobyField() {
		List<MemberDto> results = queryFactory
				.select(Projections.fields(MemberDto.class, member.username, member.age))
				.from(member)
				.fetch();
		
		System.out.println(results.toString());
	}
	
	/*
	 * DTO 생성자로 반환
	 * 컴파일 시점에서의 오류를 잡지못한다. 런타임시점에서 오류가 발생한다.
	 * */
	@Test
	public void projectionDtobyConstructor() {
		List<MemberDto> results = queryFactory
				.select(Projections.constructor(MemberDto.class, member.username, member.age))
				.from(member)
				.fetch();
		
		System.out.println(results.toString());
	}
	
	/*
	 * 서브쿼리의 별칭을 정할 때 사용
	 * */
	@Test
	public void ExpressionUtilAs() {
		List<MemberDto> results = queryFactory
				.select(Projections.constructor(MemberDto.class, 
						member.username, 
						ExpressionUtils.as(member.age, "age")))
				.from(member)
				.fetch();
		
		System.out.println(results.toString());
	}
	
	/*
	 * DTO를 Q클래스로 만들어서 반환
	 * */
	@Test
	public void QueryProjection() {
		List<MemberDto> results = queryFactory
				.select(
						new QMemberDto(member.username, member.age)
						)
				.from(member)
				.fetch();
		
		System.out.println(results.toString());
	}
	
	/*
	 * BooleanBuilder를 활용한 동적쿼리
	 * */
	@Test
	public void dynamicQuery_booleanBuilder1() {
		String username = "member1";
		Integer age = 10;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		if(username!=null) {
			builder.and(member.username.eq(username));
		}
		if(age!=null) {
			builder.and(member.age.eq(age));
		}
		
		List<MemberDto> results = queryFactory
				.select(new QMemberDto(member.username, member.age))
				.from(member)
				.where(builder)
				.fetch();
		
		System.out.println(results);
	}
	
	/*
	 * 메소드를 활용한 동적쿼리
	 * */
	@Test
	public void dynamicQuery_booleanBuilder2() {
		String username = "member1";
		Integer age = 10;
		
		List <MemberDto> results = queryFactory
				.select(new QMemberDto(member.username, member.age))
				.from(member)
				.where(usernameEq(username), ageEq(age))
				.fetch();
		
		System.out.println(results);
	}
	
	private BooleanExpression usernameEq(String username) {
		return username != null ? member.username.eq(username) : null;
	}
	
	private BooleanExpression ageEq(Integer age) {
		return age != null ? member.age.eq(age) : null;
	}
	
	/*
	 * bulk연산으로 대량 데이터를 update하면
	 * 영속성 컨텍스트와 DB데이터가 상이하기 때문에 초기화 해줘야한다.
	 * */
	@Test
	public void bulkUpdate() {
		long count = queryFactory
				.update(member)
				.set(member.age, member.age.add(1))
				.where(member.age.lt(30))
				.execute();
		
		em.flush();
		em.clear();
	}
	
	/*
	 * delete연산
	 * */
	@Test
	public void bulkDelete() {
		long count = queryFactory
				.delete(member)
				.where(member.age.lt(30))
				.execute();
	}
	
	/*
	 * Sql 함수호출
	 * */
	@Test
	public void sqlFunction() {
		List<String> result = queryFactory
				.select(Expressions.stringTemplate(
						"function('replace',{0},{1},{2})",
								member.username, "테스트", "Test"))
				.from(member)
				.fetch();
		
		System.out.println(result);
	}
}

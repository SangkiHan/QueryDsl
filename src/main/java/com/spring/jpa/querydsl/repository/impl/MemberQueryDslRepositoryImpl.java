package com.spring.jpa.querydsl.repository.impl;

import static com.spring.jpa.querydsl.entity.QMember.member;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpa.querydsl.entity.Member;
import com.spring.jpa.querydsl.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl implements MemberRepository{
	
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	@Override
	public void save(Member member) {
		em.persist(member);
	}

	@Override
	public Optional<Member> findById(Long id) {
		return Optional.ofNullable(queryFactory
				.select(member)
				.from(member)
				.where(member.id.eq(id))
				.fetchOne());
	}

	@Override
	public List<Member> findAll() {
		return queryFactory
				.select(member)
				.from(member)
				.fetch();
	}

	@Override
	public List<Member> findByUsername(String username) {
		return queryFactory
				.select(member)
				.from(member)
				.where(member.username.eq(username))
				.fetch();
	}
}

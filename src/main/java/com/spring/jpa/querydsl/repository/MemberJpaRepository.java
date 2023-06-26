package com.spring.jpa.querydsl.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpa.querydsl.entity.Member;

@Repository
public class MemberJpaRepository {
	
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	
	public MemberJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	public void save(Member member) {
		em.persist(member);
	}
	
	public Optional<Member> findById(Long id){
		Member findMember = em.find(Member.class, id);
		return Optional.ofNullable(findMember);
	}
	
	public List<Member> findAll(){
		return em.createQuery(
				"select m from Member m",Member.class)
				.getResultList();
	}
	
	public List<Member> findByUsername(String username) {
		return em.createQuery(
				"select m from Member m where username = :username",Member.class)
				.setParameter("username", username)
				.getResultList();
	}
	
}
### QueryDsl 검색조건
``` java
member.user.eq("테스트1")//username = '테스트1'
member.user.ne("테스트1")//username != '테스트1'
member.user.eq("테스트1").not()//username != '테스트1'

member.username.isNotNull()//username is not null

member.age.in(10,20)//age in (10,20)
member.age.notIn(10,20)//age not in (10,20)
member.age.between(10,30)//age between 10, 30

member.age.goe(30)//age >= 30
member.age.gt(30)//age > 30
member.age.loe(30)//age <= 30
member.age.lt(30)//age	< 30

member.username.like("테스트1%")//username like '테스트1%'
member.username.contains("테스트1")//username like '%테스트1%'
member.username.startsWith("테스트1")//username like '테스트1%'
```

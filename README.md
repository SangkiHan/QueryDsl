### QueryDsl �˻�����
``` java
member.user.eq("�׽�Ʈ1") 					//username = '�׽�Ʈ1'
member.user.ne("�׽�Ʈ1") 					//username != '�׽�Ʈ1'
member.user.eq("�׽�Ʈ1").not() 			//username != '�׽�Ʈ1'

member.username.isNotNull()				//username is not null

member.age.in(10,20)						//age in (10,20)
member.age.notIn(10,20)					//age not in (10,20)
member.age.between(10,30)				//age between 10, 30

member.age.goe(30)							//age >= 30
member.age.gt(30)							//age > 30
member.age.loe(30)							//age <= 30
member.age.lt(30)							//age	< 30

member.username.like("�׽�Ʈ1%")			//username like '�׽�Ʈ1%'
member.username.contains("�׽�Ʈ1")		//username like '%�׽�Ʈ1%'
member.username.startsWith("�׽�Ʈ1")	//username like '�׽�Ʈ1%'
```
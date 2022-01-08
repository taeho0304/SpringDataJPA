package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PreUpdate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEnitity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("testB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        /**
         * JPA의 em.persist를 호출하면 바로 DB에 Insert call을 날리지 않고
         * 영속성 Context안에 member, team 을 다 모아놓고 flush하면 강제로 DB Insert call을 날림
         */
        em.flush();

        /**
         * JPA 영속성 Context의 캐시를 다 날려버림
         */
        em.clear();

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for(Member member : members){
            System.out.println("member = "+ member);
            System.out.println("-> member.team = "+member.getTeam());
        }
    }

    @Test
    public void JpaEventBaseEntity() throws Exception {
        //given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist

        Thread.sleep(100);
        member.setUserName("member2");

        em.flush(); //@PreUpdate
        em.clear();
        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.createdDate = " + findMember.getCreatedDate());
        System.out.println("findMember.updatedDate = " + findMember.getLastModifiedDate());
        System.out.println("findMember.createdBy = " + findMember.getCreatedBy());
        System.out.println("findMember.updatedBy = " + findMember.getLastModifiedBy());
    }
}
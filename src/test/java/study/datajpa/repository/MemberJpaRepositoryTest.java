package study.datajpa.repository;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // -> Test가 끝날 때 다 Rollback을 시켜주고 JPA의 영속성 Context도 flush 안함 ( DB에 아무 쿼리도 보내지 않음 )
@Rollback(false) // 등록 쿼리 확인용 -> Rollback을 안하고 Commit 시킴 ( 실무에서는 사용 x )
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUserName()).isEqualTo(member.getUserName());

        /**
         * true가 나옴
         * JPA 특성상 같은 Transaction에서는 영속성 context의 동일성을 보장해줌 ( 1차 캐시 )
         * */
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}
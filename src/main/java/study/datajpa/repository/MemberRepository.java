package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

/**
 * @Repository 생략 가능
 * - 컴포넌트 스캔을 Spring Data JPA가 자동으로 처리
 * - JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 스프링 데이터 JPA는 메소드 이름을 분석해서 JPQL을 생성하고 실행
     * 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 함
     * -> 애플리케이션 로딩 시점에 오류를 인지할 수 있음
     */
    List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUserName") // 지워도 동작함
    List<Member> findByUserName(@Param("username") String username);

    /**
     * 애플리케이션 로딩 시점에 query 문을 파싱해 SQL을 다 만들어 놓기 때문에 문법 오류가 생기면 바로 알 수 있음
     */
    @Query("select m from Member m where m.userName= :username and m.age= :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.userName from Member m")
    List<String> findUserNameList();

    @Query("select new study.datajpa.dto.MemberDto( m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);
}
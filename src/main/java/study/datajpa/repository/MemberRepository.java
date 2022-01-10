package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Repository 생략 가능
 * - 컴포넌트 스캔을 Spring Data JPA가 자동으로 처리
 * - JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom , JpaSpecificationExecutor<Member>{
    /**
     * 스프링 데이터 JPA는 메소드 이름을 분석해서 JPQL을 생성하고 실행
     * 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 함
     * -> 애플리케이션 로딩 시점에 오류를 인지할 수 있음
     */
    List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUserName")
        // 지워도 동작함
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

    /**
     * 파라미터 바인딩은 위치 기반, 이름 기반 중 가급적 이름 기반을 사용
     * 위치 기반은 위치가 바껴버리면 에러가 발생할 수 있음
     * 가독성, 유지보수면에서 이름 기반이 낫다.
     */
    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUserName(String userName);
    Member findMemberByUserName(String userName);
    Optional<Member> findOptionalByUserName(String userName);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * bulk 연산 다음에는 가급적 영속성 context를 날려야함
     * bulk 연산 시 영속성 context를 무시하고 바로 DB 값을 바꿔주기 때문
     * */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age+1 where m.age >=:age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * fetch join은 연관관계가 있는 것들을 DB의 join을 활용해서 한번에 select에 포함시켜 다 가져온다.
     * DB의 조인은 그냥 단순 조인
     * */
    @Query("select  m from Member m left join fetch m.team")
    List<Member> findMemberByFetchJoin();

    /**
     * @EntityGraph를 사용하면 Jpql 없이도 객체그래프를 한번에 엮어서 성능 최적화를 해서 가져올 수 있음
     * */
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUserName(@Param("username") String username);

    @QueryHints(value = @QueryHint( name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUserName(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUserName(String username);
}
package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

/**
 * @Repository 생략 가능
 * - 컴포넌트 스캔을 Spring Data JPA가 자동으로 처리
 * - JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리
 */
public interface TeamRepository extends JpaRepository<Team, Long> { // 멤버 Entity, pk 타입
}

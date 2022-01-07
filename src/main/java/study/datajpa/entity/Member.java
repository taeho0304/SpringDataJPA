package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
/**
 * jpa 표준 스펙을 참고하면 @Entity는 기본적으로 Default 생성자가 하나 있어야 함!
 * access 레벨이 private 이면 안됨 ( 최소 protected까지 열어놔야함 )
 * -> JPA가 proxing 기술을 사용할 때 JPA구현체( Hibernate ) 들이 proxing 하고 객체를 강제로 만들어 내야하는데
 * 이때 private로 막아놓으면 그런게 다 막혀버릴 수 있다고 한다.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})

/**
 * Named Query는 애플리케이션 로딩 시점에 쿼리문을 파싱해 문법 오류가 있으면 알려주는 장점이 있음
 * */
@NamedQuery(
        name = "Member.findByUserName",
        query="select  m from Member m where m.userName = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
// 연관관계 필드는 toString 안하는게 좋음 ( Team ) -> 연관관계를 계속 타며 출력하기 때문에 무한루프에 빠질 수 있음
public class Member extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userName;
    private int age;

    /**
     * FetchType이 즉시로딩으로 걸려있으면 성능 최적화가 매우 어려워 가능한 Lazy로 셋팅
     * Lazy로 설정해 둘 경우 프록시 가짜 객체를 만들어서 놔둠
     * -> 이후 team.getName()과 같이 호출이 들어오면 그때 실제 DB에 쿼리를 날려서 값을 가져옴
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String userName) { this.userName = userName; }

    public Member(String userName, int age, Team team) {
        this.userName = userName;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}

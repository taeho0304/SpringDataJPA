package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String userName;

    /**
     jpa 표준 스펙을 참고하면 @Entity는 기본적으로 Default 생성자가 하나 있어야 함!
     access 레벨이 private 이면 안됨 ( 최소 protected까지 열어놔야함 )
     -> JPA가 proxing 기술을 사용할 때 JPA구현체( Hibernate ) 들이 proxing 하고 객체를 강제로 만들어 내야하는데
        이때 private로 막아놓으면 그런게 다 막혀버릴 수 있다고 한다.
     */
    protected Member() {
    }

    public Member(String userName) {
        this.userName = userName;
    }
}

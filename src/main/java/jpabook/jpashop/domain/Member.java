package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    /*
     1. 검증로직이 엔티티에 들어가면 좋지않다. (로직에 따라 다를 수 있기때문)
     2. 엔티티가 바뀌면 api스펙이 바뀐다(가 동작하지 않는다). -> 엔티티와 api가 1대1로 매핑된다.
     -> api요청스펙에 맞춰서 별도의 DTO를 파라미터로 받는 것이 좋다.
    */
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore// 양방향 연관관계라면 한쪽을 JsonIgnore 해줘야 무한 루프를 돌지 않음
    /*
    회원정보는 진짜 회원 관련된 정보만 나오도록 JsonIgnore
    -> 하지만 엔티티에 이런거 하면안된다.
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}

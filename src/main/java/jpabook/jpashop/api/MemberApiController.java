package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

// @Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 조회 API
    // v1
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    // v2 엔티티 -> DTO로 변환
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()    // member DTO로 변환
                .map(m -> new MemberDto(m.getName()))     // member엔티티에서 이름을 꺼내와서 DTO로 넣고
                .collect(Collectors.toList());            // 리스트로 바꾼다
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {   // Result 클래스로 컬렉션을 감싸서 향후 필요한 필드를 추가할 수 있다(안하면 배열로 담긴다)
        private int count;
        private T data;        // data 필드의 값은 리스트가 나간다.
    }

    // Member DTO
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
    ///////////////////////////////////////////

    // 회원 등록 API
    // v1 엔티티(Member을 파라미터에 노출시키고 바인딩하는 방법은 지양하자)
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {  // @RequestBody(JSON으로 온 body를 member에 넣어준다)로 들어오는 객체에 대한 검증을 수행하게 된다 -> @valid
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    // v2 DTO
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 수정 API
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(                 // update용 응답 dto
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {  // update용 request dto

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    // v1
    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    // v2 dto
    @Data
    static class CreateMemberRequest {
        private String Name;
    }

    // 수정 API
    @Data
    @AllArgsConstructor                    // 모든 필드 값을 파라미터로 받는 생성자를 만들어줌
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }
}

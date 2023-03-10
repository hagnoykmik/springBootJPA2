package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

// @Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // v1 엔티티(Member을 파라미터에 노출시키고 바인딩하는 방법은 지양하자)
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {  // @RequestBody(JSON으로 온 body를 member에 넣어준다)로 들어오는 객체에 대한 검증을 수행하게 된다 -> @valid
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // v2 DTO
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request)


    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}

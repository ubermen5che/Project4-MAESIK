package MAESIK.demo.controller;

import MAESIK.demo.domain.Member;
import MAESIK.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/api/members")
    public ResponseEntity<?> members() {

        List<Member> memberList = memberService.findAllMember();

        return ResponseEntity.ok(memberList);
    }

}

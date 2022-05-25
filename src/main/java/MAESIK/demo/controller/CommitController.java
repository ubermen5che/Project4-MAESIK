package MAESIK.demo.controller;

import MAESIK.demo.domain.Member;
import MAESIK.demo.repository.MemberGroupRepository;
import MAESIK.demo.service.CommitService;
import MAESIK.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommitController {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberGroupRepository memberGroupRepository;

    @Autowired
    CommitService commitService;

    @GetMapping("/commit/save")
    public ResponseEntity<?> stubCommitData(HttpServletRequest request, Authentication authentication) {
        Member member = memberService.findByOauthId(authentication.getName()).get();
        Long groupId = Long.parseLong(request.getParameter("groupId"));
        String repo = request.getParameter("repo");
        int ret = commitService.saveCommitData(member, repo, groupId);

        if (ret == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("{}");
        } else  {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
        }
    }

    /**
    @GetMapping("/commit")
    public void commitTest(Authentication authentication) {
        System.out.println(authentication.getName());
    }
    **/
}

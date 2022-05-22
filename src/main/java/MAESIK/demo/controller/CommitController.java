package MAESIK.demo.controller;

import MAESIK.demo.domain.*;
import MAESIK.demo.domain.dto.CommitDTO;
import MAESIK.demo.repository.CommitRepository;
import MAESIK.demo.repository.MemberGroupRepository;
import MAESIK.demo.service.CommitService;
import MAESIK.demo.service.MemberService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

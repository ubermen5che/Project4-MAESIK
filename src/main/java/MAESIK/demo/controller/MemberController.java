package MAESIK.demo.controller;

import MAESIK.demo.domain.MailInfo;
import MAESIK.demo.domain.Member;
import MAESIK.demo.jwt.TokenService;
import MAESIK.demo.service.ConfirmationTokenService;
import MAESIK.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final ConfirmationTokenService confirmationTokenService;
    private final TokenService tokenService;

    @Autowired
    public MemberController(MemberService memberService, ConfirmationTokenService confirmationTokenService, TokenService tokenService) {
        this.memberService = memberService;
        this.confirmationTokenService = confirmationTokenService;
        this.tokenService = tokenService;
    }

    // Proxy 객체 바인딩 오류 해결 필요.
    @GetMapping("/members")
    public ResponseEntity<?> members() {

        List<Member> memberList = memberService.findAllMember();

        return ResponseEntity.ok(memberList);
    }

    @GetMapping("confirm-email")
    public RedirectView viewConfirmEmail(@Valid @RequestParam String token){
        memberService.confirmEmail(token);

        return new RedirectView("/login");
    }

    @GetMapping("/auth/email/{email}")
    public ResponseEntity<?> emailAuth(@PathVariable String email, Authentication authentication) {
        Optional<Member> member = memberService.findByOauthId(authentication.getName());

        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JWT 토큰 정보를 검증할 수 없습니다.");
        }

        member.get().updateEmail(email);
        memberService.save(member.get());
        String tokenId = null;

        SimpleMailMessage simpleEmail = new SimpleMailMessage();
        simpleEmail.setTo(member.get().getEmail());
        simpleEmail.setSubject("회원가입 이메일 인증");

        MailInfo mailInfo = MailInfo.builder()
                .mailType("emailAuth")
                .build();

        tokenId = confirmationTokenService.createEmailConfirmationToken(authentication.getName(), member.get().getEmail(), simpleEmail, mailInfo);

        if (tokenId != null)
            return ResponseEntity.status(HttpStatus.OK).body("이메일 발송 완료.");
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 토큰 검증 오류.");
    }
}

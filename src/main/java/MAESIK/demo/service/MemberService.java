package MAESIK.demo.service;

import MAESIK.demo.domain.ConfirmationToken;
import MAESIK.demo.domain.Group;
import MAESIK.demo.domain.Member;
import MAESIK.demo.repository.MemberRepository;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public MemberService(MemberRepository memberRepository, ConfirmationTokenService confirmationTokenService) {
        this.memberRepository = memberRepository;
        this.confirmationTokenService = confirmationTokenService;
    }

    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }
    public Member save(Member member) {
        return memberRepository.save(member);
    }
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByOauthId(String oauthId) {
        return memberRepository.findByOauthId(oauthId);
    }

    /**
     * 이메일 인증 로직
     * @param token
     */
    public void confirmEmail(String token) {
        ConfirmationToken findConfirmationToken = confirmationTokenService.findByIdAndExpirationDateAfterAndExpired(token);

        if (findConfirmationToken == null) // 토큰 만료 체크
            return;

        Optional<Member> member = findByOauthId(findConfirmationToken.getUserId());
        member.get().updateEmailAuth(true);
        findConfirmationToken.useToken();	// 토큰 만료 로직을 구현해주면 된다. ex) expired 값을 true로 변경
        memberRepository.save(member.get());	// 유저의 이메일 인증 값 변경 로직을 구현해주면 된다. ex) emailVerified 값을 true로 변경
    }
}

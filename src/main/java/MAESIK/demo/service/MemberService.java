package MAESIK.demo.service;

import MAESIK.demo.domain.ConfirmationToken;
import MAESIK.demo.domain.Group;
import MAESIK.demo.domain.Member;
import MAESIK.demo.domain.MemberGroup;
import MAESIK.demo.repository.GroupRepository;
import MAESIK.demo.repository.MemberGroupRepository;
import MAESIK.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, ConfirmationTokenService confirmationTokenService, GroupRepository groupRepository, MemberGroupRepository memberGroupRepository) {
        this.memberRepository = memberRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.groupRepository = groupRepository;
        this.memberGroupRepository = memberGroupRepository;
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

    public Optional<Member> findByGithubId(String githubId) {
        return memberRepository.findByGithubId(githubId);
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
        System.out.println("emailAuth : " + member.get().getEmailAuth());
        memberRepository.save(member.get());	// 유저의 이메일 인증 값 변경 로직을 구현해주면 된다. ex) emailVerified 값을 true로 변경
    }

    public Boolean inviteConfirm(String token, Long groupId) {
        ConfirmationToken findConfirmationToken = confirmationTokenService.findByIdAndExpirationDateAfterAndExpired(token);

        if (findConfirmationToken == null) // 토큰 만료 체크
            return false;

        Optional<Member> member = findByGithubId(findConfirmationToken.getUserId());
        // Member의 MemberGroupList에
        Optional<Group> group = groupRepository.findById(groupId);
        MemberGroup memberGroup = MemberGroup.builder()
                .group(group.get())
                .memberGroupRepoUrlList(new ArrayList<>())
                .build();

        memberGroupRepository.save(memberGroup);
        member.get().getMemberGroupList().add(memberGroup); // 초대받은 그룹에 멤버 추가

        findConfirmationToken.useToken();
        memberRepository.save(member.get());

        return true;
    }
}

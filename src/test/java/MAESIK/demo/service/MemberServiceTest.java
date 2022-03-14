package MAESIK.demo.service;

import MAESIK.demo.domain.Group;
import MAESIK.demo.domain.Member;
import MAESIK.demo.domain.MemberGroup;
import MAESIK.demo.domain.Role;
import MAESIK.demo.domain.dto.CommitDTO;
import MAESIK.demo.repository.GroupRepository;
import MAESIK.demo.repository.MemberGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    public MemberServiceTest(MemberService memberService, GroupRepository groupRepository, MemberGroupRepository memberGroupRepository) {
        this.memberService = memberService;
        this.groupRepository = groupRepository;
        this.memberGroupRepository = memberGroupRepository;
    }

    private final MemberService memberService;
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;


    @Test
    public void saveMember() {
        Member member = Member.builder()
                .oauthId("1234")
                .email("ubermen5ch1308@gmail.com")
                .imageUrl("https://www.abc.com")
                .name("ubermen5che")
                .memberGroupList(new ArrayList<>())
                .role(Role.USER)
                .build();

        Member newMember = memberService.save(member);
        assertThat(newMember.getOauthId(), is("1234"));
    }

    @Test
    @Transactional
    public void createGroup() {
        Member member = Member.builder()
                .oauthId("1234")
                .email("ubermen5ch1308@gmail.com")
                .imageUrl("https://www.abc.com")
                .name("ubermen5che")
                .memberGroupList(new ArrayList<>())
                .role(Role.USER)
                .build();

        memberService.save(member);

        Group group1 = Group.builder()
                .groupName("알고 스터디 그룹")
                .groupMasterId(member.getName())
                .build();

        groupRepository.save(group1);

        Group group2 = Group.builder()
                .groupName("자바 스터디 그룹")
                .groupMasterId(member.getName())
                .build();

        groupRepository.save(group2);

        MemberGroup memberGroup1 = MemberGroup.builder()
                .group(group1)
                .build();

        memberGroupRepository.save(memberGroup1);


        MemberGroup memberGroup2 = MemberGroup.builder()
                .group(group2)
                .build();

        memberGroupRepository.save(memberGroup2);

        member.getMemberGroupList().add(memberGroup1);
        member.getMemberGroupList().add(memberGroup2);
        memberService.save(member);
        Member searchedMember = memberService.findById(member.getMemberId()).get();
        assertThat(searchedMember.getMemberGroupList().get(0).getGroup().getGroupName(), is("알고 스터디 그룹"));
        assertThat(searchedMember.getMemberGroupList().get(1).getGroup().getGroupName(), is("자바 스터디 그룹"));
    }
    @Test
    public void commitDataShouldBeSaved() {
        Member member = Member.builder()
                .oauthId("1234")
                .email("ubermen5ch1308@gmail.com")
                .imageUrl("https://www.abc.com")
                .name("ubermen5che")
                .role(Role.USER)
                .repoUrl("ubermen5che.github.io")
                .build();

        // github api 사용해서 commit 데이터 가져오기
        String url = "https://api.github.com/repos";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient wc = WebClient.builder().uriBuilderFactory(factory).baseUrl(url).build();

        Flux<CommitDTO> commitDTO = wc.get()
                .uri("/{name}/{repo}/commits", member.getName(), member.getRepoUrl())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CommitDTO.class);

        commitDTO.toStream().forEach(x -> {
            System.out.println(x.getCommit().getAuthor().getDate());
        });

        assertThat(commitDTO.blockLast().getCommit().getAuthor().getName(), is("ubermen5che"));
    }
}
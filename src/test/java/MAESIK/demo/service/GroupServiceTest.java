package MAESIK.demo.service;

import MAESIK.demo.domain.*;
import MAESIK.demo.domain.dto.CommitDTO;
import MAESIK.demo.repository.GroupRepository;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

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
import java.util.List;

@SpringBootTest
@Transactional
class GroupServiceTest {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final MemberGroupRepository memberGroupRepository;
    private final MemberService memberService;

    @Autowired
    GroupServiceTest(GroupRepository groupRepository, GroupService groupService, MemberGroupRepository memberGroupRepository, MemberService memberService) {
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.memberGroupRepository = memberGroupRepository;
        this.memberService = memberService;
    }

    @Test
    @Transactional
    void findGroup() {
        Group group1 = Group.builder()
                .groupName("알고 그룹")
                .groupMasterId("1")
                .build();

        Group group2 = Group.builder()
                .groupName("AI 그룹")
                .groupMasterId("2")
                .build();

        groupRepository.save(group1);
        groupRepository.save(group2);

        assertThat(groupRepository.findById(group1.getId()).get().getGroupName(), is("알고 그룹"));
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
                .groupMasterId(member.getOauthId())
                .build();

        groupRepository.save(group1);

        Group group2 = Group.builder()
                .groupName("자바 스터디 그룹")
                .groupMasterId(member.getOauthId())
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
    void findAllGroup() {
    }

    @Test
    @Transactional
    void repoUrlShouldBeSave() {
        //given
        Member member = Member.builder()
                .oauthId("1234")
                .email("ubermen5ch1308@gmail.com")
                .imageUrl("https://www.abc.com")
                .name("ubermen5che")
                .memberGroupList(new ArrayList<>())
                .role(Role.USER)
                .build();

        memberService.save(member);
        String groupName = "알고 스터디 그룹";
        Group group = groupService.createGroup(groupName, member.getOauthId());
        groupRepository.save(group);
        MemberGroup memberGroup = MemberGroup.builder()
                .memberGroupRepoUrlList(new ArrayList<>())
                .group(group)
                .build();
        memberGroupRepository.save(memberGroup);
        member.getMemberGroupList().add(memberGroup);
        String userRepoUrl = "https://github.com/abc";
        MemberGroupRepoUrl memberGroupRepoUrl = MemberGroupRepoUrl.builder()
                .repo(userRepoUrl)
                .build();

        memberGroup.getMemberGroupRepoUrlList().add(memberGroupRepoUrl);
        memberGroupRepository.save(memberGroup);
        //when
        member = memberService.findById(member.getMemberId()).get();
        System.out.println(member.getMemberId());
        List<MemberGroup> memberGroups = member.getMemberGroupList();
        assertThat(memberGroups, notNullValue());

        MemberGroup matchedMemberGroup = null;
        for (MemberGroup mg : memberGroups) {
            if (mg.getGroup().getId() == group.getId())
                matchedMemberGroup = mg;
        }
        MemberGroup found = memberGroupRepository.findById(matchedMemberGroup.getMemberGroupId()).get();
        //then
        assertThat(found.getMemberGroupRepoUrlList().size(), is(1));
    }

    @Test
    public void commitDataShouldBeSaved() {

        //given
        Member member = Member.builder()
                .oauthId("1234")
                .email("ubermen5ch1308@gmail.com")
                .imageUrl("https://www.abc.com")
                .name("ubermen5che")
                .memberGroupList(new ArrayList<>())
                .role(Role.USER)
                .build();

        memberService.save(member);

        Group group = Group.builder()
                .groupMasterId(member.getOauthId())
                .groupName("스프링 스터디")
                .build();

        groupService.createGroup(group.getGroupName(), member.getOauthId());

        MemberGroup memberGroup = MemberGroup.builder()
                .group(group)
                .memberGroupRepoUrlList(new ArrayList<>())
                .build();

        MemberGroupRepoUrl memberGroupRepoUrl = MemberGroupRepoUrl.builder()
                .repo("knu-hackerton21")
                .commitEachRepo(new ArrayList<>())
                .build();

        memberGroup.getMemberGroupRepoUrlList().add(memberGroupRepoUrl);
        memberGroupRepository.save(memberGroup);

        member.getMemberGroupList().add(memberGroup);
        memberService.save(member);

        // github api 사용해서 commit 데이터 가져오기
        String url = "https://api.github.com/repos";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient wc = WebClient.builder().uriBuilderFactory(factory).baseUrl(url).build();

        Flux<CommitDTO> commitDTO = wc.get()
                .uri("/{name}/{repo}/commits", member.getName(), memberGroup.getMemberGroupRepoUrlList().get(0).getRepo())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CommitDTO.class);

        MemberGroupRepoUrl foundedRepoUrl = null;
        for (MemberGroupRepoUrl memberGroupRepoUrl1 : memberGroup.getMemberGroupRepoUrlList()) {
            System.out.println(memberGroupRepoUrl1.getRepo());
            if (memberGroupRepoUrl1.getRepo().equals(memberGroupRepoUrl.getRepo())) {
                foundedRepoUrl = memberGroupRepoUrl1;
            }
        }

        List<CommitDAO> commitDAOS = foundedRepoUrl.getCommitEachRepo();

        commitDTO.toStream().forEach(x -> {
            CommitDAO commitDAO = CommitDAO.builder()
                    .sha(x.getSha())
                    .url(x.getUrl())
                    .commitUrl(x.getCommit().getUrl())
                    .date(x.getCommit().getAuthor().getDate())
                    .email(x.getCommit().getAuthor().getEmail())
                    .message(x.getCommit().getMessage())
                    .build();
            commitDAOS.add(commitDAO);
        });

        assertThat(commitDAOS.size(), is(30));
        assertThat(commitDTO.blockLast().getCommit().getAuthor().getName(), is("ubermen5che"));
    }
}
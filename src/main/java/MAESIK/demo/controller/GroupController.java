package MAESIK.demo.controller;

import MAESIK.demo.domain.*;
import MAESIK.demo.domain.dto.*;
import MAESIK.demo.repository.CommitRepository;
import MAESIK.demo.repository.GroupRepository;
import MAESIK.demo.repository.MemberGroupRepository;
import MAESIK.demo.repository.MemberRepository;
import MAESIK.demo.service.ConfirmationTokenService;
import MAESIK.demo.service.EmailSenderService;
import MAESIK.demo.service.GroupService;
import MAESIK.demo.service.MemberService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
public class GroupController {

    @Autowired
    public GroupController(GroupService groupService, MemberService memberService, MemberGroupRepository memberGroupRepository) {
        this.groupService = groupService;
        this.memberService = memberService;
        this.memberGroupRepository = memberGroupRepository;
    }

    GroupService groupService;
    MemberService memberService;
    MemberGroupRepository memberGroupRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CommitRepository commitRepository;

    @PostMapping("/group")
    public ResponseEntity<List<GroupResponseDTO>> findGroups(@RequestBody MemberRequestDTO memberRequestDTO, Authentication authentication) {

        Optional<Member> member = memberService.findByOauthId(authentication.getName());
        List<GroupResponseDTO> listGroup = groupRepository.findGroupDTOs(member.get().getOauthId());


        return ResponseEntity.ok().body(listGroup);
    }

    @GetMapping("/get-user-commit-data")
    public ResponseEntity<?> getUserCommitData() {
        List<AllUserCommitDataDTO> allUserCommitDataDTOS = new ArrayList<>();
        List<MemberGroup> memberGroupList = memberGroupRepository.findAll();
        DateTimeZone zone = DateTimeZone.forID("Asia/Seoul"); // Or DateTimeZone.UTC
        DateTime nowDt = new DateTime(DateTime.now(), zone);

        long currentMillis = nowDt.getMillis();
        Long beforeOneDay = currentMillis - 3600L * 24L * 1000L;

        for (MemberGroup memberGroup : memberGroupList) {
            List<MemberGroupRepoUrl> memberGroupRepoUrlList = memberGroup.getMemberGroupRepoUrlList();
            for (MemberGroupRepoUrl memberGroupRepoUrl : memberGroupRepoUrlList) {
                List<CommitDAO> commitDAOS = commitRepository.findByMemberGroupRepoIdAndCommitDateGreaterThanOrderByCommitDateDesc(memberGroupRepoUrl.getMemberGroupRepoId(), beforeOneDay);
                allUserCommitDataDTOS.add(AllUserCommitDataDTO.builder()
                        .oAuthId(memberGroup.getMember().getOauthId())
                        .commitDAOS(commitDAOS)
                        .build());
            }
        }

        if (allUserCommitDataDTOS.isEmpty()) {
            return ResponseEntity.ok().body("User commit data is empty");
        } else {
            return ResponseEntity.ok().body(allUserCommitDataDTOS);
        }
    }

    @GetMapping("/group/{id}/statistics")
    public ResponseEntity<?> sendGroupStatistics(@PathVariable Long id, Authentication authentication) {
        // groupId 기반으로 그룹에 속한 모든 멤버 리스트를 가져온다.
        List<GroupStatisticsDTO> response = new ArrayList<>();
        List<MemberGroup> memberGroupList = memberGroupRepository.findByGroup_Id(id);

        // 그룹에 속해 있는 멤버들의 각 repo의 커밋정보를 읽어들인 후
        // GroupStatisticsDTO에 유저명, 커밋갯수를 맵핑하여 List로 만든 후 response한다.
        for (MemberGroup mg : memberGroupList) {
            GroupStatisticsDTO groupStatisticsDTO = new GroupStatisticsDTO();
            List<MemberGroupRepoUrl> memberGroupRepoUrlList = mg.getMemberGroupRepoUrlList();
            String githubName = "";
            int commitCnt = 0;
            for (MemberGroupRepoUrl mgr : memberGroupRepoUrlList) {
                List<CommitDAO> commitDAOList = commitRepository.findByMemberGroupRepoId(mgr.getMemberGroupRepoId());
                if (commitDAOList.isEmpty()) {
                    continue;
                } else {
                    githubName = commitDAOList.get(0).getName();
                    commitCnt = commitDAOList.size();   //추후 커밋 반영 시간 고려해서 커밋 갯수 계산해야함.
                    break; // mgr은 우선 하나만 있다고 가정
                }
            }
            groupStatisticsDTO.setUsername(githubName);
            groupStatisticsDTO.setNumOfCommit(commitCnt);
            response.add(groupStatisticsDTO);
        }

        if (response.isEmpty()) {
            return ResponseEntity.ok().body("Not exist Data");
        } else {
            return ResponseEntity.ok().body(response);
        }
    }
    @PostMapping("/group/create")
    public ResponseEntity<?> create(@Valid @RequestBody GroupRequestDTO groupRequestDTO, Authentication authentication) throws URISyntaxException {

        String groupName = groupRequestDTO.getGroupName();
        Group createdGroup = groupService.createGroup(groupName, authentication.getName());

        URI location = new URI("/group/" + createdGroup.getId());
        return ResponseEntity.created(location).body("{}");
    }

    @GetMapping("/group/delete/{id}")
    public ResponseEntity<?> groupDelete(@PathVariable Long id, Authentication authentication) {
        Member member = memberService.findByOauthId(authentication.getName()).get();

        // 권한 확인

        // 그룹을 삭제하려면 우선 그룹에 가입되어있는 모든 유저들의 그룹 가입 정보들을 지우고 그룹을 지워야한다.

        List<MemberGroup> allMemberGroup = memberGroupRepository.findAll();

        for (MemberGroup memberGroup : allMemberGroup) {
            if (memberGroup.getGroup().getId() == id) {
                memberGroupRepository.delete(memberGroup);
            }
        }

        List<MemberGroup> memberGroupList = member.getMemberGroupList();

        for (MemberGroup mg : memberGroupList) {
            Group group = mg.getGroup();
            if (group.getId() == id) {
                if (!group.getGroupMasterId().equals(member.getOauthId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
                }

                memberGroupRepository.deleteById(mg.getMemberGroupId());
                memberGroupList.remove(mg);
                //memberGroupRepository.save(mg);
                return ResponseEntity.ok().body(GroupResponseDTO.builder()
                        .groupId(group.getId())
                        .groupMasterId(group.getGroupMasterId())
                        .groupExp(group.getGroupExp())
                        .groupName(group.getGroupName())
                        .groupTier(group.getGroupTier())
                        .build());
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/group/add/repourl")
    public ResponseEntity<?> addRepoUrl(@Valid @RequestBody GroupRequestDTO groupRequestDTO, Authentication authentication) {
        Optional<Member> member = memberService.findByOauthId(authentication.getName());

        System.out.println(member.toString());
        List<MemberGroup> memberGroupList = member.get().getMemberGroupList();

        // 멤버가 가입한 그룹 리스트 둘 중 현재 url추가 하고자 하는 그룹을 필터링
        MemberGroup searchedMemberGroup = null;

        for (MemberGroup memberGroup : memberGroupList) {
            if (memberGroup.getGroup().getId() == groupRequestDTO.getGroupId()) {
                searchedMemberGroup = memberGroup;
                break;
            }
        }

        if (searchedMemberGroup == null) {
            return ResponseEntity.badRequest().body("searchedMemberGroup is null");
        }

        searchedMemberGroup.getMemberGroupRepoUrlList().add(MemberGroupRepoUrl.builder().
                repo(groupRequestDTO.getRepoUrl())
                .build());

        memberGroupRepository.save(searchedMemberGroup);

        return ResponseEntity.ok().body("{}");
    }

    @PostMapping("/group/update/repourl")
    public ResponseEntity<?> updateRepoUrl(@Valid @RequestBody GroupRequestDTO groupRequestDTO, Authentication authentication) {
        Optional<Member> member = memberService.findByOauthId(authentication.getName());

        List<MemberGroup> memberGroupList = member.get().getMemberGroupList();

        // 멤버가 가입한 그룹 리스트 둘 중 현재 url추가 하고자 하는 그룹을 필터링
        MemberGroup searchedMemberGroup = null;

        for (MemberGroup memberGroup : memberGroupList) {
            if (memberGroup.getGroup().getId() == groupRequestDTO.getGroupId()) {
                searchedMemberGroup = memberGroup;
                break;
            }
        }

        if (searchedMemberGroup == null) {
            return ResponseEntity.badRequest().body("searchedMemberGroup is null");
        }

        List<MemberGroupRepoUrl> memberGroupRepoUrlList= searchedMemberGroup.getMemberGroupRepoUrlList();

        Boolean isUpdated = false;

        for (MemberGroupRepoUrl mu : memberGroupRepoUrlList) {
            if (mu.getRepo().equals(groupRequestDTO.getRepoUrl())) {
                mu.setRepo(groupRequestDTO.getUpdateUrl());
                isUpdated = true;
            }
        }

        if (isUpdated) {
            memberGroupRepository.save(searchedMemberGroup);
            return ResponseEntity.ok().body("{}");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/group/info")
    public ResponseEntity<GroupResponseDTO> getGroupInfo(Long groupId, Authentication authentication) {
        Optional<Member> member = memberService.findByOauthId(authentication.getName());

        List<MemberGroup> memberGroupList = member.get().getMemberGroupList();

        for (MemberGroup memberGroup : memberGroupList) {
            if (memberGroup.getGroup().getId() == groupId) {
                Group group = memberGroup.getGroup();
                return ResponseEntity.ok().body(GroupResponseDTO.builder()
                        .groupId(group.getId())
                        .groupMasterId(group.getGroupMasterId())
                        .groupName(group.getGroupName())
                        .groupTier(group.getGroupTier())
                        .groupExp(group.getGroupExp())
                        .build());
            }
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/group/invite")
    public ResponseEntity<?> invite(@RequestBody InviteDTO inviteDTO, Authentication authentication) {
        Boolean authFlag = true;

        List<String> notAuthMemberList = new ArrayList<>();
        List<Member> authMemberList = new ArrayList<>();

        if (inviteDTO.getMemberIds().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<MemberRequestDTO> invitedMemberList = inviteDTO.getMemberIds();

        for (MemberRequestDTO memberRequestDTO : invitedMemberList) {
            Optional<Member> member = memberService.findByGithubId(memberRequestDTO.getGithubId());
            if (member.get().getEmailAuth() == false) {
                notAuthMemberList.add(member.get().getGithubId());
                authFlag = false;
            } else {
                authMemberList.add(member.get());
            }
        }

        if (authFlag == false) {
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(notAuthMemberList);
        } else {
            for (Member member : authMemberList) {
                SimpleMailMessage simpleEmail = new SimpleMailMessage();
                simpleEmail.setTo(member.getEmail());
                simpleEmail.setSubject("그룹 초대");
                MailInfo mailInfo = MailInfo.builder()
                        .groupId(inviteDTO.getGroupId())
                        .mailType("inviteAuth")
                        .build();

                confirmationTokenService.createEmailConfirmationToken(member.getGithubId(), member.getEmail(), simpleEmail, mailInfo);
            }
        }

        return ResponseEntity.ok().body("{}");
    }

    @GetMapping("/group/accept/{token}/{groupId}")
    public ResponseEntity<?> inviteConfirm(@PathVariable(name = "token") String token, @PathVariable(name = "groupId") Long groupId) throws URISyntaxException {
        if (memberService.inviteConfirm(token, groupId)) {
            URI location = new URI("/group/" + groupId);
            return ResponseEntity.created(location).body("{}");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

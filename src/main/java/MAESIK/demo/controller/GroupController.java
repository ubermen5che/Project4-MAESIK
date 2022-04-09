package MAESIK.demo.controller;

import MAESIK.demo.domain.Group;
import MAESIK.demo.domain.Member;
import MAESIK.demo.domain.MemberGroup;
import MAESIK.demo.domain.MemberGroupRepoUrl;
import MAESIK.demo.domain.dto.GroupRequestDTO;
import MAESIK.demo.domain.dto.GroupResponseDTO;
import MAESIK.demo.domain.dto.MemberRequestDTO;
import MAESIK.demo.repository.GroupRepository;
import MAESIK.demo.repository.MemberGroupRepository;
import MAESIK.demo.service.GroupService;
import MAESIK.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.net.URISyntaxException;
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
    GroupRepository groupRepository;

    @PostMapping("/group")
    public ResponseEntity<List<GroupResponseDTO>> findGroups(@RequestBody MemberRequestDTO memberRequestDTO, Authentication authentication) {

        Optional<Member> member = memberService.findByOauthId(authentication.getName());
        List<GroupResponseDTO> listGroup = groupRepository.findGroupDTOs(member.get().getOauthId());


        return ResponseEntity.ok().body(listGroup);
    }

    @PostMapping("/group/create")
    public ResponseEntity<?> create(@Valid @RequestBody GroupRequestDTO groupRequestDTO, Authentication authentication) throws URISyntaxException {

        String groupName = groupRequestDTO.getGroupName();
        Group createdGroup = groupService.createGroup(groupName, authentication.getName());

        URI location = new URI("/group/" + createdGroup.getId());
        return ResponseEntity.created(location).body("{}");
    }

    @Transactional
    @GetMapping("/group/delete/{id}")
    public ResponseEntity<?> groupDelete(@PathVariable Long id, Authentication authentication) {
        Member member = memberService.findByOauthId(authentication.getName()).get();

        List<MemberGroup> memberGroupList = member.getMemberGroupList();

        for (MemberGroup mg : memberGroupList) {
            Group group = mg.getGroup();
            if (group.getId() == id) {
                if (group.getGroupMasterId() != member.getOauthId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
                }
                groupRepository.deleteById(group.getId());
                memberGroupRepository.delete(mg);
                memberGroupList.remove(mg);
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
}

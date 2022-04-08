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
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

    @PostMapping("/group/addRepoUrl")
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
}

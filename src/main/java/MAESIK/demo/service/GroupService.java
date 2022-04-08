package MAESIK.demo.service;

import MAESIK.demo.domain.Group;
import MAESIK.demo.domain.Member;
import MAESIK.demo.domain.MemberGroup;
import MAESIK.demo.repository.GroupRepository;
import MAESIK.demo.repository.MemberGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private GroupRepository groupRepository;
    private MemberGroupRepository memberGroupRepository;
    private MemberService memberService;

    @Autowired
    public GroupService(GroupRepository groupRepository, MemberGroupRepository memberGroupRepository, MemberService memberService) {
        this.groupRepository = groupRepository;
        this.memberGroupRepository = memberGroupRepository;
        this.memberService = memberService;
    }

    public Optional<Group> findGroup(Long id) {
        return groupRepository.findById(id);
    }

    public List<Group> findAllGroup() {
        return groupRepository.findAll();
    }

    public Group createGroup(String groupName, String oAuthId) {
        Group group = Group.builder()
                .groupMasterId(oAuthId)
                .groupName(groupName)
                .build();

        Group savedGroup = groupRepository.save(group);

        MemberGroup memberGroup = MemberGroup.builder()
                .group(savedGroup)
                .memberGroupRepoUrlList(new ArrayList<>())
                .build();

        Optional<Member> member = memberService.findByOauthId(oAuthId);
        member.get().getMemberGroupList().add(memberGroup);

        memberGroupRepository.save(memberGroup);
        memberService.save(member.get());

        return savedGroup;
    }
}

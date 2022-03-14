package MAESIK.demo.service;

import MAESIK.demo.domain.Group;
import MAESIK.demo.repository.GroupRepository;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GroupServiceTest {

    private final GroupRepository groupRepository;

    @Autowired
    GroupServiceTest(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Test
    void findGroup() {
        Group group1 = Group.builder()
                .groupName("알고 그룹")
                .groupMasterId("1234D")
                .build();

        Group group2 = Group.builder()
                .groupName("AI 그룹")
                .groupMasterId("4321D")
                .build();

        groupRepository.save(group1);
        groupRepository.save(group2);

        assertThat(groupRepository.findById(1L).get().getGroupName(), is("알고 그룹"));
    }

    @Test
    void findAllGroup() {
    }
}
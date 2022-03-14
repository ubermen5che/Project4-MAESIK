package MAESIK.demo.service;

import MAESIK.demo.domain.Group;
import MAESIK.demo.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Optional<Group> findGroup(Long id) {
        return groupRepository.findById(id);
    }

    public List<Group> findAllGroup() {
        return groupRepository.findAll();
    }
}

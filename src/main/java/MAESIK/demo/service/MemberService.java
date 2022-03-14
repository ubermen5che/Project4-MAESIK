package MAESIK.demo.service;

import MAESIK.demo.domain.Member;
import MAESIK.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
}

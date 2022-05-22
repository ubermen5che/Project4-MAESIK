package MAESIK.demo.controller;

import MAESIK.demo.domain.Member;
import MAESIK.demo.domain.dto.UserDTO;
import MAESIK.demo.exception.ResourceNotFoundException;
import MAESIK.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserDTO getCurrentUser(Authentication authentication) {
        Member member = memberRepository.findByOauthId(String.valueOf(authentication.getName()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authentication.getName()));

        UserDTO userDto = UserDTO.builder()
                .name(member.getName())
                .oauthId(member.getOauthId())
                .imageUrl(member.getImageUrl())
                .build();
        return userDto;
    }
}
package MAESIK.demo.service;

import MAESIK.demo.domain.Member;
import MAESIK.demo.domain.OAuthAttributes;
import MAESIK.demo.domain.UserProfile;
import MAESIK.demo.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    public OAuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth 서비스(github, google, naver)에서 가져온 유저 정보를 담고있음

        String registrationId = userRequest.getClientRegistration()
                                           .getRegistrationId(); // OAuth 서비스 이름(ex. github, naver, google)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                  .getUserInfoEndpoint().getUserNameAttributeName(); // OAuth 로그인 시 키(pk)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // OAuth 서비스의 유저 정보들

        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌

        Member member = saveOrUpdate(userProfile); // DB에 저장

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes,
                userNameAttributeName);
    }

    private Member saveOrUpdate(UserProfile userProfile) {
        Member member = memberRepository.findByOauthId(userProfile.getOauthId())
                                        .map(m -> m.update(userProfile.getName(), userProfile.getEmail(), userProfile.getImageUrl())) // OAuth 서비스 사이트에서 유저 정보 변경이 있을 수 있기 때문에 우리 DB에도 update
                                        .orElse(userProfile.toMember());
        return memberRepository.save(member);
    }
}

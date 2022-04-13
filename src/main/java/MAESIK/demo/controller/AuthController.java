package MAESIK.demo.controller;

import MAESIK.demo.domain.dto.TokenDto;
import MAESIK.demo.domain.dto.TokenRequestDTO;
import MAESIK.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDTO tokenRequestDto) {
        try {
            TokenDto tokenDto = authService.reissue(tokenRequestDto);
            return ResponseEntity.ok(tokenDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

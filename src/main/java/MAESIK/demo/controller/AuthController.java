package MAESIK.demo.controller;

import MAESIK.demo.domain.dto.TokenDTO;
import MAESIK.demo.domain.dto.TokenRequestDTO;
import MAESIK.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDTO tokenRequestDto) {
        try {
            TokenDTO tokenDto = authService.reissue(tokenRequestDto);
            return ResponseEntity.ok(tokenDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok().body("hello");
    }
}

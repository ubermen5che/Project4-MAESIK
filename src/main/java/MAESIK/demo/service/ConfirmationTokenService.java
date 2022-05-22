package MAESIK.demo.service;

import MAESIK.demo.domain.ConfirmationToken;
import MAESIK.demo.domain.MailInfo;
import MAESIK.demo.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    /**
     * 이메일 인증 토큰 생성
     * @return
     */
    public String createEmailConfirmationToken(String userId, String receiverEmail, SimpleMailMessage mailMessage, MailInfo mailInfo){

        Assert.hasText(userId,"userId는 필수 입니다.");
        Assert.hasText(receiverEmail,"receiverEmail은 필수 입니다.");

        ConfirmationToken emailConfirmationToken = ConfirmationToken.createEmailConfirmationToken(userId);
        confirmationTokenRepository.save(emailConfirmationToken);

        if (mailInfo.getMailType().equals("emailAuth")) {
            mailMessage.setText("http://localhost:8080/confirm-email?token="+emailConfirmationToken.getId());
        } else if (mailInfo.getMailType().equals("inviteAuth")) {
            mailMessage.setText("http://localhost:8080/group/accept/"+emailConfirmationToken.getId()+"/"+mailInfo.getGroupId());
        }

        emailSenderService.sendEmail(mailMessage);
        return emailConfirmationToken.getId();
    }

    /**
     * 유효한 토큰 가져오기
     * @param confirmationTokenId
     * @return
     */
    public ConfirmationToken findByIdAndExpirationDateAfterAndExpired(String confirmationTokenId){
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByIdAndExpirationDateAfterAndExpired(confirmationTokenId, LocalDateTime.now(),false);
        if (!confirmationToken.isEmpty())
            return confirmationToken.get();
        else
            return null;
    };

}

package HiobsServer.utilities;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Den 4.12.2024
 */

@Component
public class MailSenden {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}") private String sender;

    /**
     * BENUTZT: von ApiLoginController/ @PostMapping(value = "/loginApi")
     * <br><br>
     *
     * PARAMETER:   emailParam → example@example.com, als String (kein json)
     *              aktivierungCode → 1234 (int)
     *
     *  RETURN:     'versendet' → benutzt in ApiLoginController/ @PostMapping(value = "/loginApi")
     *              'nichtversendet' → zurzeit keine verwendung
     *
     *  FAZIT:      versendet wird nur aktivierungCode für die Anmeldung, basiert auf spring boot
     *              durch die google-mail-adresse
     *
     *  VORAUSSETZUNG:  pom.xml → 2 anhänichkeit
     *                  application.properties → google anmeldung an gmail, mit Google-App-Password
     *                  spring boot ->  @Autowired private JavaMailSender mailSender;
     *                  BEISPIEL: -> https://www.baeldung.com/spring-email (3.2 + 4.2)
     *
     * @return
     */
    public String sendEmail(String emailParam, String textParam, String subjectParam) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(emailParam);
            helper.setText( textParam, true);
            helper.setSubject(subjectParam);
            mailSender.send(mimeMessage);

            return "versendet";
        } catch (MessagingException ex) {
            //return "Error, keine E-Mail sendung";
            return "nichtversendet";
        }
    }
}

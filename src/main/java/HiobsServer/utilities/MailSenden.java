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
    public String sendEmail(String emailParam, int anmeldeCode) {

        String textParam = "<p>hier erhalten Sie ihre Messenger Anmelde Code </p>"
                +"<b>" + anmeldeCode + "</b>"
                +"<p>Bitte beachten Sie, dass dieser Token nur dieser Sitzung g&#252;ltig ist. </p>"
                +"<p>mit Freundlichen Gr&#252;ßen</p>"
                +"<p>Ihr Hiobs Post Team</p>";
        String subjectParam = "Hiobs Post: aktuelle Anmelde Code";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(emailParam);
            helper.setText( textParam, true);
            helper.setSubject(subjectParam);
            //mailSender.send(mimeMessage);

            return "versendet";
        } catch (MessagingException ex) {
            //System.err.println("Fehler beim Senden der Mail");
            return "nichtversendet";
        }
    }
}

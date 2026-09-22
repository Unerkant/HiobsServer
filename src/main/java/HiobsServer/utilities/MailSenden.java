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
     * BENUTZT: von ApiLoginController/ @PostMapping(value = "/loginMail")
     * <br><br>
     *
     * PARAMETER:   emailParam → example@example.com, als String (kein json)
     *              aktivierungCode → 1234 (int)
     *  *
     *  RETURN:     'versendet' → benutzt in ApiLoginController/ @PostMapping(value = "/loginMail")
     *              'nichtversendet' → wenn z.b.s 'example@example.com' wird angegeben
     *  *
     *  FAZIT:      versendet wird nur aktivierungCode für die Anmeldung, basiert auf spring boot
     *              durch die Strato-Mail-Adresse code@hiobspost.de
     *  *
     *  VORAUSSETZUNG:  pom.xml → 2 anhänichkeit
     *                  application.properties → z.b.s: spring.mail.username=code@hiobspost.de
     *                  spring boot →  @Autowired private JavaMailSender mailSender;
     *                  BEISPIEL: → https: //www.baeldung.com/spring-email (3.2 + 4.2)
     */
    public String sendEmail(String emailParam, int anmeldeCode) {

        String textParam = "<p>hier erhalten Sie ihre Messenger Anmelde Code </p>"
                +"<b>" + anmeldeCode + "</b>"
                +"<p>Bitte beachten Sie, dass dieser Token nur dieser Sitzung g&#252;ltig ist. </p>"
                +"<p>mit Freundlichen Gr&#252;ßen</p>"
                +"<p>Ihr Hiobs Post Team</p>";
        String subjectParam = "Hiobs Post: aktuelle Anmelde Code: " + anmeldeCode;

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
            // Fehler beim Bauen der MimeMessage
            //System.err.println("Fehler beim Erstellen der Mail: " + ex.getMessage());
            return "nichtversendet";

        } catch (org.springframework.mail.MailException ex) {
            // Fängt ungültige Adressen (550 MBL-R / SMTP-Blockaden), Server-Fehler etc. ab
            //System.err.println("Fehler beim Versenden via SMTP: " + ex.getMessage());
            return "nichtversendet";

        } catch (Exception ex) {
            // Sicherheitsnetz für alle unerwarteten Ausnahmen
            //System.err.println("Unerwarteter Fehler: " + ex.getMessage());
            return "nichtversendet";
        }
    }
}

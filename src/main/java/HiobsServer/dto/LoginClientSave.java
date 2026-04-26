package HiobsServer.dto;

/**
 * BESCHREIBUNG: erstellt in HiobsClient/MailLoginController, Zeile: 140
 * die eingegebene anmelde Code von der E-Mail wird von
 * HiobsClient/MailLoginController/@PostMapping(value = "/login/save") an der
 * HiobsServer/ApiLoginController/@PostMapping(value = "/loginSave") zugesendet, für den vergleich
 * @param userMail
 * @param codeVonMail
 */
public record LoginClientSave(String userMail, int codeVonMail) {
}

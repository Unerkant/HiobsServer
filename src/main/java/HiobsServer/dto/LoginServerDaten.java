package HiobsServer.dto;

/**
 * Den 28.12.2025
 */

/**
 * ACHTUNG: NUR FÜR INTERNE(HIOBSSERVER) BEDARF
 *
 * BESCHREIBUNG: erstellt in HiobsServer/ApiLoginController/@PostMapping(value = "/loginMail") und
 * speichert den anmelde Code, mail und mail gefunden/nichtgefunden in LoginService für den nächsten abruf
 * von HiobsServer/ApiLoginController/@PostMapping(value = "/loginSave")
 * @param anmeldeMail
 * @param existiertMail
 * @param anmeldeCode
 */
public record LoginServerDaten(String anmeldeMail, String existiertMail, int anmeldeCode) {
}

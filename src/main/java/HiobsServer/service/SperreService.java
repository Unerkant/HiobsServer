package HiobsServer.service;

import HiobsServer.model.Sperre;
import HiobsServer.repository.SperreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Den 17.01.2026, Update auf MongoDB von 16.01.2024)
 */

@Service
public class SperreService {

    @Autowired
    private SperreRepository sperreRepository;

    /**
     * prüfen auf sperrung
     * @param token
     * @return
     */
    public boolean istGesperrt(String token) {
        return sperreRepository.findByToken(token)
                .map(s -> s.getSperrdatum() > jetzt()) // Wenn Sperrdatum in der Zukunft liegt
                .orElse(false);
    }

    /**
     * Sperre Aktivieren
     * @param sperrUser
     */
    public void sperreAktivieren(Sperre sperrUser) {
        sperreRepository.save(sperrUser);
    }

    /**
     * Sperre aufheben
     * @param token
     */
    public void sperreAufheben(String token) {
        sperreRepository.deleteById(token);
    }

    /**
     * SperrDatum eintragen/aktualisieren
     * @param token
     * @param datumString
     */
    public void sperreAktualisieren(String token, String datumString) {
        // Umwandlung des Strings (dd.MM.yyyy HH:mm:ss) in Long
        Long millis = getMillisFromString(datumString);
        sperreRepository.updateSperre(millis, token);
    }

    /**
     * Löschen abgelaufene sperrZeit in Datenbank
     */
    public void loescheAbgelaufeneSperren() {
        List<Sperre> abgelaufen = sperreRepository.findBySperrdatumLessThan(jetzt());
        sperreRepository.deleteAll(abgelaufen);
    }


    // Hilfsmethode: für die function istGesperrt, Aktuelle Zeit in Millisekunden
    private Long jetzt() {
        return System.currentTimeMillis();
    }


    /**
     * Hilfsmethode: für function sperreAktualisieren
     * @param datum
     * @return
     */
    private Long getMillisFromString(String datum) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return LocalDateTime.parse(datum, f)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}



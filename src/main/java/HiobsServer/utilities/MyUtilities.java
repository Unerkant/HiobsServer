package HiobsServer.utilities;

import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Den 14.12.2024
 */

@Component
public class MyUtilities {


    /**
     * aktuelle datum in DE-Format
     * ============================================================
     * 1. Deutsches Format, für die Allgemeine anzeige
     *
     */
    public String deDatum(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(Instant.now());
    }



    /**
     * aktuelle Datum in US-Format
     * ==============================================================
     * für MySql-Datenbank gedacht, weil die datum wird in US-Format gespeichert
     * @return
     */
    public String usDatum(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(Instant.now());
    }


    /**
     * Aktuelle Tag + Monat + Jahr (Deutsche Format)
     * ==============================================================
     * @return
     */
    public String tagDatum() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                .withZone(ZoneId.systemDefault());
        return formatter.format(Instant.now());
    }


    /**
     * Aktuelles Tages Zeit
     * ==============================================================
     * @return
     */
    public String zeitDatum() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(Instant.now());
    }


    /**
     * Benutzt: nur zur Registrierung, ApiLoginController
     * ==============================================================
     * IdentifikationToken ist einen 15-stelliges eindeutiges nummer aus Datum zusammen gestellt ohne punkten,
     *  *format: Jahr + Monat + Tag + Stunden + Minuten + Sekunden + 1
     * @return
     */
    public String userToken(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss"+1);
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).format(formatter);
    }


    /**
     * Benutzt: nur für Message Token, bei neuer Freunde anlegen
     * ==============================================================
     * messageToken ist einen 15-stelliges eindeutiges nummer aus Datum zusammen gestellt ohne punkten,
     *   *format: Jahr + Monat + Tag + Stunden + Minuten + Sekunden + 2
     * @return
     */
    public String messageToken() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss"+2);
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).format(formatter);
    }


    /**
     * Benutzt: nur für Hiobs Post Token
     * ==============================================================
     *  gespeichertes Token ist einen 15-stelliges eindeutiges nummer aus Datum zusammen gestellt ohne punkten,
     *   *format: Jahr + Monat + Tag + Stunden + Minute + Sekunde + 3
     *
     * @return
     */
    public String msgHiobsToken(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss"+3);
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).format(formatter);
    }


    /**
     * Benutzt: nur für Gespeichertes Token
     * ==============================================================
     *  gespeichertes Token ist einen 15-stelliges eindeutiges nummer aus Datum zusammen gestellt ohne punkten,
     *   *format: Jahr + Monat + Tag + Stunden + Minute + Sekunde + 4
     *
     * @return
     */
    public String msgGespeichertesToken(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss"+4);
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).format(formatter);
    }


    /**
     *   Zufall Code für die Einloggen/Registrierung.
     *   ============================================================
     *   die Aktivierung Code wird 4-stellige Zahlen erstellt
     *   benutzt von
     */
    public int aktivierungsCode(){
        int min = 1;
        int max = 9;
        int eins = (ThreadLocalRandom.current().nextInt(min, max) + min);
        int zwei = (ThreadLocalRandom.current().nextInt(min, max) + min);
        int drei = (ThreadLocalRandom.current().nextInt(min, max) + min);
        int vier = (ThreadLocalRandom.current().nextInt(min, max) + min);
        int result = Integer.parseInt(eins +""+ zwei +""+ drei +""+ vier);
        return result;
    }


    /**
     *  Tag in Millisekunden umrechnen(für die Sperre)
     *  =============================================================
     *
     *  <br><br>
     *  hier werden Millisekunden ermittelt bis eine bestimmte Zeit, für 1,2 oder meer Monate
     *  <br><br>
     *  ACHTUNG: soll genau dieser format zugesendet sein: dd.MM.yyyy HH:mm:ss
     *          a. Zeit bis Ende des Jahres: 31.12.2024 22:00:00
     *          b. das ergib sich in Millisekunden: 1735678800000
     *  <br>
     *  @parameter
     *      1. zugesendet:  dd.MM.yyyy HH:mm:ss
     *      2. return:      1735678800000
     *      <code>
     *          //String sperrData = "16.01.2026 18:40:00";
     *         //String datum = String.valueOf(myUtilities.getMillis(sperrData));
     *         // oder model.addAttribute("millis", myUtilities.getMillis("26.01.2026 21:22:22"));
     *      </code>
     *
     *      Tipp für die Praxis: Wenn du die Millisekunden für einen Vergleich (z.B. "Ist die Sperre abgelaufen?")
     *      nutzt, kannst du in Java 2026 auch direkt Objekte vergleichen, was viel lesbarer ist:
     *      if (dateTime.isBefore(LocalDateTime.now())) { ... }
     *
     * @return
     * Benutzt: soll von Admin benutzt werden(noch nicht)
     */
    public Long getMillis(String sperrDatum) {
        // 1. Definition des Formats
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        // 2. Parsen in ein LocalDateTime (modernes Java Objekt)
        LocalDateTime dateTime = LocalDateTime.parse(sperrDatum, formatter);

        // 3. Umwandlung in Millisekunden (Epochen-Zeit)
        // .atZone(ZoneId.systemDefault()) fügt die lokale Zeitzone hinzu
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}

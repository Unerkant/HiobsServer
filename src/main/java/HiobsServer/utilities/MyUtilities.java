package HiobsServer.utilities;

import org.springframework.stereotype.Component;

import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Den 14.12.2024
 */

@Component
public class MyUtilities {


    /**
     *   aktuelle datum in DE-Format
     *   ============================================================
     *   1. Deutsches Format, für die Allgemeine anzeige
     *
     */
    public String deDatum(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date de = new Date();
        return format.format(de);
    }



    /**
     * aktuelle Datum in US-Format
     * ==============================================================
     * für MySql-Datenbank gedacht, weil die datum wird in US-Format gespeichert
     * @return
     */
    public String usDatum(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date us = new Date();
        return format.format(us);
    }


    /**
     * Aktuelle Tag + Monat + Jahr (Deutsche Format)
     * ==============================================================
     * @return
     */
    public String jahrTag() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date de = new Date();
        return format.format(de);
    }


    /**
     * Aktuelles Tages Zeit
     * ==============================================================
     * @return
     */
    public String tagZeit() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date de = new Date();
        return format.format(de);
    }


    /**
     * Benutzt: nur zur Registrierung
     * ==============================================================
     * IdentifikationToken ist einen 14-stelliges eindeutiges nummer aus Datum zusammen gestellt ohne punkten,
     *  *Deutsche format: Tag + Monat + Jahr + Stunden + Minuten + Sekunden
     * @return
     */
    public String IdentifikationToken(){
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date token = new Date();
        return format.format(token);
    }


    /**
     * Benutzt: nur für Message Token
     * ==============================================================
     * messageToken ist einen 14-stelliges eindeutiges nummer aus Datum zusammen gestellt ohne punkten,
     *   *USA format: Jahr + Monat + Tag + Stunden + Minuten + Sekunden
     * @return
     */
    public String messageToken() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date token = new Date();
        return format.format(token);
    }


    /**
     * Benutzt: nur für Gespeichertes Token
     * ==============================================================
     *  gespeichertes Token ist einen 12-stelliges eindeutiges nummer aus Datum zusammen gestellt ohne punkten,
     *   *USA format: Jahr + Monat + Tag + Stunden + Minute + Sekunde(einstellig)
     *
     * @return
     */
    public String otherToken(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        Date tag = new Date();
        return format.format(tag);
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
     *
     * @return
     * Benutzt: soll von Admin benutzt werden(noch nicht)
     */
    public Long getMillis (String sperrDatum) {

        //String sperrDatum = "31.12.2024 22:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(sperrDatum);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        long millis = date.getTime();
        return millis;
    }

}

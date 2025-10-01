package HiobsServer.service;

import HiobsServer.admin.model.AdminLogin;
import HiobsServer.admin.repository.AdminLoginRepository;
import HiobsServer.utilities.GeoLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Den 3.05.20025
 */

@Service
public class AdminLoginService {
    @Autowired
    private GeoLocation geoLocation;
    @Autowired
    private AdminLoginRepository adminLoginRepository;
    public AdminLoginService(GeoLocation geoLocation, AdminLoginRepository adminLoginRepository) {
        this.geoLocation = geoLocation;
        this.adminLoginRepository = adminLoginRepository;
    }

    /**
     * save
     */

    /**
     * select
     */

    /**
     * update
     */

    /**
     * delete
     */

    /**
     * Alle ankommende Fehler nur von Admin
     *
     * Einloggen versuche von Admin Seite mit den falschen Daten, ins H2-Datenbase/AdminLogin Protokollieren
     * zugesendet: von AdminService
     *
     * @param fehlerCode
     * @param inputValue
     */
    public void anmeldenProtokolieren(String fehlerCode, String inputValue) {

        long ms = System.currentTimeMillis();
        Date date = new Date(ms);
        AdminLogin AdminLog = new AdminLogin();

        AdminLog.setBrowser("Unbekant");
        AdminLog.setDatum(date.toString());
        AdminLog.setIp(geoLocation.clientIp());
        AdminLog.setLand(geoLocation.clientCity("countryName")+", "+geoLocation.clientCity("principalSubdivision"));
        AdminLog.setName(inputValue);
        AdminLog.setOther("Leer");
        AdminLog.setPlattform("Unbekannt");
        AdminLog.setRole("keine");
        AdminLog.setStandort(geoLocation.clientCity("city"));
        String text = "es war einen Versuch mit den falschen Namen ["+ inputValue +"] Einzuloggen \n" +
                "Datum: "+date+ "\n" +
                "Standort: " + geoLocation.clientCity("continent")+", " +
                geoLocation.clientCity("countryName")+", " +
                geoLocation.clientCity("principalSubdivision")+", " +
                geoLocation.clientCity("city")+"\n";
        AdminLog.setText(text);

        switch (fehlerCode) {
            case "eingeloggt":      System.out.println("Admin Login Service, eingeloggt: " + inputValue);
                                    break;
            case "falschename":     adminLoginRepository.save(AdminLog);
                                    System.out.println("Admin Login Service, falsche Name: " + inputValue);
                                    break;
            default:   System.out.println("Default: " + inputValue);
        }
    }

}

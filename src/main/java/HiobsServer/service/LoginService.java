package HiobsServer.service;

import HiobsServer.dto.LoginServerDaten;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Den 28.12.2025
 */


/**
 *  BESCHREIBUNG: hier sind die record Login Daten gespeichert, HiobsServer/ApiLoginController
 *
 *  die Daten wie, e-mail, mail gefunden/nichtgefunden und aktivierungsCode werden
 *  in @PostMapping(value = "/loginMail") gespeichert
 *  und in @PostMapping(value = "/loginSave") wieder geladen um den gleichen sicherheitscode zu Prüfen
 */
@Service
public class LoginService {
    // Speichert E-Mail -> ServerDaten
    private final Map<String, LoginServerDaten> storage = new ConcurrentHashMap<>();

    // storage Save
    public void save(LoginServerDaten daten) {
        storage.put(daten.anmeldeMail(), daten);

        // Nach 10 Minuten automatisch genau diesen Key löschen
        //storage.remove(daten.anmeldeMail());
        CompletableFuture.delayedExecutor(10, TimeUnit.MINUTES).execute(this::clearAllStorage);
    }

    // storage Laden
    public LoginServerDaten get(String email) {

        return storage.get(email);
    }

    // Diese Methode löscht alles storage Daten
    public void clearAllStorage() {
        System.out.println("LoginService clearAllStorage: " + storage.size());
        if (storage.size() > 0) {
            storage.clear();
        }
    }

}


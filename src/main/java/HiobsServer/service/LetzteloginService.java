package HiobsServer.service;

import HiobsServer.primary.model.Letztelogin;
import HiobsServer.primary.repository.LetzteloginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Den 5.04.2025
 */

@Service
public class LetzteloginService {

    @Autowired
    private final LetzteloginRepository letzteloginRepo;

    public LetzteloginService(LetzteloginRepository letzteloginRepo) {
        this.letzteloginRepo = letzteloginRepo;
    }


    /**
     * Letzte Anmeldung speichern
     * @param log
     * @return
     */
    public Letztelogin loginSave(Letztelogin log) { return letzteloginRepo.save(log); }


    /**
     * Anmeldungen suchen nach token
     * @param token
     * @return
     */
    public Letztelogin allEinloggen(String token) {

        return letzteloginRepo.findByToken(token);
    }
}

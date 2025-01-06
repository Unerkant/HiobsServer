package HiobsServer.service;

import HiobsServer.model.Exception;
import HiobsServer.repository.ExceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Den 5.12.2024
 */

@Service
public class ExceptionService {

    @Autowired
    private ExceptionRepository exceptionRepository;


    /**
     * eingehende fehler speichern
     * <br><br>
     *
     * @param ex
     */
    public void saved(Exception ex) {
       exceptionRepository.save(ex);
    }


    /**
     * suchen nach ip-adresse
     * <br><br>
     *
     * return: null oder ~192.168.0.246
     *
     * @param ip
     * @return
     */
    public Exception findeIp(String ip) {

        return exceptionRepository.findByErrip(ip);

    }


    /**
     * suchen nach statusCode
     * <br><br>
     *
     * return: null oder 400, 405 oder 1004, ...
     *
     * @param code
     * @return
     */
    public Exception findeStatusCode(int code) {
        return exceptionRepository.findByErrcode(code);
    }


    /**
     * die spalte 'count' aktualisieren
     * <br><br>
     *
     * return: 1
     *
     * @param neuercount
     * @param ip
     * @return
     */
    public Integer countUpdateNachIp(int neuercount, String ip) {
        return exceptionRepository.updateCountNachIp(neuercount, ip);
    }


    /**
     * die spalte 'count' aktualisieren
     * <br><br>
     *
     * return: 1
     *
     * @param neucount
     * @param code
     * @return
     */
    public Integer countUpdateNachCode(int neucount, int code) {
        return exceptionRepository.updateCountNachCode(neucount, code);
    }

}

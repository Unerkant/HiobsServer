package HiobsServer.repository;

import HiobsServer.model.Exception;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Den 5.12.2024
 */

@Repository
public interface ExceptionRepository extends CrudRepository<Exception, Integer> {


    /**
     * ip-adresse suchen
     * <br><br>
     *
     * return: null oder array
     *
     * @param errip
     * @return
     */
    Exception findByErrip(String errip);


    /**
     * StatusCode suchen
     * <br><br>
     *
     * return: null oder array
     *
     * @param errcode
     * @return
     */
    Exception findByErrcode(int errcode);


    /**
     * count Update, nach ip-adresse
     * <br><br>
     *
     * return: 1
     *
     * @param neucount
     * @param ip
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE EXCEPTION SET count = :neuercount WHERE errip = :ip", nativeQuery = true)
    Integer updateCountNachIp(int neuercount, String ip);


    /**
     * count Update nach statusCode
     * <br><br>
     *
     * return: 1
     *
     * @param neucount
     * @param code
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE EXCEPTION SET count = :neucount WHERE errcode = :code", nativeQuery = true)
    Integer updateCountNachCode(int neucount, int code);

}

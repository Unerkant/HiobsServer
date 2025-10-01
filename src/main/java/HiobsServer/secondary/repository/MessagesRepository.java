package HiobsServer.secondary.repository;

import HiobsServer.secondary.model.Messages;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Den 23.04.2025
 */

@Repository
public interface MessagesRepository extends CrudRepository<Messages, Integer> {

    Iterable<Messages> findAllByMessagetoken(String msgtoken);

}

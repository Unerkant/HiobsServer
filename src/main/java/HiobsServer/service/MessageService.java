package HiobsServer.service;

import HiobsServer.secondary.model.Messages;
import HiobsServer.secondary.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Den 2.09.2025
 */

@Service
public class MessageService {

    @Autowired
    private MessagesRepository messagesRepository;

    /**
     * Laden Messages nach messsage Token
     * return: null oder Iterable(Array)
     *
     * @param msgtoken
     * @return
     */
    public Iterable<Messages> findeMessages(String msgtoken) {

        Iterable<Messages> result = messagesRepository.findAllByMessagetoken(msgtoken);
        if (!result.iterator().hasNext()){
            return null;
        }
        return result;
    }

}

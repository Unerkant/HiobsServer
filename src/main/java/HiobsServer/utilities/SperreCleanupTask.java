package HiobsServer.utilities;

import HiobsServer.service.SperreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Den 17.01.2026
 */

@Component
public class SperreCleanupTask {

    @Autowired
    private SperreService sperreService;

    // Führt die Reinigung alle 30 Minuten aus (Tag: 84 400 000)
    @Scheduled(fixedRate = 1800000)
    public void cleanupSperren() {
        sperreService.loescheAbgelaufeneSperren();
        System.out.println("Abgelaufene Sperren wurden bereinigt.");
    }
}


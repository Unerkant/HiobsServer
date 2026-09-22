package HiobsServer.dto;

/**
 * Den 4.09.2026
 * HiobsClient-> ProfileControlle
 * HiobsServer-> ApiProfilBildController/@PostMapping(path = "/profilbild/bildDelete")
 * @param bildName
 * @param targetType
 */
public record ProfileBildDelete(String bildName, String targetType) {
}

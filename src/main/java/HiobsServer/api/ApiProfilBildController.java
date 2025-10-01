package HiobsServer.api;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

/**
 * Den 28.05.2025
 */

@Controller
public class ApiProfilBildController {

    /**
     *  PROFIL BILD AUS DEN ORDNER HiobsServer/profilbild LADEN
     *  ACHTUNG: nur PNG Bilder, keine svg
     *
     *  der @GetMapping("/profilbild/{imageName}") generiert und gibt der Bild-Link zurück, dass
     *  ermöglicht das profil Bild anzuzeigen in alle vorhandenen App wie HiobsServer oder
     *  HiobsClient oder HiobsWeb, weil von Außen es ist unmöglich einen zugriff auf resources datei
     *  wie resources/static/profilbild
     *
     *  BEMERKUNG: bei erstellung von profil Bilder, wird die Bildnamen von User Token genommen + png format
     *
     *  1. Anzeigen das profil Bild in HiobsServer:
     *      Controller: String imageName = "123456789";
     *                  model.addAttribute("bildName", imageName);
     *            HTML: <img data-th-src="@{ ${'http://localhost:8080/profilbild/'+bildName+'.png'} }"
     *                                          class="imgCircle" decoding="async" draggable="false">
     *                  oder (ohne http, aber nur bei HiobsServer)
     *                  <img data-th-src="@{ ${'profilbild/'+bildName+'.png'} }"
     *                       class="imgCircle" decoding="async" draggable="false">
     *  2.  Anzeige das profil Bild in HiobsClient:
     *      public final String SERVER_PROFILBILD       = "http://localhost:8080/profilbild/";
     *      Controller: String profilbildURL = webConfig.SERVER_PROFILBILD;
     *                  model.addAttribute("profilBildUrl", profilbildURL);
     *            HTML:<div data-th-if="${freundeDaten != null}" data-th-each="friends : ${freundeDaten}">
     *                      <img data-th-src="@{ ${profilBildUrl+bildName+'.png'} }"
     *                                          class="imgCircle" decoding="async" draggable="false">
     *                 </div>
     *
     * @param   bildName
     * @return  Bild Adresse
     * @throws IOException
     */

    @GetMapping(value = "/profilbild/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] profilbild(@PathVariable(value = "imageName", required = true) String bildName)
                                throws IOException {
        /**
         * Verwenden Sie ClassPathResource, um die Datei zu finden, unabhängig davon,
         * ob sie im Dateisystem oder in einer JAR-Datei ist.
         */
        String resourcePath = "profilbild/" + bildName;
        ClassPathResource classPathResource = new ClassPathResource(resourcePath);

        if (classPathResource.exists()) {
            try (InputStream inputStream = classPathResource.getInputStream()) {

                return IOUtils.toByteArray(inputStream);
            }
        } else {

            return null;
        }

    }
}


    /* :::::::::::::::::::::::::::::::: ANLEINTUG ZU PROJECT HIOBS SERVER ::::::::::::::::::::::::: */

        das Project HiobsServer wurde am 15.09.2024 neu angelegt
        
    Project HiobsServer ablauf 

    1. HiobsServer
        HiobsServer ist das Herz von hiobspost Project, Admin mit Zugriffverwaltung mit Rechtevergabe,
        Message Socket, die alle msg von Client-App laufen über den HiobsServer, Einloggen oder neue
        User Registriren, Sicherchetscode per E-Mail versenden und Exception von anderer Client-App

        A.  Admin Rechtevergabe: SecurityConfig.java + DefaultController
            a. admin.html           (Rechte: ROLE_ADMIN) -> text-Format zum eintragung ins spalte 'role'
            b. developer.html       (Rechte: ROLE_DEVELOPER)
            c. entwickler.html      (Rechte: ROLE_ENTWICKLER)
            d. statistik.html       (Rechte: ROLE_STATISTIK)
        
        B. Message Socket von spring boot: SocketConfig.java + SocketEventListener
            a. register soll in SecurityConfig frei gegeben sein, weil zugriff erfolgt von Außen

        C. Einloggen/Registrieren von Client-App: ApiLoginController.java
            a. api verzeichnis soll in SecurityConfig frei gegeben sein, weil zugriff erfolgt von Außen

        D. Sicherheitscode versand: utilitis/MailSenden
            a. bei jeder einloggen/registrieren wir per E-Mail eine Sicherheitscode versendet

        E. Globale Exception: exception/GlobaleException
            a. Alle störungen oder Fehler von Client-App, wenn erfast sind, werden per Request an den 
                GlobalException gesendet, wenn gleiche Fehler wird nochmal gesendet wird nur count erhöcht,
                geprüft wird nach StatusCode
                
        G. Sperre von User: ApiSperreController.java 
            a. der ablauf von sperre ist bei 'User Sperre' Detailiert beschrieben



    2. 

        


    /* :::::::::::::::::::::::::::::::: ZUERST LESEN :::::::::::::::::::::::::::::::::::::::::::::: */

    1. wenn neue html Sete wird erstellt dann müssen sie in SecurityConfig.java die Rechte vergeben
        + in DefaultController die Seite zun abruff festlegen



    /* ::::::::::::::::::::::::::::::: WAS SOLL NOCH GEMACHT WERDEN ::::::::::::::::::::::::::::::: */
        Admin Seite
        1. 31.12.2024 -> MailSenden -> freischalten: //mailSender.send(mimeMessage); 
        2. 12.01.2024 -> Admin Pannel die statuscode.json integrieren( da sind schon gemeldete Fehler )
        3. 14.01.2024 -> Rechte-Verwaltungs Pannel aufbauen, spalte role Format: ROLE_ADMIN oder ROLE_BLABLA
        4. 15.01.2024 -> Sperre, soll einer schudelerService um jeder Tag auf sperre Prüfen und Automatisch
                          zu angemeldeten  Client-App die sperrZeit in Datanbnk/Auth zu aktualisieren
        5. 17.01.2024 ->  sollte noch eine schudelerService geben zu prüfen, 'zuletzt Online'... nach 1 Jahr Löschen
        6.

        Support Seite
        1. Pannel für die exception anzeigen von Datenbank/exception + erledigt Button mit Datum + Fehler Archivieren
        2. 

        Statistik Seite
        1. wie-viele insgesammt user, wie-viele Online, wie-viele gelöscht, wie-viele gesperrt
        2. wie-viele messagen sind versendet, 

        Developer Seite
        1. noch keine aufgabe



    /* :::::::::::::::::::::::::::::::: ALLGEMEINE DATEN :::::::::::::::::::::::::::::::::::::::::: */
    CSS
            html, body 	    { width: 100%; height: 100%; min-width: 500px; min-height: 600px; }
            html            { font-family: sans-serif; line-height: 1.15; -webkit-text-size-adjust: 100%; 
                               -ms-text-size-adjust: 100%; }
        .container          { min-height: calc(100vh - 0px); } /* height:100% - footer */
        .article            { /* border: 1px solid black; */ }
        .layout             { margin-left: auto; margin-right: auto; /* border: 1px solid red; */ }
        @media (min-width:576px) { .layout{ width: 560px; } }
        @media (min-width:768px) { .layout{ width: 750px; } }
        @media (min-width:992px) { .layout{ width: 950px; } }
        @media (min-width:1200px) { .layout{ width: 1170px; } }

    Color:
        Background-Header:  background-color: #4169E1; 
        Border:             border-bottom: #EAEAEA; (header/bottom)

    Bilder: 


    /* :::::::::::::::::::::::::::::::: socket(spring boot) ::::::::::::::::::::::::::::::::::::::: */
    1. 
    



    /* :::::::::::::::::::::::::::::::: spring Boot Security(Rechte) :::::::::::::::::::::::::::::: */
    
    ACHTUNG: registrierung von neuer Developer noch nicht festgelegt, warscheinlich wird in der admin.html 
             der formular geben... rechte für Developer sind in MySql  gespeichert....

             Aktuell müssen alle Daten über Developer ins Mysql mit Hand eintragen!!!

    pom.xml ist nur eine dependency erforderlich: <artifactId>spring-boot-starter-security</artifactId>

    ZUGRIFF RECHTE VON AU?EN:
    von Außen sid erlaubt zugriffe von Client-App per:
        a. POST -> z.b.s. wie Api-Verbindunge per Rquest, Login oder Exception
        b. GET  -> z.b.s. wie Message von Andere Client-App, alle msg laufen über den HiobsServer/Socket
        c. DELETE   -> noch nicht benutzt
 
    1. 

    ERKLERUNG: bei einlogen werden von MySql die login-Daten mit AdminService geholt( da wir automatisch von 
                spring Security die Methode: public UserDetails loadUserByUsername(){...} gestartet ),  
                Die Daten sind in einem List-Array gespeichert:
                Admin: [Admin{  id=1, datum='30.10.2024 15:10:15', username='Admin', uservorname='null',
                        pseudonym='null', email='admin@admin.de', telefon='null', 
                        passwort='$2a$12$9L1myAJRx6/LUKcfmaJ3o.Bi64DC6J0.ZTvvOVfdlzox6ODabC7M.', 
                        other='null', role='ROLE_ADMIN'}]
                dieser List-Array wir an AdminDetails/getAuthorities() weiter gesendet, die rechte (role)
                werden ausgelesen und nach rechten die richtige Seite geöffnet, das alles luäft in
                SecurityConfig/public SecurityFilterChain
                die richtige Seite wird in DefaultController(){ ... } nach rechten(role) ermitelt und 
                schlieslich geöffnet

    2.  Passwort: wird veschlüsselt und in H2 gespeichert, https://bcrypt-generator.com/
    3.  eintragung im Datenbank in splate role: ROLE_ADMIN oder ROLE_USER oder ROLE_DEVELOPER
    ACHTUNG: in GrantedAuthority wirt die ROLE_ angeschnitten und nur die endung benutzt( ADMIN )

    FAZIT: Aktuell die Einloggen übernimmt alles spring boot security, von den Login.html + logout.html
            + rechte abfrage von MySql + sperre html seiten nach Rechten und cookie setzen und löschen,
            die neue Rechte anlegen können in MySql/admin role vornehmen, rechte einstellungen in
            SecurityConfig
    ACHTUNG: bei spring boot Security die Rechte + passwort von MySql zu benutzen brauchen Sie volgenes:
            Package service: 2 classen von UserService oder kann AdminService heisen
            Package repository: 1 mothode
            a. die erste classe ist AdminService:
                @Service
                public class AdminService implements UserDetailsService {

                    @Autowired
                    private AdminRepository adminRepository;

                    @Override
                    public UserDetails loadUserByUsername(String usernameOrEmal) throws UsernameNotFoundException {

                    Admin admins =  adminRepository.findByUsernameOrEmail(usernameOrEmal, usernameOrEmal)
                    .orElseThrow(() -> new UsernameNotFoundException("User Name oder Email existiert nicht"));
                    return new AdminDetails(admins);
                    }
                }
            die service classe braucht eine implementierung von UserDetailsService (spring boot) mit die 
            methode loadUserByUsername(){...} mit den return an die neu erstelte classe, z.b.b 
            die kann belibig ernant: UserDetail oder MyUserDetails oder wie hier AdminDetails...

            2. die Zweite classe heist AdminDetails
            public class AdminDetails implements UserDetails {

                private Admin admin;
                public AdminDetails(Admin admin) {
                    this.admin = admin;
                }
                
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {

                    List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
                    roles.add(new SimpleGrantedAuthority(admin.getRole()));
                        //System.out.println("getAuthorities: " + roles);
                        // output: getAuthorities: [ROLE_ADMIN]
                    return roles;
                }

                // weitere 6 methoden, lesen sie unten wie soll geladen sein

                // ACHTUNG:bei erstellung die class wird die implementierung rot dargestellt- einfach rechte 
                    Taste und abhenichkein einfach erstellen lassen....insgesmt mit public Collection... soll 7 sein 
            }

            3. die Methode von repository
                @Repository
                public interface AdminRepository extends CrudRepository<Admin, Integer> {

                    // suchen nach Name oder E-Mail, für spring boot Security
                    Optional<Admin> findByUsernameOrEmail(String username, String email);
                }
                



    /* :::::::::::::::::::::::::::::::: H2-Database (globalHiobs) :::::::::::::::::::::::::::::: */

    1. pom.xml: 2 dependency erforderlich 
        <!-- H2 Database -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
			<version>3.3.5</version>
		</dependency>
        <dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<version>2.3.232</version>
		</dependency>

    2. application.properties
        # ==========================================================
        # = Connect H2 Database
        #===========================================================
        spring.datasource.url=jdbc:h2:file:./h2database/globalHiobs
        #spring.datasource.url=jdbc:h2:file:~/h2database/bote
        spring.datasource.driverClassName=org.h2.Driver
        spring.datasource.username=sa
        spring.datasource.password=

        # =========================================================
        # <!-- H2 Browser console: http://localhost:8076/h2-console -->
        # =========================================================
        spring.h2.console.enabled=true
        spring.h2.console.path=/h2-console


    3. Tabelle: admin 
        * ACHTUNG: Whitelabel Error Page, wenn in Datenbank 'Admin' die spalte role leer ist,
        * wird die Security-exception ausgelöst & Whitelabel Error Page

            a. spalte datum: @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss") hatte Deutsche Datum-Ausgabe
                Tag.Monat.Jahr Stnde:Minute:Secunde
            b. spalte role: ROLE_ADMIN oder ROLE_USER oder ROLE_DEVELOPER (für enwickler + statistik)
                AdminDetails/public Collection<? extends GrantedAuthority> getAuthorities(){...} 
                ACHTUNG: in GrantedAuthority wirt die ROLE_ angeschnitten und nur die endung benutzt( ADMIN )
    4.
    5.
    6. Fazit: spalte 'role' darf kein NULL sein oder Leer


    /* :::::::::::::::::::::::::::::::: Login + Logout :::::::::::::::::::::::::::::::::::::::::::: */

    ACHTUNG: throw new UsernameNotFoundException("user not found!"); 
    QUELLE:   ist von spring boot Security
                    Anzeiger für Fehler in Login.html (über den input: Name)
    BENUTZT: AdminDeteils.java Zeile: 42

    1. Login System ist Original von spring boot security, 
        in pom.xml ist nur eine dependency erforderlich: <artifactId>spring-boot-starter-security</artifactId>
    2. die abfrage von UsersService wird an MyUsersDeteils(von spring boot security) gesendet, dann wird es 
        irgentwie von SecurityConfig zugegriefen und verarbeitet...
    3. RolesRepository: ob das von spring benutzt wier ungewissen! von mir bestimmt nicht
    4.  FAZIT: Die Login + Logout System ist komplekt von spring boot security, eigentlich functioniert gut
        in zukunft kann auf eingenes Login umgestellt werden



    /* :::::::::::::::::::::::::::::::: SecurityConfig.java + DefaultController ::::::::::::::::::: */

    1. securityFilterChain regelt mit welchen Rechten welche Seite wird geöffnet
        z.b.s. ADMIN- hatte Rechte alle Seite öffnen, die verwaltung.html nur mit verwaltung rechten
    2. die AUTH_WHITELIST behält die Dateien welche sind den spring boor Security erlaubt
        z.b.s zugrif auf css-datei ansonsten keine style vorhanden
    3. formLogin: login-Seite ist noch Original von spring boot security nur defaultSuccessUrl("/default")
        wird an DefaultController ausgelagert...
        WEIL: der DefaultController prüft wer hatte sich angemeldet und entsprechen wird die passende
                Seite geladen... 
                z.b.s. jeder wer ist an der verwaltung zugelassen bekommt entsperechende Rechte (ROLE)
                        ADMIN -> kann auf jede Seite zugreifen aber OTHER nur an other seite oder
                        VERWALTUNG ist nur an verwaltung.html zugelassen und entspechen bei einloggen
                        in DefaultController wird geprüft nach Rechten und die Seite geladen
    4.  logout: alles noch von spring boot 
        ACHTUNG: .deleteCookies("JSESSIONID"), die cookie ist von spring boot security angelegt und wird
        nach den logout wieder gelöscht, 
        die ERROR-Seiten, 403.html + 404.html wen wird an den Button(GO to Login) gedrückt werden
        cookie auch gelöscht, weil der button ist an logout link angebunden
        <a href="/logout" class="btn">Go to Login</a>
   

    /* :::::::::::::::::::::::::::::::: Globale Exception ::::::::::::::::::::::::::::::::::::::::: */
    GlobaleException ist die classe wo werden alle entstehende Fehler oder Errors zusammen kommen und ins
    Datenbank gespeichert um die Fehler zentral zu Verwalten
    Controller: GlobalExeption.java   mit 2 methoden
                1. @PostMapping("/exception"), hier werden alle Fehler empfangen von anderer Server und 
                    ins Datenbank gespeichert
                2. public void setInternFehler(Exception except){...}, hier werden nur Fehler von diesem Server 
                    behandelt (HiobsServer), weil kein request brauchen
                3. die Alle Fehler werden nach StatusCode gespeichert, mit den gleichen statusCode wird nur
                    den count von dieser fehler erhöcht
    Database: H2: globalHiobs, Tabelle exception
    model + service + repository : Exception
    REQUEST: alle Fehler von anderer Server werden mit den request an die @PostMapping("/exception") gesendet
    PARAMETER: als parameter wird den exception.toString() an HiobsServer zugesendet
    STATUSCODE: der statusCode muss in jeder exception.toString() vorhanden sein, weil die Update von count
                wird nach statusCode behandelt, bei gleiche statusCode wird nur count erhöcht!!!
    EXCEPTION: notwendige Daten:
            Daten-Array als: Exception excipt = new Exception(); erstellt und min. die 3 parametr eingetragen
            1. statusCode, als int (ist nowendeg)
            2. queller der Fehlers z.b.s. HiobsClient/NameControllers
            3. text, kurze fehler beschribung
        empfehlen:
            4. Datum, aktuelle datum das fehlers entstehung
            5. IP-Adresse, die ip-adresse wenn vorhanden ist
        zusezlich:
            6. other, ???
            7. role, ???
    FAZIT: bei einer eingetreten exception im einem andere Server, (wenn notwendig ist einen Fehler zu melden), 
            wird einen array zusammen gestelt und mit request an die @PostMapping("/exception") gesendet, die
            Fehlder von HiobsServer werden in die methode: public void setInternFehler(Exception except) gesendet


    /* :::::::::::::::::::::::::::::::: User Sperre ::::::::::::::::::::::::::::::::::::::::::::::: */

        USER SPERRE ABLAUF
            a. z.b.s wenn einen User wird für 3 Monate Gesperrt
            b. auf der Admin Seite soll Funktion geben zum SperrZeit ins Datenbank einzutragen
            c. ABLAUF: die SperrZeit in Millisecunden soll von Admin ins globalen Datenbank Tabelle: Sperre
                ( soll noch gemacht) eingetragen sein, gleich sollte Automatisch die sperrZeit an die
                Client-App weitergeleitet, weil Anmelde-Daten liegen auf dem Client-App in der Datenbank
                Tabelle: Auth...
            d. AUF DEN CLIENT-APP: ablauf -> bei starten oder Aktualisieren die index.html Seite wird immer
                geprüft auf die sperre, wenn sperrZeit(in Millisecunden) in Tabelle Auth vorhanden ist wird
                die ganze Seite index.html ausgeblendet und den sperre.html angezeigt mit den rücklaufzeit
            e.  der SperreController wird die sperrZeit von Tabelle: Auth, in Millisecunden, auslesen und an die 
                sperre.html weitergesendet(model) gesendet und in einem input type:hidden gespeichert, 
                gleichzeitlich wirt den javascript  freigegeben: 
                <script data-th-if="${sperrJS}" data-th-src="@{/js/sperreCountdown.js}"></script>
            f.  die javascript erledigt die ganze arbeit, ein Zähler läuft rückwerz, bei ablauf die sperrzeit
                wird den fetch aktiviert und einen request an sperreController gesendet, in der function
                 @DeleteMapping("/sperreDelete") ... wird dann in Datenbanken eine Update durchgeführt und die
                 sperrzeit auf null gesetzt, die index.html sollte Automatisch neu gestartet sein
            g.  Datenbank: wo sollte update ausgeführt
                    HiobsServer/H2 Database: globalHiobs, Tabelle sperre spalte sperrdatum
                    HiobsClient/H2 Database: hiobsClient, Tabelle Auth splalte sperrdatum


    /* :::::::::::::::::::::::::::::::: Weitere Beschreibungen :::::::::::::::::::::::::::::::::::: */
    /* :::::::::::::::::::::::::::::::: Weitere Beschreibungen :::::::::::::::::::::::::::::::::::: */
    /* :::::::::::::::::::::::::::::::: Weitere Beschreibungen :::::::::::::::::::::::::::::::::::: */
    /* :::::::::::::::::::::::::::::::: Weitere Beschreibungen :::::::::::::::::::::::::::::::::::: */


    
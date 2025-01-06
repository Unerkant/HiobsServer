
    /* :::::::::::::::::::::::::::::::: ANLEINTUG ZU PROJECT HIOBS SERVER ::::::::::::::::::::::::: */

        das Project wurde am 15.09.2024 neu angelegt
        
        Informationstechnologie betrachtet es im IT-Service-Management (ITSM)
        Informations Technologie, IT-Projektmanager (ITPM)

        Der Developer (auch Softwareentwickler, Programmierer, Entwickler oder Software Engineer) ist ein IT-Spezialist, 
        der sich auf Planung, Erstellung, Testen und Wartung von Softwareanwendungen und Systemen spezialisiert hat.


    /* :::::::::::::::::::::::::::::::: ZUERST LESEN :::::::::::::::::::::::::::::::::::::::::::::: */

    1. wenn neue html Sete wird erstellt dann müssen sie in SecurityConfig.java die Rechte vergeben
        + in DefaultController die Seite zun abruff festlegen


    /* ::::::::::::::::::::::::::::::: WAS SOLL NOCH GEMACHT WERDEN ::::::::::::::::::::::::::::::: */
        1. 31.12.2024 -> MailSenden -> freischalten: //mailSender.send(mimeMessage); 



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

    /* :::::::::::::::::::::::::::::::: Project Hiobs ablauf :::::::::::::::::::::::::::::::::::::: */

    1. HiobsServer
        HiobsServer ist das Herz von hiobspost Project, auf HiobsServer läuft der Message Socket, alle messages laufen
        über den socket, das ist auch Admin Seite mit 5 verschidenen Teile(zuerst) mit eingenen Rechten
            a. admin.html           (Rechte: ROLE_ADMIN)
            b. entwickler.html      (Rechte: ROLE_ENTWICKLER)
            c. statistik.html       (Rechte: ROLE_STATISTIK)
            d. kann erweitert sein
        
        A. Einloggen System ist von spring boot Security, die rechte werden auch von security geregelt,
            einstellung in SecurityConfig.java möglich
        B. die Developer Daten werden in einem H2 Datanbank gespeichert (Database: hiobsAdmin.mv.db)
        C. Rechte sind in Table: ROLE festgelegt
        D. Rechte an Developer zugewiesen sind in H2 Table: developer_role


    2. HiobsClient



    /* :::::::::::::::::::::::::::::::: socket(spring boot) ::::::::::::::::::::::::::::::::::::::: */
    1. 
    



    /* :::::::::::::::::::::::::::::::: spring Boot Security(Rechte) :::::::::::::::::::::::::::::: */
    
    ACHTUNG: registrierung von neuer Developer noch nicht festgelegt, warscheinlich wird in der admin.html 
             der formular geben... rechte für Developer sind in MySql  gespeichert....

             Aktuell müssen alle Daten über Developer ins Mysql mit Hand eintragen!!!

    pom.xml ist nur eine dependency erforderlich: <artifactId>spring-boot-starter-security</artifactId>
 
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
                



    /* :::::::::::::::::::::::::::::::: MySql-Database (globalHiobs) :::::::::::::::::::::::::::::: */

    1. pom.xml: 3 dependency, 
        <!-- MySql + H2 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
			<version>3.3.5</version>
		</dependency>
		<!-- MySql connect (phpMYAdmin) von MAMP -->
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<version>9.1.0</version>
		</dependency>
		<!-- für @Entity: import jakarta.persistence.Entity; -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
			<version>3.3.5</version>
		</dependency>

    2. application.properties
        # ==========================================================
        # MySql = http://localhost:8888/phpMyAdmin5/
        # ==========================================================
        spring.datasource.url=jdbc:mysql://localhost:8889/globalHiobs
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        spring.datasource.username=root
        spring.datasource.password=root

        # ==========================================================
        # = Hibernate MySql-Dialect
        #===========================================================
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

    3. Tabelle: admin 
            a. spalte datum: @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss") hatte Deutsche Datum-Ausgabe
                Tag.Monat.Jahr Stnde:Minute:Secunde
            b. spalte role: ROLE_ADMIN oder ROLE_USER oder ROLE_DEVELOPER (für enwickler + statistik)
                AdminDetails/public Collection<? extends GrantedAuthority> getAuthorities(){...} 
                ACHTUNG: in GrantedAuthority wirt die ROLE_ angeschnitten und nur die endung benutzt( ADMIN )

    4.
    5.
    6. Fazit: 


    /* :::::::::::::::::::::::::::::::: Login + Logout :::::::::::::::::::::::::::::::::::::::::::: */

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
                3. die Alle Fehler werden nach StausCode gespeichert, mit den gleichen statusCode wird nur
                    den count von dieser fehler erhöcht
    Database: MySql: globalHiobs, Tabelle exception
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


    /* :::::::::::::::::::::::::::::::: Globale Exception ::::::::::::::::::::::::::::::::::::::::: */
    /* :::::::::::::::::::::::::::::::: Globale Exception ::::::::::::::::::::::::::::::::::::::::: */


    
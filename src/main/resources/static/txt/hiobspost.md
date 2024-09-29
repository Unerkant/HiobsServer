
    /* :::::::::::::::::::::::::::::::: ANLEINTUG ZU PROJECT HIOBS SERVER ::::::::::::::::::::::::: */

        das Project wurde am 15.09.2024 neu angelegt
        
        Informationstechnologie betrachtet es im IT-Service-Management (ITSM)
        Informations Technologie, IT-Projektmanager (ITPM)

        Der Developer (auch Softwareentwickler, Programmierer, Entwickler oder Software Engineer) ist ein IT-Spezialist, 
        der sich auf Planung, Erstellung, Testen und Wartung von Softwareanwendungen und Systemen spezialisiert hat.


    /* :::::::::::::::::::::::::::::::: ZUERST LESEN :::::::::::::::::::::::::::::::::::::::::::::: */

    1. wenn neue html Sete wird erstellt dann müssen sie in SecurityConfig.java die Rechte vergeben
        + in DefaultController die Seite zun abruff festlegen



    /* :::::::::::::::::::::::::::::::: ALLGEMEINE DATEN :::::::::::::::::::::::::::::::::::::::::: */




    /* :::::::::::::::::::::::::::::::: Project Hiobs ablauf :::::::::::::::::::::::::::::::::::::: */

    1. HiobsServer
        HiobsServer ist das Herz von hiobspost Project, auf HiobsServer läuft der Message Socket, alle messages laufen
        über den socket, das ist auch Admin Seite mit 5 verschidenen Teile(zuerst) mit eingenen Rechten
            a. admin.html           (Rechte: ROLE_ADMIN)
            b. engineer.html        (Rechte: ROLE_ENGINEER)
            c. entwickler.html      (Rechte: ROLE_ENTWICKLER)
            d. statistik.html       (Rechte: ROLE_STATISTIK)
            e. wartung.html         (Rechte: ROLE_WARTUNG)
            f. kann erweitert sein
        
        A. Einloggen System ist von spring boot Security, die rechte werden auch von security geregelt,
            einstellung in SecurityConfig.java möglich
        B. die Developer Daten werden in einem H2 Datanbank gespeichert (Database: hiobsAdmin.mv.db)
        C. Rechte sind in Table: ROLE festgelegt
        D. Rechte an Developer zugewiesen sind in H2 Table: developer_role


    2. HiobsClient



    /* :::::::::::::::::::::::::::::::: socket(spring boot) :::::::::::::::::::::::::::::::::::::::: */
    1. 
    



    /* :::::::::::::::::::::::::::::::: spring Boot Security(Rechte) :::::::::::::::::::::::::::::: */
    
    ACHTUNG: registrierung von neuer Developer noch nicht festgelegt, warscheinlich wird in der admin.html 
             der formular geben... für die rechte an Developer in H2 speichern....

             Aktuell müssen alle Daten über Developer ins H2 mit Hand eintragen!!!

    pom.xml ist nur eine dependency erforderlich: <artifactId>spring-boot-starter-security</artifactId>

    KURZE BESCREIBUNG:  Table: developer, sind alle developer gspeichert, 
                        Table: role, sind Rechte festgelegt
                        Table: developer_role, werden Rechte vergeben(bei registrier neuen developer)
                        
    1. zugelasen developer, z.b.s nur name und passwort
        Database: hiobsAdmin.mv.db Table: developer
                id              passwort            username
        a.      1               superadmin          SuperAdmin
        b.      2               admin               Admin
        c.      3               engineer            Engineer
        d.      4               entwickler          Entwickler
        e.      5               statistik           Statistik
        f.      6               wartung             Wartung
   
        g.      die passwort in h2 ist Bcrypt encrypted hash.

    2. Role-Parameter, Rechte festlegen: 
        Database: hiobsAdmin.mv.db Table: role
                id              authority
        a.      1               ROLE_SUPERADMIN
        b.      2               ROLE_ADMIN
        c.      3               ROLE_ENGINEER
        d.      4               ROLE_ENTWICKLER
        e.      5               ROLE_STATISTIK
        f.      6               ROLE_WARTUNG
        g.      weiter können noch festgelegt 
    ACHTUNG: in SecurityConfig wirt die ROLE_ angeschnitten und nur die endung benutzt( ENGINEER)

    3. Rechte-vergeben
        Database: hiobsAdmin.mv.db  Table: developer_role
                user_id         role_id
        a.      1               1           für den SuperAdmin            
        b.      2               2           für den Admin
        c.      3               3           für den Engineer
        d.      4               4           für den Entwickler
        e.      5               5           für den Statistik
        f.      6               1           für den Wartung

    ERKLERUNG: bei einlogen werden die login-Daten von DeveloperService von h2 abgerufen und weiter an 
                MyDeveloperDetails.java gesendet, hier mit getAuthorities() die Rechte ermitellt
                und schliesslich in SecurityConfig.java von spring boot security benutzt...
    FAZIT: Aktuell die Einloggen übernimmt alles spring boot security, von den Login.html + logout.html
            + rechte abfrage von H2 + sperre html seiten nach Rechten und cookie setzen und löschen,
            die neue Rechte anlegen können in h2/Table role vornehmen, rechte einstellungen in
            SecurityConfig
                



    /* :::::::::::::::::::::::::::::::: H2-Database (hiobsAdmin) :::::::::::::::::::::::::::::::::: */

    1. pom.xml: 2 dependency, <artifactId>spring-boot-starter-data-jpa</artifactId> 
        <artifactId>h2</artifactId> 
    2. Table: Aktuell, DEVELOPER, ROLE + DEVELOPER_ROLE
        Table DEVELOPER + ROLE sind Automatisch von model erstellt
        Table DEVELOPER_ROLE ist von model/Developer class erstellt, Zeile: 30
        @JoinTable(name = "developer_role",....
    3. Table: DEVELOPER -> sind alle Developer mit bestimten Rechten hinterlegt
    4. Table: ROLE -> sind nur Rechte(authority) hinterlegt z.b.s 
                    a. Admin        (ROLE_ADMIN)        ID(1)
                    b. engineer     (ROLE_ENGINEER)     ID(2)
                    c. entwickler   (ROLE_ENTWICKLER)   ID(3)
                    d. wartung      (ROLE_WARTUNG)      ID(5)
                    e. statistik    (ROLE_STATISTIK)    ID(4)
    5. Table: DEVELOPER_ROLE -> wird Automatisch von Developer class erstellt und behält nur 2 spalten
        developer id(von DEVELOPER) und role id(von ROLE)
    6. Fazit: 


    /* :::::::::::::::::::::::::::::::: Login + Logout :::::::::::::::::::::::::::::::::::::::::::: */

    1. Login System ist Original von spring boot security, 
        in pom.xml ist nur eine dependency erforderlich: <artifactId>spring-boot-starter-security</artifactId>
    2. die abfrage von UsersService wird an MyUsersDeteils(von spring boot security) gesendet, dann wird es 
        irgentwie von SecurityConfig zugegriefen und verarbeitet...
    3. RolesRepository: ob das von spring benutzt wier ungewissen! von mir bestimmt nicht
    4.  FAZIT: Die Login + Logout System ist komplekt von spring boot security, eigentlich functioniert gut
        in zukunft kann auf eingenes Login umgestellt werden



    /* :::::::::::::::::::: SecurityConfig.java + DefaultController ::::::::::::::::::::::::::::::: */

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
    5.  Passwort: wird veschlüsselt und in H2 gespeichert, https://bcrypt-generator.com/




    
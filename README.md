Applicazione web di una piattaforma multi-sided di coupon geolocalizzati.
Sviluppata cercando di utilizzare linee guida e soluzioni efficenti.
L'obiettivo di questo repository git è esclusivamente quello di mostrare alcune soluzioni tecniche o di design che ritengo interessanti e riutilizzabili in altri contesti.

# Librerie Utilizzate
[Vedi dichiarazioni su POM](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/pom.xml)

# Architettura
La suddivisione dei package da una visione di massima dell'architettura a 3 layer: [Vedi Package](https://github.com/ascuderetti/devastapp-spring-stateofart/tree/master/src/main/java/it/bologna/devastapp).
Ecco la responsabilità logica dei componenti:
* [Presentation Layer](https://github.com/ascuderetti/devastapp-spring-stateofart/tree/master/src/main/java/it/bologna/devastapp/presentation)
  * Controller: espone le API REST e mappa i dati Json provenienti dal client in oggetti Dto
* [Business Layer](https://github.com/ascuderetti/devastapp-spring-stateofart/tree/master/src/main/java/it/bologna/devastapp/business)
  * Service: contiene la logica di business. Orchestra componenti di mapping, validazione, integrazione. Espone generalmente delle operazioni transazionali. Tipicamente la sequenza di componenti richiamata è questa:
    1. Mapper: mapping da Dto ad Entity ([esempio](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/mapper/PosizioneMapper.java))
    2. Validator: validazione regole di business ([esempio](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/validator/ProdottoValidatorImpl.java))
    3. Repository: componente di interfacciamento a DB ([esempio]())
    4. Mapper: mapping da Entity a Dto ([esempio](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/mapper/PosizioneMapper.java))
    
* [Persistence Layer](https://github.com/ascuderetti/devastapp-spring-stateofart/tree/master/src/main/java/it/bologna/devastapp/persistence)
  * Repository: espone i metodi di accesso a DB ([esempio](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/persistence/MovimentiLocaleRepository.java))

* [Enterprise Integration Layer](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/resources/META-INF/spring/spring-integration-context.xml) (vedi paragrafo "Notifiche e Spring Integration"): integrazione con sistemi esterni

# Gestione Eccezioni
Due tipi di eccezione:
* [Eccezione di Sistema](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/signal/ErroreSistema.java): eredita da "RuntimeException" e viene lanciata se si riscontrano errori dovuti all'implementazione del sistema software. Ad esempio: se dal client si richiede una modifica su db ma il campo che identifica l'oggetto su DB (tipicamente un "ID") non è stato valorizzato. Questo non è una eccezione innescata da dati inseriti dall' utente. E' appunto un errore del sistema software, chi ha implementato il client ha dimenticato di valorizzare il campo ID. Serve per avere più controllo e specificare in modo più chiaro errori di Runtime. 

* [Segnalazione di Business](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/signal/BusinessSignal.java): è un oggetto di modello che rappresenta delle segnalazioni di business che possono avere diversi tipi di severity (errori, ma anche informazioni), non eredita da "Exception" (per questo ho usato il termine "segnalazione). I Dto contengono sempre una lista di segnalazioni di business, che sarà vuota nel caso in cui non si sia presentata nessuna eccezione di business (ad esempio un errore di validazione...)

**Gestione Centralizzata Eccezioni**

Spring, tramite l'annotation [@ControllerAdvice](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc#global-exception-handling) permette di definire un singolo controller che intercetta tutte le eccezioni generate dall'applicazione.

Vantaggi:
* Si sgrava lo sviluppatore dal gestire le eccezioni
* Si ottiene un codice più pulito e leggibile
* Si realizza una gestione uniforme e centralizzata delle eccezioni

La classe che implementa questa funzionalità è la [GlobalExceptionController.java](
https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/presentation/GlobalExceptionController.java)

# Gestione Log
Politiche di logging applicativo:
* [Log di contesto](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/resources/log4j.xml): datetime, id univoco richiesta http, id sessione utente
* Log con severity INFO: log all'ingresso e all'uscita dei metodi di tutti i componenti architetturali, Controller, Service, Mapper, Valitator, Repository)
* Log con severity DEBUG: log degli argomenti in ingresso ai componenti "Service", in particolare viene loggato il contenuto degli oggetti di modello Dto.

**Gestione Centralizzata del LOG**

[Il Log è gestito tramite un aspect]
(https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/log/LoggingAspect.java) che opera in accordo con le convenzioni su package e annotation spring (ad esempio i componenti Service vengono individuati definendo un pointcut sull'annotation @Service).

Vantaggi:
* Si sgrava lo sviluppatore dalla gestione del logging
* Si ottiene un codice più pulito e leggibile
* Si realizza una gestione uniforme e centralizzata del log

# Accesso al DB e ORM
E' stato utilizzato [spring-data-jpa](http://projects.spring.io/spring-data-jpa/) e Hibernate.

Funzionalità rilevanti:
* [Query-method](http://docs.spring.io/spring-data/jpa/docs/1.6.2.RELEASE/reference/htmlsingle/#repositories.query-methods) - creazione query automatica: utilizzando specifiche configurazioni e convenzioni di nomenclatura nei nomi dei metodi è possibile delegare a "spring-data-jpa" l'implementazione delle query ([esempio](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/persistence/LocaleRepository.java)) 
* [Query-Dsl JPA](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa) - query tipizzate: nei casi in cui si è utilizzata una implementazione custom, le query sono state scritte con query-dsl ([esempio](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/persistence/MovimentiOffertaRepositoryImpl.java))

# Notifiche e Spring Integration
Per il sistema di notifiche è stata utilizzata una architettura "Pipes-and-Filters" (o, forse più appropiato, "Pipes-and-Processing") basata su code, scambio di messaggi ed enterprise integration pattern (EIP).
Il framework utilizzato è [Spring-Integration](https://projects.spring.io/spring-integration/).
Un esempio è l' invio di una notifica push ai dispositivi mobile che comporta l'integrazione con 3 sistemi diversi, uno per ogni tipo di dispositivo (Android, Iphone, Windows).

[L' interfaccia Gateway](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/notifications/NotificheGateway.java) costituisce il ponte tra l'architettura "Layered" e l'architettura "Pipes-and-Filters".

Questa interfaccia da l'accesso al [contesto di spring integration](
https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/resources/META-INF/spring/spring-integration-context.xml) in cui il flusso operativo è definito tramite EIP (code, router, splitter ecc..).


# Test
Oltre ad avere una copertura totale di test, l'obiettivo è stato quello di rendere l'ambiente tale da poter testare, in locale, quante più casisistiche possibili.

Ciò significa dover riprodurre o simulare, in locale, i sistemi esterni alla nostra applicazione:
* DB: e' stato creato un DB "hsql" interno al progetto che veniva ricreato ad ogni esecuzione dei test e popolato dal test in base alla casistica da riprodurre.
* Gli altri sistemi sono stati riprodotti tramite Mock (ad esempio il servizio di pagamento paypal, o i server per le notifiche push su mobile...).
E' stato utilizzato [mockito](http://www.baeldung.com/mockito-behavior) e powermock.

Ecco una [classe](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/test/java/test/it/bologna/devastapp/notifiche/NotificheGatewayFollowLocaleTest.java) e relativo [contesto spring di test](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/test/resources/META-INF/spring/spring-notifiche-test.xml).

Spring permette di creare un contesto di test che simula la gestione dello strato http senza la necessità di deploy su un application server.
E' stato possibile quindi implementare casistiche di test (tipicamente funzionali) puntando direttamente alle API REST che verranno esposte sull'application server.
Per effettuare le chiamate è stato usato il client spring [RestTemplate](http://www.baeldung.com/rest-template)
[Un esempio di test http](
https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/test/java/test/it/bologna/devastapp/funzionali/OffertaAppHttpTest.java).

# Mapping Dto<=>Entity
Per gestire il mapping tra i due principali oggetti di modello è stata usata la libreria [MapStruct](http://mapstruct.org/) che automatizza la generazione dei mapper tramite convenzioni sui nomi e configurazioni sull'interfaccia (unico componente da realizzare) 
Un esempio di [mapper](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/mapper/PosizioneMapper.java).

# Ricerche e Geolocalizzazione
Le ricerche filtrate per geolocalizzazione sono state realizzate utilizzando (hibernate-serch)[http://hibernate.org/search/].
[Un esempio](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/persistence/OffertaRepositoryImpl.java).

# Audit
E' stato usato [Hibernet-Envers](http://docs.jboss.org/hibernate/core/4.2/devguide/en-US/html/ch15.html).

Ecco [un esempio di entity](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/persistence/entity/MovimentiOfferta.java) a cui è stata aggiunta l'annotation "@Audited" per effettuare l'audit e tracciare quindi su db tutti i cambiamenti di stato. Notare che sarà quindi necessario creare una specifica tabella su db.

Segnalo [questo post](http://blog.octo.com/en/audit-with-jpa-creation-and-update-date/) che illustra le possibili alternative per la gestione dell'Audit.

# Debiti Tecnici
In questo progetto ci sono anche soluzioni tecniche da evitare.

Ad esempio la profilazione delle variabili di ambiente tramite profilo maven. E' meglio utilizzare un approccio che non lega l'ambiente alla fase di compilazione, quindi o esternalizzare le variabili direttamente nell'ambiente [ https://12factor.net/it/ ] o sfruttare funzionalità specifiche di alcuni application server che associano le variabili di ambiente a tempo di deploy [ad esempio deployment-plan su oracle weblogic].

Gli script di creazione DB (DDL) non usano ancora (il progetto è una beta) le migration per tenere traccia del versionamento del DB, tool che consentono di gestire le migration sono [FlyWayDB](https://flywaydb.org/) o [MyBatisMigration](http://www.mybatis.org/migrations/). Questi ed altri debiti tecnici sono tracciati in una Kanban (privata).

# Considerazioni Finali
*[Bozza]*

Questo progetto è stato sviluppato nell'arco di un anno, il 2014, per una startup di cui facevo parte e che ha finito il suo corso.
Lo sviluppo e in generale tutta la gestione della startup è avvenuta in parallelo ad una classica attività lavorativa, quindi il tempo dedicato è stato limitato.

Parte delle soluzioni sono state acquisite per lo più leggendo libri (alcuni li trovate [qui](https://www.goodreads.com/review/list/26454731-alessandro?shelf=work-it) e [qui quelli non tecnici](https://www.goodreads.com/review/list/26454731-alessandro?per_page=30&shelf=work-bus&utf8=%E2%9C%93)).

Le soluzioni elencate sono quasi tutte assodate da parecchio tempo, eppure capita ancora oggi in ambito lavorativo di trovare progetti che strutturano aspetti del software in modo meno efficiente, ad esempio gestione "anarchica" del logging, mapping scritti a mano campo per campo, i test parziali e mal strutturati. La conseguenza è un abbassamento della qualità del prodotto sw ma, soprattutto, della qualità della vita lavorativa.

Mi aspettavo che fosse l'esperienza lavorativa costellata di senior, pm, budget, aziende, a darmi un valore aggiunto per portare avanti questa esperienza, per certi versi "amatoriale", ma invece è stato più il contrario.

Perchè?

-perchè è stato un lavoro con obiettivo chiaro condiviso, che presupponeva un grande beneficio. Queste sono le premesse che hanno fatto scaturire passione e determinazione. In un "classico" contesto lavorativo questo potrebbe tradursi in un percorso di crescita professionale di valore, visione d'insieme condivisa e percezione del proprio apporto.

-perchè il mantra di innovazione e best-practices hanno guidato il lavoro. E questo dovrebbe

dovrebbe esserci un sindacato che si occupa di questo aspetto come se fosse un benefit.

Queste argomentazioni sono al di fuori 

nell'accezione più positiva che 

-passione obiettivi chiari anno fatto con passione, perseguendo un obiettivo chiaro ne vale dieci fatti con approssimazione
-innovazione e best-practices ricordandoci che il lavoro è una forma di espressione della persona stessa e che l'informatica per sua, nata per redimerci dalle attività che denigrano le capacità creative della mente umana.
- 


innovazione da intraprendere per crescere, ricordandosi che si cresce anche solo provando strade nuove e sbagliando .


Ultima nota: il progetto come dicevo è del 2014, alcune tecnologie nel frattempo si sono evolute e altre sono nate, monitorare l'andamento della tecnologia fa parte della gestione di un progetto.

soluzioni in progetti enterprise su cui mi capita di lavorare. / formativo passione studio libri / critica mondo del lavoro - organizzazione / l'equazione non deve essere innovazione==il cliente non la chiede quindi puppa, ma innovazione == aumento qualità di vita lavorativa (vedetelo come un benefit) => punto di vista interno all'azienda non esterno. Colpa anche degli sviuppatori che non si fanno carico di...ecc... / il lavoro ci rappresenta

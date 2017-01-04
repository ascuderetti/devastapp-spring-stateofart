Applicazione web di una piattaforma multi-sided di coupon geolocalizzate.
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
    1. Mapper: mapping da Dto ad Entity 
    2. Validator: validazione regole di business
    3. Repository: componente di interfacciamento a DB
    4. Mapper: mapping da Entity a Dto
    
* [Persistence Layer](https://github.com/ascuderetti/devastapp-spring-stateofart/tree/master/src/main/java/it/bologna/devastapp/persistence)
  * Repository: espone i metodi di accesso a DB
  
# Gestione Eccezioni
* E' stato definita l' [Eccezioni di Sistema](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/signal/ErroreSistema.java), eredita da "RuntimeException" e viene rilanciata in caso si riscontrino errori dovuti all'implementazione del sistema software: ad esempio se dal client si richiede una modifica su db ma il campo che identifica l'ogggetto su DB (tipicamente un "ID") non è stato valorizzato. Questo non è una eccezione innescata da dati inseriti dall' utente. E' appunto un errore del sistema software, chi ha implementato il client ha dimenticato di valorizzare il campo ID...  
 * [Eccezioni di Business](https://github.com/ascuderetti/devastapp-spring-stateofart/blob/master/src/main/java/it/bologna/devastapp/business/signal/BusinessSignal.java): è un oggetto di modello che rappresenta delle segnalazioni di business, non eredita da "Exception". I Dto contengono sempre una lista di segnalazioni di business, che sarà vuota nel caso in cui non è presentata nessuna eccezione di business (ad esempio un errore di validazione...)

# Gestione Log


# Accesso ai dati e Spring JPA

# Notifiche e Spring Integration

# Test

### Test di Integrazione

### Test Funzionali

# Mapping Dto<=>Entity

# Geolocalizzazione

# Audit


# Debiti Tecnici
In questo progetto ci sono anche soluzioni tecniche da evitare.
Ad esempio la profilazione delle variabili di ambiente tramite profilo maven. E' meglio utilizzare un approccio che non lega l'ambiente alla fase di compilazione, quindi o esternalizzare le variabili direttamente nell'ambiente [ https://12factor.net/it/ ] o sfruttare funzionalità specifiche di alcuni application server che associano le variabili di ambiente a tempo di deploy [ad esempio deployment-plan su oracle weblogic]. Gli script di creazione DB (DDL) non usano ancora (il progetto è una beta) le migration per tenere traccia del versionamento del DB, tool che consentono di gestire le migration sono [FlyWayDB](https://flywaydb.org/) o [MyBatisMigration](http://www.mybatis.org/migrations/). Questi ed altri debiti tecnici sono tracciati in una Kanban (privata).

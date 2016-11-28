package it.bologna.devastapp.presentation;

/**
 * Contiene gli URL REST
 * 
 * @author ascuderetti
 * @author fbusacca
 * 
 * 
 *         Best practice per le URL Rest da utilizzare seguire la sintassi <br>
 *         d'esempio: <br>
 * <br>
 * 
 *         GET /locale/lista - ritorna una lista di tutti i locali <br>
 *         GET /locale/12 ritorna lo specifico locale<br>
 *         POST /locale/inserisci - crea un nuovo locale<br>
 *         PUT /locale/12/aggiorna - aggiorna locale #12 <br>
 *         DELETE /locale/12/elimina - elimina locale #12 <br>
 * <br>
 * 
 * 
 *         Per entity relazionate tra loro <br>
 * <br>
 * 
 *         GET /gestore/12/locale/lista - ritorna una lista di tutti i locali
 *         per il gestore #12 <br>
 *         GET /gestore/12/locale/5 - ritorna il locale #5 per il gestore #12 <br>
 *         POST /gestore/12/locale/inserisci - crea un nuovo locale nel gestore
 *         #12 <br>
 *         PUT /gestore/12/locale/5/aggiorna - aggiorna locale #5 for gestore
 *         #12 <br>
 *         DELETE /gestore/12/locale/5/elimina - elimina il locale #5 del
 *         gestore #12 <br>
 * <br>
 * <br>
 * 
 * @title ricerca con querystring <br>
 *        Per le azioni di ricerca "/ricerca" il resto dell'url viene generato <br>
 *        tramite le opzioni @PathVariable, @RequestParam o l'intero oggetto <br>
 *        Per gli esempi di mapping vedere il file @link{TestController} <br>
 * <br>
 * <br>
 * 
 *        Link utili<br>
 *        Capitolo 5 del libro pro_spring_3.pdf<br>
 * @link{http://www.vinaysahni.com/best-practices-for-a-pragmatic -restful-api} <br>
 * 
 * 
 */
public interface RestUrl {

	// LOGIN - Url definiti su spring-security.xml
	public static final String LOGIN = "/login";
	public static final String LOGOUT = "/logout";
	// <-//

	/*
	 * Profili
	 */
	public static final String GESTORE = "/gestore";
	// TODO: cercare un nome legato al tipo di profilo utente! (app non è
	// l'utente è il tipo di client..)
	public static final String APP = "/app";

	/*
	 * Entity:
	 */
	public static final String OFFERTA = "/offerta";
	public static final String LOCALE = "/locale";
	public static final String ID = "/{id}";
	public static final String PRODOTTO = "/prodotto";

	/*
	 * Azioni:
	 */
	public static final String RICERCA = "/ricerca";
	public static final String LISTA = "/lista";
	public static final String INSERISCI = "/inserisci";
	public static final String AGGIORNA = "/aggiorna";
	public static final String ACQUISTA = "/acquista";
	public static final String ORDINE = "/ordine";
	public static final String CANCELLA = "/cancella";
	public static final String PAGA = "/paga";
	public static final String FOLLOW = "/follow";
	public static final String CHECK = "/check";

	/*
	 * Concetti di Business
	 */
	// <-//

	/*
	 * ONLINE = offerta pagata e live
	 */
	public static final String ONLINE = "/online";

	// Path per Test
	public static final String ROOT_TEST = "/test";
}

package it.bologna.devastapp.business.signal;

import it.bologna.devastapp.business.Definitions;

/**
 * Enum contenente:<br>
 * <Codice, Messaggio, Tipo di Messaggio, Sorgente> <br>
 * di tutte le Segnalazioni di Business.
 * 
 * Esempio Nomenclatura codice "OFFERTA_DESCRIZ_OBB_EB"
 * <ul>
 * <li>OFFERTA: e' il come dell'entity,</li>
 * <li>DESCRIZ: e' la sorgente</li>
 * <li>OBB: sta per Obbligatorio indica in dettaglio il tipo di errore</li>
 * <li>
 * - EB per {@link TipoMessaggio#ERRORE_BUSINESS} <br>
 * - ES {@link TipoMessaggio#ERRORE_SISTEMA} <br>
 * - OK per {@link TipoMessaggio#SUCCESSO} - <br>
 * - INFO per {@link TipoMessaggio#INFO}<br>
 * </li>
 * </ul>
 */
public enum SignalDescriptor {

	// *******************************************************//
	// ********************** SEGNALAZIONI *******************//
	// *******************************************************//

	// *********** MESSAGGI DI ERRORE DI SISTEMA **********//

	GENERICO_ES("generic_system_error", TipoMessaggio.ERRORE_SISTEMA, null),
	// ----- //
	DATABASE_ES(
			"Cavolo cè stato un errore, forse hai inserito qualcosa che esiste già...prova a cambiare valori ",
			TipoMessaggio.ERRORE_SISTEMA, null),
	// ----- //
	INSERIMENTO_CON_ID_ES("Cavolo cè stato un errore",
			TipoMessaggio.ERRORE_SISTEMA, null),
	// ----- //
	AGGIORNAMENTO_SENZA_ID_ES("Cavolo cè stato un errore",
			TipoMessaggio.ERRORE_SISTEMA, null),
	// ----- //
	FOLLOW_CHECK_SENZA_ID_OGGETTO_ES("Cavolo cè stato un errore",
			TipoMessaggio.ERRORE_SISTEMA, null),
	// ----- //
	FOLLOW_CHECK_SENZA_ID_TELEFONO_ES("Cavolo cè stato un errore",
			TipoMessaggio.ERRORE_SISTEMA, null),
	// ----- //
	FOLLOW_CHECK_SENZA_TIPO_TELEFONO_ES("Cavolo cè stato un errore",
			TipoMessaggio.ERRORE_SISTEMA, null),
	// ----- //
	FOLLOW_CHECK_SENZA_ID_UTENTE_ES("Cavolo cè stato un errore",
			TipoMessaggio.ERRORE_SISTEMA, null),

	// ***********//
	// * OFFERTA *//
	// ***********//

	// *********** MESSAGGI DI ERRORE DI BUSINESS **********//

	// Controlli sulle ricerche

	OFFERTA_COORDINATE_NON_VALORIZZATE("Coordinate non valorizzate",
			TipoMessaggio.ERRORE_SISTEMA, null),

	// Controlli sull'aggiornamento
	OFFERTA_AGGIORNAMENTO_OFFERTA_IN_STATO_PUBBLICATA(
			"Non e' possibile modificare un'offerta in stato PUBBLICATA",
			TipoMessaggio.ERRORE_SISTEMA, null),

	// Messaggi sugli id

	OFFERTA_CREAZIONE_OFFERTA_CON_ID(
			"Non e' possibile inserire un'offerta con ID valorizzato",
			TipoMessaggio.ERRORE_BUSINESS, Definitions.Offerta.PMO_PROP_id),

	OFFERTA_MODIFICA_OFFERTA_SENZA_ID(
			"Non e' possibile modificare un'offerta senza ID valorizzato",
			TipoMessaggio.ERRORE_SISTEMA, Definitions.Offerta.PMO_PROP_id),

	// Messaggi sulle date

	// Data iniziale
	OFFERTA_DATA_FINE_MAGGIORE_DATA_FINE(
			"La data iniziale dell''offerta {0} deve essere successiva alla data di oggi {1}",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_dataFine),

	OFFERTA_SECONDI_MILLISECONDI_ZERO(
			"I secondi e i millisecondi devono essere a zero",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_dataInizio),

	OFFERTA_DATA_INIZIO_QUARTO_ORA(
			"La data iniziale dell''offerta deve essere allineata al quarto d'ora",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_dataInizio),

	// Date fine
	OFFERTA_DATA_FINE_MAGGIORATA_DI_UN_ORA(
			"La data finale dell''offerta {0} e'' errata. La data finale dell''offerta deve essere uguale alla data iniziale piu' un ora: {1}",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_dataFine),

	// Messaggi su campi obbligatori

	OFFERTA_E001("Il campo " + Definitions.Offerta.PMO_PROP_dataFine
			+ " e'' obbligatorio", TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_dataFine),

	OFFERTA_E002("Il campo " + Definitions.Offerta.PMO_PROP_dataInizio
			+ " e'' obbligatorio", TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_dataInizio),

	OFFERTA_E003("Il campo " + Definitions.Offerta.PMO_PROP_descrizione
			+ " e'' obbligatorio", TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_descrizione),

	OFFERTA_E004("Il campo " + Definitions.Offerta.ENTITY_PROP_locale
			+ " e'' obbligatorio", TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.ENTITY_PROP_locale),

	OFFERTA_CAMPO_PREZZO_OBBLIGATORIO("Il campo "
			+ Definitions.Offerta.PMO_PROP_prezzoUnitarioPieno
			+ " e'' obbligatorio", TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_prezzoUnitarioPieno),

	OFFERTA_CAMPO_PRODOTTO_OBBLIGATORIO("Il campo "
			+ Definitions.Offerta.PMO_PROP_idProdotto + " e'' obbligatorio",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_idProdotto),

	OFFERTA_E007("Il campo " + Definitions.Offerta.PMO_PROP_quantita
			+ " e'' obbligatorio", TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_quantita),

	OFFERTA_E008("Il campo " + Definitions.Offerta.PMO_PROP_titolo
			+ " e'' obbligatorio", TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_titolo),

	OFFERTA_QUANTITA_MINOREUGUALEAZERO("Inserisci la quantita",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Offerta.PMO_PROP_quantita),

	CHECK_FOLLOW_OFFERTA_SCADUTA_EB("Siamo spiacenti, l'offerta e'' scaduta",
			TipoMessaggio.ERRORE_BUSINESS, null),

	CHECK_FOLLOW_OFFERTA_QUANTITA_ESAURITA_EB("Offerta esaurita",
			TipoMessaggio.ERRORE_BUSINESS, null),

	CHECK_OFFERTA_GIA_CHECK_ES(
			"Puoi usufruire sono una volta della stessa offerta!",
			TipoMessaggio.ERRORE_SISTEMA, null),

	// *********** MESSAGGI GLOBALI DI SUCCESSO O ERRORE**********//

	OFFERTA_INSERIMENTO_OK("L''offerta {0} e'' stata inserita correttamente",
			TipoMessaggio.SUCCESSO, "QUA_METTERE_SORGENTE"),

	OFFERTA_INSERIMENTO_KO("Correggi gli errori",
			TipoMessaggio.ERRORE_BUSINESS, null),

	OFFERTA_FOLLOW_OK(
			"Stai seguendo questa offerta. Riceverai notifiche e informazioni.",
			TipoMessaggio.SUCCESSO, null),

	OFFERTA_FOLLOW_KO(
			"Si e'' verificato un errore durante l''azione di follow su un offerta",
			TipoMessaggio.ERRORE_BUSINESS, null),

	CHECK_OFFERTA_OK("La bevuta e'' tua, bravo figliuolo...",
			TipoMessaggio.SUCCESSO, null),

	CHECK_OFFERTA_KO("Offerta scaduta e/o esaurita",
			TipoMessaggio.ERRORE_BUSINESS, null),

	OFFERTA_GLOBALE_OK("L''offerta e'' stata inserita correttamente",
			TipoMessaggio.SUCCESSO, null),

	OFFERTA_GLOBALE_KO("L''offerta contiene errori",
			TipoMessaggio.ERRORE_BUSINESS, null),

	OFFERTA_LISTA_OK("Ricerca effettuata correttamente",
			TipoMessaggio.SUCCESSO, null),

	OFFERTA_LISTA_KO("Ricerca in errore", TipoMessaggio.ERRORE_BUSINESS, null),

	// <-//

	// ***********//
	// * LOCALE *//
	// ***********//

	// *********** MESSAGGI DI ERRORE DI BUSINESS **********//

	LOCALE_GESTORE_NON_PRESENTE("Gestore non esiste",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_idGestore),

	LOCALE_NOME_OBB_EB("Il campo nome e'' obbligatorio",
			TipoMessaggio.ERRORE_BUSINESS, Definitions.Locale.PMO_PROP_nome),

	LOCALE_DESCRIZIONE_OBB_EB("locale_descrizione_obb_eb",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_descrizione),

	LOCALE_POSIZIONE_OBB_EB("La posizione del locale e'' obbligatoria",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_posizione),

	LOCALE_INSERIMENTO_KO("Correggi gli errori", TipoMessaggio.ERRORE_BUSINESS,
			null),

	LOCALE_AGGIORNAMENTO_KO("Correggi gli errori",
			TipoMessaggio.ERRORE_BUSINESS, null),

	// TODO LOGO del locale da controllare???

	LOCALE_POSIZIONE_INDIRIZZO_OBB_EB(
			"L''indirizzo del locale e'' obbligatorio",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_POSIZIONE_INDIRIZZO),

	LOCALE_POSIZIONE_NUMERO_OBB_EB("Il numero del locale e'' obbligatorio",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_POSIZIONE_NUMERO),

	LOCALE_POSIZIONE_CAP_OBB_EB("Il cap del locale e'' obbligatorio",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_POSIZIONE_CAP),

	LOCALE_POSIZIONE_CITTA_OBB_EB("La citta' del locale e'' obbligatoria",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_POSIZIONE_CITTA),

	LOCALE_POSIZIONE_LONGITUDINE_OBB_EB(
			"La longitudine del locale e'' obbligatoria",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_POSIZIONE_LONGITUDINE),

	LOCALE_POSIZIONE_LATITUDINE_OBB_EB(
			"La latitudine del locale e'' obbligatoria",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Locale.PMO_PROP_POSIZIONE_LATITUDINE),

	// *********** MESSAGGI DI INFO **********//

	LOCALE_INSERIMENTO_OK("Il locale e'' stato inserito correttamente",
			TipoMessaggio.SUCCESSO, null),

	LOCALE_AGGIORNAMENTO_OK("Il locale e'' stato aggiornato correttamente",
			TipoMessaggio.SUCCESSO, null),

	LOCALE_LISTA_OK("Lista locali ricevuta correttamente",
			TipoMessaggio.SUCCESSO, null),

	LOCALE_FOLLOW_OK(
			"Stai seguendo questo locale. Riceverai notifiche e informazioni.",
			TipoMessaggio.SUCCESSO, null),

	LOCALE_FOLLOW_KO("Si e'' verificato un errore durante l''azione di follow",
			TipoMessaggio.ERRORE_BUSINESS, null),
	// <-//

	// ************//
	// * PRODOTTO *//
	// ************//

	PRODOTTO_DESCRIZIONE_OBB_EB("prodotto_descrizione_obb_eb",
			TipoMessaggio.ERRORE_BUSINESS,
			Definitions.Prodotto.PMO_PROP_descrizione),

	PRODOTTO_INSERIMENTO_GLOBALE_KO("prodotto_insert_KO",
			TipoMessaggio.ERRORE_BUSINESS, null),

	PRODOTTO_INSERIMENTO_GLOBALE_OK(
			"Il prodotto e'' stato inserito correttamente",
			TipoMessaggio.SUCCESSO, null),

	PRODOTTO_GET_LISTA_GLOBALE_OK("Prodotti recuperati",
			TipoMessaggio.SUCCESSO, null),

	// <-//

	// ************//
	// * PAGAMENTO *//
	// ************//

	// *********** MESSAGGI DI ERRORE **********//

	PAGAMENTO_PAYPAL_CREATO_KO("paypal_payment_creation_KO",
			TipoMessaggio.ERRORE_BUSINESS, null),

	PAGAMENTO_PAYPAL_VISUALIZZATO_KO(
			"Si e'' verificato un errore durante l''azione di visualizzazione ordine",
			TipoMessaggio.ERRORE_BUSINESS, null),

	PAGAMENTO_PAYPAL_ESEGUITO_KO(
			"Si e'' verificato un errore durante l''azione di esecuzione pagamento",
			TipoMessaggio.ERRORE_BUSINESS, null),

	PAGAMENTO_PAYPAL_ERRORE_FILE_CONFIGURAZIONE_ES(
			"Si e'' verificato un errore durante la configurazione per la comunicazione a Paypal",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_PAYPAL_ERRORE_COMUNICAZIONE_ES(
			"Si e'' verificato un errore durante la comunicazione a Paypal: {0}",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_PAYPAL_CREAZIONE_EXPIRED_EB(
			"Si e'' verificato un errore durante la creazione del pagamento a Paypal: stato expired",
			TipoMessaggio.ERRORE_BUSINESS, null),

	PAGAMENTO_PAYPAL_CREAZIONE_FAILED_EB(
			"Si e'' verificato un errore durante la creazione del pagamento a Paypal: stato failed",
			TipoMessaggio.ERRORE_BUSINESS, null),

	PAGAMENTO_PAYPAL_EXPIRED_EB(
			"Si e'' verificato un errore durante il pagamento a Paypal: stato expired",
			TipoMessaggio.ERRORE_BUSINESS, null),

	PAGAMENTO_PAYPAL_FAILED_EB(
			"Si e'' verificato un errore durante il pagamento a Paypal: stato failed",
			TipoMessaggio.ERRORE_BUSINESS, null),

	PAGAMENTO_PAYPAL_ERRORE_TRANSAZIONE_ES(
			"Si e'' verificato un errore durante la transazione di pagamento con Paypal: {0}",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_LISTA_OFFERTA_VUOTA_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: lista offerta vuota",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_GESTORE_NON_PRESENTE_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: gestore non presente",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_TITOLO_ACQUISTO_NON_PRESENTE_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: titolo acquisto non presente",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_DATA_NON_VALIDA_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: data di pagamento non valida",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_IMPORTO_TOTALE_NON_VALIDO_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: importo totale non valido",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_PAGAMENTO_ID_NON_PRESENTE_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: transazione id non valido",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_REDIRECT_URL_NON_PRESENTE_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: redirect url non presente",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_REDIRECT_URL_NON_VALIDA_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: redirect url non valido",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_PAYER_ID_NON_PRESENTE_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: transazione id non valido",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_ACCESS_TOKEN_NON_PRESENTE_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: access token non valido",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_STATO_NON_PRESENTE_ES(
			"Si e'' verificato un errore durante la creazione del pagamento: stato non presente",
			TipoMessaggio.ERRORE_SISTEMA, null),

	PAGAMENTO_STATO_NON_VALIDO_EB(
			"Si e'' verificato un errore durante la creazione del pagamento: stato non valido",
			TipoMessaggio.ERRORE_BUSINESS, null),

	// *********** MESSAGGI DI INFO **********//

	PAGAMENTO_PAYPAL_CREATO_OK("paypal_payment_creation_OK",
			TipoMessaggio.SUCCESSO, null),

	PAGAMENTO_PAYPAL_VISUALIZZATO_OK(
			"Il pagamento e'' stato visualizzato su devastapp website",
			TipoMessaggio.SUCCESSO, null),

	PAGAMENTO_PAYPAL_ESEGUITO_OK("Il pagamento e'' stato eseguito su Paypal",
			TipoMessaggio.SUCCESSO, null),

	// <-//

	// ************//
	// * NOTIFICHE *//
	// ************//
	NOTIFICA_FOLLOW_LOCALE_OFF_ONLINE_INFO(
			"Un locale che segui ha inserito un offerta!", TipoMessaggio.INFO,
			null),

	NOTIFICA_FOLLOW_OFFERTA_CHECKATA_INFO("L'' offerta e' stata checckata!",
			TipoMessaggio.INFO, null);
	// <-//

	/**
	 * Il messaggio descrittivo dell'eccezione.
	 */
	private final String messaggio;
	/**
	 * Il tipo di messaggio
	 */
	private final TipoMessaggio tipoMessaggio;

	/**
	 * Sorgente della signal
	 */
	private final String sorgente;

	/**
	 * Inizializza gli attributi di una nuova istanza di questa classe.
	 * 
	 * @param messaggio
	 *            il messaggio descrittivo dell'eccezione.
	 */
	private SignalDescriptor(String messaggio, TipoMessaggio tipoMessaggio,
			String sorgente) {
		this.messaggio = messaggio;
		this.tipoMessaggio = tipoMessaggio;
		this.sorgente = sorgente;
	}

	/**
	 * 
	 * @return il codice dell'eccezione.
	 */
	public String getCodice() {
		return name();
	}

	/**
	 * @return il messaggio descrittivo dell'eccezione.
	 */
	public String getMessaggio() {
		return messaggio;
	}

	/**
	 * @return il messaggio descrittivo dell'eccezione.
	 */
	public TipoMessaggio getTipoMessaggio() {
		return tipoMessaggio;
	}

	public String getSorgente() {
		return sorgente;
	}
}

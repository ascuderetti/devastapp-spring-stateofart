package it.bologna.devastapp.business;

/**
 * Interfaccia contenente costanti di Business e di modello. <br>
 * 
 * Va utilizzata ad esempio per definire i nomi delle properties da mappare nei
 * Mapper, per definire la sorgente di una BusinessSignal, e in generale
 * costanti di dominio.
 * 
 * @author ascuderetti
 * 
 */
public interface Definitions {

	public static final String PMO_PROP_ID = "id";
	public static final String ENTITY_PROP_ID = "id";

	public interface ParametriBusiness {
		public static final int RAGGIO_RICERCA_KM = 1;
	}

	public interface Paypal {
		/*
		 * Properties Paypal
		 */
		public static final String REDIRECT = "REDIRECT";
		public static final String SALE = "sale";
		public static final String CURRENCY_EUR = "EUR";
		public static final String PAYPAL = "paypal";
		public static final String CLIENT_ID = "clientID";
		public static final String CLIENT_SECRET = "clientSecret";
		public static final String PAYER_ID = "PayerID";
		public static final String CREATED = "created";
		public static final String APPROVED = "approved";
		public static final String FAILED = "failed";
		public static final String CANCELED = "canceled";
		public static final String EXPIRED = "expired";
		public static final String PAYPAL_SDK = "/sdk_config.properties";
		public static String ID_PAYPAL_PAYMENT_FAKE = "PAY-5U6912489D785871JKOLPENA";
		public static String REDIRECT_PAYPAL_FAKE = "http://sandbox.paypal.fake.com";
	}

	public interface Offerta {

		/*
		 * Properties Pmo e Entity
		 */

		public static final String PMO_PROP_id = "id";
		public static final String PMO_PROP_titolo = "titolo";
		public static final String PMO_PROP_descrizione = "descrizione";
		public static final String PMO_PROP_idProdotto = "idProdotto";
		public static final String PMO_PROP_dataInizio = "dataInizio";
		public static final String PMO_PROP_dataFine = "dataFine";
		public static final String PMO_PROP_quantita = "quantita";
		public static final String PMO_PROP_quantitaRimanente = "quantitaRimanente";
		public static final String PMO_PROP_prezzoUnitarioPieno = "prezzoUnitarioPieno";
		public static final String PMO_PROP_prezzoUnitarioScontato = "prezzoUnitarioScontato";
		public static final String PMO_PROP_idLocale = "idLocale";
		public static final String PMO_PROP_statoOfferta = "statoOfferta";

		public static final String ENTITY_PROP_id = "id";
		public static final String ENTITY_PROP_titolo = "titolo";
		public static final String ENTITY_PROP_descrizione = "descrizione";
		public static final String ENTITY_PROP_prodotto = "prodotto";
		public static final String ENTITY_PROP_dataInizio = "dataInizio";
		public static final String ENTITY_PROP_dataFine = "dataFine";
		public static final String ENTITY_PROP_quantita = "quantita";
		public static final String ENTITY_PROP_prezzoUnitarioPieno = "prezzoUnitarioPieno";
		public static final String ENTITY_PROP_prezzoUnitarioScontato = "prezzoUnitarioScontato";
		public static final String ENTITY_PROP_locale = "locale";
		public static final String ENTITY_PROP_statoOfferta = "statoOfferta";

	}

	public interface Prodotto {

		/*
		 * Properties Pmo e Entity
		 */
		public static final String PMO_PROP_id = "id";
		public static final String PMO_PROP_descrizione = "descrizione";
		public static final String ENTITY_PROP_id = "id";
		public static final String ENTITY_PROP_descrizione = "descrizione";

	}

	public interface Locale {

		/*
		 * Properties Pmo e Entity
		 */
		public static final String PMO_PROP_id = "id";
		public static final String PMO_PROP_idGestore = "idGestore";
		public static final String PMO_PROP_nome = "nome";
		public static final String PMO_PROP_descrizione = "descrizione";
		public static final String PMO_PROP_posizione = "posizione";
		public static final String PMO_PROP_fidelizzazione = "fidelizzazione";

		public static final String PMO_PROP_POSIZIONE_ID = "posizionePmo.id";
		public static final String PMO_PROP_POSIZIONE_INDIRIZZO = "posizionePmo.indirizzo";
		public static final String PMO_PROP_POSIZIONE_NUMERO = "posizionePmo.numero";
		public static final String PMO_PROP_POSIZIONE_CITTA = "posizionePmo.citta";
		public static final String PMO_PROP_POSIZIONE_CAP = "posizionePmo.cap";
		public static final String PMO_PROP_POSIZIONE_LONGITUDINE = "posizionePmo.longitudine";
		public static final String PMO_PROP_POSIZIONE_LATITUDINE = "posizionePmo.latitudine";

		public static final String ENTITY_PROP_ID = "id";
		public static final String ENTITY_PROP_idGestore = "idGestore";
		public static final String ENTITY_PROP_NOME = "nome";
		public static final String ENTITY_PROP_DESCRIZIONE = "descrizione";
		public static final String ENTITY_PROP_POSIZIONE = "posizione";
		public static final String ENTITY_PROP_fidelizzazione = "fidelizzazione";

	}

	public interface Gestore {

		/*
		 * Properties Pmo e Entity
		 */
		public static final String PMO_PROP_ID = "id";
		public static final String PMO_PROP_NOME = "nome";
		public static final String PMO_PROP_MAIL = "mail";
		public static final String PMO_PROP_LISTA_LOCALI = "listaLocali";

		public static final String ENTITY_PROP_ID = "id";
		public static final String ENTITY_PROP_NOME = "nome";
		public static final String ENTITY_PROP_MAIL = "mail";
		public static final String ENTITY_PROP_LISTA_LOCALI = "listaLocali";
	}

	public interface Posizione {

		/*
		 * Properties Pmo e Entity
		 */

		public static final String PMO_PROP_ID = "id";
		public static final String PMO_PROP_INDIRIZZO = "indirizzo";
		public static final String PMO_PROP_NUMERO = "numero";
		public static final String PMO_PROP_CITTA = "citta";
		public static final String PMO_PROP_CAP = "cap";
		public static final String PMO_PROP_LONGITUDINE = "longitudine";
		public static final String PMO_PROP_LATITUDINE = "latitudine";

		public static final String ENTITY_PROP_ID = "id";
		public static final String ENTITY_PROP_INDIRIZZO = "indirizzo";
		public static final String ENTITY_PROP_NUMERO = "numero";
		public static final String ENTITY_PROP_CITTA = "citta";
		public static final String ENTITY_PROP_CAP = "cap";
		public static final String ENTITY_PROP_LONGITUDINE = "longitudine";
		public static final String ENTITY_PROP_LATITUDINE = "latitudine";
	}

	public interface Utente {

		/*
		 * Properties Pmo e Entity
		 */
		public static final String PMO_PROP_ID = "id";
		public static final String PMO_PROP_ID_TELEFONO = "idTelefono";
		public static final String PMO_PROP_TIPO_TELEFONO = "tipoTelefono";

		public static final String ENTITY_PROP_ID = "id";
		public static final String ENTITY_PROP_ID_TELEFONO = "idTelefono";
		public static final String ENTITY_PROP_TIPO_TELEFONO = "tipoTelefono";
	}

	public interface MovimentoUtente {

		// Pmo
		public static final String PMO_PROP_ID_OGGETTO = "idOggetto";
		public static final String PMO_PROP_ID_UTENTE = "idUtente";
	}

	public interface MovimentiOfferta {

		// Entity
		public static final String ENTITY_PROP_ID_OFFERTA = "idOfferta";
		public static final String ENTITY_PROP_STATO = "stato";
		public static final String ENTITY_PROP_ID_UTENTE = "idUtente";
	}

	public interface MovimentiLocale {

		// Entity
		public static final String ENTITY_PROP_ID_LOCALE = "idLocale";
		public static final String ENTITY_PROP_STATO = "stato";
		public static final String ENTITY_PROP_ID_UTENTE = "idUtente";
	}

	public interface Pagamento {

		/*
		 * Properties Pmo e Entity
		 */

		public static final String PMO_PROP_ID = "id";
		public static final String PMO_PROP_LISTA_ID_OFFERTA = "listaIdOfferta";
		public static final String PMO_PROP_ID_GESTORE = "idGestore";
		public static final String PMO_PROP_TITOLO_ACQUISTO = "titoloAcquisto";
		public static final String PMO_PROP_DATA_PAGAMENTO = "dataPagamento";
		public static final String PMO_PROP_IMPORTO_TOTALE = "importoTotale";
		public static final String PMO_PROP_PAGAMENTO_ID = "pagamentoID";
		public static final String PMO_PROP_REDIRECT_URL = "redirectURL";
		public static final String PMO_PROP_PAYER_ID = "payerID";
		public static final String PMO_PROP_STATO = "stato";

		public static final String ENTITY_PROP_ID = "id";
		public static final String ENTITY_PROP_OFFERTE = "offerte";
		public static final String ENTITY_PROP_ID_GESTORE = "idGestore";
		public static final String ENTITY_PROP_TITOLO_ACQUISTO = "titoloAcquisto";
		public static final String ENTITY_PROP_DATA_PAGAMENTO = "dataPagamento";
		public static final String ENTITY_PROP_IMPORTO_TOTALE = "importoTotale";
		public static final String ENTITY_PROP_ID_PAYPAL_PAYMENT = "idPaypalPayment";
		public static final String ENTITY_PROP_REDIRECT_URL = "redirectURL";
		public static final String ENTITY_PROP_ID_PAYPAL_PAYER = "idPaypalPayer";
		public static final String ENTITY_PROP_STATO = "stato";
	}

	public interface Payment {

		/*
		 * Properties Entity Paypal
		 */
		public static final String ENTITY_PROP_DATA_PAGAMENTO = "updateTime";
		public static final String ENTITY_PROP_STATO = "state";
	}
}

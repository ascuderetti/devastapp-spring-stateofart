var MOCK_MODE = false;

var SERVER_PORT = '8080';
var LOCALHOST = "http://localhost:" + SERVER_PORT + "/devastapp";
var HEROKU_SVIL = "http://devastapp.herokuapp.com/";

// var BaseUrl = '${devastapp.host}';
var BaseUrl = HEROKU_SVIL;

// PROFILI
var GESTORE = "/gestore";
var APP = "/APP";

// CONTROLLER
var PRODOTTO = "/prodotto";
var LOCALE = "/locale";
var OFFERTA = "/offerta";

// OPERAZIONI
var LISTA = "/lista";
var INSERISCI = "/inserisci";
var AGGIORNA = "/aggiorna";
var PAGA = "/paga";

// URL base gestore e app
var BaseUrl_Gestore = BaseUrl + GESTORE;
var BaseUrl_APP = BaseUrl + APP;

var RestUrl = {

	/*
	 * PRODOTTO
	 */
	GET_PRODOTTI : (MOCK_MODE ? 'responseMock/getProdotti.json'
			: BaseUrl_Gestore + PRODOTTO + LISTA),

	/*
	 * LOCALE
	 */
	AGGIORNA_LOCALE : (MOCK_MODE ? 'responseMock/getLocalePiazzaMaggiore.json'
					: BaseUrl_Gestore + LOCALE + AGGIORNA),
					
	GET_LOCALE_BY_ID : (MOCK_MODE ? 'responseMock/getLocalePiazzaMaggiore.json'
			: BaseUrl_Gestore + LOCALE),

	/*
	 * GESTORE
	 */
	GET_GESTORI : (MOCK_MODE ? 'responseMock/getProdotti.json'
			: BaseUrl_Gestore + GESTORE + LISTA),

	/*
	 * OFFERTA
	 */
	INSERISCI_OFFERTA : (MOCK_MODE ? 'responseMock/inserisciResponse.json'
			: BaseUrl_Gestore + OFFERTA + INSERISCI),
	INSERISCI_PAGA : (MOCK_MODE ? 'responseMock/inserisciResponse.json'
			: BaseUrl_Gestore + OFFERTA + PAGA),

	/*
	 * NOTIFICHE
	 */
	NOTIFICA_OFFERTA_INSERITA : (MOCK_MODE ? 'responseMock/inserisciResponse.json'
			: BaseUrl_Gestore + "/notifica/offertaInserita"),
};

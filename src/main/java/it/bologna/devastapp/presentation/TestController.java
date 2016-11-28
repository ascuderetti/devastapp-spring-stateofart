package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.business.InvioMailService;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.presentation.pmo.Test1Pmo;
import it.bologna.devastapp.presentation.pmo.Test2Pmo;
import it.bologna.devastapp.presentation.pmo.Test3Pmo;
import it.bologna.devastapp.util.JsonResponseUtil;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;

/**
 * Gestisce le richieste di offerte
 * 
 * @author ascuderetti
 */
@Controller
@RequestMapping(RestUrl.ROOT_TEST)
public class TestController extends AbstractController {

	@Autowired
	MessageSource messageSource;

	@Autowired
	InvioMailService invioMailService;

	private String clientID;

	private String clientSecret;

	String accessToken;

	String paymentID;

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(TestController.class);

	@RequestMapping(value = "/richiestaOk", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody BaseRispostaJson getOffertaOk(
			HttpServletResponse response, @RequestParam String position) {

		LOG.info("Richiesta offerta in posizione: {}.", position);

		/*
		 * PMO DI TEST
		 */
		Test2Pmo es1 = new Test2Pmo();
		es1.setName("nome");
		Test3Pmo pos1 = new Test3Pmo();
		pos1.setLatitude(11.3322323);
		pos1.setLongitude(1232.42354);
		Test1Pmo off1 = new Test1Pmo();
		off1.setId(1L);
		off1.setDescription("Per mezz'ora birra a 1euro");
		off1.setOfferer(es1);
		off1.setPosition(pos1);
		off1.setQuantity(100);
		off1.setPrice(BigDecimal.valueOf(2.5));

		/*
		 * OFFERTA 2
		 */
		Test2Pmo es2 = new Test2Pmo();
		es2.setName("Gazzetta");
		Test3Pmo pos2 = new Test3Pmo();
		pos2.setLatitude(111.3322323);
		pos2.setLongitude(1222332.42354);
		Test1Pmo off2 = new Test1Pmo();

		off2.setDescription("Per mezz'ora cocktail a 1euro");
		off2.setOfferer(es2);
		off2.setPosition(pos2);
		// off2.setStuff("cocktail");
		off2.setQuantity(200);
		off2.setPrice(BigDecimal.valueOf(4.5));

		List<Test1Pmo> listOffers = new ArrayList<Test1Pmo>();
		listOffers.add(off1);
		listOffers.add(off2);

		/*
		 * Creo la risposta Json
		 */
		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(off1,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;

	}

	@RequestMapping(value = "/richiestaKo", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody BaseRispostaJson getOffertaKo(
			HttpServletResponse response, @RequestParam String position) {

		// usare @PathVariable e @RequestMapping(value = "/offers/{position}"

		LOG.info("Richiesta offerta in posizione: {}.", position);

		/*
		 * OFFERTA 1
		 */
		Test2Pmo es1 = new Test2Pmo();
		es1.setName("nome");
		Test3Pmo pos1 = new Test3Pmo();
		pos1.setLatitude(11.3322323);
		pos1.setLongitude(1232.42354);
		Test1Pmo off1 = new Test1Pmo();
		off1.setId(null);
		off1.setDescription("Per mezz'ora birra a 1euro");
		off1.setOfferer(es1);
		off1.setPosition(pos1);
		off1.setQuantity(100);
		off1.setPrice(BigDecimal.valueOf(2.5));

		MessaggiBusinessSignalUtils.aggiungiSignal(off1,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB);

		/*
		 * Creo la risposta Json
		 */

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(off1,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;

	}

	// chiamata con
	// localhost:8080/devastapp/test/ricerca?latitudine=11.43333&longitudine=22.43333
	@RequestMapping(value = RestUrl.RICERCA, method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody BaseRispostaJson ricercaOfferta(
			HttpServletResponse response,
			@RequestParam("longitudine") Double longitudine,
			@RequestParam("latitudine") Double latitudine) {

		// usare @PathVariable e @RequestMapping(value = "/offers/{position}"

		LOG.info(
				"Trova offerte dal punto di longitudine: {} e latitudine: {}.",
				longitudine, latitudine);

		/*
		 * DATI MOCK - TODO: spostare in apposite classi test data
		 */

		/*
		 * OFFERTA 1
		 */
		Test2Pmo es1 = new Test2Pmo();
		es1.setName("nome");
		Test3Pmo pos1 = new Test3Pmo();
		pos1.setLatitude(latitudine);
		pos1.setLongitude(longitudine);
		Test1Pmo off1 = new Test1Pmo();
		off1.setId(null);
		off1.setDescription("Per mezz'ora birra a 1euro");
		off1.setOfferer(es1);
		off1.setPosition(pos1);
		off1.setQuantity(100);
		off1.setPrice(BigDecimal.valueOf(2.5));

		MessaggiBusinessSignalUtils.aggiungiSignal(off1,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB);

		/*
		 * Creo la risposta Json
		 */

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(off1,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;

	}

	// chiamata con
	// localhost:8080/devastapp/test/searchOgg?latitudine=11.43333&longitudine=22.43333
	@RequestMapping(value = "/searchOgg", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody BaseRispostaJson ricercaOffertaDaOggettoPMO(
			HttpServletResponse response, PosizionePmo posizionePmo) {

		// usare @PathVariable e @RequestMapping(value = "/offers/{position}"

		LOG.info(
				"Trova offerte dal punto di longitudine: {} e latitudine: {}.",
				posizionePmo.getLongitudine(), posizionePmo.getLatitudine());

		/*
		 * DATI MOCK - TODO: spostare in apposite classi test data
		 */

		/*
		 * OFFERTA 1
		 */
		Test2Pmo es1 = new Test2Pmo();
		es1.setName("nome");
		Test3Pmo pos1 = new Test3Pmo();
		// pos1.setLatitude(latitudine);
		// pos1.setLongitude(longitudine);
		Test1Pmo off1 = new Test1Pmo();
		off1.setId(null);
		off1.setDescription("Per mezz'ora birra a 1euro");
		off1.setOfferer(es1);
		off1.setPosition(pos1);
		off1.setQuantity(100);
		off1.setPrice(BigDecimal.valueOf(2.5));

		MessaggiBusinessSignalUtils.aggiungiSignal(off1,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB);

		/*
		 * Creo la risposta Json
		 */

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(off1,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;

	}

	// chiamata con
	// localhost:8080/devastapp/test/searchModel?posizionePmo.latitudine=11.43333&posizionePmo.longitudine=22.43333
	@RequestMapping(value = "/searchModel", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody BaseRispostaJson ricercaOffertaDaModel(
			HttpServletResponse response, LocalePmo localePmo) {

		// usare @PathVariable e @RequestMapping(value = "/offers/{position}"

		LOG.info(
				"Trova offerte dal punto di longitudine: {} e latitudine: {}.",
				localePmo.getPosizione().getLongitudine(), localePmo
						.getPosizione().getLatitudine());

		/*
		 * DATI MOCK - TODO: spostare in apposite classi test data
		 */

		/*
		 * OFFERTA 1
		 */
		Test2Pmo es1 = new Test2Pmo();
		es1.setName("nome");
		Test3Pmo pos1 = new Test3Pmo();
		// pos1.setLatitude(latitudine);
		// pos1.setLongitude(longitudine);
		Test1Pmo off1 = new Test1Pmo();
		off1.setId(null);
		off1.setDescription("Per mezz'ora birra a 1euro");
		off1.setOfferer(es1);
		off1.setPosition(pos1);

		/*
		 * Creo la risposta Json
		 */

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(off1,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;

	}

	// chiamata con
	// localhost:8080/devastapp/test/offerta/2
	@RequestMapping(value = RestUrl.OFFERTA + RestUrl.ID, method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody BaseRispostaJson ricercaOffertaPerId(
			HttpServletResponse response, @PathVariable("id") Long id) {

		LOG.info("Ottieni offerta con id: " + id);

		/*
		 * DATI MOCK - TODO: spostare in apposite classi test data
		 */

		/*
		 * OFFERTA 1
		 */
		Test2Pmo es1 = new Test2Pmo();
		es1.setName("nome");
		Test3Pmo pos1 = new Test3Pmo();
		// pos1.setLatitude(latitudine);
		// pos1.setLongitude(longitudine);
		Test1Pmo off1 = new Test1Pmo();
		off1.setId(null);
		off1.setDescription("Per mezz'ora birra a 1euro");
		off1.setOfferer(es1);
		off1.setPosition(pos1);
		off1.setQuantity(100);
		off1.setPrice(BigDecimal.valueOf(2.5));

		MessaggiBusinessSignalUtils.aggiungiSignal(off1,
				SignalDescriptor.PRODOTTO_DESCRIZIONE_OBB_EB);

		/*
		 * Creo la risposta Json
		 */

		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(off1,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;

	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/eccezioneRunTime", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody BaseRispostaJson lanciaEccezioneRunTime(
			HttpServletResponse response) {

		LOG.info("Lancio eccezione NullPointer");

		BaseRispostaJson risp = null;
		risp.setCodice("");
		return risp;

	}

	@RequestMapping(value = "/eccezioneDataAccess", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody BaseRispostaJson lanciaEccezioneDataAccess(
			HttpServletResponse response) {

		LOG.info("Lancio eccezione DataIntegrityViolationException");

		throw new DataIntegrityViolationException(
				"DataIntegrityViolationException da /eccezioneDataAccess");

	}

	@RequestMapping(value = "/eccezioneErroreSistema", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson lanciaEccezioneErroreSistema(
			HttpServletResponse response) {

		LOG.info("Lancio eccezione ErroreSistema");

		throw new ErroreSistema(SignalDescriptor.INSERIMENTO_CON_ID_ES);

	}

	@RequestMapping(value = "/provadata", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody OffertaScritturaPmo provadata(
			HttpServletRequest request, HttpSession session,
			HttpServletResponse response,
			@RequestBody OffertaScritturaPmo offertaPmo) {
		// @RequestBody OffertaPmo offertaPmo
		// OffertaPmo offertaPmo = new OffertaPmo();
		LOG.info("dateTime " + offertaPmo.getDataInizio());
		// offertaPmo.setDataInizio(new DateTime());

		return offertaPmo;

	}

	@RequestMapping(value = "/invioMail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson invioMail(HttpServletRequest request,
			HttpSession session, HttpServletResponse response) {

		invioMailService.inviaMail();

		// risposta fake, interessa solo inviare la mail
		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(null,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;

	}

	@RequestMapping(value = "/acquista", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ModelAndView acquista(HttpServletRequest request,
			HttpSession session, HttpServletResponse response) {

		InputStream is = null;
		try {
			is = TestController.class
					.getResourceAsStream("/sdk_config.properties");
			// Payment.initConfig(is);
			Properties props = new Properties();
			props.load(is);
			PayPalResource.initConfig(props);
			// ConfigManager.getInstance().load(props);
			String clientID = props.getProperty("clientID");
			String clientSecret = props.getProperty("clientSecret");
			// String clientID =
			// ConfigManager.getInstance().getConfigurationMap()
			// .get("clientID");
			// String clientSecret = ConfigManager.getInstance()
			// .getConfigurationMap().get("clientSecret");
			LOG.info("clientID: " + clientID);
			LOG.info("clientSecret: " + clientSecret);

			// STEP 1 :Get an access token using client id and client secret.
			OAuthTokenCredential oAuth = new OAuthTokenCredential(clientID,
					clientSecret);
			LOG.info("oAuth: " + oAuth);
			accessToken = oAuth.getAccessToken();
			LOG.info("accessToken: " + accessToken);

			// STEP 2 : Create a payment by constructing a payment object using
			// access token.
			APIContext apiContext = new APIContext(accessToken);
			// apiContext.setConfigurationMap(sdkConfig);

			Amount amount = new Amount();
			amount.setCurrency("USD");
			amount.setTotal("12");

			Transaction transaction = new Transaction();
			transaction.setDescription("creating a payment");
			transaction.setAmount(amount);

			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);

			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");

			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);
			RedirectUrls redirectUrls = new RedirectUrls();
			redirectUrls.setCancelUrl("http://localhost:8080/devastapp/");
			redirectUrls
					.setReturnUrl("http://localhost:8080/devastapp/test/paga");
			payment.setRedirectUrls(redirectUrls);

			Payment createdPayment = payment.create(apiContext);
			LOG.info("createdPayment: " + createdPayment);

			paymentID = createdPayment.getId();
			String redirectUrl = null;

			for (Links link : createdPayment.getLinks()) {
				if (link.getMethod().equals(Definitions.Paypal.REDIRECT)) {
					redirectUrl = link.getHref();
					break;
				}
			}

			return new ModelAndView("redirect:" + redirectUrl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	@RequestMapping(value = "/paga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson paga(HttpServletRequest request,
			HttpSession session, HttpServletResponse response) {

		try {// Visualizzare una pagina con bottone PAY NOW

			// Chiamare il codice sotto per effettuare il pagamento alla
			// pressione del bottone PAY NOW
			APIContext apiContext = new APIContext(accessToken);

			Payment payment = new Payment();
			payment.setId(paymentID);
			PaymentExecution paymentExecute = new PaymentExecution();
			String payerID = request.getParameter("PayerID");
			if (payerID != null) {
				paymentExecute.setPayerId(request.getParameter("PayerID"));
				payment = payment.execute(apiContext, paymentExecute);
				// Poi redirigere in una pagina di ordine confermanto l'utente
				LOG.info("payment: " + payment);
			} else {
				throw new PayPalRESTException("Error: No payerID");
			}

		} catch (PayPalRESTException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		// risposta fake, interessa solo inviare la mail
		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(null,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;

	}

	@RequestMapping(value = "/i18n", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson i18n() {

		// risposta fake, interessa solo controllare che localizza i messaggi
		// nella lingua del browser
		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(null,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_KO,
						SignalDescriptor.PRODOTTO_INSERIMENTO_GLOBALE_OK);

		return risposta;
	}
}
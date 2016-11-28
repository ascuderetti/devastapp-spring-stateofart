package it.bologna.devastapp.util;

import it.bologna.devastapp.business.signal.BusinessSignal;
import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaDati;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.jsonmodel.RispostaInserimentoSingolo;
import it.bologna.devastapp.presentation.jsonmodel.RispostaPagamento;
import it.bologna.devastapp.presentation.jsonmodel.RispostaRicercaLista;
import it.bologna.devastapp.presentation.pmo.BasePmo;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * 
 * @author ascuderetti
 * 
 */
public final class JsonResponseUtil {

	private static String ERROR_TRANSLATION_STRING = "Ops! We forget to translate this message on business signals!";

	/**
	 * Crea una risposta JSON con una lista di PMO
	 * 
	 * @param listaPmoOutput
	 * @param inputPmo
	 * @param globaleKo
	 * @param globaleOk
	 * @return
	 */
	public static <Pmo extends BasePmo> BaseRispostaJson creaRispostaJsonRicercaLista(
			List<Pmo> listaPmoOutput, BasePmo inputPmo,
			SignalDescriptor globaleKo, SignalDescriptor globaleOk) {

		BaseRispostaJson risposta = new BaseRispostaJson();

		/*
		 * Caso Esito OK
		 */
		// nota se abbiamo una ricerca di tipo "findAll" inputPmo e' null,
		// quindi non avrà mai errori
		if (inputPmo == null
				|| MessaggiBusinessSignalUtils.isValidPmo(inputPmo)) {

			risposta.setSuccesso(true);
			risposta.setCodice(globaleOk.getCodice());
			risposta.setMessaggio(globaleOk.getMessaggio());
			risposta.setTipoMessaggio(globaleOk.getTipoMessaggio());
			risposta.setSorgente(globaleOk.getSorgente());

		} else {

			risposta.setSuccesso(false);

			risposta.setCodice(globaleKo.getCodice());
			risposta.setMessaggio(globaleKo.getMessaggio());
			risposta.setTipoMessaggio(globaleKo.getTipoMessaggio());
			risposta.setSorgente(globaleKo.getSorgente());

		}

		/*
		 * Mapping dati comuni
		 */

		RispostaRicercaLista<Pmo> dati = new RispostaRicercaLista<Pmo>();
		dati.setLista(listaPmoOutput);
		dati.setListaBusinessSignal(inputPmo == null ? null : inputPmo
				.getListaBusinessSignal());

		risposta.setDati(dati);

		return risposta;
	}

	/**
	 * Dato un PMO, una signal di OK e una signal di KO crea un
	 * {@link BaseRispostaJson}.
	 * 
	 * @param pmo
	 *            ritornato dal service
	 * @param globaleKo
	 *            messaggio globale da mostrare in caso di KO
	 * @param globaleOk
	 *            messaggio globale da mostrare in caso di OK
	 * @return RispostaJson con oltre al messaggio globale, in caso di OK
	 *         ritorna l'id dell'oggetto inserito, in caso di KO ritorna l'id e
	 *         la lista di businessSignal dell'oggetto in input
	 */
	public static BaseRispostaJson creaRispostaJsonInserimentoSingolo(
			BasePmo pmo, SignalDescriptor globaleKo, SignalDescriptor globaleOk) {

		BaseRispostaJson risposta = new BaseRispostaJson();

		/*
		 * Caso Esito OK
		 */
		if (MessaggiBusinessSignalUtils.isValidPmo(pmo)) {

			risposta.setSuccesso(true);

			risposta.setCodice(globaleOk.getCodice());
			risposta.setMessaggio(globaleOk.getMessaggio());
			risposta.setTipoMessaggio(globaleOk.getTipoMessaggio());
			risposta.setSorgente(globaleOk.getSorgente());

		}

		/*
		 * Caso Esito KO
		 */
		else {

			risposta.setSuccesso(false);

			risposta.setCodice(globaleKo.getCodice());
			risposta.setMessaggio(globaleKo.getMessaggio());
			risposta.setTipoMessaggio(globaleKo.getTipoMessaggio());
			risposta.setSorgente(globaleKo.getSorgente());

		}

		/*
		 * Mapping dei dati Comuni (sia in OK che KO)
		 */
		// id e listaBusinessSignal
		RispostaInserimentoSingolo modelInsSingolo = new RispostaInserimentoSingolo();
		modelInsSingolo.setId(pmo.getId());
		modelInsSingolo.setListaBusinessSignal(pmo.getListaBusinessSignal());
		risposta.setDati(modelInsSingolo);

		return risposta;
	}

	public static BaseRispostaJson creaRispostaJsonPagamento(PagamentoPmo pmo,
			SignalDescriptor globaleKo, SignalDescriptor globaleOk) {

		BaseRispostaJson risposta = new BaseRispostaJson();

		/*
		 * Caso Esito OK
		 */
		if (MessaggiBusinessSignalUtils.isValidPmo(pmo)) {

			risposta.setSuccesso(true);

			risposta.setCodice(globaleOk.getCodice());
			risposta.setMessaggio(globaleOk.getMessaggio());
			risposta.setTipoMessaggio(globaleOk.getTipoMessaggio());
			risposta.setSorgente(globaleOk.getSorgente());

		}

		/*
		 * Caso Esito KO
		 */
		else {

			risposta.setSuccesso(false);

			risposta.setCodice(globaleKo.getCodice());
			risposta.setMessaggio(globaleKo.getMessaggio());
			risposta.setTipoMessaggio(globaleKo.getTipoMessaggio());
			risposta.setSorgente(globaleKo.getSorgente());

		}

		/*
		 * Mapping dei dati Comuni (sia in OK che KO)
		 */
		// id e listaBusinessSignal
		RispostaPagamento rispJsonPagamento = new RispostaPagamento();
		rispJsonPagamento.setId(pmo.getId());
		rispJsonPagamento.setPagamentoID(pmo.getPagamentoID());
		rispJsonPagamento.setPayerID(pmo.getPayerID());
		rispJsonPagamento.setRedirectURL(pmo.getRedirectURL());
		rispJsonPagamento.setTitoloAcquisto(pmo.getTitoloAcquisto());
		rispJsonPagamento.setDataPagamento(pmo.getDataPagamento());
		rispJsonPagamento.setImportoTotale(pmo.getImportoTotale());
		rispJsonPagamento.setListaBusinessSignal(pmo.getListaBusinessSignal());

		rispJsonPagamento.setIdGestore(pmo.getIdGestore());
		rispJsonPagamento.setAccessToken(pmo.getAccessToken());
		rispJsonPagamento.setStato(pmo.getStato());

		risposta.setDati(rispJsonPagamento);

		return risposta;
	}

	/**
	 * Dato un {@link ErroreSistema} crea una {@link BaseRispostaJson}.
	 * 
	 * @param eccezione
	 *            di sistema da mappare su BaseRispostaJson
	 * 
	 * @return BaseRispostaJson
	 */
	public static BaseRispostaJson creaRispostaJsonSystemException(
			ErroreSistema eccezione) {

		BaseRispostaJson risposta = new BaseRispostaJson();

		SignalDescriptor signalSistema = eccezione.getSignal();

		risposta.setSuccesso(false);
		risposta.setMessaggio(signalSistema.getMessaggio());
		risposta.setTipoMessaggio(signalSistema.getTipoMessaggio());
		risposta.setCodice(signalSistema.getCodice());
		risposta.setSorgente(signalSistema.getSorgente());

		// Preso da IOR - rfo-grails (classe Jsonp.groovy)
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		eccezione.printStackTrace(pw);
		// <-//
		risposta.setDati(sw.toString());

		return risposta;
	}

	/**
	 * Data una eccezione generica e un {@link SignalDescriptor} crea una
	 * {@link BaseRispostaJson}
	 * 
	 * @param eccezione
	 *            da mappare su BaseRispostaJson
	 * 
	 * @return BaseRispostaJson
	 */
	public static BaseRispostaJson creaRispostaJsonEccezioni(
			Throwable eccezione, SignalDescriptor signalSistema) {

		BaseRispostaJson risposta = new BaseRispostaJson();

		risposta.setSuccesso(false);
		risposta.setMessaggio(signalSistema.getMessaggio());
		risposta.setTipoMessaggio(signalSistema.getTipoMessaggio());
		risposta.setCodice(signalSistema.getCodice());
		risposta.setSorgente(signalSistema.getSorgente());

		// Preso da IOR - rfo-grails (classe Jsonp.groovy)
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		eccezione.printStackTrace(pw);
		// <-//
		risposta.setDati(sw.toString());

		return risposta;
	}

	/**
	 * From JSON TO {@link BaseRispostaJson}
	 *
	 * REF: http://www.baeldung.com/jackson-collection-array
	 * 
	 * Data la versione json di {@link BaseRispostaJson} e la classe specifica
	 * del campo {@link BaseRispostaJson#getDati()} ritorna una istanza di
	 * {@link BaseRispostaJson}
	 * 
	 *
	 * @param <?> classe campo "dati"
	 * 
	 * 
	 * @param baseRispostaJson
	 *            Stringa json
	 * 
	 * @return BaseRispostaJson
	 * @throws IOException
	 */
	public static BaseRispostaJson creaRispostaDaStringaJson(
			String baseRispostaJson, TypeReference<?> classeCampoDati)
			throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		mapper.registerModule(new JodaModule());
		mapper.setDateFormat(new SimpleDateFormat(
				DateUtils.DATE_TIME_OFFSET_ISO_8601_FORMAT));

		BaseRispostaJson risposta = mapper.readValue(baseRispostaJson,
				BaseRispostaJson.class);

		JsonNode rootNode = mapper.readTree(baseRispostaJson);
		String rispostaCampoDatiJson = rootNode.get("dati").toString();
		Object rispostaCampoDatiObj = mapper.readValue(rispostaCampoDatiJson,
				classeCampoDati);
		risposta.setDati(rispostaCampoDatiObj);

		return risposta;
	}

	public static BaseRispostaJson i18nTranslator(
			BaseRispostaJson baseRisposta, Locale locale,
			MessageSource messageSource) {

		String message = messageSource.getMessage(baseRisposta.getMessaggio(),
				new Object[] {}, "Ops! We forget to translate this message!",
				locale);

		baseRisposta.setMessaggio(message);

		if (baseRisposta.getDati() instanceof BaseRispostaDati) {
			BaseRispostaDati brd = (BaseRispostaDati) baseRisposta.getDati();
			translatorMessage(brd, locale, messageSource);
		} else {
			// TODO gestire errore di sistema per i18n
		}

		return baseRisposta;
	}

	private static BaseRispostaDati translatorMessage(BaseRispostaDati brd,
			Locale locale, MessageSource messageSource) {
		for (BusinessSignal bs : brd.getListaBusinessSignal()) {
			String message = messageSource.getMessage(bs.getMessaggio(),
					new Object[] {}, ERROR_TRANSLATION_STRING, locale);
			bs.setMessaggio(message);
		}
		return brd;
	}
}

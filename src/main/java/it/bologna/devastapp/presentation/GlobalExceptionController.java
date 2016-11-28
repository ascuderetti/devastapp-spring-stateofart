package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.log.LoggingAspect;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.util.JsonResponseUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Classe che gestisce tutte le eccezioni dell'applicazione (non gestite
 * altrove)
 */
@ControllerAdvice
public class GlobalExceptionController {

	private static final Logger LOG = LoggerFactory
			.getLogger(GlobalExceptionController.class);

	public static final String URI = " - URI: ";

	/**
	 * Gestione delle {@link ErroreSistema}
	 */
	@ExceptionHandler(ErroreSistema.class)
	public @ResponseBody
	BaseRispostaJson gestioneSystemException(HttpServletRequest req,
			ErroreSistema ex) {

		LOG.info(LoggingAspect.LOG_KEY_INIZIO + "gestioneSystemException");

		// Loggo l'errore ->
		String uri = req.getRequestURI();
		String queryString = StringUtils.isEmpty(req.getQueryString()) ? ""
				: "?" + req.getQueryString();

		LOG.error(ex.getLocalizedMessage() + URI + uri + queryString, ex);
		// <-//

		BaseRispostaJson risp = JsonResponseUtil
				.creaRispostaJsonSystemException(ex);

		LOG.info(LoggingAspect.LOG_KEY_FINE + "gestioneSystemException");

		return risp;

	}

	/**
	 * Gestione delle {@link DataAccessException}, eccezione padre di tutte le
	 * eccezioni su DB
	 */
	@ExceptionHandler(DataAccessException.class)
	public @ResponseBody
	BaseRispostaJson gestioneDataAccessException(HttpServletRequest req,
			DataAccessException ex) {

		LOG.info(LoggingAspect.LOG_KEY_INIZIO + "gestioneDataAccessException");

		// Loggo l'errore ->
		String uri = req.getRequestURI();
		String queryString = StringUtils.isEmpty(req.getQueryString()) ? ""
				: "?" + req.getQueryString();

		LOG.error(ex.getLocalizedMessage() + URI + uri + queryString, ex);
		// <-//

		BaseRispostaJson risp = JsonResponseUtil.creaRispostaJsonEccezioni(ex,
				SignalDescriptor.DATABASE_ES);

		LOG.info(LoggingAspect.LOG_KEY_FINE + "gestioneDataAccessException");

		return risp;

	}

	/**
	 * Gestione delle {@link Exception}
	 */
	@ExceptionHandler(Exception.class)
	public @ResponseBody
	BaseRispostaJson gestioneException(HttpServletRequest req, Exception ex) {

		LOG.info(LoggingAspect.LOG_KEY_INIZIO + "gestioneException");

		// Loggo l'errore ->
		String uri = req.getRequestURI();
		String queryString = StringUtils.isEmpty(req.getQueryString()) ? ""
				: "?" + req.getQueryString();

		LOG.error(ex.getLocalizedMessage() + URI + uri + queryString, ex);
		// <-//

		BaseRispostaJson risp = JsonResponseUtil.creaRispostaJsonEccezioni(ex,
				SignalDescriptor.GENERICO_ES);

		LOG.info(LoggingAspect.LOG_KEY_FINE + "gestioneException");

		return risp;

	}
}

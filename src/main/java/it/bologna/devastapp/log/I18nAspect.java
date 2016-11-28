package it.bologna.devastapp.log;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Gestione del Logging applicativo mediante Aspect.
 * 
 * Riferimenti:<br>
 * 
 * <ul>
 * <li>Spring in Action 3rd Edition - capitolo 4</li>
 * <li>Spring Reference:
 * http://docs.spring.io/spring/docs/3.2.x/spring-framework
 * -reference/html/aop.html</li>
 * <li>Sintassi pointcut:
 * http://blog.espenberntsen.net/2010/03/20/aspectj-cheat-sheet/</li>
 * </ul>
 * 
 * ESEMPIO: "execution(* "it.bologna.devastapp.business.validator.*.*(..))") =>
 * definisce tutti i pointcut su qualsiasi metodo di qualsiasi classe che si
 * trova in qualsiasi sottopackage di
 * it.bologna.devastapp.business.validatorr..." e che ha qualsiasi tipo di
 * ritorno.
 * 
 * NOTA 1: si puo' filtrare anche per Bean> && bean(idOrNameOfBean).<br>
 * NOTA 2: non si possono mettere dei pointcut su metodi statici<br>
 * NOTA 3: per dire di applicare il pointcut a tutte le implementazioni di un
 * interfaccia si usa "+", vale anche per i metodi su classe abstract. vedi
 * {@link this#componentRepository()}<br>
 * NOTA 4: e' possibile intercettare anche il valore di ritorno di un metodo con
 * =>@AfterReturning(pointcut = "execution(..omesso..)", returning = "retVal")<br>
 * NOTA 5: e' possibile intercettare le eccezioni con =>@AfterThrowing(pointcut
 * = "execution(..omesso..)", throwing = "error")<br>
 * 
 * @author ascuderetti@gmail.com
 * 
 */
@Component
@Aspect
public class I18nAspect {

	Logger LOG_ASPECT = LoggerFactory.getLogger(I18nAspect.class);

	// package root dell'applicazione
	public static final String PACKAGE_ROOT = "it.bologna.devastapp.";

	@Autowired
	MessageSource messageSource;

	/*
	 * DEFINIZIONE POINTCUT
	 */

	// POINTCUT - Controller - Tutte le classi con @Controller
	@Pointcut("@within(org.springframework.stereotype.Controller)")
	public void componentController() {
	}

	/*
	 * DEFINIZIONE ADVICE
	 */

	// TODO: commentato in attesa di definizione su come gestire
	// l'internazionalizzazione
	// ADVICE - logInizioInfo
	// @Around("componentController()")
	public void gestioneI18n(JoinPoint joinPoint) {

		try {

			// Recupero la Signature intercettata dall'aspect
			Signature signature = joinPoint.getSignature();

			// Creo il Log
			Logger LOG = LoggerFactory.getLogger(signature
					.getDeclaringTypeName());

			// Internazionalizzo..
			Object[] objArray = joinPoint.getArgs();
			HttpServletRequest sr = (HttpServletRequest) objArray[0];

			// TODO Testare l'utility i18nTranslator sotto aspect
			// JsonResponseUtil.i18nTranslator(risposta, sr.getLocale(),
			// messageSource);

		} catch (Exception e) {

			LOG_ASPECT.error("aspect in errore" + e);
		}
	}
}

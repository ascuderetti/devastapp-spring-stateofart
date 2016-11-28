package it.bologna.devastapp.log;

import it.bologna.devastapp.util.DateUtils;

import java.text.SimpleDateFormat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

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
public class LoggingAspect {

	Logger LOG_ASPECT = LoggerFactory.getLogger(LoggingAspect.class);

	// Chiavi per le varie Politiche di Logging ->
	public static String LOG_KEY_INIZIO = "INIZIO - ";
	public static String LOG_KEY_INPUT = "INPUT - ";
	public static String LOG_KEY_FINE = "FINE - ";
	public static String LOG_KEY_SIGNAL = "AGGIUNGI SIGNAL - ";
	// <-//

	// Errore in caso di eccezione durante la fase di logging
	public static final String LOGGING_ASPECT_ERRORE_GENERICO = "LOGGING - ERRORE GENERICO: ";

	// package root dell'applicazione
	public static final String PACKAGE_ROOT = "it.bologna.devastapp.";

	// ObjectMapper - Utilizzato per loggare oggetti (serializzazione)
	public static final ObjectMapper objectMapperPerLog = new ObjectMapper();

	// configurazioni objectMapper
	static {

		objectMapperPerLog.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapperPerLog.registerModule(new JodaModule());
		objectMapperPerLog.setDateFormat(new SimpleDateFormat(
				DateUtils.DATE_TIME_OFFSET_ISO_8601_FORMAT));
	}

	/*
	 * DEFINIZIONE POINTCUT
	 */

	// POINTCUT - Controller - Tutte le classi con @Controller
	@Pointcut("@within(org.springframework.stereotype.Controller)")
	public void componentController() {
	}

	// POINTCUT - Service - Tutte le classi con @Service
	@Pointcut("@within(org.springframework.stereotype.Service)")
	public void componentService() {
	}

	// POINTCUT - Validator - Tutte le classi sotto il package
	// ...business.validator.*
	@Pointcut("execution(* " + PACKAGE_ROOT + "business.validator.*.*(..))")
	public void packageValidator() {
	}

	// POINTCUT - Mapper - Tutte le classi sotto il package ...business.mapper.*
	@Pointcut("execution(* " + PACKAGE_ROOT + "business.mapper.*.*(..))")
	public void packageMapper() {
	}

	// POINTCUT - JpaRepository interface - Tutte le classi sotto il
	// package org.springframework.data.jpa.repository.JpaRepository (solo
	// metodi public) il "+" dice che vale per tutte le implementazioni
	// dell'interfaccia JpaRepository
	@Pointcut("execution(public * org.springframework.data.jpa.repository.JpaRepository+.*(..))")
	public void componentRepository() {
	}

	// POINTCUT - Sicurezza - Tutte le classi sotto il package .security
	@Pointcut("execution(* " + PACKAGE_ROOT + "security.*.*(..))")
	public void componentSecurity() {
	}

	/*
	 * DEFINIZIONE ADVICE
	 */

	// ADVICE - logInizioInfo
	@Before("componentController() || packageValidator() || packageMapper() || componentRepository() || componentSecurity()")
	public void logInizioInfo(JoinPoint joinPoint) {

		try {

			// Recupero la Signature intercettata dall'aspect
			Signature signature = joinPoint.getSignature();

			// Creo il Log
			Logger LOG = LoggerFactory.getLogger(signature
					.getDeclaringTypeName());
			// Loggo..
			LOG.info(LOG_KEY_INIZIO + signature.getName());

		} catch (Exception e) {

			LOG_ASPECT.error(LOGGING_ASPECT_ERRORE_GENERICO + e);
		}
	}

	// ADVICE - logInizioInfoAndInputArgsDebug
	@Before("componentService()")
	public void logInizioInfoAndInputArgsDebug(JoinPoint joinPoint) {

		try {
			// Recupero la Signature intercettata dall'aspect
			Signature signature = joinPoint.getSignature();

			// Creo il Log
			Logger LOG = LoggerFactory.getLogger(signature
					.getDeclaringTypeName());

			// Loggo..
			LOG.info(LOG_KEY_INIZIO + signature.getName());

			// Se debug e' attivo...
			if (LOG.isDebugEnabled()) {

				// ..loggo anche gli argomenti in Input
				LOG.debug(LOG_KEY_INPUT
						+ objectMapperPerLog.writeValueAsString(joinPoint
								.getArgs()));
			}

		} catch (JsonProcessingException e) {

			LOG_ASPECT.error("LOGGING - JsonProcessingException: " + e);

		} catch (Exception e) {

			LOG_ASPECT.error(LOGGING_ASPECT_ERRORE_GENERICO + e);
		}
	}

	// ADVICE - logFineInfo
	@AfterReturning("componentController() || componentService() || packageValidator() || packageMapper() || componentRepository() || componentSecurity()")
	public void logFineInfo(JoinPoint joinPoint) {

		try {
			// Recupero la Signature intercettata dall'aspect
			Signature signature = joinPoint.getSignature();

			// Creo il Log
			Logger LOG = LoggerFactory.getLogger(signature
					.getDeclaringTypeName());

			// Loggo..
			LOG.info(LOG_KEY_FINE + signature.getName());

		} catch (Exception e) {

			LOG_ASPECT.error(LOGGING_ASPECT_ERRORE_GENERICO + e);
		}

	}

}

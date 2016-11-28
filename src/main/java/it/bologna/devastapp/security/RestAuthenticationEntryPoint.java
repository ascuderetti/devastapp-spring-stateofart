package it.bologna.devastapp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 
 * @author ascuderetti@gmail.com
 * 
 */
@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/**
	 * Se il client accede ad una risorsa senza le autorizzazioni necessari (o
	 * senza aver eseguito l'autenticazione), spring security richiama questo
	 * metodo. Tipicamente in una applicazione NON rest, con jsp corrisponde al
	 * forward alla pagina di login, in questo caso, essendo l'applciazione REST
	 * è semplicemente una risposta HTTP che notifica le mancanza di
	 * autorizzazioni.
	 * 
	 * @see spring-security.xml
	 */
	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		/*
		 * TODO: mettere JSON
		 */
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"Non hai le autorizzazioni necessarie per poter accedere a questa risorsa");

	}

}

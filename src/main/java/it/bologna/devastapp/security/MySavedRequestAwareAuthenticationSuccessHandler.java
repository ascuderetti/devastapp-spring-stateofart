package it.bologna.devastapp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/**
 * Riferimento: Building-REST-Services-with-Spring.pdf - pag.9
 * 
 * Il metodo "onAuthenticationSuccess" viene sovrascritto mantenendo la stessa
 * implementazione originale della classe padre a meno del redirect, il redirect
 * serve in un applicazione NON rest (in cui il presentation è server-side e si
 * fa il redirect alla homepage post login OK)
 * 
 * @author ascuderetti@gmail.com
 * 
 */
public class MySavedRequestAwareAuthenticationSuccessHandler extends
		SimpleUrlAuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			clearAuthenticationAttributes(request);
			return;
		}// else

		String targetUrlParam = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParam != null && StringUtils
						.hasText(targetUrlParam))) {
			requestCache.removeRequest(request, response);
			clearAuthenticationAttributes(request);
			return;
		}// else
		clearAuthenticationAttributes(request);

		/*
		 * TODO: ritornare JSON
		 * response.setContentType("application/json;charset=UTF-8");
		 * response.setHeader("Cache-Control", "no-cache");
		 * response.getWriter().write(String.format(JSON_VALUE, key, message));
		 */
		// response.getWriter().write("/{prova: ciao/}");

	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
}

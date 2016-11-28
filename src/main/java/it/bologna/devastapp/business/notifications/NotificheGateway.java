package it.bologna.devastapp.business.notifications;

import it.bologna.devastapp.persistence.entity.Offerta;

import java.util.List;

import org.springframework.integration.annotation.Gateway;
import org.springframework.stereotype.Service;

/**
 * Interfaccia per i Servizi di Invio Notifiche.
 * 
 * @author ascuderetti
 * 
 */
@Service("notificheGateway")
public interface NotificheGateway {

	/**
	 * NOTIFICHE A FOLLOW OFFERTA
	 * 
	 * Data una offerta checkata, questo metodo invia una notifica a tutti i
	 * follow dell'offerta.
	 * 
	 * @param offertaChecked
	 */
	// PubSubChannel
	@Gateway(requestChannel = "notificaCheckToFollowOfferta")
	public void notificaCheckToFollowOfferta(Offerta offertaChecked);

	/**
	 * NOTIFICHE A FOLLOW LOCALE
	 * 
	 * Questo metodo invia una notifica a tutti i Follow dei Locali associati
	 * alle offerte "listaOfferteOnline" in INPUT al metodo.
	 * 
	 * @param listaOfferteOnline
	 */
	@Gateway(requestChannel = "notificaOffertaFollowLocale")
	public void notificaOffertaOnlineToFollowLocale(
			List<Offerta> listaOfferteOnline);

	/*
	 * ...metodo per invio mail, ecc..
	 */
}

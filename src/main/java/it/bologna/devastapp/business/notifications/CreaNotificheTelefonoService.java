package it.bologna.devastapp.business.notifications;

import it.bologna.devastapp.business.notifications.model.NotificaTelefono;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Stato;

import java.util.List;

public interface CreaNotificheTelefonoService {

	/**
	 * Data una Offerta {@link Stato#CHECK} recupera gli utenti in
	 * {@link Stato#FOLLOW} e restituisce la lista di notifiche da inviare ai
	 * device
	 * 
	 * @param offertaChecked
	 * @return lista Notifiche da inviare agli Utenti in Follow di
	 *         offertaChecked
	 */
	public List<NotificaTelefono> creaNotificheCheckToFollow(
			Offerta offertaChecked);

	/**
	 * Crea Notifiche per i follow locali
	 * 
	 * 1. Recupera la lista gli utenti follow dei locali
	 * 
	 * 2. Crea e ritorna le notifiche
	 * 
	 * @param listaOfferteOnline
	 * @return Notifiche da inviare agli Utenti in Follow agli locali della
	 *         listaOfferteOnline
	 */
	public List<NotificaTelefono> creaNotificheToFollowLocale(
			List<Offerta> listaOfferteOnline);

}

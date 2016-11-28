package it.bologna.devastapp.business.notifications;

import it.bologna.devastapp.business.notifications.model.NotificaTelefono;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.MovimentiLocaleRepository;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.UtenteRepository;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.MovimentiLocale;
import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(CreaNotificheTelefonoServiceImpl.KEY_SPRING_CREA_NOTIFICHE_TELEFONO_SERVICE)
public class CreaNotificheTelefonoServiceImpl implements
		CreaNotificheTelefonoService {

	/**
	 * Chiave del Bean
	 */
	public final static String KEY_SPRING_CREA_NOTIFICHE_TELEFONO_SERVICE = "creaNotificheTelefonoService";

	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;

	@Autowired
	MovimentiLocaleRepository movimentiLocaleRepository;

	@Autowired
	UtenteRepository utenteRepository;

	@Transactional(readOnly = true)
	@Override
	public List<NotificaTelefono> creaNotificheCheckToFollow(
			Offerta offertaChecked) {

		/*
		 * Recupero Utenti in Follow
		 */

		List<Long> listaIdUtentiFollow = new ArrayList<Long>();

		// recupero i movimenti dell'offerta in stato follow...
		List<MovimentiOfferta> listaMovimentiOffertaFollow = movimentiOffertaRepository
				.findByIdOffertaAndStato(offertaChecked.getId(), Stato.FOLLOW);

		// ...e recupero gli IdUtente
		for (MovimentiOfferta mov : listaMovimentiOffertaFollow) {

			listaIdUtentiFollow.add(mov.getIdUtente());
		}

		// recupero gli utenti
		List<Utente> listaUtentiFollow = utenteRepository
				.findAll(listaIdUtentiFollow);

		List<NotificaTelefono> listaNotifiche = new ArrayList<NotificaTelefono>();

		for (Utente utente : listaUtentiFollow) {

			NotificaTelefono notifica = new NotificaTelefono(
					utente.getTipoTelefono(), utente.getIdTelefono(),
					SignalDescriptor.NOTIFICA_FOLLOW_OFFERTA_CHECKATA_INFO);

			listaNotifiche.add(notifica);

		}

		return listaNotifiche;

	}

	@Transactional(readOnly = true)
	@Override
	public List<NotificaTelefono> creaNotificheToFollowLocale(
			List<Offerta> listaOfferteOnline) {

		List<Locale> listaLocaliConOfferteOnline = new ArrayList<Locale>();

		// recupero la lista dei locali
		for (Offerta off : listaOfferteOnline) {
			listaLocaliConOfferteOnline.add(off.getLocale());
		}

		// recupero i follow dei locali
		List<MovimentiLocale> listaFollow = movimentiLocaleRepository
				.findByIdLocaleIsIn(CollectionUtils
						.extractEntityIDs(listaLocaliConOfferteOnline));

		// recupero gli utenti
		List<Long> listaIdUtentiFollow = new ArrayList<Long>();
		for (MovimentiLocale movLoc : listaFollow) {
			listaIdUtentiFollow.add(movLoc.getIdUtente());
		}
		List<Utente> listaUtente = utenteRepository
				.findAll(listaIdUtentiFollow);

		// creo le notifiche
		List<NotificaTelefono> listaNotifiche = new ArrayList<NotificaTelefono>();

		for (Utente utente : listaUtente) {

			NotificaTelefono notifica = new NotificaTelefono(
					utente.getTipoTelefono(), utente.getIdTelefono(),
					SignalDescriptor.NOTIFICA_FOLLOW_LOCALE_OFF_ONLINE_INFO);

			listaNotifiche.add(notifica);
		}

		return listaNotifiche;
	}
}

package it.bologna.devastapp.business;

import it.bologna.devastapp.business.mapper.MovimentiLocaleMapper;
import it.bologna.devastapp.business.notifications.NotificheGateway;
import it.bologna.devastapp.business.validator.MovimentiLocaleValidator;
import it.bologna.devastapp.persistence.MovimentiLocaleRepository;
import it.bologna.devastapp.persistence.entity.MovimentiLocale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class MovimentiLocaleServiceImpl implements MovimentiLocaleService {

	@Autowired
	MovimentiLocaleValidator movimentiLocaleValidator;
	@Autowired
	MovimentiLocaleRepository movimentiLocaleRepository;
	@Autowired
	MovimentiLocaleMapper movimentiLocaleMapper;
	@Autowired
	OffertaService offertaService;
	@Autowired
	NotificheGateway notificheGateway;

	@Override
	@Transactional(readOnly = false)
	public MovimentoUtentePmo followLocale(MovimentoUtentePmo followLocale) {

		// ********************************//
		// ** 1 - VALIDAZIONE ************//
		// ********************************//

		/*
		 * 1.1 Errori Sistema PMO in Input
		 */
		movimentiLocaleValidator.validaErroriSistemaPmo(followLocale);

		/*
		 * 1.2 Errori Business
		 */
		movimentiLocaleValidator.validaFollowLocale(followLocale);

		if (!MessaggiBusinessSignalUtils.isValidPmo(followLocale)) {
			return followLocale;
		}

		/*
		 * 2) chiamo il mapper
		 */
		MovimentiLocale movLocale = movimentiLocaleMapper.pmoToEntity(
				followLocale, Stato.FOLLOW);

		/*
		 * 3) Scrittura su repository del movimento offerta salva anche il
		 * corrispettivo utente se non esiste
		 */
		movLocale = movimentiLocaleRepository.save(movLocale);

		/*
		 * Mapping da Entitu a PMO
		 */
		followLocale = movimentiLocaleMapper.entityToPmo(movLocale);

		return followLocale;
	}

	@Override
	// @Scheduled(fixedDelay = 1000L)
	public void notificaFollowLocale() {

		/*
		 * Recupero la listaOfferteOnline
		 */
		List<Offerta> listaOfferteOnline = offertaService
				.getListaOfferteOnline();

		if (!CollectionUtils.isEmpty(listaOfferteOnline)) {
			/*
			 * Chiamo il metodo di notifica ai follow locale
			 */
			notificheGateway
					.notificaOffertaOnlineToFollowLocale(listaOfferteOnline);
		}

	}

}

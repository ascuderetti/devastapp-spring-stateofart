package it.bologna.devastapp.business.validator;

import it.bologna.devastapp.business.signal.ErroreSistema;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.presentation.pmo.BasePmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Hours;
import org.springframework.stereotype.Component;

/*
 *  OffertaPmo
 *  
 *  String titolo;
 *	String descrizione;
 *	ProdottoPmo prodottoPmo;
 *	Date dataInizio;
 *	Date dataFine;
 *	int quantita;
 *	BigDecimal prezzo;
 *	LocalePmo localePmo;
 *
 */

@Component
public class OffertaValidatorImpl implements OffertaValidator {

	@Override
	public void validaInserisciOfferta(OffertaScritturaPmo offertaPmo) {

		validaCampiOfferta(offertaPmo, offertaPmo);

		// controllo che il PMO non abbia l'id

		if (offertaPmo.getId() != null) {
			MessaggiBusinessSignalUtils.aggiungiSignal(offertaPmo,
					SignalDescriptor.OFFERTA_CREAZIONE_OFFERTA_CON_ID);
		}

	}

	private void validaCampiOfferta(OffertaScritturaPmo offertaPmo,
			BasePmo pmoPerSignal) {

		// Controlli sulle date

		if (offertaPmo.getDataInizio() != null
				&& offertaPmo.getDataFine() != null) {
			if (!offertaPmo.getDataFine().isEqual(
					offertaPmo.getDataInizio().plus(Hours.ONE))) {
				MessaggiBusinessSignalUtils
						.aggiungiSignal(
								pmoPerSignal,
								SignalDescriptor.OFFERTA_DATA_FINE_MAGGIORATA_DI_UN_ORA);
			}
			// if (offertaPmo.getDataInizio().getMinuteOfHour() % 15 != 0) {
			// MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
			// SignalDescriptor.OFFERTA_DATA_INIZIO_QUARTO_ORA);
			// }
			// if (offertaPmo.getDataInizio().getSecondOfMinute() != 0
			// || offertaPmo.getDataInizio().getMillisOfSecond() != 0) {
			// MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
			// SignalDescriptor.OFFERTA_SECONDI_MILLISECONDI_ZERO);
			// }

			if (offertaPmo.getDataFine().isBefore(offertaPmo.getDataInizio())) {
				MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
						SignalDescriptor.OFFERTA_DATA_FINE_MAGGIORE_DATA_FINE);
			}
		}

		// Controlli sulla quantita'

		// Campi obbligatori

		if (offertaPmo.getDataFine() == null) {

			MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
					SignalDescriptor.OFFERTA_E001);

		}

		if (offertaPmo.getDataInizio() == null) {

			MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
					SignalDescriptor.OFFERTA_E002);

		}

		if (StringUtils.isEmpty(offertaPmo.getDescrizione())) {

			MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
					SignalDescriptor.OFFERTA_E003);

		}

		if (offertaPmo.getIdLocale() == null) {

			MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
					SignalDescriptor.OFFERTA_E004);

		}

		if (offertaPmo.getPrezzoUnitarioPieno() == null
				|| offertaPmo.getPrezzoUnitarioPieno().signum() == -1) {

			MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
					SignalDescriptor.OFFERTA_CAMPO_PREZZO_OBBLIGATORIO);

		}

		if (offertaPmo.getPrezzoUnitarioScontato() == null
				|| offertaPmo.getPrezzoUnitarioScontato().signum() == -1) {

			MessaggiBusinessSignalUtils.aggiungiSignal(offertaPmo,
					SignalDescriptor.OFFERTA_CAMPO_PREZZO_OBBLIGATORIO);

		}

		if (offertaPmo.getIdProdotto() == null) {

			MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
					SignalDescriptor.OFFERTA_CAMPO_PRODOTTO_OBBLIGATORIO);

		}

		if (offertaPmo.getQuantita() == null) {

			MessaggiBusinessSignalUtils.aggiungiSignal(pmoPerSignal,
					SignalDescriptor.OFFERTA_E007);

		} else if (offertaPmo.getQuantita() <= 0) {

			MessaggiBusinessSignalUtils.aggiungiSignal(offertaPmo,
					SignalDescriptor.OFFERTA_QUANTITA_MINOREUGUALEAZERO);

		}

		if (StringUtils.isEmpty(offertaPmo.getTitolo())) {

			MessaggiBusinessSignalUtils.aggiungiSignal(offertaPmo,
					SignalDescriptor.OFFERTA_E008);

		}

	}

	@Override
	public void validaAggiornaOfferta(Offerta offertaDaAggiornare,
			OffertaScritturaPmo offertaPmo, BasePmo pmoPerSignal) {

		if (offertaPmo.getId() == null) {
			throw new ErroreSistema(
					SignalDescriptor.OFFERTA_MODIFICA_OFFERTA_SENZA_ID);
		}

		validaStatoOffertaDaAggiornare(offertaDaAggiornare);

		validaCampiOfferta(offertaPmo, pmoPerSignal);

	}

	@Override
	public void validaStatoOffertaDaAggiornare(Offerta offertaDaAggiornare) {
		if (StatoOfferta.PUBBLICATA.equals(offertaDaAggiornare
				.getStatoOfferta())) {
			throw new ErroreSistema(
					SignalDescriptor.OFFERTA_AGGIORNAMENTO_OFFERTA_IN_STATO_PUBBLICATA);
		}
	}

	@Override
	public List<OffertaScritturaPmo> validaRestituisciListaOfferte(
			PosizionePmo posizionePmo) {

		if (posizionePmo.getLatitudine() == null
				|| posizionePmo.getLongitudine() == null) {
			throw new ErroreSistema(
					SignalDescriptor.OFFERTA_COORDINATE_NON_VALORIZZATE);
		}

		return Collections.<OffertaScritturaPmo> emptyList();

	}
}

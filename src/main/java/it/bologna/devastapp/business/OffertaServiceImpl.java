package it.bologna.devastapp.business;

import it.bologna.devastapp.business.mapper.OffertaMapper;
import it.bologna.devastapp.business.mapper.PosizioneMapper;
import it.bologna.devastapp.business.validator.OffertaValidator;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.persistence.entity.StatoOfferta;
import it.bologna.devastapp.presentation.pmo.OffertaLetturaPmo;
import it.bologna.devastapp.presentation.pmo.OffertaLetturaResponse;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OffertaServiceImpl implements OffertaService {

	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;
	@Autowired
	OffertaMapper offertaMapper;
	@Autowired
	PosizioneMapper posizioneMapper;
	@Autowired
	OffertaValidator offertaValidator;

	@Override
	@Transactional(readOnly = false)
	public OffertaScritturaPmo createOfferta(OffertaScritturaPmo offertaPmo) {

		/*
		 * 1) Validazione Pmo
		 */
		offertaValidator.validaInserisciOfferta(offertaPmo);

		/*
		 * 2) Se valido chiamo mapper altrimenti return
		 */

		if (!MessaggiBusinessSignalUtils.isValidPmo(offertaPmo)) {
			return offertaPmo;
		}

		Offerta offerta = offertaMapper.scritturaPmoToEntity(offertaPmo);

		/*
		 * 3) Scrittura su repository
		 */

		// FIXME: messo per la demo
		offerta.setStatoOfferta(StatoOfferta.PUBBLICATA);
		// offerta.setDataInizio(offerta.getDataInizio());
		// offerta.setDataFine(offerta.getDataFine().plus(Hours.TWO));
		offerta = offertaRepository.save(offerta);

		/*
		 * Mapping da Entitu a PMO
		 */

		offertaPmo = offertaMapper.entityToScritturaPmo(offerta);

		return offertaPmo;
	}

	@Override
	@Transactional(readOnly = false)
	public OffertaScritturaPmo updateOfferta(OffertaScritturaPmo offertaPmo) {

		/*
		 * 1) Validazione Pmo
		 */

		Offerta offertaDaAggiornare = offertaRepository.findOne(offertaPmo
				.getId());

		offertaValidator.validaAggiornaOfferta(offertaDaAggiornare, offertaPmo,
				offertaPmo);

		/*
		 * 2) Se valido chiamo mapper altrimenti return
		 */

		if (!MessaggiBusinessSignalUtils.isValidPmo(offertaPmo)) {
			return offertaPmo;
		}

		Offerta offerta = offertaMapper.scritturaPmoToEntity(offertaPmo);

		/*
		 * 3) Scrittura su repository
		 */

		offerta = offertaRepository.save(offerta);

		/*
		 * Mapping da Entita' a PMO
		 */

		offertaPmo = offertaMapper.entityToScritturaPmo(offerta);

		return offertaPmo;
	}

	@Override
	@Transactional
	public OffertaLetturaResponse getListaOfferteOnlineByRaggioAndPosizione(
			PosizionePmo posizionePmo) {

		OffertaLetturaResponse letturaOffertaResponse = new OffertaLetturaResponse();

		letturaOffertaResponse.setPosizionePmo(posizionePmo);

		/*
		 * 1) Validazione Pmo
		 */

		offertaValidator.validaRestituisciListaOfferte(posizionePmo);

		if (!MessaggiBusinessSignalUtils.isValidPmo(posizionePmo)) {
			return letturaOffertaResponse;
		}

		Posizione posizione = posizioneMapper
				.posizionePmoToPosizione(posizionePmo);

		/*
		 * Chiamo il repository
		 */

		List<Offerta> listaOfferte = offertaRepository
				.findOffertaOnlineByLocationAndDistance(posizione,
						Definitions.ParametriBusiness.RAGGIO_RICERCA_KM);

		List<OffertaLetturaPmo> listaOffertePmo = new ArrayList<OffertaLetturaPmo>(
				listaOfferte.size());

		for (Offerta offerta : listaOfferte) {

			long numCheckOfferta = movimentiOffertaRepository
					.countMovimentiOffertaByIdOffertaByStato(offerta.getId(),
							Stato.CHECK);

			Integer quantitaRimanente = (int) (offerta.getQuantita() - numCheckOfferta);

			listaOffertePmo.add(offertaMapper.entityToLetturaPmo(offerta,
					quantitaRimanente));

		}

		letturaOffertaResponse.setListLetturaOffertaPmo(listaOffertePmo);

		return letturaOffertaResponse;
	}

	@Override
	public List<Offerta> getListaOfferteOnline() {
		DateTime now = DateTime.now();

		return offertaRepository
				.findByDataInizioBeforeAndDataFineAfterAndStatoOfferta(now,
						now, StatoOfferta.PUBBLICATA);
	}
}

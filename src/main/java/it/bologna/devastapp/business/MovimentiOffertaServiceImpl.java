package it.bologna.devastapp.business;

import it.bologna.devastapp.business.mapper.MovimentiOffertaMapper;
import it.bologna.devastapp.business.validator.MovimentiOffertaValidator;
import it.bologna.devastapp.persistence.MovimentiOffertaRepository;
import it.bologna.devastapp.persistence.OffertaRepository;
import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MovimentiOffertaServiceImpl implements MovimentiOffertaService {

	@Autowired
	OffertaRepository offertaRepository;
	@Autowired
	MovimentiOffertaValidator movimentiOffertaValidator;
	@Autowired
	MovimentiOffertaMapper movimentiOffertaMapper;
	@Autowired
	MovimentiOffertaRepository movimentiOffertaRepository;

	@Override
	@Transactional(readOnly = false)
	public MovimentoUtentePmo followOfferta(
			MovimentoUtentePmo checkFollowOffertaPmo) {
		/*
		 * 1) Validazione Pmo
		 */
		movimentiOffertaValidator.validaErroriSistemaPmo(checkFollowOffertaPmo);

		// preparo i dati
		Long idOfferta = checkFollowOffertaPmo.getIdOggetto();

		// recupero l'offerta
		Offerta offerta = offertaRepository.findOne(idOfferta);
		// recupero il num di check gia fatti
		long numCheck = movimentiOffertaRepository
				.countMovimentiOffertaByIdOffertaByStato(idOfferta, Stato.CHECK);

		movimentiOffertaValidator.validaFollowOfferta(checkFollowOffertaPmo,
				offerta, numCheck);

		if (!MessaggiBusinessSignalUtils.isValidPmo(checkFollowOffertaPmo)) {
			return checkFollowOffertaPmo;
		}

		/*
		 * 2) chiamo il mapper
		 */
		MovimentiOfferta movimentiOfferta = movimentiOffertaMapper.pmoToEntity(
				checkFollowOffertaPmo, Stato.FOLLOW);

		/*
		 * 3) Scrittura su repository del movimento offerta salva anche il
		 * corrispettivo utente se non esiste
		 */
		movimentiOfferta = movimentiOffertaRepository.save(movimentiOfferta);

		/*
		 * Mapping da Entitu a PMO
		 */
		checkFollowOffertaPmo = movimentiOffertaMapper
				.entityToPmo(movimentiOfferta);

		return checkFollowOffertaPmo;
	}

	@Override
	@Transactional(readOnly = false)
	public MovimentoUtentePmo checkOfferta(
			MovimentoUtentePmo checkFollowOffertaPmo) {
		/*
		 * 1) Validazione Pmo
		 */
		movimentiOffertaValidator.validaErroriSistemaPmo(checkFollowOffertaPmo);

		// preparo i dati
		Long idOfferta = checkFollowOffertaPmo.getIdOggetto();
		Long idUtente = checkFollowOffertaPmo.getIdUtente();

		// 1.1 Recupero da DB dati necessari alla validazione

		// check gia' presente (se si, errore)
		boolean checkGiaPresente = movimentiOffertaRepository
				.existMovimentiOffertaByIdOffertaAndidUtenteAndStato(idOfferta,
						idUtente, Stato.CHECK);

		// numero check gia fatti
		long numCheck = movimentiOffertaRepository
				.countMovimentiOffertaByIdOffertaByStato(idOfferta, Stato.CHECK);

		// recupero l'offerta che si vuole checkare per controllare che sia
		// attiva e quantità>0
		Offerta offerta = offertaRepository.findOne(checkFollowOffertaPmo
				.getIdOggetto());

		/*
		 * 1.2 Chiamo il Validator
		 */
		movimentiOffertaValidator.validaCheckOfferta(checkFollowOffertaPmo,
				offerta, checkGiaPresente, numCheck);

		/*
		 * Se il PMO non è valido return
		 */
		if (!MessaggiBusinessSignalUtils.isValidPmo(checkFollowOffertaPmo)) {
			return checkFollowOffertaPmo;
		}

		/*
		 * 2) Mapping da pmo a entity
		 */
		MovimentiOfferta movimentoOfferta = movimentiOffertaMapper.pmoToEntity(
				checkFollowOffertaPmo, Stato.CHECK);

		/*
		 * 3) Chiamate al Repository
		 */

		// 3.1) inserisco il movimento offerta check
		movimentoOfferta = movimentiOffertaRepository.save(movimentoOfferta);

		/*
		 * 4) Invio Notifica Async - TODO
		 */

		/*
		 * 5) Mapping da entity a pmo
		 */
		checkFollowOffertaPmo = movimentiOffertaMapper
				.entityToPmo(movimentoOfferta);

		return checkFollowOffertaPmo;
	}
}

package it.bologna.devastapp.business;

import it.bologna.devastapp.business.mapper.ProdottoMapper;
import it.bologna.devastapp.business.validator.ProdottoValidator;
import it.bologna.devastapp.persistence.ProdottoRepository;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * "@Service" => mi dice che questo e' un componente Spring di Business
 * 
 * "@Transactional"(readOnly = true)=> dichiaro che di defautl tutti i metodi
 * implementati eseguiranno all'interno di una transazione e possono solo
 * LEGGERE. <br>
 * 
 * N.B. ricordarsi nei metodi di SCRITTURA di dichiarare @Transactional (di
 * default ha readOnly=false).
 */
@Service
@Transactional(readOnly = true)
public class ProdottoServiceImpl implements ProdottoService {

	/*
	 * Definizione Componenti Utilizzati
	 */
	@Autowired
	ProdottoRepository prodottoRepository;
	@Autowired
	ProdottoValidator prodottoValidator;
	@Autowired
	ProdottoMapper prodottoMapper;

	// <-//

	@Override
	@Transactional
	public ProdottoPmo createProdotto(ProdottoPmo prodottoPmo) {

		/*
		 * 1) Validazione Pmo
		 */

		prodottoValidator.validaProdotto(prodottoPmo);

		/*
		 * 2) Se valido chiamo mapper altrimenti return
		 */

		if (!MessaggiBusinessSignalUtils.isValidPmo(prodottoPmo)) {
			return prodottoPmo;
		}

		// Mapping Pmo to Entity
		Prodotto prodotto = prodottoMapper.pmoToEntity(prodottoPmo);

		/*
		 * 3) Scrittura su repository
		 */

		prodotto = prodottoRepository.save(prodotto);

		/*
		 * 4) Mapping - Entity to Pmo
		 */

		prodottoPmo = prodottoMapper.entityToPmo(prodotto);

		return prodottoPmo;
	}

	@Override
	public List<ProdottoPmo> getProdotti() {

		List<Prodotto> listaProdottiAll = prodottoRepository.findAll();

		return prodottoMapper.entitiesToPmos(listaProdottiAll);

	}
}

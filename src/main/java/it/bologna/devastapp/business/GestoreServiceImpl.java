package it.bologna.devastapp.business;

import it.bologna.devastapp.business.mapper.GestoreMapper;
import it.bologna.devastapp.persistence.GestoreRepository;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.presentation.pmo.GestorePmo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GestoreServiceImpl implements GestoreService {

	@Autowired
	GestoreRepository gestoreRepository;
	@Autowired
	GestoreMapper gestoreMapper;

	@Override
	@Transactional
	public List<GestorePmo> getListaGestori() {

		List<GestorePmo> response = new ArrayList<GestorePmo>();

		/*
		 * Validazione Pmo => niente da validare
		 */

		/*
		 * Chiamo il repository
		 */

		List<Gestore> listaGestori = gestoreRepository.findAll();

		/*
		 * Mapper
		 */
		for (Gestore gestore : listaGestori) {
			response.add(gestoreMapper.gestoreToGestorePmo(gestore));
		}

		return response;
	}

}

package it.bologna.devastapp.persistence;

import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.persistence.entity.StatoOfferta;

import java.util.List;

public interface OffertaRepositoryCustom {

	public List<Offerta> findOffertaOnlineByLocationAndDistance(Posizione posizione,
			double distanza);

	public void updateStatoById(Long id, StatoOfferta statoOfferta);

}

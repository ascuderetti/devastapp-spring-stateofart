package it.bologna.devastapp.util;

import it.bologna.devastapp.persistence.entity.AbstractEntity;
import it.bologna.devastapp.presentation.pmo.BasePmo;

import java.util.ArrayList;
import java.util.List;

public final class CollectionUtils {

	/**
	 * Data una lista di Pmo, ritorna la lista di id
	 * 
	 * @param list
	 * @return List<Long>
	 */
	public static <Pmo extends BasePmo> List<Long> extractPmoIDs(List<Pmo> list) {
		List<Long> ids = new ArrayList<Long>();

		for (BasePmo pmo : list) {
			ids.add(pmo.getId());
		}

		return ids;
	}

	/**
	 * Data una lista di Entity, ritorna la lista di id
	 * 
	 * @param list
	 * @return List<Long>
	 */
	public static <Entity extends AbstractEntity> List<Long> extractEntityIDs(
			List<Entity> list) {
		List<Long> ids = new ArrayList<Long>();

		for (AbstractEntity pmo : list) {
			ids.add(pmo.getId());
		}

		return ids;
	}

	/**
	 * Data una lista di entity ritorna l'entity con l'id come parametro di
	 * input
	 * 
	 * @param id
	 *            , dell'entity da ritornare
	 * @param list
	 *            di entity
	 * @return entity con id
	 */
	public static <Entity extends AbstractEntity> Entity extractEntity(Long id,
			List<Entity> list) {

		for (Entity e : list) {
			if (id.equals(e.getId())) {
				return e;
			}
		}
		return null;
	}

}

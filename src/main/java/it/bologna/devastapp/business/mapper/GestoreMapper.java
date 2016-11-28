package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.Gestore;
import it.bologna.devastapp.presentation.pmo.GestorePmo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = { LocaleMapper.class })
public interface GestoreMapper {

	@Mappings(value = {
			@Mapping(source = Definitions.Gestore.PMO_PROP_ID, target = Definitions.Gestore.ENTITY_PROP_ID),
			@Mapping(source = Definitions.Gestore.PMO_PROP_NOME, target = Definitions.Gestore.ENTITY_PROP_NOME),
			@Mapping(source = Definitions.Gestore.PMO_PROP_MAIL, target = Definitions.Gestore.ENTITY_PROP_MAIL),
			@Mapping(source = Definitions.Gestore.PMO_PROP_LISTA_LOCALI, target = Definitions.Gestore.ENTITY_PROP_LISTA_LOCALI) })
	public Gestore gestorePmoToGestore(GestorePmo gestorePmo);

	// @Mappings(value = {
	// @Mapping(source = Definitions.Gestore.ENTITY_PROP_ID, target =
	// Definitions.Gestore.PMO_PROP_ID),
	// @Mapping(source = Definitions.Gestore.ENTITY_PROP_NOME, target =
	// Definitions.Gestore.PMO_PROP_NOME),
	// @Mapping(source = Definitions.Gestore.ENTITY_PROP_MAIL, target =
	// Definitions.Gestore.PMO_PROP_MAIL),
	// @Mapping(source = Definitions.Gestore.ENTITY_PROP_LISTA_LOCALI, target =
	// Definitions.Gestore.PMO_PROP_LISTA_LOCALI) })
	public GestorePmo gestoreToGestorePmo(Gestore gestore);
}

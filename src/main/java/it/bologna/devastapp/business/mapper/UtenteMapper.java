package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.Utente;
import it.bologna.devastapp.presentation.pmo.UtentePmo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface UtenteMapper {

	@Mappings(value = {
			@Mapping(source = Definitions.Utente.PMO_PROP_ID, target = Definitions.Utente.ENTITY_PROP_ID),
			@Mapping(source = Definitions.Utente.PMO_PROP_ID_TELEFONO, target = Definitions.Utente.ENTITY_PROP_ID_TELEFONO),
			@Mapping(source = Definitions.Utente.PMO_PROP_TIPO_TELEFONO, target = Definitions.Utente.ENTITY_PROP_TIPO_TELEFONO)

	})
	public Utente pmoToEntity(UtentePmo utentePmo);

	@Mappings(value = {
			@Mapping(source = Definitions.Utente.ENTITY_PROP_ID, target = Definitions.Utente.PMO_PROP_ID),
			@Mapping(source = Definitions.Utente.ENTITY_PROP_ID_TELEFONO, target = Definitions.Utente.PMO_PROP_ID_TELEFONO),
			@Mapping(source = Definitions.Utente.ENTITY_PROP_TIPO_TELEFONO, target = Definitions.Utente.PMO_PROP_TIPO_TELEFONO)

	})
	public UtentePmo entityToPmo(Utente utente);
}

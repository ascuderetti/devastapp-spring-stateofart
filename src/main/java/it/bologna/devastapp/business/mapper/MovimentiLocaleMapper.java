package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.MovimentiLocale;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface MovimentiLocaleMapper {

	@Mappings(value = { @Mapping(source = "pmo.idOggetto", target = Definitions.MovimentiLocale.ENTITY_PROP_ID_LOCALE) })
	public MovimentiLocale pmoToEntity(MovimentoUtentePmo pmo, Stato stato);

	@Mappings(value = {
			@Mapping(source = Definitions.ENTITY_PROP_ID, target = Definitions.PMO_PROP_ID),
			@Mapping(source = Definitions.MovimentiLocale.ENTITY_PROP_ID_LOCALE, target = Definitions.MovimentoUtente.PMO_PROP_ID_OGGETTO),
			@Mapping(source = Definitions.MovimentiLocale.ENTITY_PROP_ID_UTENTE, target = Definitions.MovimentoUtente.PMO_PROP_ID_UTENTE) })
	public MovimentoUtentePmo entityToPmo(MovimentiLocale entity);
}

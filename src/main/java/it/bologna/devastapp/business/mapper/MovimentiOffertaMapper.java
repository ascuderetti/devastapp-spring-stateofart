package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.MovimentiOfferta;
import it.bologna.devastapp.persistence.entity.Stato;
import it.bologna.devastapp.presentation.pmo.MovimentoUtentePmo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface MovimentiOffertaMapper {

	@Mappings(value = {
			@Mapping(source = "pmo." + Definitions.PMO_PROP_ID, target = Definitions.ENTITY_PROP_ID),
			@Mapping(source = "pmo."
					+ Definitions.MovimentoUtente.PMO_PROP_ID_OGGETTO, target = Definitions.MovimentiOfferta.ENTITY_PROP_ID_OFFERTA),
			@Mapping(source = "pmo."
					+ Definitions.MovimentoUtente.PMO_PROP_ID_UTENTE, target = Definitions.MovimentiOfferta.ENTITY_PROP_ID_UTENTE) })
	public MovimentiOfferta pmoToEntity(MovimentoUtentePmo pmo, Stato stato);

	@Mappings(value = {
			@Mapping(source = Definitions.ENTITY_PROP_ID, target = Definitions.PMO_PROP_ID),
			@Mapping(source = Definitions.MovimentiOfferta.ENTITY_PROP_ID_OFFERTA, target = Definitions.MovimentoUtente.PMO_PROP_ID_OGGETTO),
			@Mapping(source = Definitions.MovimentiOfferta.ENTITY_PROP_ID_UTENTE, target = Definitions.MovimentoUtente.PMO_PROP_ID_UTENTE) })
	public MovimentoUtentePmo entityToPmo(MovimentiOfferta movimentiOfferta);
}

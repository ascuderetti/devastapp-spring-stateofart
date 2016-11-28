package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.Posizione;
import it.bologna.devastapp.presentation.pmo.PosizionePmo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface PosizioneMapper {

	@Mappings(value = {
			@Mapping(source = Definitions.Posizione.PMO_PROP_ID, target = Definitions.Posizione.ENTITY_PROP_ID),
			@Mapping(source = Definitions.Posizione.PMO_PROP_LONGITUDINE, target = Definitions.Posizione.ENTITY_PROP_LONGITUDINE),
			@Mapping(source = Definitions.Posizione.PMO_PROP_LATITUDINE, target = Definitions.Posizione.ENTITY_PROP_LATITUDINE),
			@Mapping(source = Definitions.Posizione.PMO_PROP_INDIRIZZO, target = Definitions.Posizione.ENTITY_PROP_INDIRIZZO),
			@Mapping(source = Definitions.Posizione.PMO_PROP_NUMERO, target = Definitions.Posizione.ENTITY_PROP_NUMERO),
			@Mapping(source = Definitions.Posizione.PMO_PROP_CITTA, target = Definitions.Posizione.ENTITY_PROP_CITTA),
			@Mapping(source = Definitions.Posizione.PMO_PROP_CAP, target = Definitions.Posizione.ENTITY_PROP_CAP) })
	public Posizione posizionePmoToPosizione(PosizionePmo posizionePmo);

	@Mappings(value = {
			@Mapping(source = Definitions.Posizione.ENTITY_PROP_ID, target = Definitions.Posizione.PMO_PROP_ID),
			@Mapping(source = Definitions.Posizione.ENTITY_PROP_LONGITUDINE, target = Definitions.Posizione.PMO_PROP_LONGITUDINE),
			@Mapping(source = Definitions.Posizione.ENTITY_PROP_LATITUDINE, target = Definitions.Posizione.PMO_PROP_LATITUDINE),
			@Mapping(source = Definitions.Posizione.ENTITY_PROP_INDIRIZZO, target = Definitions.Posizione.PMO_PROP_INDIRIZZO),
			@Mapping(source = Definitions.Posizione.ENTITY_PROP_NUMERO, target = Definitions.Posizione.PMO_PROP_NUMERO),
			@Mapping(source = Definitions.Posizione.ENTITY_PROP_CITTA, target = Definitions.Posizione.PMO_PROP_CITTA),
			@Mapping(source = Definitions.Posizione.ENTITY_PROP_CAP, target = Definitions.Posizione.PMO_PROP_CAP) })
	public PosizionePmo posizioneToPosizionePmo(Posizione posizione);
}

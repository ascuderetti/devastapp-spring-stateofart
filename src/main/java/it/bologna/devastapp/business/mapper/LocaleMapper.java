package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.presentation.pmo.LocalePmo;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = PosizioneMapper.class)
public interface LocaleMapper {

	@Mappings(value = {
			@Mapping(source = Definitions.Locale.PMO_PROP_id, target = Definitions.Locale.ENTITY_PROP_ID),
			@Mapping(source = Definitions.Locale.PMO_PROP_posizione, target = Definitions.Locale.ENTITY_PROP_POSIZIONE),
			@Mapping(source = Definitions.Locale.PMO_PROP_idGestore, target = Definitions.Locale.ENTITY_PROP_idGestore),
			@Mapping(source = Definitions.Locale.PMO_PROP_nome, target = Definitions.Locale.ENTITY_PROP_NOME),
			@Mapping(source = Definitions.Locale.PMO_PROP_fidelizzazione, target = Definitions.Locale.ENTITY_PROP_fidelizzazione),
			// @Mapping(source = "logo", target = "logo"),
			@Mapping(source = Definitions.Locale.PMO_PROP_descrizione, target = Definitions.Locale.ENTITY_PROP_DESCRIZIONE) })
	public Locale localePmoToLocale(LocalePmo localePmo);

	@Mappings(value = {
			@Mapping(source = Definitions.Locale.ENTITY_PROP_ID, target = Definitions.Locale.PMO_PROP_id),
			@Mapping(source = Definitions.Locale.ENTITY_PROP_POSIZIONE, target = Definitions.Locale.PMO_PROP_posizione),
			@Mapping(source = Definitions.Locale.ENTITY_PROP_idGestore, target = Definitions.Locale.PMO_PROP_idGestore),
			@Mapping(source = Definitions.Locale.ENTITY_PROP_NOME, target = Definitions.Locale.PMO_PROP_nome),
			@Mapping(source = Definitions.Locale.PMO_PROP_fidelizzazione, target = Definitions.Locale.ENTITY_PROP_fidelizzazione),
			// @Mapping(source = "logo", target = "logo"),
			@Mapping(source = Definitions.Locale.ENTITY_PROP_DESCRIZIONE, target = Definitions.Locale.PMO_PROP_descrizione) })
	public LocalePmo localeToLocalePmo(Locale locale);

	public List<LocalePmo> listLocaleToListLocalePmo(List<Locale> localeList);

	public List<Locale> listLocalePmoToListLocale(List<LocalePmo> localePmoList);
}

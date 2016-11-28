package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.pmo.OffertaLetturaPmo;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/*
 *  OffertaPmo
 *  
 *  String titolo;
 *	String descrizione;
 *	ProdottoPmo prodottoPmo;
 *	Date dataInizio;
 *	Date dataFine;
 *	int quantita;
 *	BigDecimal prezzo;
 *	LocalePmo localePmo;
 *
 *  Offerta
 *
 *  String titolo;
 *  String descrizione;
 *  Prodotto prodotto;
 *  Date dataInizio;
 *  Date dataFine;
 *  int quantita;
 *  BigDecimal prezzo;
 *  Locale locale; 
 *
 */

@Mapper(uses = { ProdottoMapper.class, LocaleMapper.class })
public abstract class OffertaMapper {

	@Mappings(value = {
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_id, target = Definitions.Offerta.PMO_PROP_id),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_titolo, target = Definitions.Offerta.PMO_PROP_titolo),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_descrizione, target = Definitions.Offerta.PMO_PROP_descrizione),
			@Mapping(source = "prodotto.id", target = "idProdotto"),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_dataInizio, target = Definitions.Offerta.PMO_PROP_dataInizio),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_dataFine, target = Definitions.Offerta.PMO_PROP_dataFine),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_quantita, target = Definitions.Offerta.PMO_PROP_quantita),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_prezzoUnitarioPieno, target = Definitions.Offerta.PMO_PROP_prezzoUnitarioPieno),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_prezzoUnitarioScontato, target = Definitions.Offerta.PMO_PROP_prezzoUnitarioScontato),
			@Mapping(source = "locale.id", target = "idLocale") })
	public abstract OffertaScritturaPmo entityToScritturaPmo(Offerta entity);

	public Offerta scritturaPmoToEntity(OffertaScritturaPmo scritturaPmo) {

		if (scritturaPmo == null) {
			return null;
		}

		Offerta offerta = new Offerta();

		offerta.setId(scritturaPmo.getId());
		offerta.setTitolo(scritturaPmo.getTitolo());
		offerta.setDescrizione(scritturaPmo.getDescrizione());

		Prodotto prodotto = new Prodotto();
		prodotto.setId(scritturaPmo.getIdProdotto());
		offerta.setProdotto(prodotto);

		offerta.setDataInizio(scritturaPmo.getDataInizio());
		offerta.setDataFine(scritturaPmo.getDataFine());
		offerta.setQuantita(scritturaPmo.getQuantita());
		offerta.setPrezzoUnitarioPieno(scritturaPmo.getPrezzoUnitarioPieno());
		offerta.setPrezzoUnitarioScontato(scritturaPmo
				.getPrezzoUnitarioScontato());

		Locale locale = new Locale();
		locale.setId(scritturaPmo.getIdLocale());
		offerta.setLocale(locale);

		return offerta;

	}

	@Mappings(value = {
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_id, target = Definitions.Offerta.PMO_PROP_id),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_titolo, target = Definitions.Offerta.PMO_PROP_titolo),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_descrizione, target = Definitions.Offerta.PMO_PROP_descrizione),
			@Mapping(source = "prodotto", target = "prodotto"),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_dataInizio, target = Definitions.Offerta.PMO_PROP_dataInizio),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_dataFine, target = Definitions.Offerta.PMO_PROP_dataFine),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_quantita, target = Definitions.Offerta.PMO_PROP_quantita),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_prezzoUnitarioPieno, target = Definitions.Offerta.PMO_PROP_prezzoUnitarioPieno),
			@Mapping(source = Definitions.Offerta.ENTITY_PROP_prezzoUnitarioScontato, target = Definitions.Offerta.PMO_PROP_prezzoUnitarioScontato),
			@Mapping(source = "locale", target = "locale") })
	public abstract OffertaLetturaPmo entityToLetturaPmo(Offerta offerta);

	public abstract OffertaLetturaPmo entityToLetturaPmo(Offerta offerta,
			Integer quantitaRimanente);

}

package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.Prodotto;
import it.bologna.devastapp.presentation.pmo.ProdottoPmo;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ProdottoMapper {

	@Mappings(value = {
			@Mapping(source = Definitions.Prodotto.PMO_PROP_id, target = Definitions.Prodotto.ENTITY_PROP_id),
			@Mapping(source = Definitions.Prodotto.PMO_PROP_descrizione, target = Definitions.Prodotto.ENTITY_PROP_descrizione)

	})
	public Prodotto pmoToEntity(ProdottoPmo prodottoPmo);

	@InheritInverseConfiguration
	public ProdottoPmo entityToPmo(Prodotto prodotto);

	public List<ProdottoPmo> entitiesToPmos(List<Prodotto> prodotto);

}

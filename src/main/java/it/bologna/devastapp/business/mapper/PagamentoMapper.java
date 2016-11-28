package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.business.Definitions;
import it.bologna.devastapp.persistence.entity.Offerta;
import it.bologna.devastapp.persistence.entity.Pagamento;
import it.bologna.devastapp.presentation.pmo.PagamentoPmo;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = OffertaMapper.class)
public abstract class PagamentoMapper {

	/*
	 * TODO: gestire mapping da List<Offerta> (nell'entity) <=> List<Long> ids
	 * (nel pmo) con mapping automatico
	 */
	public Pagamento pmoToEntity(PagamentoPmo pagamentoPmo) {
		if (pagamentoPmo == null) {
			return null;
		}

		Pagamento pagamento = new Pagamento();

		pagamento.setId(pagamentoPmo.getId());
		pagamento.setDataPagamento(pagamentoPmo.getDataPagamento());
		pagamento.setTitoloAcquisto(pagamentoPmo.getTitoloAcquisto());
		pagamento.setIdPaypalPayment(pagamentoPmo.getPagamentoID());
		pagamento.setIdGestore(pagamentoPmo.getIdGestore());
		pagamento.setIdPaypalPayer(pagamentoPmo.getPayerID());
		pagamento.setImportoTotale(pagamentoPmo.getImportoTotale());

		List<Offerta> listaOff = new ArrayList<Offerta>();
		for (Long id : pagamentoPmo.getListaIdOfferta()) {

			Offerta off = new Offerta();
			off.setId(id);
			listaOff.add(off);
		}

		pagamento.setOfferte(listaOff);

		return pagamento;
	}

	@Mappings(value = {
			@Mapping(source = Definitions.Pagamento.ENTITY_PROP_ID, target = Definitions.Pagamento.PMO_PROP_ID),
			// @Mapping(source = Definitions.Pagamento.ENTITY_PROP_OFFERTE,
			// target = Definitions.Pagamento.PMO_PROP_LISTA_ID_OFFERTA),
			@Mapping(source = Definitions.Pagamento.ENTITY_PROP_ID_GESTORE, target = Definitions.Pagamento.PMO_PROP_ID_GESTORE),
			@Mapping(source = Definitions.Pagamento.ENTITY_PROP_TITOLO_ACQUISTO, target = Definitions.Pagamento.PMO_PROP_TITOLO_ACQUISTO),
			@Mapping(source = Definitions.Pagamento.ENTITY_PROP_DATA_PAGAMENTO, target = Definitions.Pagamento.PMO_PROP_DATA_PAGAMENTO),
			@Mapping(source = Definitions.Pagamento.ENTITY_PROP_IMPORTO_TOTALE, target = Definitions.Pagamento.PMO_PROP_IMPORTO_TOTALE),
			@Mapping(source = Definitions.Pagamento.ENTITY_PROP_ID_PAYPAL_PAYMENT, target = Definitions.Pagamento.PMO_PROP_PAGAMENTO_ID),
			// @Mapping(source = Definitions.Pagamento.ENTITY_PROP_REDIRECT_URL,
			// target = Definitions.Pagamento.PMO_PROP_REDIRECT_URL),
			// @Mapping(source = Definitions.Pagamento.ENTITY_PROP_STATO, target
			// = Definitions.Pagamento.PMO_PROP_STATO),
			@Mapping(source = Definitions.Pagamento.ENTITY_PROP_ID_PAYPAL_PAYER, target = Definitions.Pagamento.PMO_PROP_PAYER_ID) })
	public abstract PagamentoPmo entityToPmo(Pagamento pagamento);

}

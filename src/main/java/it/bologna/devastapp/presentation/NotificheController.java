package it.bologna.devastapp.presentation;

import it.bologna.devastapp.business.notifications.IphoneServiceImpl;
import it.bologna.devastapp.business.notifications.TelefonoService;
import it.bologna.devastapp.business.notifications.model.NotificaTelefono;
import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.entity.TipoTelefono;
import it.bologna.devastapp.presentation.jsonmodel.BaseRispostaJson;
import it.bologna.devastapp.presentation.pmo.OffertaScritturaPmo;
import it.bologna.devastapp.util.JsonResponseUtil;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = RestUrl.GESTORE + "/notifica")
public class NotificheController {

	@Autowired
	@Qualifier(IphoneServiceImpl.KEY_SPRING_IPHONE_SERVICE)
	TelefonoService telefonoService;

	@RequestMapping(value = "/offertaInserita", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BaseRispostaJson notifica(HttpServletResponse response) {

		NotificaTelefono notifica = new NotificaTelefono(TipoTelefono.IPHONE,
				"idTelefonoGino", null);

		telefonoService.inviaNotifica(notifica);

		//
		// List<GestorePmo> listaGestoriPmo = gestoreService.getListaGestori();
		//
		// risposta fittizzia - questo metodo dovrà essere cancellato - TODO
		BaseRispostaJson risposta = JsonResponseUtil
				.creaRispostaJsonInserimentoSingolo(new OffertaScritturaPmo(),
						SignalDescriptor.OFFERTA_GLOBALE_KO,
						SignalDescriptor.OFFERTA_GLOBALE_OK);

		return risposta;
	}
}

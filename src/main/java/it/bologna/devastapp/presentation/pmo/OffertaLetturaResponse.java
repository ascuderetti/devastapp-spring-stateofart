package it.bologna.devastapp.presentation.pmo;

import java.util.List;

/*
 * Wrapper che contiene pmo di input e pmo di output
 */

public class OffertaLetturaResponse {

	PosizionePmo posizionePmo;

	List<OffertaLetturaPmo> listLetturaOffertaPmo;

	public PosizionePmo getPosizionePmo() {
		return posizionePmo;
	}

	public void setPosizionePmo(PosizionePmo posizionePmo) {
		this.posizionePmo = posizionePmo;
	}

	public List<OffertaLetturaPmo> getListLetturaOffertaPmo() {
		return listLetturaOffertaPmo;
	}

	public void setListLetturaOffertaPmo(
			List<OffertaLetturaPmo> listLetturaOffertaPmo) {
		this.listLetturaOffertaPmo = listLetturaOffertaPmo;
	}

}

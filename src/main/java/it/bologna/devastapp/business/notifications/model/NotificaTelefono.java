package it.bologna.devastapp.business.notifications.model;

import it.bologna.devastapp.business.signal.SignalDescriptor;
import it.bologna.devastapp.persistence.entity.TipoTelefono;

import java.io.Serializable;

/**
 * Model Notifica ad Utente App
 * 
 * @author ascuderetti
 * 
 */
public class NotificaTelefono implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 8251399263381273058L;

	private TipoTelefono tipoTelefono;
	private String idTelefono;
	private SignalDescriptor segnalazione;

	public NotificaTelefono(TipoTelefono tipoTelefono, String idTelefono,
			SignalDescriptor segnalazione) {

		this.tipoTelefono = tipoTelefono;
		this.idTelefono = idTelefono;
		this.setSegnalazione(segnalazione);

	}

	public TipoTelefono getTipoTelefono() {
		return tipoTelefono;
	}

	public void setTipoTelefono(TipoTelefono tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}

	public String getIdTelefono() {
		return idTelefono;
	}

	public void setIdTelefono(String idTelefono) {
		this.idTelefono = idTelefono;
	}

	public SignalDescriptor getSegnalazione() {
		return segnalazione;
	}

	public void setSegnalazione(SignalDescriptor segnalazione) {
		this.segnalazione = segnalazione;
	}

}

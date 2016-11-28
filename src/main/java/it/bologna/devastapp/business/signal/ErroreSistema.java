package it.bologna.devastapp.business.signal;

/**
 * Oggetto che rappresenta una eccezione di sistema (lanciata ad esempio quando
 * arriva una richiesta di inserimento e il PMO ha l'id valorizzato! )
 * 
 * @author ascuderetti
 */
public class ErroreSistema extends RuntimeException {

	/**
	 * Unique ID for Serialized object
	 */
	private static final long serialVersionUID = -7834735379888169059L;

	SignalDescriptor signal;

	public ErroreSistema(SignalDescriptor signal) {

		this.signal = signal;

	}

	public SignalDescriptor getSignal() {
		return signal;
	}

	public void setSignal(SignalDescriptor signal) {
		this.signal = signal;
	}

}

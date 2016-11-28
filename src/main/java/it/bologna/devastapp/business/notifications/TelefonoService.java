package it.bologna.devastapp.business.notifications;

import it.bologna.devastapp.business.notifications.model.NotificaTelefono;

/**
 * Interfaccia che espone i servizi relativi a comunicazione con il Device
 * 
 * @author ascuderetti
 * 
 */
public interface TelefonoService {

	public void inviaNotifica(NotificaTelefono notifica);

}

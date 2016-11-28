package it.bologna.devastapp.business.signal;

/**
 * TIPI DI MESSAGGI DI RISPOSTA<br>
 * 
 * {@link TipoMessaggio#SUCCESSO}: tipo per messaggio di OK, esempio: "L'offerta
 * è stata inserita correttamente"<br>
 * 
 * {@link TipoMessaggio#ERRORE_BUSINESS}:tipo messaggio di KO di Business, esempio:
 * "Data inizio offerta non valida" <br>
 * 
 * {@link TipoMessaggio#ERRORE_SISTEMA}: tipo messaggio di KO di Sistema, esempio:
 * "Errore di sistema, il campo ID non deve essere valorizzato in un inserimento!"
 * <br>
 * 
 * {@link TipoMessaggio#INFO}: tipo messaggio di OK per casi in cui non c'e'
 * stata nessuna anomalia, ma si vuole segnalare qualcosa di rilevante ad
 * esempio:
 * "Non e' stata trovata nessuna offerta, provare ad allargare il campo di ricerca.."
 * <br>
 * 
 * 
 * @author ascuderetti
 * 
 */
public enum TipoMessaggio {

	SUCCESSO, ERRORE_BUSINESS, ERRORE_SISTEMA, INFO
}

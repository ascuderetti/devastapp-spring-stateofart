package it.bologna.devastapp.persistence.entity;

import javax.persistence.Entity;

import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.spatial.Coordinates;

@Entity
public class Posizione extends AbstractEntity {

	private static final long serialVersionUID = -3937391503778514906L;

	private Double longitudine;

	private Double latitudine;

	private String indirizzo;

	private String numero;

	private String citta;

	private String cap;

	/**
	 * Serve ad Hibernate-Search per ricerche spaziali. In alternativa si può
	 * annotare latitudine e longitudine con @Latitude e @Longitude
	 * 
	 * @return
	 */
	@Spatial
	public Coordinates getLocation() {
		return new Coordinates() {
			@Override
			public Double getLatitude() {
				return latitudine;
			}

			@Override
			public Double getLongitude() {
				return longitudine;
			}
		};
	}

	public Double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(Double longitudine) {
		this.longitudine = longitudine;
	}

	public Double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(Double latitudine) {
		this.latitudine = latitudine;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

}

package it.bologna.devastapp.presentation.pmo;

import java.io.Serializable;

/**
 * Model: Posizione
 * 
 * @author AS
 * 
 */
public class Test3Pmo implements Serializable {

	/** Generated serial UID */
	private static final long serialVersionUID = 994328173762243906L;

	private Double latitude;
	private Double longitude;

	// *******************************//
	// ********* GET E SET ***********//
	// *******************************//

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}

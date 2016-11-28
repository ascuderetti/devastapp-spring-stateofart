package it.bologna.devastapp.presentation.pmo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Model: Offerta
 * 
 * @author AS
 * 
 */
public class TestPmo implements Serializable {

	/** Generated serial UID */
	private static final long serialVersionUID = -3458738008199745281L;

	private Integer id;

	private String description;
	private Test3Pmo posizione;
	private Double lat;

	private Map<Long, String> listaProdotti;

	private Date dateEnd;

	// *******************************//
	// ********* GET E SET ***********//
	// *******************************//

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Test3Pmo getPosizione() {
		return posizione;
	}

	public void setPosizione(Test3Pmo posizione) {
		this.posizione = posizione;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<Long, String> getListaProdotti() {
		return listaProdotti;
	}

	public void setListaProdotti(Map<Long, String> listaProdotti) {
		this.listaProdotti = listaProdotti;
	}

}

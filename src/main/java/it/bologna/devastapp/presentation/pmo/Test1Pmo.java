package it.bologna.devastapp.presentation.pmo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Model: Offerta
 * 
 * @author AS
 * 
 */
public class Test1Pmo extends BasePmo {

	/** Generated serial UID */
	private static final long serialVersionUID = -3458738008199745281L;

	private String description;
	private Test3Pmo position;
	private Test2Pmo offerer;

	// TODO: Usare Calendar?
	private Date dateStart;
	private Date dateEnd;
	private Date visibleFrom;

	/*
	 * DATI SULL'OGGETTO IN VENDITA
	 */
	// TODO: creare un Obj?
	private String stuff;
	private Integer quantity;
	// TODO: creare un Obj prezzo? (con valuta?)
	private BigDecimal price;

	// *******************************//
	// ********* GET E SET ***********//
	// *******************************//

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Test3Pmo getPosition() {
		return position;
	}

	public void setPosition(Test3Pmo position) {
		this.position = position;
	}

	public Date getVisibleFrom() {
		return visibleFrom;
	}

	public void setVisibleFrom(Date visibleFrom) {
		this.visibleFrom = visibleFrom;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getStuff() {
		return stuff;
	}

	public void setStuff(String stuff) {
		this.stuff = stuff;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Test2Pmo getOfferer() {
		return offerer;
	}

	public void setOfferer(Test2Pmo offerer) {
		this.offerer = offerer;
	}

}

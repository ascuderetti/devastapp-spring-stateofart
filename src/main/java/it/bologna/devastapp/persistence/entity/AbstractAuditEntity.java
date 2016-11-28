package it.bologna.devastapp.persistence.entity;

import javax.persistence.MappedSuperclass;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public abstract class AbstractAuditEntity extends AbstractEntity {

	/** Serial UID */
	private static final long serialVersionUID = -2308946342682143961L;

	@CreatedDate
	private DateTime dataCreazione;

	@LastModifiedDate
	private DateTime ultimaModifica;

}

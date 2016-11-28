package it.bologna.devastapp.persistence.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Gestore extends AbstractEntity {

	private static final long serialVersionUID = 319130536281582827L;

	private String nome;
	private String mail;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "idGestore")
	private List<Locale> listaLocali;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public List<Locale> getListaLocali() {
		return listaLocali;
	}

	public void setListaLocali(List<Locale> listaLocali) {
		this.listaLocali = listaLocali;
	}

}

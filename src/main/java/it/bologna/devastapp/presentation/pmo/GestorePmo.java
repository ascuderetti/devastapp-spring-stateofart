package it.bologna.devastapp.presentation.pmo;

import java.util.List;

public class GestorePmo extends BasePmo {

	private static final long serialVersionUID = -8770745549869950423L;

	private String nome;
	private String mail;
	private List<LocalePmo> listaLocali;

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

	public List<LocalePmo> getListaLocali() {
		return listaLocali;
	}

	public void setListaLocali(List<LocalePmo> listaLocali) {
		this.listaLocali = listaLocali;
	}

}

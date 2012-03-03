package com.gdm.namechooser.model;

public class Prenom {

	public enum Genre {
		GARS, FILLE
	}

	private long id;

	private String prenom;

	private int frequence;

	private int annee;

	private Genre genre;

	private boolean favoris;

	public Prenom(String prenom, int frequence, int annee, Genre genre, boolean favoris) {
		this.prenom = prenom;
		this.frequence = frequence;
		this.annee = annee;
		this.genre = genre;
		this.favoris = favoris;
	}

	public Prenom() {
	}

	public String getPrenom() {
		return prenom;
	}

	public int getAnnee() {
		return annee;
	}

	public int getFrequence() {
		return frequence;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public boolean isFavoris() {
		return favoris;
	}

	public void setFavoris(boolean favoris) {
		this.favoris = favoris;
	}
}

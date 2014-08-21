package com.anjoyo.bean;

public class LocalMusic {
	private String muiscName ;
	private String path;
	private String artist;
	private String duration;
	private String addedDate ;
	private String album;
	private String letter;
	public String getMuiscName() {
		return muiscName;
	}
	
	public LocalMusic(String muiscName, String path, String artist,
			String duration, String addedDate, String album, String letter) {
		super();
		this.muiscName = muiscName;
		this.path = path;
		this.artist = artist;
		this.duration = duration;
		this.addedDate = addedDate;
		this.album = album;
		this.letter = letter;
	}
	/**
	 * 
	 */
	public LocalMusic() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setMuiscName(String muiscName) {
		this.muiscName = muiscName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
}

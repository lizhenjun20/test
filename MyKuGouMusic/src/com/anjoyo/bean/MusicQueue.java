package com.anjoyo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicQueue implements Parcelable{
	private int duration;
	private String source;
	private int mp3Size;
	private String path;
	private int type;
	private String name;
	private String artist;
	private int index;
	private boolean isAdd;
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getMp3Size() {
		return mp3Size;
	}
	public void setMp3Size(int mp3Size) {
		this.mp3Size = mp3Size;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isAdd() {
		return isAdd;
	}
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	@Override
	public int describeContents() {
		
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(duration);
		dest.writeString(source);
		dest.writeInt(mp3Size);
		dest.writeString(path);
		dest.writeInt(type);
		dest.writeString(name);
		dest.writeString(artist);
		dest.writeInt(index);
		dest.writeByte((byte) (isAdd ? 1 : 0));
	}
	public static final Parcelable.Creator<MusicQueue> CREATOR=new Creator<MusicQueue>() {

		@Override
		public MusicQueue createFromParcel(Parcel source) {
			MusicQueue music = new MusicQueue();
			music.setDuration(source.readInt());
			music.setSource(source.readString());
			music.setMp3Size(source.readInt());
			music.setPath(source.readString());
			music.setType(source.readInt());
			music.name = source.readString();
			music.setArtist(source.readString());
			music.setIndex(source.readInt());
			music.setAdd(source.readByte() == 1);
			return music;
		}

		@Override
		public MusicQueue[] newArray(int size) {
			return new MusicQueue[size];
		}
	};

}

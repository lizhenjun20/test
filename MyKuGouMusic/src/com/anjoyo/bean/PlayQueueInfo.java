package com.anjoyo.bean;

import java.util.List;

public class PlayQueueInfo {

	private List<MusicQueue> musics;
	private int curPlayPos;
	private String curQueueTag;
	private int curMusicPosition;
	public List<MusicQueue> getMusics() {
		return musics;
	}
	public void setMusics(List<MusicQueue> musics) {
		this.musics = musics;
	}
	public int getCurPlayPos() {
		return curPlayPos;
	}
	public void setCurPlayPos(int curPlayPos) {
		this.curPlayPos = curPlayPos;
	}
	public String getCurQueueTag() {
		return curQueueTag;
	}
	public void setCurQueueTag(String curQueueTag) {
		this.curQueueTag = curQueueTag;
	}
	public int getCurMusicPosition() {
		return curMusicPosition;
	}
	public void setCurMusicPosition(int curMusicPosition) {
		this.curMusicPosition = curMusicPosition;
	}
	
}

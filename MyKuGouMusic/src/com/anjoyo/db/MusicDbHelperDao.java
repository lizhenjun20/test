package com.anjoyo.db;


import java.util.ArrayList;
import java.util.List;

import com.anjoyo.bean.LocalMusic;
import com.anjoyo.bean.MusicQueue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MusicDbHelperDao extends MusicDbHelper {
	private SQLiteDatabase dbWrite;

	public MusicDbHelperDao(Context context) {
		super(context);
		dbWrite=getWritableDatabase();
	}
	public void insertMusic(LocalMusic music){
		ContentValues values=new ContentValues();
		values.put(LocalMusicColumns.ALBUM, music.getAlbum());
		values.put(LocalMusicColumns.ARTIST, music.getArtist());
		values.put(LocalMusicColumns.DURATION, music.getDuration());
		values.put(LocalMusicColumns.LETTER, music.getLetter());
		values.put(LocalMusicColumns.DATE_ADDED, music.getAddedDate());
		values.put(LocalMusicColumns.MUSIC_NAME, music.getMuiscName());
		values.put(LocalMusicColumns.PATH, music.getPath());
		dbWrite.insertOrThrow(LOCAL_MUSIC_TABLE, null, values);
	}
	public List<MusicQueue> getLocalQueue(){
		List<MusicQueue> musicQueues=new ArrayList<MusicQueue>();
		Cursor cursor = dbWrite.query(LOCAL_MUSIC_TABLE, null, null, null, null, null, LocalMusicColumns.LETTER);
		int index=0;
		while (cursor.moveToNext()) {
			
			MusicQueue music = new MusicQueue();
			music.setSource("本地音乐");
			music.setPath(cursor.getString(cursor.getColumnIndex(LocalMusicColumns.PATH)));
			music.setName(cursor.getString(cursor.getColumnIndex(LocalMusicColumns.MUSIC_NAME)));
			music.setArtist(cursor.getString(cursor.getColumnIndex(LocalMusicColumns.ARTIST)));
			music.setDuration(cursor.getInt(cursor.getColumnIndex(LocalMusicColumns.DURATION)));
			music.setMp3Size(cursor.getInt(cursor.getColumnIndex(LocalMusicColumns.MP3_SIZE)));
			music.setIndex(index++);
			musicQueues.add(music);
			
		}
		return musicQueues;

	}
}

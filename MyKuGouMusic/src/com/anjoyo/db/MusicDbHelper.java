package com.anjoyo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDbHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "kugou_music";
	public static final int VERSION = 1;

	public static final String LOCAL_MUSIC_TABLE = "local_music";

	public MusicDbHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + LOCAL_MUSIC_TABLE + "("
				+ LocalMusicColumns._ID + " integer primary key autoincrement,"
				+ LocalMusicColumns.MUSIC_NAME + " text not null default \"\","
				+ LocalMusicColumns.PATH + " text not null default \"\","
				+ LocalMusicColumns.ARTIST + "  text not null default \"\","
				+ LocalMusicColumns.ALBUM + "  text not null default \"\","
				+ LocalMusicColumns.DURATION + " integer not null default 0,"
				+ LocalMusicColumns.LETTER + " text,"
				+ LocalMusicColumns.DATE_ADDED + " long,"
				+ LocalMusicColumns.MP3_SIZE + " integer default 0)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	public interface LocalMusicColumns{
		String _ID = "_id";
		String MUSIC_NAME = "music_name";
		String PATH = "path";
		String ARTIST = "artist";
		String DURATION = "duration";
		String ALBUM = "album";
		String DATE_ADDED = "date_added";
		String LETTER = "letter";
		String MP3_SIZE="mp3_size";
	}


}

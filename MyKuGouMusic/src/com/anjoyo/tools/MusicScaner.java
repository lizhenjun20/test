package com.anjoyo.tools;


import com.anjoyo.bean.LocalMusic;
import com.anjoyo.db.MusicDbHelperDao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class MusicScaner {
	private Context context;

	public MusicScaner(Context context) {
		super();
		this.context = context;
	}
	public void startScan(){
		if (!isSdcardHave()) {
			return;
		}
		MusicDbHelperDao dao=new MusicDbHelperDao(context);
		ContentResolver resolver=context.getContentResolver();
		new ScanThread(resolver, dao).start();;


	}
	private class ScanThread extends Thread{
		private ContentResolver resolver;
		private MusicDbHelperDao dao;
		public ScanThread(ContentResolver resolver,MusicDbHelperDao dao) {
			super();
			this.resolver = resolver;
			this.dao = dao;
		}
		@Override
		public void run() {
			Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
			while (cursor.moveToNext()) {
				String muiscName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
				String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
				String addedDate = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
				String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				String letter = LettetUtil.getLetter(artist);
				dao.insertMusic(new LocalMusic(muiscName, path, artist, duration, addedDate, album, letter));
				System.out.println("歌曲名字"+muiscName);
			}
			if (completeListener!=null) {
				
				completeListener.OnComplete(cursor.getCount());
			}
			dao.close();
			
		}
	}
	private Boolean isSdcardHave(){
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;

		}
		return false;
	}
	public void log(Object o){
		Log.d("MusicScanner", o+"");
	}
	public void setOnCompleteListener(OnCompleteListener completeListener) {
		this.completeListener=completeListener;
		
	}
	private OnCompleteListener completeListener;
	public interface OnCompleteListener{
		void OnComplete(int count);
	}

}

package com.anjoyo.tools;

import java.util.ArrayList;

import com.anjoyo.tools.HanziToPinyin.Token;
public class LettetUtil {
	
	public static String getLetter(String musicName) {
		HanziToPinyin hanziToPinyin = HanziToPinyin.getInstance();
		String newMusicName = musicName.trim();
		if(newMusicName.length() == 0){
			return "��";
		}
		char firstChar = newMusicName.charAt(0);
	
		if((firstChar >= 'A' && firstChar <= 'Z') || (firstChar >= 'a' && firstChar <= 'z')){
			return (firstChar+"").toUpperCase();
		}
		ArrayList<Token> tokens = hanziToPinyin.get(musicName);
		Token token = tokens.get(0);
		if(token.type == Token.PINYIN){
			return token.target.charAt(0)+"";
		}
		return "��";
	}
}

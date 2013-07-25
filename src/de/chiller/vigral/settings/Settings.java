package de.chiller.vigral.settings;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import de.chiller.vigral.menubar.FileOperator;

public class Settings {

	public static final String COLOR_UNVISITED = "COLOR_UNVISITED";
	public static final String COLOR_VISITED = "COLOR_VISITED";
	public static final String COLOR_ACTIVE = "COLOR_ACTIVE";
	public static final String COLOR_FINISHED_AND_RELEVANT = "COLOR_FINISHED_AND_RELEVANT";
	public static final String COLOR_FINISHED_AND_NOT_RELEVANT = "COLOR_FINISHED_AND_NOT_RELEVANT";
	public static final String COLOR_PICKED = "COLOR_PICKED";
	public static final String KEY_UNDIRECTED_EDGE = "KEY_UNDIRECTED";
	public static final String KEY_DIRECTED_EDGE = "KEY_DIRECTED_EDGE";
	public static final String KEY_MULTIPLE_SELECT = "KEY_MULTIPLE_SELECT";
	public static final String KEY_RECTANGULAR_SELECT = "KEY_RECTANGULAR_SELECT";
	
	public static final String DEF_COLOR_UNVISITED = "#33B5E5";
	public static final String DEF_COLOR_VISITED = "#FF4444";
	public static final String DEF_COLOR_ACTIVE = "#FFBB33";
	public static final String DEF_COLOR_FINISHED_AND_RELEVANT = "#99CC00";
	public static final String DEF_COLOR_FINISHED_AND_NOT_RELEVANT = "#C4C4C4";
	public static final String DEF_COLOR_PICKED = "#F7FE2E";
	public static final int DEF_KEY_UNDIRECTED_EDGE = KeyEvent.VK_U;
	public static final int DEF_KEY_DIRECTED_EDGE = KeyEvent.VK_D;
	public static final int DEF_KEY_MULTIPLE_SELECT = KeyEvent.VK_CONTROL;
	public static final int DEF_KEY_RECTANGULAR_SELECT = KeyEvent.VK_SHIFT;
	
	public static HashMap<String, String> mSettingsColor = new HashMap<String, String>();
	public static HashMap<String, Integer> mSettingsKey = new HashMap<String, Integer>();
	
	public static ArrayList<String> mColorKeyset = initColorKeyset();
	public static ArrayList<String> mKeyKeyset = initKeyKeyset();
	
	private static ArrayList<String> initKeyKeyset() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		keyset.add(KEY_UNDIRECTED_EDGE);
		keyset.add(KEY_DIRECTED_EDGE);
		keyset.add(KEY_MULTIPLE_SELECT);
		keyset.add(KEY_RECTANGULAR_SELECT);
		
		return keyset;
	}


	private static ArrayList<String> initColorKeyset() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		keyset.add(COLOR_UNVISITED);
		keyset.add(COLOR_ACTIVE);
		keyset.add(COLOR_VISITED);
		keyset.add(COLOR_FINISHED_AND_RELEVANT);
		keyset.add(COLOR_FINISHED_AND_NOT_RELEVANT);
		keyset.add(COLOR_PICKED);
		
		return keyset;
	}
	
	private static ArrayList<String> initDefColors() {
		ArrayList<String> defColors = new ArrayList<String>();
		
		defColors.add(DEF_COLOR_UNVISITED);
		defColors.add(DEF_COLOR_ACTIVE);
		defColors.add(DEF_COLOR_VISITED);
		defColors.add(DEF_COLOR_FINISHED_AND_RELEVANT);
		defColors.add(DEF_COLOR_FINISHED_AND_NOT_RELEVANT);
		defColors.add(DEF_COLOR_PICKED);
		
		return defColors;
	}
	
	public static void restoreDefaultColors() {
		mSettingsColor.put(COLOR_UNVISITED, DEF_COLOR_UNVISITED);
		mSettingsColor.put(COLOR_ACTIVE, DEF_COLOR_ACTIVE);
		mSettingsColor.put(COLOR_VISITED, DEF_COLOR_VISITED);
		mSettingsColor.put(COLOR_FINISHED_AND_RELEVANT, DEF_COLOR_FINISHED_AND_RELEVANT);
		mSettingsColor.put(COLOR_FINISHED_AND_NOT_RELEVANT, DEF_COLOR_FINISHED_AND_NOT_RELEVANT);
		mSettingsColor.put(COLOR_PICKED, DEF_COLOR_PICKED);
	}
	
	public static void restoreDefaultKeys() {
		mSettingsKey.put(KEY_UNDIRECTED_EDGE, DEF_KEY_UNDIRECTED_EDGE);
		mSettingsKey.put(KEY_DIRECTED_EDGE, DEF_KEY_DIRECTED_EDGE);
		mSettingsKey.put(KEY_MULTIPLE_SELECT, DEF_KEY_MULTIPLE_SELECT);
		mSettingsKey.put(KEY_RECTANGULAR_SELECT, DEF_KEY_RECTANGULAR_SELECT);
	}
	
	public static void loadSettings() {
		mSettingsColor = FileOperator.getInstance(null).loadColorSettings(Settings.mColorKeyset);
		if(mSettingsColor == null) {
			System.out.println("color settings is null");
			mSettingsColor = new HashMap<String, String>();
			restoreDefaultColors();
		}
		System.out.println("color settings: "+ mSettingsColor);
		mSettingsKey = FileOperator.getInstance(null).loadKeySettings(Settings.mKeyKeyset);
		if(mSettingsKey == null) {
			mSettingsKey = new HashMap<String, Integer>();
			restoreDefaultKeys();
		}
		System.out.println("key settings: "+ mSettingsKey);
	}


	public static void saveSettings() {
		FileOperator.getInstance(null).saveSettings(mSettingsColor, mSettingsKey);
	}
}

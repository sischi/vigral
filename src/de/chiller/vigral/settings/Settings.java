package de.chiller.vigral.settings;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import de.chiller.vigral.menubar.FileOperator;

public class Settings {


	public static final String COLOR_UNVISITED = "COLOR_UNVISITED";
	public static final String COLOR_VISITED = "COLOR_VISITED";
	public static final String COLOR_ACTIVE = "COLOR_ACTIVE";
	public static final String COLOR_FINISHED_AND_RELEVANT = "COLOR_FINISHED_AND_RELEVANT";
	public static final String COLOR_FINISHED_AND_NOT_RELEVANT = "COLOR_FINISHED_AND_NOT_RELEVANT";
	public static final String COLOR_PICKED = "COLOR_PICKED";
	private static final String DEF_COLOR_UNVISITED = "#33B5E5";
	private static final String DEF_COLOR_VISITED = "#FF4444";
	private static final String DEF_COLOR_ACTIVE = "#FFBB33";
	private static final String DEF_COLOR_FINISHED_AND_RELEVANT = "#99CC00";
	private static final String DEF_COLOR_FINISHED_AND_NOT_RELEVANT = "#C4C4C4";
	private static final String DEF_COLOR_PICKED = "#F7FE2E";
	
	public static final String KEY_UNDIRECTED_EDGE = "KEY_UNDIRECTED";
	public static final String KEY_DIRECTED_EDGE = "KEY_DIRECTED_EDGE";
	public static final String KEY_MULTIPLE_SELECT = "KEY_MULTIPLE_SELECT";
	public static final String KEY_RECTANGULAR_SELECT = "KEY_RECTANGULAR_SELECT";
	private static final int DEF_KEY_UNDIRECTED_EDGE = KeyEvent.VK_U;
	private static final int DEF_KEY_DIRECTED_EDGE = KeyEvent.VK_D;
	private static final int DEF_KEY_MULTIPLE_SELECT = KeyEvent.VK_CONTROL;
	private static final int DEF_KEY_RECTANGULAR_SELECT = KeyEvent.VK_SHIFT;
	
	public static final String VIEW_WEIGHT = "WEIGHT";
	public static final String VIEW_MIN_CAPACITY = "MIN_CAPACITY";
	public static final String VIEW_MAX_CAPACITY = "MAX_CAPACITY";
	private static final boolean DEF_VIEW_WEIGHT = true;
	private static final boolean DEF_VIEW_MIN_CAPACITY = false;
	private static final boolean DEF_VIEW_MAX_CAPACITY = false;
	
	private HashMap<String, String> mColorSettings = new HashMap<String, String>();
	private HashMap<String, Integer> mKeySettings = new HashMap<String, Integer>();
	private HashMap<String, Boolean> mViewSettings = new HashMap<String, Boolean>();
	
	private static ArrayList<String> mColorKeyset = initColorKeyset();
	private static ArrayList<String> mKeyKeyset = initKeyKeyset();
	private static ArrayList<String> mViewKeyset = initViewKeyset();
	
	private static Settings mSettings = new Settings();
	
	private Settings() {
		loadSettings();
	}
	
	public static Settings getInstance() {
		return mSettings;
	}
	
	private static ArrayList<String> initKeyKeyset() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		keyset.add(KEY_UNDIRECTED_EDGE);
		keyset.add(KEY_DIRECTED_EDGE);
		keyset.add(KEY_MULTIPLE_SELECT);
		keyset.add(KEY_RECTANGULAR_SELECT);
		
		return keyset;
	}


	private static ArrayList<String> initViewKeyset() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		keyset.add(VIEW_WEIGHT);
		keyset.add(VIEW_MIN_CAPACITY);
		keyset.add(VIEW_MAX_CAPACITY);
		
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
	
	private void restoreDefaultView() {
		mViewSettings = new HashMap<String, Boolean>();
		mViewSettings.put(VIEW_WEIGHT, DEF_VIEW_WEIGHT);
		mViewSettings.put(VIEW_MIN_CAPACITY, DEF_VIEW_MIN_CAPACITY);
		mViewSettings.put(VIEW_MAX_CAPACITY, DEF_VIEW_MAX_CAPACITY);
	}
	
	public void restoreDefaultColors() {
		mColorSettings = new HashMap<String, String>();
		mColorSettings.put(COLOR_UNVISITED, DEF_COLOR_UNVISITED);
		mColorSettings.put(COLOR_ACTIVE, DEF_COLOR_ACTIVE);
		mColorSettings.put(COLOR_VISITED, DEF_COLOR_VISITED);
		mColorSettings.put(COLOR_FINISHED_AND_RELEVANT, DEF_COLOR_FINISHED_AND_RELEVANT);
		mColorSettings.put(COLOR_FINISHED_AND_NOT_RELEVANT, DEF_COLOR_FINISHED_AND_NOT_RELEVANT);
		mColorSettings.put(COLOR_PICKED, DEF_COLOR_PICKED);
	}
	
	public void restoreDefaultKeys() {
		mKeySettings = new HashMap<String, Integer>();
		mKeySettings.put(KEY_UNDIRECTED_EDGE, DEF_KEY_UNDIRECTED_EDGE);
		mKeySettings.put(KEY_DIRECTED_EDGE, DEF_KEY_DIRECTED_EDGE);
		mKeySettings.put(KEY_MULTIPLE_SELECT, DEF_KEY_MULTIPLE_SELECT);
		mKeySettings.put(KEY_RECTANGULAR_SELECT, DEF_KEY_RECTANGULAR_SELECT);
	}
	
	private void loadSettings() {
		mColorSettings = FileOperator.getInstance(null).loadColorSettings(Settings.mColorKeyset);
		if(mColorSettings == null) {
			System.out.println("color settings is null");
			restoreDefaultColors();
		}
		System.out.println("color settings: "+ mColorSettings);
		
		mKeySettings = FileOperator.getInstance(null).loadKeySettings(Settings.mKeyKeyset);
		if(mKeySettings == null) {
			restoreDefaultKeys();
		}
		System.out.println("key settings: "+ mKeySettings);
		
		mViewSettings = FileOperator.getInstance(null).loadViewSettings(mViewKeyset);
		if(mViewSettings == null) {
			restoreDefaultView();
		}
		System.out.println("view settings: "+ mViewSettings);
	}


	public void saveSettings() {
		FileOperator.getInstance(null).saveSettings(mColorSettings, mKeySettings, mViewSettings);
	}
	
	
	public String getColor(String whatColor) {
		return mColorSettings.get(whatColor);
	}
	
	public int getKey(String whatKey) {
		return mKeySettings.get(whatKey);
	}
	
	public boolean getView(String whatView) {
		return mViewSettings.get(whatView);
	}
	
	public void updateColorSetting(String whatColor, String val) {
		mColorSettings.put(whatColor, val);
	}
	
	public void updateKeySetting(String whatKey, int val) {
		mKeySettings.put(whatKey, val);
	}
	
	public void updateViewSetting(String whatView, boolean val) {
		mViewSettings.put(whatView, val);
	}
	
	public ArrayList<String> getColorKeySet() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		for(String val : mColorKeyset)
			keyset.add(val);
		
		return keyset;			
	}
	
	public ArrayList<String> getKeyKeySet() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		for(String val : mKeyKeyset)
			keyset.add(val);
		
		return keyset;			
	}
	
	public ArrayList<String> getViewKeySet() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		for(String val : mViewKeyset)
			keyset.add(val);
		
		return keyset;			
	}
	
	
	
}

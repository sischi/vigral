package de.chiller.vigral.settings;

import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import de.chiller.vigral.util.ErrorDialog;
import de.chiller.vigral.util.FileOperator;



/**
 * singleton class that is responsible for saving and loading the settings
 * @author Simon Schiller
 *
 */
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
	
	public static final String KEY_UNDIRECTED_EDGE = "KEY_UNDIRECTED_EDGE";
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
	
	public static final String LABEL_VERTEX = "LABEL_VERTEX";
	public static final String LABEL_EDGE = "LABEL_EDGE";
	private static final int DEF_LABEL_VERTEX = 12;
	private static final int DEF_LABEL_EDGE = 12;
	
	private HashMap<String, String> mColorSettings = new HashMap<String, String>();
	private HashMap<String, Integer> mKeySettings = new HashMap<String, Integer>();
	private HashMap<String, Boolean> mViewSettings = new HashMap<String, Boolean>();
	private HashMap<String, Integer> mLabelSettings = new HashMap<String, Integer>();
	
	private static ArrayList<String> mColorKeyset = initColorKeyset();
	private static ArrayList<String> mKeyKeyset = initKeyKeyset();
	private static ArrayList<String> mViewKeyset = initViewKeyset();
	private static ArrayList<String> mLabelKeyset= initLabelKeyset();
	
	private static Settings mSettings = null;
	
	private Settings() {
		loadSettings();
	}
	
	/**
	 * getter for the singleton instance
	 * @return returns the singleton instance
	 */
	public static Settings getInstance() {
		if(mSettings == null)
			mSettings = new Settings();
		return mSettings;
	}
	
	
	private static ArrayList<String> initLabelKeyset() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		keyset.add(LABEL_VERTEX);
		keyset.add(LABEL_EDGE);
		
		return keyset;
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
	
	/**
	 * restores the default color settings
	 */
	public void restoreDefaultColors() {
		mColorSettings = new HashMap<String, String>();
		mColorSettings.put(COLOR_UNVISITED, DEF_COLOR_UNVISITED);
		mColorSettings.put(COLOR_ACTIVE, DEF_COLOR_ACTIVE);
		mColorSettings.put(COLOR_VISITED, DEF_COLOR_VISITED);
		mColorSettings.put(COLOR_FINISHED_AND_RELEVANT, DEF_COLOR_FINISHED_AND_RELEVANT);
		mColorSettings.put(COLOR_FINISHED_AND_NOT_RELEVANT, DEF_COLOR_FINISHED_AND_NOT_RELEVANT);
		mColorSettings.put(COLOR_PICKED, DEF_COLOR_PICKED);
	}
	
	/**
	 * restores the default key settings
	 */
	public void restoreDefaultKeys() {
		mKeySettings = new HashMap<String, Integer>();
		mKeySettings.put(KEY_UNDIRECTED_EDGE, DEF_KEY_UNDIRECTED_EDGE);
		mKeySettings.put(KEY_DIRECTED_EDGE, DEF_KEY_DIRECTED_EDGE);
		mKeySettings.put(KEY_MULTIPLE_SELECT, DEF_KEY_MULTIPLE_SELECT);
		mKeySettings.put(KEY_RECTANGULAR_SELECT, DEF_KEY_RECTANGULAR_SELECT);
	}
	
	public void restoreDefaultLabels() {
		mLabelSettings = new HashMap<String, Integer>();
		mLabelSettings.put(LABEL_VERTEX, DEF_LABEL_VERTEX);
		mLabelSettings.put(LABEL_EDGE, DEF_LABEL_EDGE);
	}
	
	/**
	 * loads the settings
	 */
	private void loadSettings() {
		try {
			mColorSettings = FileOperator.getInstance().loadColorSettings(Settings.mColorKeyset);
		} catch(Exception e) {
			ErrorDialog.showQuickErrorDialog(null, "cannot load color settings", e);
			restoreDefaultColors();
		}
		
		try {
			mKeySettings = FileOperator.getInstance().loadKeySettings(Settings.mKeyKeyset);
		} catch(Exception e) {
			ErrorDialog.showQuickErrorDialog(null, "cannot load key settings", e);
			restoreDefaultKeys();
		}

		try {
			mViewSettings = FileOperator.getInstance().loadViewSettings(mViewKeyset);
		} catch(Exception e) {
			ErrorDialog.showQuickErrorDialog(null, "cannot load view settings", e);
			restoreDefaultView();
		}
		
		try {
			mLabelSettings = FileOperator.getInstance().loadLabelSettings(mLabelKeyset);
		} catch(Exception e) {
			ErrorDialog.showQuickErrorDialog(null, "cannot load label settings", e);
			restoreDefaultLabels();
		}
	}


	/**
	 * saves the settings to file
	 */
	public void saveSettings() {
		try {
			FileOperator.getInstance().saveSettings(mColorSettings, mKeySettings, mViewSettings, mLabelSettings);
		} catch(Exception e) {
			ErrorDialog.showQuickErrorDialog(null, "cannot save settings", e);
		}
	}
	
	/**
	 * returns the demanded color
	 * @param whatColor the color that should be returned
	 * @return returns the appropriate color
	 */
	public String getColor(String whatColor) {
		return mColorSettings.get(whatColor);
	}
	
	
	public int getLabelSize(String whatLabel) {
		return mLabelSettings.get(whatLabel);
	}
	
	/**
	 * returns the demanded key
	 * @param whatKey the key that should be returned
	 * @return returns the appropriate key
	 */
	public int getKey(String whatKey) {
		return mKeySettings.get(whatKey);
	}

	/**
	 * returns the demanded view setting
	 * @param whatView the view setting that should be returned
	 * @return returns the appropriate view setting
	 */
	public boolean getView(String whatView) {
		return mViewSettings.get(whatView);
	}
	
	/**
	 * updates color settings
	 * @param whatColor the color that should be updated
	 * @param val the new value for the color
	 */
	public void updateColorSetting(String whatColor, String val) {
		mColorSettings.put(whatColor, val);
	}
	
	public void updateLabelSetting(String whatLabel, int val) {
		mLabelSettings.put(whatLabel, val);
	}
	
	/**
	 * updates key settings
	 * @param whatKey the key that should be updated
	 * @param val the new value for the key
	 */
	public void updateKeySetting(String whatKey, int val) {
		mKeySettings.put(whatKey, val);
	}
	
	/**
	 * updates view settings
	 * @param whatView the view setting that should be updated
	 * @param val the new value of the view setting
	 */
	public void updateViewSetting(String whatView, boolean val) {
		mViewSettings.put(whatView, val);
	}
	
	/**
	 * 
	 * @return returns an arraylist of the available colors
	 */
	public ArrayList<String> getColorKeySet() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		for(String val : mColorKeyset)
			keyset.add(val);
		
		return keyset;			
	}
	
	/**
	 * 
	 * @return returns an arraylist of the available keys
	 */
	public ArrayList<String> getKeyKeySet() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		for(String val : mKeyKeyset)
			keyset.add(val);
		
		return keyset;			
	}
	
	/**
	 * 
	 * @return returns an arraylist of the available view settings
	 */
	public ArrayList<String> getViewKeySet() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		for(String val : mViewKeyset)
			keyset.add(val);
		
		return keyset;			
	}
	
	
	public ArrayList<String> getLabelKeySet() {
		ArrayList<String> keyset = new ArrayList<String>();
		
		for(String val : mLabelKeyset)
			keyset.add(val);
		
		return keyset;
	}
	
	
	
}

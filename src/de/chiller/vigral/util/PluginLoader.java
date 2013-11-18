package de.chiller.vigral.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import de.chiller.vigral.algorithm.AbstractAlgorithm;


/**
 * singleton class loads the algorithms as plugins
 * @author Simon Schiller
 *
 */
public class PluginLoader {

	private File mPluginDir;
	private static final PluginLoader mPluginLoader = new PluginLoader();
	
	private PluginLoader() {
		mPluginDir = new File(System.getProperty("user.dir") + File.separator + "plugins");
	}
	
	/**
	 * getter for the singleton instance
	 * @return the only instance of this class
	 */
	public static PluginLoader getInstance() {
		return mPluginLoader;
	}
	
	/**
	 * loads the plugins in the plugin dir that extends the AbstractAlgorithm
	 * @return returns the list of found algorithms
	 */
	public ArrayList<AbstractAlgorithm> loadPlugins() {
		
		if(!mPluginDir.exists()) {
			//System.out.println("plugin dir ("+ mPluginDir +") does not exist!");
			return null;
		}
			
		String[] files = mPluginDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".class");
			}
		});
		
		String[] classes = new String[files.length];
		for(int i = 0; i < files.length; i++)
			classes[i] = files[i].substring(0, files[i].length() - 6);
		
		if(files.length == 0)
			return null;
		
		URL url = null;
		try {
			url = mPluginDir.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		URL[] urls = new URL[]{url};
		ArrayList<AbstractAlgorithm> algorithms = new ArrayList<AbstractAlgorithm>();
		ClassLoader loader = new URLClassLoader(urls);
		for(String name : classes) {
			try {
				System.out.println("name: "+ name);
				Class clss = loader.loadClass(name);
				Object o = clss.newInstance();
				System.out.println("getclassname: "+ o.getClass().getName());
				System.out.println("getsuperclass: "+ o.getClass().getSuperclass().getName());
				AbstractAlgorithm algo = (AbstractAlgorithm) o;
				algorithms.add(algo);
			} catch(NoClassDefFoundError e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return algorithms;
	}
}

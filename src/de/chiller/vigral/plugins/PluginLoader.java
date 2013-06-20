package de.chiller.vigral.plugins;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import de.chiller.vigral.algorithm.AbstractAlgorithm;

public class PluginLoader {

	private File mPluginDir;
	private static final PluginLoader mPluginLoader = new PluginLoader();
	
	private PluginLoader() {
		mPluginDir = new File(System.getProperty("user.dir") + File.separator +"plugins"+ File.separator);
	}
	
	public static PluginLoader getInstance() {
		return mPluginLoader;
	}
	
	public ArrayList<AbstractAlgorithm> loadPlugins() {
		System.out.println("plugin dir: "+ mPluginDir);
		String[] files = mPluginDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".class");
			}
		});
		
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
		System.out.println("urls: "+ urls);
		ArrayList<AbstractAlgorithm> algorithms = new ArrayList<AbstractAlgorithm>();
		ClassLoader loader = new URLClassLoader(urls);
		
		try {
		for(String name : files) {
			AbstractAlgorithm algo = (AbstractAlgorithm) loader.loadClass("Dijkstra");
			if(algo instanceof AbstractAlgorithm)
				algorithms.add((AbstractAlgorithm) algo);
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return algorithms;
	}
}

package de.chiller.vigral.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarEntry;

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
			ErrorDialog.showErrorDialog(null, "malformed url", e);
			//e.printStackTrace();
			return null;
		}
		URL[] urls = new URL[]{url};
		ArrayList<AbstractAlgorithm> algorithms = new ArrayList<AbstractAlgorithm>();
		ClassLoader loader = new URLClassLoader(urls);
		for(String name : classes) {
			try {
				//System.out.println("name: "+ name);
				Class clss = loader.loadClass(name);
				Object o = clss.newInstance();
				//System.out.println("getclassname: "+ o.getClass().getName());
				//System.out.println("getsuperclass: "+ o.getClass().getSuperclass().getName());
				AbstractAlgorithm algo = (AbstractAlgorithm) o;
				algorithms.add(algo);
			} /*catch(NoClassDefFoundError e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}*/ catch (Exception e) {
				ErrorDialog.showErrorDialog(null, "cannot load plugin '" + name + "'", e);
			}
		}
		
		try {
			algorithms.addAll(loadPluginsJar());
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return algorithms;
	}
	
	
	
	public ArrayList<AbstractAlgorithm> loadPluginsJar() {
		
		if(!mPluginDir.exists()) {
			//System.out.println("plugin dir ("+ mPluginDir +") does not exist!");
			return null;
		}
			
		String[] jarFilenames = mPluginDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		
		String[] classNames = new String[jarFilenames.length];
		File[] jarFiles = new File[jarFilenames.length];
		for(int i = 0; i < jarFilenames.length; i++) {
			classNames[i] = jarFilenames[i].substring(0, jarFilenames[i].length() - 4);
			jarFiles[i] = new File(mPluginDir.getPath() + "/" + jarFilenames[i]);
		}
		
		if(jarFilenames.length == 0)
			return null;
		
		/*
		URL url = null;
		try {
			url = mPluginDir.toURI().toURL();
		} catch (MalformedURLException e) {
			ErrorDialog.showErrorDialog(null, "malformed url", e);
			//e.printStackTrace();
			return null;
		}
		//URL[] urls = new URL[]{url};
		URL[] urls;
		try {
			urls={new URL("jar:file:"+ url +"!/")};
		} catch (Exception e) {
			ErrorDialog.showErrorDialog(null, "cannot URL Array for '" + urls + "'", e);
		}
		*/
		ArrayList<AbstractAlgorithm> algorithms = new ArrayList<AbstractAlgorithm>();
		
		String className = "";
		for(int i = 0; i < jarFiles.length; i++) {
			try {
				className = classNames[i];
				URL fileURL = jarFiles[i].toURI().toURL();
				String jarURL = "jar:" + fileURL + "!/";
				URL urls[] = { new URL(jarURL) };
				ClassLoader loader = new URLClassLoader(urls);
				//System.out.println("name: "+ name);
				Class clss = loader.loadClass(className);
				Object o = clss.newInstance();
				//System.out.println("getclassname: "+ o.getClass().getName());
				//System.out.println("getsuperclass: "+ o.getClass().getSuperclass().getName());
				AbstractAlgorithm algo = (AbstractAlgorithm) o;
				algorithms.add(algo);
			} /*catch(NoClassDefFoundError e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}*/ catch (Exception e) {
				ErrorDialog.showErrorDialog(null, "cannot load plugin '" + className + "'", e);
			}
		}
		
		return algorithms;
	}
	
	
	
	
	
}

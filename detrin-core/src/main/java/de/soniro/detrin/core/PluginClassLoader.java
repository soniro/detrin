package de.soniro.detrin.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The Plugin-ClassLoader loads all classes in the pluginfolders and saves all implementing
 * classes for an interface-
 * 
 * @author Nina Rothenberg
 *
 */
public class PluginClassLoader extends URLClassLoader {

	private Map<Class<?>, List<Class<?>>> classesForInterface = new HashMap<Class<?>, List<Class<?>>>();
	
	public PluginClassLoader(String... pluginFolder) {
		super(getUrlsForJarsInPluginFolder(pluginFolder), ClassLoader.getSystemClassLoader());
		initClassesForInterface();
	}

	private static URL[] getUrlsForJarsInPluginFolder(String[] pluginFolders) {
		try {
			List<URL> urls = new ArrayList<URL>();
			for (String pluginFolder : pluginFolders) {
				File directory = new File(pluginFolder);
				if (!directory.exists()) {
					throw new RuntimeException("Der angegebene Plugin-Ordner '" + pluginFolder + "' existiert nicht.");
				}
				assert directory.isDirectory();
				for (File jarFile : directory.listFiles(new JarFileFilter())) {
					urls.add(jarFile.toURI().toURL());
				}
			}
			return urls.toArray(new URL[urls.size()]);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setClassesForInterface(JarFile jarFile) {
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while(jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			if(jarEntry.getName().endsWith(".class")) {
				String classname = jarEntry.getName();
				classname = classname.substring(0, classname.lastIndexOf("."));
				classname = classname.replace(File.separator, ".");
				classname = classname.replace("\\", ".");
				classname = classname.replace("/", ".");
				try {
					Class<?> currentClass = loadClass(classname);
					for (Class<?> currentInterface : currentClass.getInterfaces()) {
						if (!classesForInterface.containsKey(currentInterface)) {
							classesForInterface.put(currentInterface, new ArrayList<Class<?>>());
						}
						classesForInterface.get(currentInterface).add(currentClass);
					}
				} catch (Throwable e) {
					System.out.println(String.format("Ein(e) %s ist aufgetreten: %s", e.getClass().getName(), e.getMessage()));
				}
			}
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<Class> loadClassesForInterface(Class searchedInterface) {
		List<Class> result = new ArrayList<Class>();
		if (classesForInterface.containsKey(searchedInterface)) {
			result.addAll(classesForInterface.get(searchedInterface));
		} else {
			result = new ArrayList<Class>();
		}
		return result;
	}
	
	private void initClassesForInterface() {
		for (URL url : getURLs()) {
			try {				
				setClassesForInterface(new JarFile(new File(url.toURI())));
			} catch (URISyntaxException e) {
					
			} catch (IOException e) {
					
			}
		}
	}
	
}

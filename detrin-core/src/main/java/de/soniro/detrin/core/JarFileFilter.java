package de.soniro.detrin.core;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Accept jar-Files.
 * 
 * @author Nina Rothenberg
 *
 */
public class JarFileFilter implements FilenameFilter {
	
	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(".jar");
	}
	
}

package de.soniro.detrin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;

import de.soniro.detrin.model.DecisionTree;
import de.soniro.detrin.model.Properties;

/**
 * An interface for FileExports exporting a decision tree generation report.
 * 
 * @author Nina Rothenberg
 *
 */
public interface FileExport extends Extensionable {

	/**
	 * Writes a report.
	 * 
	 * @param file - target-File
	 * @param tree - decisiontree which should be printed
	 * @param properties - Properties which were used for generation
	 * @param image - BufferedImage of the DecisionTree.
	 * @param locale - language for the report
	 */
	void writeReport(File file, DecisionTree tree, Properties properties, BufferedImage image, Locale locale);
	
	/**
	 * Returns the File-Type for the rport.
	 * 
	 * @return file type
	 */
	String getFileEnding();
	
}

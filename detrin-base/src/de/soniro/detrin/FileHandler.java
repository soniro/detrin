package de.soniro.detrin;

import java.io.File;
import java.util.List;

import de.soniro.detrin.exception.InvalidInstanceException;
import de.soniro.detrin.model.Dataset;

/**
 * Interface for Fileimports.
 * The import datasets for the decisiontreegeneration. 
 * 
 * @author Nina Rothenberg
 *
 */
public interface FileHandler extends Extensionable {

	/**
	 * Returns a list of all handable files.
	 * 
	 * @return List<String>
	 */
	List<String> getHandableFileEndings();
	
	/**
	 * Transform the filecontents in a dataset.
	 * 
	 * @param file - input-File
	 * @return the created Dataset
	 * @throws InvalidInstanceException if an invalid instance ist in the dataset.
	 */
	Dataset handleFile(File file) throws InvalidInstanceException;
	
}

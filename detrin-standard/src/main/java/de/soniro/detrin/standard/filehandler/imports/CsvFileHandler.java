package de.soniro.detrin.standard.filehandler.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.soniro.detrin.FileHandler;
import de.soniro.detrin.exception.InvalidInstanceException;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Instance;
import de.soniro.detrin.model.NominalAttribute;
import de.soniro.detrin.model.NumericAttribute;
import de.soniro.detrin.standard.Messages;

/**
 * FileHandler for a CSV-Import.
 * 
 * @author Nina Rothenberg
 *
 */
public class CsvFileHandler implements FileHandler {

	public String[] attributes;
	
	@Override
	public List<String> getHandableFileEndings() {
		List<String> fileEndings = new ArrayList<String>();
		fileEndings.add(Messages.getString("CsvFileHandler.fileEnding"));
		return fileEndings;
	}

	@Override
	public Dataset handleFile(File file) throws InvalidInstanceException {
		try {
		Dataset dataSet = new Dataset();
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);
		String line;
		boolean first = true;
	    while ((line = reader.readLine()) != null) {
	    	if (first) {
	    		createAttributes(line, dataSet);
	    		first = false;
	    	} else {
	    		createInstance(line, dataSet);
	    	}
        }
		return dataSet;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void createAttributes(String line, Dataset dataSet) {
		String[] attributes = line.split(Messages.getString("CsvFileHandler.splitCharacter"));
		for (String attributeName : attributes) {
			if (attributeName.startsWith(Messages.getString("CsvFileHandler.valueWrapper")) && attributeName.endsWith(Messages.getString("CsvFileHandler.valueWrapper"))) {
				attributeName = attributeName.substring(1, attributeName.length() - 1);
			}
			dataSet.addAttribute(new NumericAttribute(attributeName));
		}
	}
	
	private void createInstance(String line, Dataset dataSet) throws InvalidInstanceException {
		Instance instance = new Instance();
		String[] values = line.split(Messages.getString("CsvFileHandler.splitCharacter"));
		for (int i = 0; i < values.length; i++) {
			Attribute<?> attribute = dataSet.getAttributes().get(i);
			String value = values[i];
			if (value.startsWith(Messages.getString("CsvFileHandler.valueWrapper")) && value.endsWith(Messages.getString("CsvFileHandler.valueWrapper"))) {
				value = value.substring(1, value.length() - 1);
			}
			if (attribute instanceof NominalAttribute) {
				((NominalAttribute)attribute).addPossibleValue(value);
				instance.put(attribute, value);
			} else if (attribute instanceof NumericAttribute) {
				try {
					((NumericAttribute) attribute).addPossibleValue(Double.parseDouble(value));
					instance.put(attribute, value);
				} catch (NumberFormatException e) {
					attribute = ((NumericAttribute) attribute).toNominalAttribute();
					((NominalAttribute)attribute).addPossibleValue(value);
					instance.put(attribute, value);
					dataSet.getAttributes().set(i, attribute);
				}
			}
		}
		dataSet.addInstance(instance);
	}

	@Override
	public String getLabel(Locale locale) {
		return Messages.getString("CsvFileHandler.label");
	}

}

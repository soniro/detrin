package de.soniro.detrin.gui.i18n;

/**
 * Enumeration of all available message ids.
 * 
 * @author Nina Rothenberg
 */
public enum Message {
	
	TITLE("title"),

	ERROR_MESSAGE_TITLE("errorMessage.title"),
	
	FILE("menu.file"),
	IMPORT("menu.import"),
	EXPORT("menu.export"),
	EXIT("menu.exit"),
	LANGUAGE("menu.language"),
	GERMAN("menu.german"),
	ENGLISH("menu.english"),

	ADD_FILE_HANDLER("import.fileHandler.added"),
	SELECT_FILE_HANDLER_TITLE("import.selectFileHandler.title"),
	SELECT_FILE_HANDLER_MESSAGE("import.selectFileHandler.message"),
	INVALID_FILE("import.invalidFile"),
	
	INSTANCES("dataPreparation.instances"),
	ATTRIBUTES("dataPreparation.attributes"),
	ACTIVATED("dataPreparation.activated"),
	NAME("dataPreparation.name"),
	VALUES("dataPreparation.values"),
	ATTRIBUTE_IS_ACTIVE("dataPreparation.attributeIsActive"),
	
	ALGORITHM("properties.algorithms"),
	TARGET_ATTRIBUTE("properties.targetAttribute"),
	SPLIT_CRITERION("properties.splitCriterion"),
	STOP_CRITERION("properties.stopCriterion"),
	PRUNING_METHOD("properties.pruningMethod"),
	MINIMAL_INSTANCE_COUNT("properties.minimalInstanceCount"),
	
	GENERATE_DECISION_TREE("properties.generateDecisionTree");
	
	String name;
	
	private Message(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

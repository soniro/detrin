package de.soniro.detrin.standard.filehandler.exports;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import de.soniro.detrin.FileExport;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Branch;
import de.soniro.detrin.model.DecisionTree;
import de.soniro.detrin.model.Instance;
import de.soniro.detrin.model.Leaf;
import de.soniro.detrin.model.Node;
import de.soniro.detrin.model.Properties;
import de.soniro.detrin.standard.Messages;

/**
 * Exports the whole information provided by the interface in a html-File.
 * 
 * @author Nina Rothenberg
 *
 */
public class HtmlExport implements FileExport {

	private String imageDirectoryName;
	
	@Override
	public String getFileEnding() {
		return Messages.getString("HtmlExport.fileEnding");
	}

	@Override
	public void writeReport(File file, DecisionTree tree, Properties properties, BufferedImage image, Locale locale) {
		try {
			File imageFile = writeImageFile(file, image);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.append(Messages.getString("HtmlExport.PREFACE"));
			fileWriter.append(Messages.getString("HtmlExport.HEADER"));
			writeInputs(properties, fileWriter, locale);
			fileWriter.append(String.format(Messages.getString("HtmlExport.IMG"), imageDirectoryName + File.separator + imageFile.getName())); //$NON-NLS-1$
			writeExplanation(tree, fileWriter, locale);			
			fileWriter.append(Messages.getString("HtmlExport.END_FILE")); //$NON-NLS-1$
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(Messages.getString("HtmlExport.EXCEPTION"), e); //$NON-NLS-1$
		}
	}
	
	private void writeInputs(Properties properties, FileWriter fileWriter, Locale locale) throws IOException {
		fileWriter.append(Messages.getString("HtmlExport.INPUT_HEADER"));
		fileWriter.append(Messages.getString("HtmlExport.DATASET_HEADER"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_WITH_BORDER"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_HEAD"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_ROW"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_HEAD_CELL"));
		fileWriter.append("Aktiv?");
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_HEAD_CELL"));
		for (Attribute<?> attribute : properties.getDataset().getAttributes()) {
			fileWriter.append(Messages.getString("HtmlExport.START_TABLE_HEAD_CELL"));
			fileWriter.append(attribute.getName());
			fileWriter.append(Messages.getString("HtmlExport.END_TABLE_HEAD_CELL"));
		}
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_HEAD"));
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_ROW"));
		for (Instance instance : properties.getDataset().getInstances()) {
			fileWriter.append(Messages.getString("HtmlExport.START_TABLE_ROW"));
			fileWriter.append(Messages.getString("HtmlExport.START_TABLE_CELL"));
			fileWriter.append("x");
			fileWriter.append(Messages.getString("HtmlExport.END_TABLE_CELL"));
			for (Attribute<?> attribute : properties.getDataset().getAttributes()) {
				fileWriter.append(Messages.getString("HtmlExport.START_TABLE_CELL"));
				fileWriter.append(instance.getValueForAttribute(attribute).toString());
				fileWriter.append(Messages.getString("HtmlExport.END_TABLE_CELL"));
			}
			fileWriter.append(Messages.getString("HtmlExport.END_TABLE_ROW"));	
		}
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE"));
		
		fileWriter.append(Messages.getString("HtmlExport.ATTRIBUTE_HEADER"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_WITH_BORDER"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_HEAD"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_ROW"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_HEAD_CELL"));
		fileWriter.append("Aktiv?");
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_HEAD_CELL"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_HEAD_CELL"));
		fileWriter.append("Name");
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_HEAD_CELL"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_HEAD_CELL"));
		fileWriter.append("M&ouml;gliche Werte");
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_HEAD_CELL"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE_HEAD_CELL"));
		fileWriter.append("Art der Gruppierung");
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_HEAD_CELL"));
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_HEAD"));
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE_ROW"));
		for (Attribute<?> attribute : properties.getDataset().getAttributes()) {
			fileWriter.append(Messages.getString("HtmlExport.START_TABLE_ROW"));
			fileWriter.append(Messages.getString("HtmlExport.START_TABLE_CELL"));
			fileWriter.append("x");
			fileWriter.append(Messages.getString("HtmlExport.END_TABLE_CELL"));
			fileWriter.append(Messages.getString("HtmlExport.START_TABLE_CELL"));
			fileWriter.append(attribute.getName());
			fileWriter.append(Messages.getString("HtmlExport.END_TABLE_CELL"));
			fileWriter.append(Messages.getString("HtmlExport.START_TABLE_CELL"));
			fileWriter.append(attribute.getPossibleValues().toString());
			fileWriter.append(Messages.getString("HtmlExport.END_TABLE_CELL"));
			fileWriter.append(Messages.getString("HtmlExport.START_TABLE_CELL"));
			fileWriter.append(attribute.getGroupingType().toString());
			fileWriter.append(Messages.getString("HtmlExport.END_TABLE_CELL"));
			fileWriter.append(Messages.getString("HtmlExport.END_TABLE_ROW"));	
		}
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE"));
		
		fileWriter.append(Messages.getString("HtmlExport.PROPERTIES_HEADER"));
		fileWriter.append(Messages.getString("HtmlExport.START_TABLE"));
		fileWriter.append(createLabelWithValue("HtmlExport.TARGET_ATTRIBUTE", properties.getTargetAttribute() == null ? "" : properties.getTargetAttribute().getName()));
		fileWriter.append(createLabelWithValue("HtmlExport.SPLIT_CRITERION", properties.getSplitCriterion() == null ? "" : properties.getSplitCriterion().getLabel(locale)));
		fileWriter.append(createLabelWithValue("HtmlExport.STOP_CRITERION", properties.getStopCriterion() == null ? "" : properties.getStopCriterion().getLabel(locale)));
		fileWriter.append(createLabelWithValue("HtmlExport.PRUNING_METHOD", properties.getPruningMethod() == null ? "" : properties.getPruningMethod().getLabel(locale)));
		fileWriter.append(createLabelWithValue("HtmlExport.MINIMAL_INSTANCE_COUNT", properties.getMinimalInstanceCount() == null ? "" : properties.getMinimalInstanceCount().toString()));
		fileWriter.append(Messages.getString("HtmlExport.END_TABLE"));
	}
	
	private String createLabelWithValue(String labelId, String value) {
		String result = Messages.getString("HtmlExport.START_TABLE_ROW");
		result += Messages.getString("HtmlExport.START_TABLE_CELL");
		result += Messages.getString(labelId);
		result += Messages.getString("HtmlExport.END_TABLE_CELL");
		result += Messages.getString("HtmlExport.START_TABLE_CELL");
		result += value;
		result += Messages.getString("HtmlExport.END_TABLE_CELL");
		result += Messages.getString("HtmlExport.END_TABLE_ROW");
		return  result;
	}
	
	private void writeExplanation(DecisionTree tree, FileWriter fileWriter, Locale locale) throws IOException {
		writeExplanation(tree, fileWriter, 2, locale);		
	}
	
	private void writeExplanation(DecisionTree tree, FileWriter fileWriter, int i, Locale locale) throws IOException {
		if (tree instanceof Node) {
			fileWriter.append(String.format(Messages.getString("HtmlExport.TITLE_NODE"), i, tree.getLabel()));
		} else if (tree instanceof Leaf) {
			fileWriter.append(String.format(Messages.getString("HtmlExport.TITLE_LEAF"), i, tree.getLabel()));
		}
		fileWriter.append(tree.getExplanation(locale) + Messages.getString("HtmlExport.NEW_LINE"));
		i++;
		if (tree instanceof Node) {
			for (Branch branch : ((Node) tree).getBranches()) {
				fileWriter.append(String.format(Messages.getString("HtmlExport.TITLE_BRANCH"), i, tree.getLabel()));
				fileWriter.append(branch.getExplanation(locale));
				writeExplanation(branch.getChild(), fileWriter, i, locale);
			}
		}
	}
	
	private File writeImageFile(File parentFile, BufferedImage image) throws IOException {
		File imagesDirectory = createFolderForImages(parentFile);
		File imageFile = new File(imagesDirectory.getAbsolutePath() + File.separator + Messages.getString("HtmlExport.imageName"));
		ImageIO.write(image, Messages.getString("HtmlExport.imageType"), imageFile);
		return imageFile;
	}
	
	private File createFolderForImages(File parentFile) {
		String fileNameWithoutFileType = parentFile.getName().split(Messages.getString("HtmlExport.splitCharacter"))[0];
		imageDirectoryName = fileNameWithoutFileType + Messages.getString("HtmlExport.imageDirectoryName");
		File directory = new File(parentFile.getAbsolutePath().replace(parentFile.getName(), imageDirectoryName));
		if (!directory.isDirectory()) directory.mkdirs();
		return directory;
	}

	@Override
	public String getLabel(Locale locale) {
		return Messages.getString("htmlExport.label");
	}

}

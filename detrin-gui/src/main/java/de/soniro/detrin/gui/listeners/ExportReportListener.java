package de.soniro.detrin.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.soniro.detrin.FileExport;
import de.soniro.detrin.core.DecisionTreeInducer;
import de.soniro.detrin.gui.DeTrInGui;
import de.soniro.detrin.gui.elements.OptionPane;
import de.soniro.detrin.gui.i18n.Message;
import de.soniro.detrin.gui.i18n.Messages;
import de.soniro.detrin.gui.panel.DecisionTreePanel;
import de.soniro.detrin.gui.panel.OptionPaneInput;
import de.soniro.detrin.gui.panel.PropertiesPanel;

/**
 * The file import listener.
 *
 * @author Nina Rothenberg
 */
public class ExportReportListener implements ActionListener {

	private static final Log LOGGER = LogFactory.getLog(ExportReportListener.class);

	private JFileChooser fileChooser;

	private Map<String, List<FileExport>> fileHandler;

	public void actionPerformed(ActionEvent arg0) {
		if (fileHandler == null) {
			initFileHandlers();
		}
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new ExportFileFilter());
		int returnVal = fileChooser.showSaveDialog(DeTrInGui.getInstance());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String[] splits = fileChooser.getSelectedFile().getName().split("\\.");
			if (splits.length == 0)	return;
			String fileEnding = splits[splits.length - 1];
			List<FileExport> fileHandlerList = fileHandler.get(fileEnding);
			if (fileHandlerList.size() == 1) {
				handleFile(fileHandlerList.get(0));
			} else if (fileHandlerList.size() > 1) {
				chooseFileExport(fileHandlerList);
			}
		}
	}

	private void chooseFileExport(List<FileExport> fileHandler) {
		List<OptionPaneInput<FileExport>> fileHandlerForOptionPane = new ArrayList<OptionPaneInput<FileExport>>();
		for (FileExport currentFileHandler : fileHandler) {
			fileHandlerForOptionPane.add(new OptionPaneInput<FileExport>(currentFileHandler.getLabel(Messages.getInstance().getLocale()), currentFileHandler));
		}
		FileExport selectedFileHandler = OptionPane.showComboBoxDialogAndGetChoice(Message.SELECT_FILE_HANDLER_TITLE,
				Message.SELECT_FILE_HANDLER_MESSAGE, fileHandlerForOptionPane, fileHandlerForOptionPane.get(0)).getValue();
		handleFile(selectedFileHandler);
	}

	private void handleFile(FileExport fileHandler) {
		BufferedImage image = DecisionTreePanel.getInstance().getImageForExport();
		fileHandler.writeReport(fileChooser.getSelectedFile(), DeTrInGui.getInstance().getCurrentTree(), PropertiesPanel.getInstance().getPropertiesOfCurrentTree(), image, null);
	}

	private void initFileHandlers() {
		fileHandler = new HashMap<String, List<FileExport>>();
		List<FileExport> fileHandlerList = new DecisionTreeInducer().getAllPossibleInstances(FileExport.class);
		for (FileExport currentFileHandler : fileHandlerList) {
			String fileEnding = currentFileHandler.getFileEnding();
			if (!fileHandler.containsKey(fileEnding)) {
				fileHandler.put(fileEnding, new ArrayList<FileExport>());
			}
			fileHandler.get(fileEnding).add(currentFileHandler);
			LOGGER.debug(Messages.getInstance().getLabel(Message.ADD_FILE_HANDLER, fileEnding, fileHandler.get(fileEnding).size()));
		}
	}

	class ExportFileFilter extends FileFilter {

		@Override
		public String getDescription() {
			String description = "";
			for (String fileEnding : fileHandler.keySet()) {
				description += fileEnding.toLowerCase() + ", ";
			}
			return description.substring(0, description.length() - 2);
		}

		@Override
		public boolean accept(File f) {
			if (f.isFile()) {
				for (String fileEnding : fileHandler.keySet()) {
					if (f.getName().endsWith("." + fileEnding)) {
						return true;
					}
				}
			}
			return f.isDirectory();
		}
	}

}

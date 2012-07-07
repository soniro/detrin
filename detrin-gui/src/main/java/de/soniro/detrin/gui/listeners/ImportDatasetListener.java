package de.soniro.detrin.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.soniro.detrin.FileHandler;
import de.soniro.detrin.core.DecisionTreeInducer;
import de.soniro.detrin.exception.InvalidInstanceException;
import de.soniro.detrin.gui.DeTrInGui;
import de.soniro.detrin.gui.elements.OptionPane;
import de.soniro.detrin.gui.i18n.Message;
import de.soniro.detrin.gui.i18n.Messages;
import de.soniro.detrin.gui.panel.OptionPaneInput;
import de.soniro.detrin.model.Dataset;

/**
 * The file import listener.
 *
 * @author Nina Rothenberg
 */
public class ImportDatasetListener implements ActionListener {

	private static final Log LOGGER = LogFactory.getLog(ImportDatasetListener.class);

	private Map<String, List<FileHandler>> fileHandler;
	private JFileChooser fileChooser;

	public void actionPerformed(ActionEvent arg0) {
		importFile();
	}

	public void importFile() {
		if (fileHandler == null) {
			fileHandler = new HashMap<String, List<FileHandler>>();
			List<FileHandler> fileHandlerList = new DecisionTreeInducer().getAllPossibleInstances(FileHandler.class);
			for (FileHandler currentFileHandler : fileHandlerList) {
				for (String fileEnding : currentFileHandler
						.getHandableFileEndings()) {
					if (!fileHandler.containsKey(fileEnding)) {
						fileHandler.put(fileEnding,
								new ArrayList<FileHandler>());
					}
					fileHandler.get(fileEnding).add(currentFileHandler);
					LOGGER.debug(Messages.getInstance().getLabel(Message.ADD_FILE_HANDLER, fileEnding, fileHandler.get(fileEnding).size()));
				}
			}
		}
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new ImportFileFilter());
		int returnVal = fileChooser.showOpenDialog(DeTrInGui.getInstance());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String[] splits = fileChooser.getSelectedFile().getName().split("\\.");
			if (splits.length == 0)	return;
			String fileEnding = splits[splits.length - 1];
			List<FileHandler> fileHandlerList = fileHandler.get(fileEnding);
			if (fileHandlerList.size() == 1) {
				handleFile(fileHandlerList.get(0));
			} else if (fileHandlerList.size() > 1) {
				chooseFileHandler(fileHandlerList);
			}
		}
	}

	private void handleFile(FileHandler fileHandler) {
		try {
			Dataset dataset = fileHandler.handleFile(fileChooser.getSelectedFile());
			DeTrInGui.getInstance().updateDataset(dataset);
		} catch (InvalidInstanceException e) {
			LOGGER.error(e.getMessage(), e);
			OptionPane.showErrorMessage(Message.INVALID_FILE, e);
		}
	}

	private void chooseFileHandler(List<FileHandler> fileHandler) {
		List<OptionPaneInput<FileHandler>> fileHandlerForOptionPane = new ArrayList<OptionPaneInput<FileHandler>>();
		for (FileHandler currentFileHandler : fileHandler) {
			fileHandlerForOptionPane.add(new OptionPaneInput<FileHandler>(currentFileHandler.getLabel(Messages.getInstance().getLocale()), currentFileHandler));
		}
		FileHandler selectedFileHandler = OptionPane.showComboBoxDialogAndGetChoice(Message.SELECT_FILE_HANDLER_TITLE,
				Message.SELECT_FILE_HANDLER_MESSAGE, fileHandlerForOptionPane, fileHandlerForOptionPane.get(0)).getValue();
		handleFile(selectedFileHandler);
	}

	class ImportFileFilter extends FileFilter {

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

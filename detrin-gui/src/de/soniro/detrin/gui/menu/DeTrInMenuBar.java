package de.soniro.detrin.gui.menu;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.Locale;

import de.soniro.detrin.gui.i18n.Message;
import de.soniro.detrin.gui.i18n.Messages;
import de.soniro.detrin.gui.i18n.SwitchToLocaleListener;
import de.soniro.detrin.gui.listeners.CloseApplicationListener;
import de.soniro.detrin.gui.listeners.ExportReportListener;
import de.soniro.detrin.gui.listeners.ImportDatasetListener;

/**
 * The DeTrIn menu bar containing all menu items.
 * 
 * @author Nina Rothenberg
 */
public class DeTrInMenuBar extends MenuBar {
	
	private static final long serialVersionUID = -3484139410287067571L;
	
	private Menu file = new Menu(Messages.getInstance().getLabel(Message.FILE));
	private MenuItem importDataSet = new MenuItem(Messages.getInstance().getLabel(Message.IMPORT));
	private MenuItem exportReport = new MenuItem(Messages.getInstance().getLabel(Message.EXPORT));
	private MenuItem exit = new MenuItem(Messages.getInstance().getLabel(Message.EXIT));
	private Menu language = new Menu(Messages.getInstance().getLabel(Message.LANGUAGE));
	private MenuItem german = new MenuItem(Messages.getInstance().getLabel(Message.GERMAN));
	private MenuItem english = new MenuItem(Messages.getInstance().getLabel(Message.ENGLISH));
	
	public DeTrInMenuBar() {
		createMenu();
		addMenuListener();
	}
	
	private void createMenu() {
		file.add(importDataSet);
		file.add(exportReport);
		file.addSeparator();
		file.add(exit);
		
		language.add(german);
		language.add(english);
		
		add(file);
		add(language);
	}
	
	public void updateLabels() {
		file.setLabel(Messages.getInstance().getLabel(Message.FILE));
		importDataSet.setLabel(Messages.getInstance().getLabel(Message.IMPORT));
		exportReport.setLabel(Messages.getInstance().getLabel(Message.EXPORT));
		exit.setLabel(Messages.getInstance().getLabel(Message.EXIT));
		language.setLabel(Messages.getInstance().getLabel(Message.LANGUAGE));
		german.setLabel(Messages.getInstance().getLabel(Message.GERMAN));
		english.setLabel(Messages.getInstance().getLabel(Message.ENGLISH));
	}
	
	private void addMenuListener() {
		german.addActionListener(new SwitchToLocaleListener(Locale.GERMAN));
		english.addActionListener(new SwitchToLocaleListener(Locale.ENGLISH));
		exit.addActionListener(new CloseApplicationListener());
		importDataSet.addActionListener(new ImportDatasetListener());
		exportReport.addActionListener(new ExportReportListener());
	}
	
}

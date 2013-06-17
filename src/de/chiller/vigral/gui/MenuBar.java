package de.chiller.vigral.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {
	
	private JFrame mMainWindow;
	
	private ActionListener onExit = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			mMainWindow.dispose();
		}
	};
	
	private ActionListener onSave = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	};
	
	public MenuBar(JFrame mainWindow) {
		super();
		
		mMainWindow = mainWindow;
		
		JMenu fileMenu = new JMenu("File");
		JMenu pluginMenu = new JMenu("PlugIn");
		JMenu helpMenu = new JMenu("Help");
		
		JMenuItem file_new = new JMenuItem("New");
		JMenuItem file_open = new JMenuItem("Open");
		JMenuItem file_save = new JMenuItem("Save");
		JMenuItem file_exit = new JMenuItem("Exit");
		
		fileMenu.add(file_new);
		fileMenu.add(file_open);
		fileMenu.add(file_save);
		fileMenu.addSeparator();
		fileMenu.add(file_exit);
		
		add(fileMenu);
		add(pluginMenu);
		add(helpMenu);
		
		file_exit.addActionListener(onExit);
	}
}

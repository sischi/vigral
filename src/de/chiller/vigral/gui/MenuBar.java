package de.chiller.vigral.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.graph.Vertex;
import de.chiller.vigral.menubar.FileOperator;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class MenuBar extends JMenuBar {
	
	private JFrame mMainWindow;
	private final Graph mGraph;
	
	private ActionListener onExit = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			mMainWindow.dispose();
		}
	};
	
	private ActionListener onSave = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			FileOperator fo = new FileOperator(mMainWindow);
			fo.saveGraphToFile(mGraph);
		}
	};
	
	private ActionListener onOpen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			FileOperator fo = new FileOperator(mMainWindow);
			Graph g = fo.readGraphFromFile();	
			if(g != null)
				((VigralGUI) mMainWindow).getGraphBuilder().setGraph(g);
			
			// TODO think about behaviour of opening in visualisation mode
		}
	};
	
	public MenuBar(JFrame mainWindow, Graph g) {
		super();
		
		mMainWindow = mainWindow;
		mGraph = g;
		
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
		file_save.addActionListener(onSave);
		file_open.addActionListener(onOpen);
	}
}

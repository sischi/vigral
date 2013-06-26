package de.chiller.vigral.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.graph.Vertex;
import de.chiller.vigral.menubar.FileOperator;

public class MenuBar extends JMenuBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ActionListener onExit = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			VigralGUI.getInstance().dispose();
		}
	};
	
	private ActionListener onSave = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			FileOperator fo = FileOperator.getInstance(VigralGUI.getInstance());
			fo.saveGraphToFile(VigralGUI.getInstance().getGraphBuilder().getGraph());
		}
	};
	
	private ActionListener onOpen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			FileOperator fo = FileOperator.getInstance(VigralGUI.getInstance());
			Graph g = fo.readGraphFromFile();
			if(g != null)
				VigralGUI.getInstance().getGraphBuilder().setGraph(g);
			
			// TODO think about behaviour of opening in visualisation mode
		}
	};
	
	private ActionListener onNew = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice = JOptionPane.showConfirmDialog(VigralGUI.getInstance(), "Save before Closing", "Do you want to save your work?", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (choice) {
			case JOptionPane.CANCEL_OPTION:
				break;
			case JOptionPane.NO_OPTION:
				VigralGUI.getInstance().getGraphBuilder().resetGraph();
				Vertex.VertexFactory.resetIdCounter();
				Edge.EdgeFactory.resetIdCounter();
				break;
			case JOptionPane.YES_OPTION:
				FileOperator fo = FileOperator.getInstance(VigralGUI.getInstance());
				fo.saveGraphToFile(VigralGUI.getInstance().getGraphBuilder().getGraph());
				VigralGUI.getInstance().getGraphBuilder().resetGraph();
				Vertex.VertexFactory.resetIdCounter();
				Edge.EdgeFactory.resetIdCounter();
				break;
			default:
				break;
			}
		}
	};
	
	
	private ActionListener onPluginsReload = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			VigralGUI.getInstance().updateAlgorithmBox();
		}
	};
	
	public MenuBar(VigralGUI mainWindow) {
		super();
		
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
		
		JMenuItem plugins_reload = new JMenuItem("Reload");
		
		pluginMenu.add(plugins_reload);
		
		add(fileMenu);
		add(pluginMenu);
		add(helpMenu);
		
		file_exit.addActionListener(onExit);
		file_save.addActionListener(onSave);
		file_open.addActionListener(onOpen);
		file_new.addActionListener(onNew);
		
		plugins_reload.addActionListener(onPluginsReload);
	}
}

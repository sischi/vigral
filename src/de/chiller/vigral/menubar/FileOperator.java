package de.chiller.vigral.menubar;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import de.chiller.vigral.graph.Graph;

public class FileOperator {
	
	private JFrame mParentFrame;
	private File mFilePath;
	
	
	public FileOperator(JFrame parent) {
		mParentFrame = parent;
	}
	
	public boolean saveGraphToFile(Graph g) {
		if(!showSaveFileDialog()) {
			System.out.println("File has not been saved!!");
		}
		else {
			System.out.println("The file would have been saved now!");
		}
		
		return false;
	}
	
	public boolean readGraphFromFile() {
		return false;
	}
	
	
	private boolean showSaveFileDialog() {
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

		int retVal = fc.showSaveDialog(mParentFrame);
		if(retVal == JFileChooser.CANCEL_OPTION) {
			System.out.println("The operation has been canceled");
			return false;
		}
		File file = fc.getSelectedFile();
		String path = file.getPath();
		System.out.println("chosen filename: "+ path);
		return true;
	}
	
	
	
}

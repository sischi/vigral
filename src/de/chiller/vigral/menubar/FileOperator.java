package de.chiller.vigral.menubar;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.graph.Vertex;

public class FileOperator {
	
	private final static int OPEN_DIALOG = 0;
	private final static int SAVE_DIALOG = 1;
	
	private JFrame mParentFrame;
	private File mFile;
	
	public FileOperator(JFrame parent) {
		mParentFrame = parent;
	}
	
	
	public boolean saveGraphToFile(final Graph g) {
		if(!showFileDialog(SAVE_DIALOG)) {
			System.out.println("File has not been saved!!");
			return false;
		}
		
		
		if(!writeToFile(g)) {
			System.out.println("The file was NOT saved due to an error that occured!");
			return false;
		}
		
		System.out.println("The file was saved successfully!");	
		return true;
	}
	
	public Graph readGraphFromFile() {
		if(!showFileDialog(OPEN_DIALOG)) {
			System.out.println("File was not loaded!!");
			return null;
		}
		
		// TODO unzip the two files
				
		List<String[]> strVertices;
		List<String[]> strEdges;
		
		try {
			
			CSVReader reader = new CSVReader(new FileReader(mFile.getParent() + mFile.separator +"Vertices"));
			strVertices = reader.readAll();
			reader.close();
			
			reader = new CSVReader(new FileReader(mFile.getParent() + mFile.separator +"Edges"));
			strEdges = reader.readAll();
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		Graph graph = Graph.parseGraph(strVertices, strEdges);
		
		return graph;
	}
	
	
	private boolean showFileDialog(int option) {
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

		int retVal;
		if(option == OPEN_DIALOG)
			retVal = fc.showOpenDialog(mParentFrame);
		else
			retVal = fc.showSaveDialog(mParentFrame);
		
		if(retVal == JFileChooser.CANCEL_OPTION) {
			System.out.println("The operation has been canceled");
			return false;
		}
		mFile = fc.getSelectedFile();
		System.out.println("chosen filename: "+ mFile);
		System.out.println("the path seperator: '"+ mFile.separator +"'");
		return true;
	}
	
	
	private boolean writeToFile(final Graph g) {
		
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(mFile.getParent() + mFile.separator +"Vertices"));
			for(Vertex v : g.getVertices())
				writer.writeNext(v.toStringArray());
			writer.close();
			
			writer = new CSVWriter(new FileWriter(mFile.getParent() + mFile.separator +"Edges"));
			for(Edge e : g.getEdges())
				writer.writeNext(e.toStringArray());
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// TODO make one file out of the two (zipping?)
		
		return true;
	}
	
}

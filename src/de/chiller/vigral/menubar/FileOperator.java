package de.chiller.vigral.menubar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import au.com.bytecode.opencsv.CSVWriter;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.graph.Vertex;

import edu.uci.ics.jung.graph.SparseMultigraph;

public class FileOperator {
	
	private JFrame mParentFrame;
	private File mFile;
	
	public FileOperator(JFrame parent) {
		mParentFrame = parent;
	}
	
	
	public boolean saveGraphToFile(final SparseMultigraph<Vertex, Edge> g) {
		if(!showSaveFileDialog()) {
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
	
	public SparseMultigraph<Vertex, Edge> readGraphFromFile() {
		SparseMultigraph<Vertex, Edge> graph = new SparseMultigraph<Vertex, Edge>();
		
		
		
		
		return null;
	}
	
	
	private boolean showSaveFileDialog() {
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

		int retVal = fc.showSaveDialog(mParentFrame);
		if(retVal == JFileChooser.CANCEL_OPTION) {
			System.out.println("The operation has been canceled");
			return false;
		}
		mFile = fc.getSelectedFile();
		String path = mFile.getPath();
		System.out.println("chosen filename: "+ path);
		return true;
	}
	
	
	private boolean writeToFile(final SparseMultigraph<Vertex, Edge> g) {
		
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(mFile.getParent() +"\\Vertices"));
			for(Vertex v : g.getVertices())
				writer.writeNext(v.toStringArray());
			writer.close();
			
			writer = new CSVWriter(new FileWriter(mFile.getParent() +"\\Edges"));
			for(Edge e : g.getEdges())
				writer.writeNext(e.toStringArray());
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}

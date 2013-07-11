package de.chiller.vigral.menubar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
	
	private static JFrame mParentFrame;
	private static FileOperator mFileOperator = new FileOperator();
	
	private File mFile;
	private String mDialogPath = System.getProperty("user.dir");
	private ArrayList<File> mFileList = new ArrayList<File>();
	
	
	public static FileOperator getInstance(JFrame parent) {
		mParentFrame = parent;
		return mFileOperator;
	}
	
	private FileOperator() {}
	
	
	public boolean saveGraphToFile(final Graph g) {
		if(!showFileDialog(SAVE_DIALOG)) {
			System.out.println("File has NOT been saved!!");
			return false;
		}
		System.out.println("got a filename");
		
		if(!writeToFile(g)) {
			System.out.println("The file was NOT saved due to an error that occured!");
			return false;
		}
		System.out.println("temp files have been created");
		
		if(!zipFiles()) {
			System.out.println("The files have NOT been zipped!");
			return false;
		}
		System.out.println("temp files have been zipped");
		
		if(!deleteTemporaryFiles()) {
			System.out.println("The temp files have NOT been deleted");
		}
		System.out.println("temp files have been deleted");
		
		
		System.out.println("The file was saved successfully!");	
		return true;
	}
	
	public Graph readGraphFromFile() {
		if(!showFileDialog(OPEN_DIALOG)) {
			System.out.println("File was NOT loaded!!");
			return null;
		}
		System.out.println("Got filename");
		
		if(!unzipFiles()) {
			System.out.println("The files have NOT been unzipped!");
			return null;
		}
		System.out.println("The files have been unzipped");
		
		Graph graph = parseGraphFromFiles();
		if(graph == null) {
			System.out.println("the graph could NOT be parsed");
			return null;
		}
		System.out.println("the graph has been parsed");
		
		if(!deleteTemporaryFiles()) {
			System.out.println("the temp files have NOT been deleted");
			return graph;
		}
		System.out.println("the temp files have been deleted");
		
		return graph;
	}
	
	
	private boolean showFileDialog(int option) {
		JFileChooser fc = new JFileChooser(mDialogPath);

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
		mDialogPath = mFile.getParent();
		mFileList.clear();
		mFileList.add(new File(mFile.getParent() + File.separator +"Vertices"));
		mFileList.add(new File(mFile.getParent() + File.separator +"Edges"));
		System.out.println("chosen filename: "+ mFile);
		System.out.println("the path seperator: '"+ File.separator +"'");
		return true;
	}
	
	
	private boolean writeToFile(final Graph g) {
		
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(mFileList.get(0)));
			for(Vertex v : g.getVertices())
				writer.writeNext(v.toStringArray());
			writer.close();
			
			writer = new CSVWriter(new FileWriter(mFileList.get(1)));
			for(Edge e : g.getEdges())
				writer.writeNext(e.toStringArray());
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	private boolean zipFiles() {
		
		byte[] buffer = new byte[1024];
		
		try {
			
			FileOutputStream fos = new FileOutputStream(mFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			for(File file : mFileList) {
			
				ZipEntry entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);
				
				FileInputStream fis = new FileInputStream(file);
				
				int length;
				while((length = fis.read(buffer)) > 0)
					zos.write(buffer, 0, length);
			
				fis.close();
			}
			
			zos.closeEntry();
			zos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	private boolean unzipFiles() {
		
		byte[] buffer = new byte[1024];
		
		try {
			
			ZipInputStream zis = new ZipInputStream(new FileInputStream(mFile));
			ZipEntry entry = zis.getNextEntry();
			
			while(entry != null) {
				
				String filename = entry.getName();
				File f = new File(mFile.getParent() + File.separator + filename);
				
				FileOutputStream fos = new FileOutputStream(f);
				
				int length;
				while((length = zis.read(buffer)) > 0)
					fos.write(buffer, 0, length);
				
				fos.close();
				entry = zis.getNextEntry();
			}
			
			zis.closeEntry();
			zis.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	
	private boolean deleteTemporaryFiles() {
		
		for(File file : mFileList) {
			
			if(file.exists())
				file.delete();
		}
		
		return true;
	}
	
	private Graph parseGraphFromFiles() {
		List<String[]> strVertices;
		List<String[]> strEdges;
		
		Graph graph = null;
		try {
			
			for(File file : mFileList) {
				if(!file.exists()) {
					System.out.println("File '"+ file.getName() +"' does not exist in zip container!");
					return null;
				}
			}
			
			CSVReader reader;
			
			reader = new CSVReader(new FileReader(mFileList.get(0)));
			strVertices = reader.readAll();
			reader.close();

			reader = new CSVReader(new FileReader(mFileList.get(1)));
			strEdges = reader.readAll();
			reader.close();
			
			graph = Graph.parseGraph(strVertices, strEdges);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		 
		 return graph;
	}
	
	
}

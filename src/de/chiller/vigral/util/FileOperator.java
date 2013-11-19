package de.chiller.vigral.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.chiller.vigral.VigralGUI;
import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.graph.Vertex;



/**
 * this class is responsible for writing and reading files
 * @author Simon Schiller
 *
 */
public class FileOperator {
	
	private final static int OPEN_DIALOG = 0;
	private final static int SAVE_DIALOG = 1;
	
	private static JFrame mParentFrame = VigralGUI.getInstance();
	private static FileOperator mFileOperator = new FileOperator();
	
	private File mFile;
	private String mDialogPath = System.getProperty("user.dir");
	private String mSettingsFile = "config.xml";
	private ArrayList<File> mFileList = new ArrayList<File>();
	
	/**
	 * singleton getter for the instance
	 * @return
	 */
	public static FileOperator getInstance() {
		return mFileOperator;
	}
	
	private FileOperator() {}
	
	/**
	 * shows a file dialog and saves the graph to the choosen file
	 * @param g the graph to save
	 * @return returns true if no errors appeared and false otherwise
	 */
	public boolean saveGraphToFile(final Graph g) {
		if(!showFileDialog(SAVE_DIALOG)) {
			System.out.println("File has NOT been saved!!");
			return false;
		}
		
		try {
			
			writeToFile(g);
			zipFiles();
			if(!deleteTemporaryFiles()) {
				System.out.println("The temp files could NOT be deleted");
			}
			System.out.println("file successfully saved!");
			return true;
			
		} catch(Exception e) {
			ErrorDialog.showErrorDialog(null, "The graph could not be saved", e);
			if(!deleteTemporaryFiles()) {
				System.out.println("The temp files could NOT be deleted");
			}
			return false;
		}

	}
	
	/**
	 * reads and parses a graph from a file
	 * @return returns the loaded graph or null, if an error occured
	 */
	public Graph readGraphFromFile() {
		if(!showFileDialog(OPEN_DIALOG)) {
			System.out.println("File was NOT loaded!!");
			return null;
		}
		
		
		try {
			Vertex.VertexFactory.getInstance().backupID();
			Edge.EdgeFactory.getInstance().backupID();
			unzipFiles();
			Graph graph = parseGraphFromFiles();
			if(!deleteTemporaryFiles()) {
				System.out.println("the temp files have NOT been deleted");
			}
			return graph;
			
		} catch(Exception e) {
			ErrorDialog.showErrorDialog(null, "The graph could not be loaded", e);
			Vertex.VertexFactory.getInstance().restoreID();
			Edge.EdgeFactory.getInstance().restoreID();
			if(!deleteTemporaryFiles()) {
				System.out.println("the temp files have NOT been deleted");
			}
			return null;
		}
		
	}
	
	
	private boolean showFileDialog(int option) {
		JFileChooser fc = new JFileChooser(mDialogPath);

		int retVal;
		if(option == OPEN_DIALOG)
			retVal = fc.showOpenDialog(mParentFrame);
		else
			retVal = fc.showSaveDialog(mParentFrame);
		
		if(retVal == JFileChooser.CANCEL_OPTION)
			return false;
		
		mFile = fc.getSelectedFile();
		mDialogPath = mFile.getParent();
		mFileList.clear();
		mFileList.add(new File(mFile.getParent() + File.separator +"Vertices"));
		mFileList.add(new File(mFile.getParent() + File.separator +"Edges"));
		return true;
	}
	
	
	private boolean writeToFile(final Graph g) throws IOException {
		
		CSVWriter writer = new CSVWriter(new FileWriter(mFileList.get(0)));
		for(Vertex v : g.getVertices())
			writer.writeNext(v.toStringArray());
		writer.close();
		
		writer = new CSVWriter(new FileWriter(mFileList.get(1)));
		for(Edge e : g.getEdges())
			writer.writeNext(e.toStringArray());
		writer.close();
		
		return true;
	}
	
	
	private boolean zipFiles() throws IOException {
		
		byte[] buffer = new byte[1024];
		
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
			
		
		return true;
	}
	
	
	private boolean unzipFiles() throws IOException {
		
		byte[] buffer = new byte[1024];
		

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
		
		return true;
	}
	
	
	
	private boolean deleteTemporaryFiles() {
		
		for(File file : mFileList) {
			
			if(file.exists())
				file.delete();
		}
		
		return true;
	}
	
	private Graph parseGraphFromFiles() throws IOException {
		List<String[]> strVertices;
		List<String[]> strEdges;
		
		Graph graph = null;

		for(File file : mFileList)
			if(!file.exists()) { return null; }
		
		CSVReader reader;
		
		reader = new CSVReader(new FileReader(mFileList.get(0)));
		strVertices = reader.readAll();
		reader.close();

		reader = new CSVReader(new FileReader(mFileList.get(1)));
		strEdges = reader.readAll();
		reader.close();
		
		graph = Graph.parseGraph(strVertices, strEdges);
			

		return graph;
	}
	
	
	/**
	 * saves the settings to xml file
	 * @param colors the color settings to save
	 * @param keys the key settings to save
	 * @param props the view settings to save
	 * @return returns true if no error occurred or false otherwise
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	public boolean saveSettings(HashMap<String, String> colors, HashMap<String, Integer> keys, HashMap<String, Boolean> props, HashMap<String, Integer> labels) 
			throws ParserConfigurationException, TransformerException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root element
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("settings");
		doc.appendChild(rootElement);

		// color element
		Element colorElement = doc.createElement("colors");
		rootElement.appendChild(colorElement);

		for (String key : colors.keySet()) {
			Attr attr = doc.createAttribute(key);
			attr.setValue(colors.get(key));
			colorElement.setAttributeNode(attr);
		}

		// key element
		Element keyElement = doc.createElement("keys");
		rootElement.appendChild(keyElement);

		for (String key : keys.keySet()) {
			Attr attr = doc.createAttribute(key);
			attr.setValue("" + keys.get(key));
			keyElement.setAttributeNode(attr);
		}
		
		
		// view element
		Element viewElement = doc.createElement("view");
		rootElement.appendChild(viewElement);

		for (String key : props.keySet()) {
			Attr attr = doc.createAttribute(key);
			attr.setValue("" + props.get(key));
			viewElement.setAttributeNode(attr);
		}
		
		
		// label element
		Element labelElement = doc.createElement("labels");
		rootElement.appendChild(labelElement);

		for (String key : labels.keySet()) {
			Attr attr = doc.createAttribute(key);
			attr.setValue("" + labels.get(key));
			labelElement.setAttributeNode(attr);
		}
		

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(mDialogPath + File.separator + "config.xml"));

		transformer.transform(source, result);


		return true;
	}
	
	/**
	 * loads the color settings
	 * @param keys a list of what colors should be loaded
	 * @return returns a map with all the colors or null if an error occurred
	 * @throws Exception 
	 */
	public HashMap<String, String> loadColorSettings(ArrayList<String> keys) throws Exception {
		HashMap<String, String> colors = new HashMap<String, String>();

		File fXmlFile = new File(mDialogPath + File.separator + "config.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("colors");

		for(int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				for(String key : keys)
					colors.put(key, element.getAttribute(key));
			}
		}

		if(colors.isEmpty())
			throw new Exception(mSettingsFile + " does not contain any color settings");
		
		return colors;
	}
	
	/**
	 * loads the key settings
	 * @param keys a list of what keys should be loaded
	 * @return returns a map with all the loaded keys or null if an error occurred
	 * @throws Exception 
	 */
	public HashMap<String, Integer> loadKeySettings(ArrayList<String> keys) throws Exception {
		HashMap<String, Integer> keyCodes = new HashMap<String, Integer>();

		File fXmlFile = new File(mDialogPath + File.separator + "config.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("keys");

		for(int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				for(String key : keys)
					keyCodes.put(key, Integer.parseInt(element.getAttribute(key)));
			}
		}
		
		if(keyCodes.isEmpty())
			throw new Exception(mSettingsFile + " does not contain any key settings");
		
		return keyCodes;
	}
	
	/**
	 * loads the view settings
	 * @param keys a list of what view settings should be loaded
	 * @return returns a map with all the loaded view settings or null if an error occurred
	 * @throws Exception 
	 */
	public HashMap<String, Boolean> loadViewSettings(ArrayList<String> keys) throws Exception {
		HashMap<String, Boolean> props = new HashMap<String, Boolean>();

		File xmlFile = new File(mDialogPath + File.separator + "config.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("view");

		for(int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				for(String key : keys)
					props.put(key, Boolean.parseBoolean(element.getAttribute(key)));
			}
		}
		
		if(props.isEmpty())
			throw new Exception(mSettingsFile + " does not contain any view settings");
		
		return props;
	}
	
	
	
	public HashMap<String, Integer> loadLabelSettings(ArrayList<String> labels) throws Exception {
		HashMap<String, Integer> labelSizes = new HashMap<String, Integer>();

		File fXmlFile = new File(mDialogPath + File.separator + "config.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("labels");

		for(int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				for(String key : labels)
					labelSizes.put(key, Integer.parseInt(element.getAttribute(key)));
			}
		}
		
		if(labelSizes.isEmpty())
			throw new Exception(mSettingsFile + " does not contain any label settings");
		
		return labelSizes;
	}
	
	
	
}

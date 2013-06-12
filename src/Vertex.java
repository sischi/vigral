import java.awt.Paint;

import org.apache.commons.collections15.Factory;


public class Vertex extends GraphElement {
	
	/**
	 * a unique id for that vertex
	 */
	private int mID;
	/**
	 * an optional label of the vertex
	 */
	private String mLabel;
	
	/**
	 * constructs a vertex
	 */
	public Vertex() {
		System.out.println("Vertex created! id= "+ mID);
		mLabel = "";
	}
	
	public Vertex(int id) {
		mID = id;
		mLabel = "V"+ mID;
		System.out.println("Vertex created! id= "+ mID +", label="+ mLabel);
	}
	
	/**
	 * getter for the label
	 * @return the label as String
	 */
	public String getLabel() {
		return mLabel;
	}
	
	/**
	 * setter for the label
	 * @param label
	 */
	public void setLabel(String label) {
		mLabel = label;
	}
	
	/**
	 * returns a string representation of the vertex
	 * @return returns the label if one is set, or the id with a leading "V" otherwise
	 */
	public String toString() {
		if(mLabel == "")
			return "V"+ mID;
		else
			return mLabel;
	}
	
	/**
	 * getter for the identifier
	 * @return returns the id with a leading "V"
	 */
	public String getIdentifier() {
		return "V"+ mID;
	}
	
	
	
	
	// singleton factory to create a vertex
	public static class VertexFactory implements Factory<Vertex> {

		private static int IDCOUNT = 0;
		private static VertexFactory mInstance = new VertexFactory();
		
		private VertexFactory() {
		}
		
		public static VertexFactory getInstance() {
			return mInstance;
		}
		
		@Override
		public Vertex create() {
			return new Vertex(IDCOUNT++);
		}
		
	}
}

package de.chiller.vigral.graph;
import java.awt.geom.Point2D;

import org.apache.commons.collections15.Factory;




/**
 * this class represents a single vertex of a graph
 * @author Simon Schiller
 *
 */
public class Vertex extends GraphElement implements Comparable<Vertex> {
	
	/**
	 * a unique id for that vertex
	 */
	private int mID;
	/**
	 * an optional label of the vertex
	 */
	private String mLabel;
	private String mLabelAddition;
	
	private Point2D mLocation;
	
	
	/**
	 * constructs a vertex
	 */
	private Vertex() {
		this(-1, new Point2D.Double(0, 0));
	}
	
	/**
	 * constructs a vertex
	 * @param id the id
	 * @param location the location
	 */
	private Vertex(int id, Point2D location) {
		super();
		mID = id;
		mLabel = getIdentifier();
		mLocation = location;
		mLabelAddition = "";
		//System.out.println("Vertex created! id= "+ mID +", label="+ mLabel);
	}
	
	/**
	 * copy constructor
	 * @param v the vertex to be copied
	 */
	public Vertex(Vertex v) {
		super(v);
		mID = v.getId();
		mLabel = v.getLabel();
		mLocation = (Point2D) v.getLocation().clone();
		mLabelAddition = v.getLabelAddition();
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
	 * getter for the label addition
	 * @return returns the string that represents the label addition
	 */
	public String getLabelAddition() {
		return mLabelAddition;
	}
	
	/**
	 * resets the labeladdition to an empty string
	 */
	public void resetLabelAddition() {
		mLabelAddition = "";
	}
	
	/**
	 * setter for the label addition
	 * @param addition the string to what the label addition should be set
	 */
	public void setLabelAddition(String addition) {
		mLabelAddition = addition;
	}
	
	/**
	 * returns a string representation of the vertex
	 * @return returns the label if one is set, or the id with a leading "V" otherwise
	 */
	public String toString() {
		if(!mLabel.equals(""))
			return mLabel;
		
		return getIdentifier();
	}
	
	/**
	 * getter for the identifier
	 * @return returns the id with a leading "V"
	 */
	public String getIdentifier() {
		return "V"+ mID;
	}
	
	/**
	 * sets a new location to the vertex
	 * @param p the new location
	 */
	public void updateLocation(Point2D p) {
		mLocation = p;
	}
	
	/**
	 * getter for the location
	 * @return
	 */
	public Point2D getLocation() {
		return mLocation;
	}
	
	/**
	 * getter for the id
	 * @return returns the id of the vertex
	 */
	public int getId() {
		return mID;
	}
	
	/**
	 * transforms the vertex into a string array representation
	 * @return a string array that represents the vertex
	 */
	public String[] toStringArray() {
		String[] values = new String[4];
		
		values[0] = ""+ mID;
		values[1] = mLabel;
		values[2] = ""+ mLocation.getX();
		values[3] = ""+ mLocation.getY();
		
		return values;
	}
	
	/**
	 * creates a vertex from a string array representation
	 * @param values the string array that represents a vertex
	 * @return returns the appropriate vertex object
	 */
	public static Vertex parseVertex(String[] values) {
		Vertex v = new Vertex();
		
		v.mID = Integer.parseInt(values[0]);
		VertexFactory.checkIdCount(v.getId());
		v.mLabel = values[1];
		Point2D p = new Point2D.Double(Double.parseDouble(values[2]), Double.parseDouble(values[3]));
		v.mLocation = p;
		
		return v;
	}


	
	
	/**
	 * singleton factory that creates a new vertex
	 * @author Simon Schiller
	 *
	 */
	public static class VertexFactory implements Factory<Vertex> {

		private static int IDCOUNT = 0;
		private static VertexFactory mInstance = new VertexFactory();
		private Point2D mPoint;
		
		/**
		 * resets IDCOUNTer to 0
		 */
		public void resetIdCounter() {
			IDCOUNT = 0;
		}
		
		/**
		 * turn the constructor off
		 */
		private VertexFactory() {}
		
		/**
		 * getter for the vertexfactory
		 * @return returns an instance of the vertexfactory
		 */
		public static VertexFactory getInstance() {
			return mInstance;
		}
		
		/**
		 * set the location of the vertex
		 * @param p
		 */
		public void setLocation(Point2D p) {
			mPoint = p;
		}
		
		@Override
		public Vertex create() {
			return new Vertex(IDCOUNT++, mPoint);
		}
		
		/**
		 * checks if the passed id is is smaller or equal to the idcounter. if so, the idcounter is set to id+1
		 * @param id the id against that the idcounter has to be checked
		 */
		public static void checkIdCount(int id) {
			if(id >= IDCOUNT)
				IDCOUNT = id+1;
		}
	}



	@Override
	public int compareTo(Vertex o) {
		return toString().compareTo(o.toString());
	}
}

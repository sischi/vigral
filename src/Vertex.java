import java.awt.Paint;
import java.awt.Point;
import java.awt.geom.Point2D;

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
	
	private Point2D mLocation;
	
	
	/**
	 * constructs a vertex
	 */
	public Vertex() {
		System.out.println("Vertex created! id= "+ mID);
		mLabel = "";
	}
	
	public Vertex(int id, Point2D location, ElementState state) {
		mID = id;
		mLabel = "V"+ mID;
		mLocation = location;
		mState = state;
		System.out.println("Vertex created! id= "+ mID +", label="+ mLabel);
	}
	
	
	public Vertex(Vertex v) {
		super(v);
		mID = v.getId();
		mLabel = v.getLabel();
		mLocation = (Point2D) v.getLocation().clone();
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
	
	public void updateLocation(Point2D p) {
		System.out.println(mID +"= "+ p);
		mLocation = p;
	}
	
	public Point2D getLocation() {
		return mLocation;
	}
	
	public int getId() {
		return mID;
	}
	
	
	
	// singleton factory to create a vertex
	public static class VertexFactory implements Factory<Vertex> {

		private static int IDCOUNT = 0;
		private static VertexFactory mInstance = new VertexFactory();
		private Point2D mPoint;
		private static ElementState DEFAULT_STATE = ElementState.UNVISITED;
		
		private VertexFactory() {
		}
		
		public static VertexFactory getInstance() {
			return mInstance;
		}
		
		public void setLocation(Point2D p) {
			mPoint = p;
		}
		
		@Override
		public Vertex create() {
			return new Vertex(IDCOUNT++, mPoint, DEFAULT_STATE);
		}
		
	}
}

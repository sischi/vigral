package de.chiller.vigral.graph;

import java.awt.Paint;
import java.util.ArrayList;

import org.apache.commons.collections15.Factory;






/**
 * this class represents a single edge of a graph
 * @author Timmae
 *
 */
public class Edge extends GraphElement {
	
	
	/**
	 * a unique identifier for the edge
	 */
	private int mID;
	/**
	 * the weight of the edge
	 */
	private double mWeight;
	/**
	 * the start vertex
	 */
	private Vertex mStart;
	/**
	 * the end vertex
	 */
	private Vertex mEnd;
	/**
	 * a boolean that determines if this edge is directed or not
	 */
	private boolean mIsDirected;
	
	/**
	 * constructs the edge
	 * @param id the identifier
	 * @param weight the weight
	 * @param start the start vertex
	 * @param end the end vertex
	 * @param directed true if it is an directed edge and false else
	 */
	public Edge(int id, double weight, Vertex start, Vertex end, boolean directed) {
		this(id, weight, start, end, directed, ElementState.UNVISITED);
	}
	
	/**
	 * constructs the edge
	 * @param id the identifier
	 * @param weight the weight
	 * @param start the start vertex
	 * @param end the end vertex
	 * @param directed true if it is an directed edge and false else
	 * @param state the state of the edge
	 */
	public Edge(int id, double weight, Vertex start, Vertex end, boolean directed, ElementState state) {
		super();
		mID = id;
		mWeight = weight;
		mStart = start;
		mEnd = end;
		mIsDirected = directed;
		mState = state;
		System.out.println("Edge created! "+ this.toString());
	}
	
	/**
	 * copy constructor
	 * @param e the edge to be copied
	 */
	public Edge(Edge e) {
		super(e);
		mID = e.getId();
		mWeight = e.getWeight();
		mStart = new Vertex(e.getStartVertex());
		mEnd = new Vertex(e.getEndVertex());
		mIsDirected = e.isDirected();
	}
	
	/**
	 * a function that indicates if the edge is directed or not
	 * @return returns true if the edge is directed and false otherwise
	 */
	public boolean isDirected() {
		return mIsDirected;
	}
	
	/**
	 * getter for the weight
	 * @return returns the weight as an integer
	 */
	public double getWeight() {
		return mWeight;
	}
	
	/**
	 * setter for the weight
	 * @param weight the integer representation of the weight
	 */
	public void setWeight(double weight) {
		mWeight = weight;
	}
	
	/**
	 * getter for the start vertex
	 * @return returns the start vertex
	 */
	public Vertex getStartVertex() {
		return mStart;
	}
	
	/**
	 * getter for the end vertex
	 * @return returns the end vertex
	 */
	public Vertex getEndVertex() {
		return mEnd;
	}
	
	/**
	 * to get the other end of the edge
	 * @param v the one end of the edge as a Vertex
	 * @return returns the other end of this edge as a vertex
	 */
	public Vertex getOtherEnd(Vertex v) {
		if(mStart == v)
			return mEnd;
		else if(mEnd == v)
			return mStart;
		
		return null;	
	}

	/**
	 * returns a string representation of the edge
	 */
	public String toString() {
		return "E"+ mID +" ("+ mStart.toString() +", "+ mEnd.toString() +")";
	}
	
	/**
	 * 
	 * @return returns the id of the edge
	 */
	public int getId() {
		return mID;
	}
	
	
	public String[] toStringArray() {
		String[] list = new String[5];
		
		list[0] = ""+ mID;
		list[1] = ""+ mWeight;
		list[2] = ""+ mStart.getId();
		list[3] = ""+ mEnd.getId();
		list[4] = ""+ mIsDirected;
		
		return list;
	}
	
	
	
	// singleton factory to create an edge
	public static class EdgeFactory<V> implements Factory<Edge> {
		
		private static int IDCOUNT = 0;
		private static EdgeFactory mInstance = new EdgeFactory();
		private V mStartVertex;
		private V mEndVertex;
		private boolean mEdgeIsDirected;
		
		/**
		 * switched the constructor off
		 */
		private EdgeFactory() {}
		
		/**
		 * gets an instance of the edge factory
		 * @return returns the edge factory
		 */
		public static EdgeFactory getInstance() {
			return mInstance;
		}
		
		/**
		 * setter for start and end vertices
		 * @param start the start vertex
		 * @param end the end vertex
		 */
		public void setStartAndEnd(V start, V end) {
			mStartVertex = start;
			mEndVertex = end;
		}
		
		/**
		 * setter for the direction
		 * @param isDirected true if the edge is directed and false otherwise
		 */
		public void setDirected(boolean isDirected) {
			mEdgeIsDirected = isDirected;
		}

		@Override
		public Edge create() {
			return new Edge(IDCOUNT++, 1.0, (Vertex)mStartVertex, (Vertex)mEndVertex, mEdgeIsDirected);
		}
	}
}

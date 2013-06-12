import java.awt.Paint;

import org.apache.commons.collections15.Factory;


public class Edge extends GraphElement {
	
	
	/**
	 * a unique identifier for the edge
	 */
	private int mID;
	/**
	 * the weight of the edge
	 */
	private int mWeight;
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
	public Edge(int id, int weight, Vertex start, Vertex end, boolean directed) {
		mID = id;
		mWeight = weight;
		mStart = start;
		mEnd = end;
		mIsDirected = directed;
		System.out.println("Edge created! "+ this.toString());
	}
	
	public Edge(Edge e) {
		super(e);
		mID = e.getId();
		mWeight = e.getWeight();
		mStart = new Vertex(e.getStartVertex());
		mEnd = new Vertex(e.getEndVertex());
		mIsDirected = e.isDirected();
	}
	
	
	public boolean isDirected() {
		return mIsDirected;
	}
	
	/**
	 * getter for the weight
	 * @return returns the weight as an integer
	 */
	public int getWeight() {
		return mWeight;
	}
	
	/**
	 * setter for the weight
	 * @param weight the integer representation of the weight
	 */
	public void setWeight(int weight) {
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
	 * getter for the en vertex
	 * @return returns the end vertex
	 */
	public Vertex getEndVertex() {
		return mEnd;
	}

	/**
	 * returns a string representation of the edge
	 */
	public String toString() {
		return "E"+ mID +" ("+ mStart.toString() +", "+ mEnd.toString() +")";
	}
	

	public int getId() {
		return mID;
	}
	
	
	
	
	// singleton factory to create an edge
	public static class EdgeFactory<V> implements Factory<Edge> {
		
		private static int IDCOUNT = 0;
		private static EdgeFactory mInstance = new EdgeFactory();
		private V mStartVertex;
		private V mEndVertex;
		private boolean mEdgeIsDirected;
		
		private EdgeFactory() {
		}
		
		public static EdgeFactory getInstance() {
			return mInstance;
		}
		
		public void setStartAndEnd(V start, V end) {
			mStartVertex = start;
			mEndVertex = end;
		}
		
		public void setDirected(boolean isDirected) {
			mEdgeIsDirected = isDirected;
		}

		@Override
		public Edge create() {
			return new Edge(IDCOUNT++, 1, (Vertex)mStartVertex, (Vertex)mEndVertex, mEdgeIsDirected);
		}
	}
}

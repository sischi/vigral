import org.apache.commons.collections15.Factory;


public class Edge {
	
	private int mID;
	private int mWeight;
	private Vertex mStart;
	private Vertex mEnd;
	private int mState;
	
	private boolean mIsDirected;
	
	
	public Edge(int id, int weight, Vertex start, Vertex end, boolean directed) {
		mID = id;
		mWeight = weight;
		mStart = start;
		mEnd = end;
		mIsDirected = directed;
		System.out.println("Edge created! "+ this.toString());
	}
	
	public int getWeight() {
		return mWeight;
	}
	
	public void setWeight(int weight) {
		mWeight = weight;
	}
	
	public Vertex getStartVertex() {
		return mStart;
	}
	
	public Vertex getEndVertex() {
		return mEnd;
	}
	
	public void setState(int state) {
		mState = state;
	}
	
	public int getState() {
		return mState;
	}
	
	
	public String toString() {
		return "E"+ mID +" ("+ mStart.toString() +", "+ mEnd.toString() +")";
	}
	
	
	
	
	
	// singleton factory to create an edge
	public static class EdgeFactory implements Factory<Edge> {
		
		private static int IDCOUNT = 0;
		private static EdgeFactory mInstance = new EdgeFactory();
		private Vertex mStartVertex;
		private Vertex mEndVertex;
		private boolean mEdgeIsDirected;
		
		private EdgeFactory() {
		}
		
		public static EdgeFactory getInstance() {
			return mInstance;
		}
		
		public void setStartAndEnd(Vertex start, Vertex end) {
			mStartVertex = start;
			mEndVertex = end;
		}
		
		public void setDirected(boolean isDirected) {
			mEdgeIsDirected = isDirected;
		}

		@Override
		public Edge create() {
			return new Edge(IDCOUNT++, 1, mStartVertex, mEndVertex, mEdgeIsDirected);
		}
	}
}

import org.apache.commons.collections15.Factory;


public class Edge {
	
	private int mID;
	private int mWeight;
	
	public Edge(int id) {
		this(id, 1);
	}
	
	public Edge(int id, int weight) {
		mID = id;
		mWeight = weight;
		System.out.println("Edge created! ID="+ mID +", weight="+ mWeight);
	}
	
	public int getWeight() {
		return mWeight;
	}
	
	public void setWeight(int weight) {
		mWeight = weight;
	}
	
	public String toString() {
		return "E"+ mID;
	}
	
	
	
	public static class EdgeFactory implements Factory<Edge> {
		
		private static int IDCOUNT = 0;
		private static EdgeFactory mInstance = new EdgeFactory();
		
		private EdgeFactory() {
		}
		
		public static EdgeFactory getInstance() {
			return mInstance;
		}

		@Override
		public Edge create() {
			return new Edge(IDCOUNT++);
		}
	}
}

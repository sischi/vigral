import org.apache.commons.collections15.Factory;


public class Edge {
	
	
	private static int IDENTIFICATION = 0;
	private int mID;
	private int mWeight;
	
	public Edge() {
		mID = IDENTIFICATION++;
		System.out.println("Edge created! ID="+ mID);
	}
	
	public int getWeight() {
		return mWeight;
	}
	
	public String toString() {
		return "E"+ mID;
	}
	
	
	
	public static class EdgeFactory implements Factory<Edge> {
		
		private static EdgeFactory mInstance = new EdgeFactory();
		
		private EdgeFactory() {
		}
		
		public static EdgeFactory getInstance() {
			return mInstance;
		}

		@Override
		public Edge create() {
			return new Edge();
		}
	}
}

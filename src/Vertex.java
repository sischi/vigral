import org.apache.commons.collections15.Factory;


public class Vertex {

	private static int IDENTIFICATION = 0;
	
	private int mID;
	private String mLabel;
	
	public Vertex() {
		mID = IDENTIFICATION++;
		System.out.println("Vertex created! id= "+ mID);
		mLabel = "";
	}
	
	public String getLabel() {
		return mLabel;
	}
	
	public String toString() {
		if(mLabel == "")
			return "V"+ mID;
		else
			return mLabel;
	}
	
	
	public static class VertexFactory implements Factory<Vertex> {

		private static VertexFactory mInstance = new VertexFactory();
		
		private VertexFactory() {
		}
		
		public static VertexFactory getInstance() {
			return mInstance;
		}
		
		@Override
		public Vertex create() {
			return new Vertex();
		}
		
	}
}

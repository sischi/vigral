import org.apache.commons.collections15.Factory;


public class Vertex {
	
	private int mID;
	private String mLabel;
	
	public Vertex() {
		System.out.println("Vertex created! id= "+ mID);
		mLabel = "";
	}
	
	public Vertex(int id) {
		mID = id;
		mLabel = "V"+ mID;
		System.out.println("Vertex created! id= "+ mID +", label="+ mLabel);
	}
	
	public String getLabel() {
		return mLabel;
	}
	
	public void setLabel(String label) {
		mLabel = label;
	}
	
	public String toString() {
		if(mLabel == "")
			return "V"+ mID;
		else
			return mLabel;
	}
	
	
	public String getIdentifier() {
		return "V"+ mID;
	}
	
	
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

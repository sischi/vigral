import java.awt.Paint;


public abstract class GraphElement {

	public static class State {
		public static final int UNVISITED = 0;
		public static final int ACTIVE = 1;
		public static final int VISITED = 2;
		public static final int FINISHED_AND_RELEVANT = 3;
		public static final int FINISHED_AND_NOT_RELEVANT = 4;
	}
	
	private int mState;
	private Paint mCustomColor;
	
	public GraphElement(){}
	
	public GraphElement(GraphElement ge) {
		mState = ge.getState();
		mCustomColor = ge.getCustomColor();
	}
	
	public Paint getCustomColor() {
		return mCustomColor;
	}
	
	public void setCustomColor(Paint color) {
		mCustomColor = color;
	}
	
	public void resetCustomColor() {
		mCustomColor = null;
	}
	
	public void setState(int state) {
		mState = state;
	}
	
	public int getState() {
		return mState;
	}
}

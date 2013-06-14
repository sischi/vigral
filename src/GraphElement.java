import java.awt.Paint;


public abstract class GraphElement {


	protected ElementState mState;
	protected Paint mCustomColor;
	
	public GraphElement(){
		mState = ElementState.UNVISITED;
	}
	
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
	
	public void setState(ElementState state) {
		mState = state;
	}
	
	public ElementState getState() {
		return mState;
	}
}

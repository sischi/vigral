import java.awt.Paint;


public abstract class GraphElement {


	private ElementState mState;
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
	
	public void setState(ElementState state) {
		mState = state;
	}
	
	public ElementState getState() {
		return mState;
	}
}

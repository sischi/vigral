package de.chiller.vigral.graph;

import java.awt.Paint;


public abstract class GraphElement {


	protected ElementState mState;
	protected Paint mCustomColor;
	protected boolean mIsPicked;
	
	public GraphElement(){
		mIsPicked = false;
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
	
	public boolean isPicked() {
		return mIsPicked;
	}
	
	public void setPicked(boolean picked) {
		mIsPicked = picked;
	}
}

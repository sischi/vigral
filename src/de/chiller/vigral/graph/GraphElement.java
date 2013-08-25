package de.chiller.vigral.graph;

import java.awt.Paint;


public abstract class GraphElement {


	protected ElementState mState;
	protected Paint mCustomColor;
	protected boolean mIsPicked;
	
	/**
	 * constructs a graph element
	 */
	public GraphElement(){
		mIsPicked = false;
		mState = ElementState.UNVISITED;
	}
	
	/**
	 * copy constructor
	 * @param ge the graph element to be copied
	 */
	public GraphElement(GraphElement ge) {
		mState = ge.getState();
		mCustomColor = ge.getCustomColor();
	}
	
	/**
	 * getter for the customcolor
	 * @return returns the customcolor as a Paint object
	 */
	public Paint getCustomColor() {
		return mCustomColor;
	}
	
	/**
	 * setter for the customcolor
	 * @param color the color to be set
	 */
	public void setCustomColor(Paint color) {
		mCustomColor = color;
	}
	
	/**
	 * resets the customcolor to null
	 */
	public void resetCustomColor() {
		mCustomColor = null;
	}
	
	/**
	 * setter for the state
	 * @param state the ElementState to be set
	 */
	public void setState(ElementState state) {
		mState = state;
	}
	
	/**
	 * getter for the state
	 * @return returns the ElementState
	 */
	public ElementState getState() {
		return mState;
	}
	
	/**
	 * indicates if this element is picked
	 * @return returns true if this element is picked or false otherwise
	 */
	public boolean isPicked() {
		return mIsPicked;
	}
	
	/**
	 * sets the picked state
	 * @param picked the boolean value to be set
	 */
	public void setPicked(boolean picked) {
		mIsPicked = picked;
	}
}

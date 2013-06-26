package de.chiller.vigral.algorithm;

import java.util.ArrayList;

import de.chiller.vigral.graph.Graph;




/**
 * abstract implementation of the algorithm interface. this class should be extended when implementing new algorithms
 * @author Timmae
 *
 */
public abstract class AbstractAlgorithm implements Algorithm {

	/**
	 * the graph object
	 */
	protected Graph mGraph;
	/**
	 * a list of graphs that represent the single steps
	 */
	protected ArrayList<Graph> mSteps;

	/**
	 * indicates the step actually at
	 */
	protected int mActualStep;
	
	/**
	 * constructor
	 */
	public AbstractAlgorithm() {
		mSteps = new ArrayList<Graph>();
		mActualStep = -1;
	}
	
	/**
	 * sets the graph object on what the algorithm will work and resets the steps and graph
	 * @param g the graph object
	 */
	public void setGraph(Graph g) {
		mGraph = g;
		mGraph.resetStates();
		mSteps.clear();
		mSteps.add(new Graph(mGraph));
		mActualStep = -1;
	}
	
	/**
	 * returns the first step of the algorithm as a graph object
	 */
	public Graph getFirstStep() {
		if(mActualStep == 0)
			return null;
		mActualStep = 0;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

	/**
	 * returns the previous step of the algorithm as a graph object
	 */
	public Graph getPreviousStep() {
		if(mActualStep > 0)
			mActualStep--;
		else
			return null;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

	/**
	 * returns the next step of the algorithm as a graph object
	 */
	public Graph getNextStep() {
		if(mActualStep < mSteps.size() - 1)
			mActualStep++;
		else
			return null;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);	
	}

	/**
	 * returns the last step of the algorithm as a graph object
	 */
	public Graph getLastStep() {
		if(mActualStep == mSteps.size()-1)
			return null;
			
		mActualStep = mSteps.size()-1;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}
	
	
	public void addStep() {
		mSteps.add(new Graph(mGraph));
	}
	

}

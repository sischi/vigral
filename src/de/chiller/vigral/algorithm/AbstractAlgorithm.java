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
		mActualStep = 0;
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
		mActualStep = 0;
	}
	
	/**
	 * returns the first step of the algorithm as a graph object
	 */
	public Graph getFirstStep() {
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
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

	/**
	 * returns the next step of the algorithm as a graph object
	 */
	public Graph getNextStep() {
		if(mActualStep < mSteps.size() - 1)
			mActualStep++;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);	
	}

	/**
	 * returns the last step of the algorithm as a graph object
	 */
	public Graph getLastStep() {
		mActualStep = mSteps.size()-1;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}
	
	
	public void addStep() {
		mSteps.add(new Graph(mGraph));
	}
	

}

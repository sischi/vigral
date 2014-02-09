package de.chiller.vigral.algorithm;

import java.util.ArrayList;

import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.util.Pair;




/**
 * abstract implementation of the algorithm interface. this class should be extended when implementing new algorithms
 * @author Simon Schiller
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
	protected ArrayList<Pair<Graph, String>> mSteps;

	/**
	 * indicates the step actually at
	 */
	protected int mActualStep;
	
	/**
	 * constructor
	 */
	public AbstractAlgorithm() {
		mSteps = new ArrayList<Pair<Graph, String>>();
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
		mSteps.add(new Pair<Graph, String>(new Graph(mGraph), "The initial Graph"));
		mActualStep = -1;
	}
	
	/**
	 * returns the first step of the algorithm as a graph object
	 */
	public Pair<Graph, String> getFirstStep() {
		if(mActualStep == 0)
			return null;
		mActualStep = 0;
		return mSteps.get(mActualStep);
	}

	/**
	 * returns the previous step of the algorithm as a graph object
	 */
	public Pair<Graph, String> getPreviousStep() {
		if(mActualStep > 0)
			mActualStep--;
		else
			return null;
		return mSteps.get(mActualStep);
	}

	/**
	 * returns the next step of the algorithm as a graph object
	 */
	public Pair<Graph, String> getNextStep() {
		if(mActualStep < mSteps.size() - 1)
			mActualStep++;
		else
			return null;
		return mSteps.get(mActualStep);	
	}

	/**
	 * returns the last step of the algorithm as a graph object
	 */
	public Pair<Graph, String> getLastStep() {
		if(mActualStep == mSteps.size()-1)
			return null;
			
		mActualStep = mSteps.size()-1;
		return mSteps.get(mActualStep);
	}
	
	/**
	 * adds a new copy of the graph with an empty explanation to the list of the steps
	 */
	public void addStep() {
		addStep("");
	}
	
	
	/**
	 * adds a new copy of the graph with the given explanation to the list of the steps
	 * @param explanation the explanation of the step
	 */
	public void addStep(String explanation) {
		addStep(mGraph, explanation);
	}
	
	
	public void addStep(Graph g, String explanation) {
		String tmp = "";
		if(!mSteps.isEmpty()) 
			tmp = mSteps.get(mSteps.size()-1).getR() +"\n\n";

		tmp += explanation;
		mSteps.add(new Pair<Graph, String>(new Graph(g), tmp));
	}
	
	
	public void addStep(Graph g) {
		addStep(g, "");
	}
	
	
	public void addStep(Graph g, Graph h, String explanation) {
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		
		graphs.add(new Graph(g));
		graphs.add(new Graph(h));
		
		String tmp = "";
		if(!mSteps.isEmpty()) 
			tmp = mSteps.get(mSteps.size()-1).getR() +"\n\n";
		
		tmp += explanation;
		// TODO: add arraylist to list of steps
		// mSteps.add(new Pair<ArrayList<Graph>, String>(graphs, tmp));
	}
	
	
	public void addStep(Graph g, Graph h) {
		addStep(g, h, "");
	}
	
	

}

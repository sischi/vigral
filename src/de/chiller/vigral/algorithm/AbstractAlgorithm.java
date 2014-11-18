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
	protected ArrayList<Pair<ArrayList<Graph>, String>> mSteps;

	/**
	 * indicates the step actually at
	 */
	protected int mActualStep;
	
	/**
	 * constructor
	 */
	public AbstractAlgorithm() {
		mSteps = new ArrayList<Pair<ArrayList<Graph>, String>>();
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
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		graphs.add(new Graph(mGraph));
		mSteps.add(new Pair<ArrayList<Graph>, String>(graphs, "The initial Graph"));
		mActualStep = -1;
	}
	
	/**
	 * returns the first step of the algorithm as a graph object
	 */
	public Pair<ArrayList<Graph>, String> getFirstStep() {
		if(mActualStep == 0)
			return null;
		mActualStep = 0;
		return mSteps.get(mActualStep);
	}

	/**
	 * returns the previous step of the algorithm as a graph object
	 */
	public Pair<ArrayList<Graph>, String> getPreviousStep() {
		if(mActualStep > 0)
			mActualStep--;
		else
			return null;
		return mSteps.get(mActualStep);
	}

	/**
	 * returns the next step of the algorithm as a graph object
	 */
	public Pair<ArrayList<Graph>, String> getNextStep() {
		if(mActualStep < mSteps.size() - 1)
			mActualStep++;
		else
			return null;
		return mSteps.get(mActualStep);	
	}

	/**
	 * returns the last step of the algorithm as a graph object
	 */
	public Pair<ArrayList<Graph>, String> getLastStep() {
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
	
	

	/**
	 * adds the given graph without explanation to the list of steps
	 * @param g the graph to be added
	 */
	public void addStep(Graph g) {
		addStep(g, "");
	}
	
	/**
	 * adds the given graphs with an empty explanation to the list of steps
	 * @param g the first graph to be added
	 * @param h the second graph to be added
	 */
	public void addStep(Graph g, Graph h) {
		addStep(g, h, "");
	}
	
	
	
	/**
	 * adds the given graph with an explanation to the list of steps
	 * @param g the graph to be added
	 * @param explanation the explanation of that step
	 */
	public void addStep(Graph g, String explanation) {
		// the list of graphs to be added
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		
		// put a copy of the given graph in that list
		graphs.add(new Graph(g));
		
		String tmp = "";
		if(!mSteps.isEmpty())
			tmp = mSteps.get(mSteps.size()-1).getR() +"\n\n";

		tmp += explanation;
		// add the list of graphs with the explanation to the list of steps
		mSteps.add(new Pair<ArrayList<Graph>, String>(graphs, tmp));
	}
	
	
	
	/**
	 * adds both of the given graphs to the list of steps
	 * @param g the first graph
	 * @param h the second graph
	 * @param explanation the explanation of that step
	 */
	public void addStep(Graph g, Graph h, String explanation) {
		// the list of graphs to be added
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		
		// put a copy of each graph in that list
		graphs.add(new Graph(g));
		graphs.add(new Graph(h));
		
		String tmp = "";
		if(!mSteps.isEmpty()) 
			tmp = mSteps.get(mSteps.size()-1).getR() +"\n\n";
		
		tmp += explanation;
		// add the list of graphs with the explanation to the list of steps
		mSteps.add(new Pair<ArrayList<Graph>, String>(graphs, tmp));
	}
	
	

	
	

}

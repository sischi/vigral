import java.util.ArrayList;


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
	 * sets the graph object on what the algorithm will work
	 * @param g the graph object
	 */
	protected void setGraph(Graph g) {
		mGraph = g;
	}

}

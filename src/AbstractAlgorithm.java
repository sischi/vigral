import java.util.ArrayList;


public abstract class AbstractAlgorithm implements Algorithm {

	protected Graph mGraph;
	protected ArrayList<Graph> mSteps;
	
	protected void setGraph(Graph g) {
		mGraph = g;
	}
}

import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.jung.graph.SparseMultigraph;


public class Graph {
	private ArrayList<Vertex> mVertices;
	private ArrayList<Edge> mEdges;
	
	private HashMap<Vertex, ArrayList<Edge>> mOutEdges;
	
	
	public static Graph parseSparseMultiGraph(SparseMultigraph<Vertex, Edge> multiGraph) {
		return null;
	}
	
	public SparseMultigraph<Vertex, Edge> toSparseMultiGraph() {
		return null;
	}
}
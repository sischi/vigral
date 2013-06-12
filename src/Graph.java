import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.jung.graph.SparseMultigraph;


public class Graph {
	
	/**
	 * a list that contains all vertices in the graph
	 */
	private ArrayList<Vertex> mVertices;
	/**
	 * a list that contains all Edges in the graph
	 */
	private ArrayList<Edge> mEdges;
	/**
	 * a hashmap that contains all outgoing edges per vertex
	 */
	private HashMap<Vertex, ArrayList<Edge>> mOutEdges;
	
	
	public Graph(Graph g) {
		mVertices = new ArrayList<Vertex>();
		for(Vertex v : g.getVertices())
			mVertices.add(new Vertex(v));
		
		mEdges = new ArrayList<Edge>();
		for(Edge e : g.getEdges())
			mEdges.add(e);
		
		mOutEdges = new HashMap<Vertex, ArrayList<Edge>>();
		for(Vertex v : mVertices) {
			mOutEdges.put(v, new ArrayList<Edge>());
			for(Edge e : g.getOutEdges(v))
				mOutEdges.get(v).add(new Edge(e));
		}
	}
	
	/**
	 * creates a graph due to the given SparseMultiGraph (JUNG graph representation)
	 * @param multiGraph the JUNG representation of a graph
	 * @return the resulting Graph representation of the graph
	 */
	public static Graph parseSparseMultiGraph(SparseMultigraph<Vertex, Edge> multiGraph) {
		return null;
	}
	
	/**
	 * converts this graph back to a SparseMultiGraph (JUNG representation of this graph) 
	 * @return
	 */
	public SparseMultigraph<Vertex, Edge> toSparseMultiGraph() {
		return null;
	}
	
	
	public ArrayList<Vertex> getVertices() {
		return mVertices;
	}
	
	public ArrayList<Edge> getEdges() {
		return mEdges;
	}
	
	public ArrayList<Edge> getOutEdges(Vertex v) {
		return mOutEdges.get(v);
	}
}
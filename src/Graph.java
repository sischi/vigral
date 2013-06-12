import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


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
	
	
	public Graph() {
		mVertices = new ArrayList<Vertex>();
		mEdges = new ArrayList<Edge>();
		mOutEdges = new HashMap<Vertex, ArrayList<Edge>>();
	}
	
	public Graph(Graph g) {
		mVertices = new ArrayList<Vertex>();
		for(Vertex v : g.getVertices())
			mVertices.add(new Vertex(v));
		
		mEdges = new ArrayList<Edge>();
		for(Edge e : g.getEdges())
			mEdges.add(new Edge(e));
		
		mOutEdges = new HashMap<Vertex, ArrayList<Edge>>();
		for(Vertex v : mVertices) {
			mOutEdges.put(v, new ArrayList<Edge>());
			for(Edge e : mEdges)
				mOutEdges.get(v).add(e);
		}
	}
	
	/**
	 * creates a graph due to the given SparseMultiGraph (JUNG graph representation)
	 * @param multiGraph the JUNG representation of a graph
	 * @return the resulting Graph representation of the graph
	 */
	public static Graph parseSparseMultiGraph(SparseMultigraph<Vertex, Edge> multiGraph) {
		Graph g = new Graph();
		
		Collection<Vertex> vertices = multiGraph.getVertices();
		for(Vertex v : vertices)
			g.getVertices().add(new Vertex(v));
		
		Collection<Edge> edges = multiGraph.getEdges();
		for(Edge e : edges)
			g.getEdges().add(new Edge(e));
		
		for(Vertex v : g.getVertices()) {
			g.getAllOutEdgesPerVertex().put(v, new ArrayList<Edge>());
			for(Edge e : g.getEdges()) {
				if(v == e.getStartVertex() || (v == e.getEndVertex() && !e.isDirected()))
					g.getOutEdges(v).add(e);
			}
		}
		
		return g;
	}
	
	/**
	 * converts this graph back to a SparseMultiGraph (JUNG representation of this graph) 
	 * @return
	 */
	public SparseMultigraph<Vertex, Edge> toSparseMultiGraph() {
		SparseMultigraph<Vertex, Edge> multiGraph = new SparseMultigraph<Vertex, Edge>();
		
		for(Edge e : mEdges) {
			Edge newEdge = new Edge(e);
			if(newEdge.isDirected())
				multiGraph.addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.DIRECTED);
			else
				multiGraph.addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.UNDIRECTED);
		}
		
		return multiGraph;
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
	
	private HashMap<Vertex, ArrayList<Edge>> getAllOutEdgesPerVertex() {
		return mOutEdges;
	}
}
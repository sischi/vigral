import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


public class MyGraph {
	
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
	
	private HashMap<Vertex, Integer> mInDegrees;
	private HashMap<Vertex, Integer> mOutDegrees;
	
	
	
	
	public MyGraph() {
		mVertices = new ArrayList<Vertex>();
		mEdges = new ArrayList<Edge>();
		mOutEdges = new HashMap<Vertex, ArrayList<Edge>>();
		mInDegrees = new HashMap<Vertex, Integer>();
		mOutDegrees = new HashMap<Vertex, Integer>();
	}
	
	public MyGraph(MyGraph g) {
		mVertices = new ArrayList<Vertex>();
		mInDegrees = new HashMap<Vertex, Integer>();
		mOutDegrees = new HashMap<Vertex, Integer>();
		
		for(Vertex v : g.getVertices()) {
			Vertex newVertex = new Vertex(v);
			mVertices.add(newVertex);
			mInDegrees.put(newVertex, g.getInDegree(v));
			mOutDegrees.put(newVertex, g.getOutDegree(v));
		}
		
		mEdges = new ArrayList<Edge>();
		for(Edge e : g.getEdges()) {
			Edge newEdge = new Edge(e.getId(), e.getWeight(), getVertexById(e.getStartVertex().getId()), getVertexById(e.getEndVertex().getId()), e.isDirected());
			mEdges.add(newEdge);
		}
		
		mOutEdges = new HashMap<Vertex, ArrayList<Edge>>();
		for(Vertex v : g.getVertices()) {
			mOutEdges.put(getVertexById(v.getId()), new ArrayList<Edge>());
			for(Edge e : g.getOutEdges(v))
				mOutEdges.get(getVertexById(v.getId())).add(getEdgeById(e.getId()));
		}
		
	}
	
	
	public Vertex getVertexById(int id) {
		for(Vertex v : mVertices) {
			if(v.getId() == id)
				return v;
		}
		return null;
	}
	
	
	public Edge getEdgeById(int id) {
		for(Edge e : mEdges) {
			if(e.getId() == id)
				return e;
		}
		return null;
	}
	
	/**
	 * creates a graph due to the given SparseMultiGraph (JUNG graph representation)
	 * @param multiGraph the JUNG representation of a graph
	 * @return the resulting Graph representation of the graph
	 */
	public static MyGraph parseGraph(SparseMultigraph<Vertex, Edge> multiGraph) {
		MyGraph g = new MyGraph();
		System.out.println(multiGraph);
		for(Vertex v : multiGraph.getVertices()) {
			Vertex newVertex = new Vertex(v);
			g.getVertices().add(newVertex);
			g.getAllInDegrees().put(newVertex, multiGraph.inDegree(v));
			g.getAllOutDegrees().put(newVertex, multiGraph.outDegree(v));
		}

		for(Edge e :  multiGraph.getEdges()) {
			Edge newEdge = new Edge(e.getId(), e.getWeight(), g.getVertexById(e.getStartVertex().getId()), g.getVertexById(e.getEndVertex().getId()), e.isDirected());
			g.getEdges().add(newEdge);
		}
		
		for(Vertex v : multiGraph.getVertices()) {
			g.getAllOutEdgesPerVertex().put(g.getVertexById(v.getId()), new ArrayList<Edge>());
			for(Edge e : multiGraph.getOutEdges(v))
				g.getAllOutEdgesPerVertex().get(g.getVertexById(v.getId())).add(g.getEdgeById(e.getId()));
		}
		
		/*
		for(Vertex v : g.getVertices()) {
			g.getAllOutEdgesPerVertex().put(v, new ArrayList<Edge>());
			for(Edge e : g.getEdges()) {
				if(v == e.getStartVertex() || (v == e.getEndVertex() && !e.isDirected()))
					g.getOutEdges(v).add(e);
			}
		}
		*/
		System.out.println(g.toString());
		return g;
	}
	
	/**
	 * converts this graph back to a SparseMultiGraph (JUNG representation of this graph) 
	 * @return
	 */
	public SparseMultigraph<Vertex, Edge> toSparseMultiGraph() {
		SparseMultigraph<Vertex, Edge> multiGraph = new SparseMultigraph<Vertex, Edge>();
		
		System.out.println(toString());
		for(Vertex v : mVertices)
			multiGraph.addVertex(new Vertex(v));
		
		
		for(Edge e : mEdges) {
			Vertex startVertex = null, endVertex = null;
			for(Vertex v : multiGraph.getVertices()) {
				if(e.getStartVertex().getId() == v.getId())
					startVertex = v;
				if(e.getEndVertex().getId() == v.getId())
					endVertex = v;
			}
			
			Edge newEdge = new Edge(e.getId(), e.getWeight(), startVertex, endVertex, e.isDirected());
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
	
	private HashMap<Vertex, Integer> getAllOutDegrees() {
		return mOutDegrees;
	}
	
	private HashMap<Vertex, Integer> getAllInDegrees() {
		return mInDegrees;
	}
	
	public int getInDegree(Vertex v) {
		return mInDegrees.get(v);
	}
	
	public int getOutDegree(Vertex v) {
		return mOutDegrees.get(v);
	}
	
	public void resetStates() {
		for(Vertex v : mVertices)
			v.setState(ElementState.UNVISITED);
	}
	
	
	
	public String toString() {
		String out = "";
		
		for(Vertex v : getVertices())
			out = out +"\n"+ v.debug();
		
		return out;
	}
	
	
}
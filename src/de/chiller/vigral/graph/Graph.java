package de.chiller.vigral.graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.chiller.vigral.graph.Edge.EdgeFactory;
import edu.uci.ics.jung.graph.OrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


@SuppressWarnings("serial")
/**
 * This class represents a Graph and extends the OrderedSparseMultigraph from the JUNG framework
 * @author Simon Schiller
 *
 */
public class Graph extends OrderedSparseMultigraph<Vertex, Edge> {

	/**
	 * creates an instance
	 */
	public Graph() {
		super();
	}
	
	/**
	 * copy constructor
	 * @param g the graph to be copied
	 */
	public Graph(Graph g) {
		super();
	
		HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
		
		for(Vertex v : g.getVertices()) {
			Vertex copy = new Vertex(v); 
			addVertex(copy);
			vertices.put(copy.getId(), copy);
		}
		
		for(Edge e : g.getEdges()) {
			Vertex startVertex = vertices.get(e.getStartVertex().getId());
			Vertex endVertex = vertices.get(e.getEndVertex().getId());
			
			Edge newEdge = Edge.EdgeFactory.getInstance().copyEdge(e, startVertex, endVertex);
			if(newEdge.isDirected())
				addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.DIRECTED);
			else
				addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.UNDIRECTED);
		}
	}
	
	/**
	 * sets the states of all elements to UNVISITED
	 */
	public void resetStates() {
		for(Vertex v : getVertices())
			v.setState(ElementState.UNVISITED);
		
		for(Edge e : getEdges())
			e.setState(ElementState.UNVISITED);
	}

	/**
	 * this methods gives all edges between the given vertices
	 * @param v1 the start vertex
	 * @param v2 the end vertex
	 * @return returns a list of all edges that starts at vertex v1 and end at vertex v2
	 */
	public ArrayList<Edge> getEdgesFromTo(Vertex v1, Vertex v2) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		for(Edge e : getOutEdges(v1))
			if(e.getOtherEnd(v1) == v2)
				edges.add(e);
		
		return edges;
	}
	
	/**
	 * returns a string representation of the graph
	 */
	public String toString() {
		String out = "";
		
		out += "Vertices: "+ getVertices() +"\nEdges: "+ getEdges();
		
		return out;
	}
	
	/**
	 * returns the vertex according to the given id
	 * @param id the id of the vertex
	 * @return returns the appropriate vertex or null, if no vertex with that id is found
	 */
	public Vertex getVertexById(int id) {
		for(Vertex v : getVertices()) {
			if(v.getId() == id)
				return v;
		}
		
		return null;
	}
	
	/**
	 * constructs a graph object from the lists of string representations of vertices and edges 
	 * @param strVertices the list of vertices as string array
	 * @param strEdges the list of edges as string array
	 * @return returns the appropriate graph object
	 */
	public static Graph parseGraph(List<String[]> strVertices, List<String[]> strEdges) {
		Graph g = new Graph();
		HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
		
		for(String[] strVertex : strVertices) {
			Vertex v = Vertex.parseVertex(strVertex);
			g.addVertex(v);
			vertices.put(v.getId(), v);
		}
		
		for(String[] strEdge : strEdges) {
			int startId = Integer.parseInt(strEdge[4]);
			int endId = Integer.parseInt(strEdge[5]);
			Vertex startVertex = vertices.get(startId);
			Vertex endVertex = vertices.get(endId);
			
   			Edge newEdge = EdgeFactory.getInstance().parseEdge(strEdge, startVertex, endVertex);
			if(newEdge.isDirected())
				g.addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.DIRECTED);
			else
				g.addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.UNDIRECTED);
		}
		
		return g;
	}
}











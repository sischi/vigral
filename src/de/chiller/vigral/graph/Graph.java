package de.chiller.vigral.graph;


import java.util.ArrayList;

import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


public class Graph extends SparseMultigraph<Vertex, Edge> {

	public Graph() {
		super();
	}
	
	public Graph(Graph g) {
		super();
	
		for(Vertex v : g.getVertices())
			addVertex(new Vertex(v));
		
		for(Edge e : g.getEdges()) {
			Vertex startVertex = null, endVertex = null;
			for(Vertex v : getVertices()) {
				if(e.getStartVertex().getId() == v.getId())
					startVertex = v;
				if(e.getEndVertex().getId() == v.getId())
					endVertex = v;
				if(startVertex != null && endVertex != null)
					break;
			}
			
			Edge newEdge = new Edge(e.getId(), e.getWeight(), startVertex, endVertex, e.isDirected(), e.getState());
			if(newEdge.isDirected())
				addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.DIRECTED);
			else
				addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.UNDIRECTED);
		}
	}
	
	public void resetStates() {
		for(Vertex v : getVertices())
			v.setState(ElementState.UNVISITED);
	}

	public ArrayList<Edge> getEdgesFromTo(Vertex v1, Vertex v2) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		for(Edge e : getOutEdges(v1))
			if(e.getOtherEnd(v1) == v2)
				edges.add(e);
		
		return edges;
	}
	
	
	public String toString() {
		String out = "";
		
		for(Vertex v : getVertices())
			out = out +"\n"+ v.debug();
		
		return out;
	}
	
	public Vertex getVertexById(int id) {
		for(Vertex v : getVertices()) {
			if(v.getId() == id)
				return v;
		}
		
		return null;
	}
}

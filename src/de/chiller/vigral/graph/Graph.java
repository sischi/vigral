package de.chiller.vigral.graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.chiller.vigral.graph.Edge.EdgeFactory;
import edu.uci.ics.jung.graph.OrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


@SuppressWarnings("serial")
public class Graph extends OrderedSparseMultigraph<Vertex, Edge> {

	public Graph() {
		super();
	}
	
	public Graph(Graph g) {
		super();
	
		HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
		
		for(Vertex v : g.getVertices()) {
			Vertex copy = new Vertex(v); 
			addVertex(copy);
			vertices.put(copy.getId(), copy);
		}
		
		for(Edge e : g.getEdges()) {
//			Vertex startVertex = null, endVertex = null;
//			for(Vertex v : getVertices()) {
//				if(e.getStartVertex().getId() == v.getId())
//					startVertex = v;
//				else if(e.getEndVertex().getId() == v.getId())
//					endVertex = v;
//				if(startVertex != null && endVertex != null)
//					break;
//			}
			Vertex startVertex = vertices.get(e.getStartVertex().getId());
			Vertex endVertex = vertices.get(e.getEndVertex().getId());
			
			Edge newEdge = Edge.EdgeFactory.getInstance().copyEdge(e, startVertex, endVertex);
			if(newEdge.isDirected())
				addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.DIRECTED);
			else
				addEdge(newEdge, newEdge.getStartVertex(), newEdge.getEndVertex(), EdgeType.UNDIRECTED);
		}
		
		System.out.println("Graph copied!");
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











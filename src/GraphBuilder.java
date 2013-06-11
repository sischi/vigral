import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;


import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ViewScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class GraphBuilder {
	
	/**
	 * numerical value that will be considered in modifying the position of the vertices
	 */
	private static final int PADDING = 20;

	private Layout<Vertex, Edge> mLayout;
	/**
	 * the graph, that inherits the vertices and egdes
	 */
	private SparseMultigraph<Vertex, Edge> mGraph;
	/**
	 * responsible for the visualization of the graph
	 */
	private VisualizationViewer<Vertex, Edge> mVViewer;
	/**
	 * responsible for the GraphMousePlugins (Drawing with the mouse and context menus)
	 */
	private MyModalGraphMouse<Vertex, Edge> mGraphMouse; 
	
	public GraphBuilder() {
		System.out.println("GraphBuilder Creation");
		
		// create a graph
		mGraph = new SparseMultigraph<Vertex, Edge>();
		// add the graph to the layout
		mLayout = new StaticLayout<Vertex, Edge>(mGraph);
		// add the layout to the VisualizationViewer
		mVViewer = new VisualizationViewer<Vertex, Edge>(mLayout);
		
		System.out.println("layout: "+ mLayout.getSize());
		
		mGraphMouse = new MyModalGraphMouse<Vertex, Edge>(mVViewer.getRenderContext());
		mVViewer.setGraphMouse(mGraphMouse);
		mGraphMouse.setMode(ModalGraphMouse.Mode.EDITING);
		mVViewer.setBackground(Color.green);
		
		mVViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		mVViewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
		
		mVViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {
			@Override
			public String transform(Edge e) {
				return ""+ e.getWeight();
			}
		});
		mVViewer.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);
		
	}
	
	/**
	 * adds the VisualisationViewer to the given panel
	 * @param panel the panel represents the graph drawing panel
	 */
	public void addToPanel(JPanel panel) {
		panel.add(mVViewer);
		onResizePanel(panel);
	}
	
	
	/**
	 * called if the panel (the frame) is resized
	 * @param panel the given panel that shows the graph
	 */
	public void onResizePanel(JPanel panel) {
		System.out.println("onResize");
		Dimension dimen = new Dimension(panel.getBounds().width, panel.getBounds().height);
		System.out.println("panel dimension: "+ dimen.toString());
		mVViewer.setPreferredSize(dimen);
		//mLayout.setSize(dimen);
		Point2D p = new Point(0, 0);	
		
		modifyLocationsIfOutOfBounds();
	}
	
	/**
	 * is called when resizing the frame
	 * checks for all vertices if their position will be gone out of view. if so, the position will
	 * be modified to avoid disappearing of some vertices. This will ensure, that the complete graph
	 * is visible all the time.
	 */
	public void modifyLocationsIfOutOfBounds() {
		
		Graph<Vertex,Edge> graph = mVViewer.getModel().getGraphLayout().getGraph();
		if(!graph.getVertices().isEmpty()) {
			Dimension dimen = mVViewer.getSize();
			int i = 0;
			
			System.out.println("dimen: "+ dimen.toString());
			for(Vertex v : graph.getVertices()) {
				Point2D p = mLayout.transform(v);
				System.out.println("vertex "+ i++ +": ("+ p.getX() +", "+ p.getY() +")");
				double x = p.getX();
				double y = p.getY();
				double newX = x;
				double newY = y;
				
				if(x < PADDING)
					newX = PADDING;
				else if(x > dimen.width-PADDING)
					newX = dimen.width-PADDING;
				if(y < PADDING)
					newY = PADDING;
				else if(y > dimen.height-PADDING)
					newY = dimen.height-PADDING;
				
				if((newX != x) || (newY != y)) {
					p.setLocation(newX, newY);
					mLayout.setLocation(v, p);
				}
			}
		}
	}
	
	
	/**
	 * calculates the rectangle of the drawed graph
	 * @return the rectangle of the graph or null, if no vertex is present
	 */
	public Rectangle getGraphRect() {
		Graph<Vertex,Edge> graph = mVViewer.getModel().getGraphLayout().getGraph();
		if(!graph.getVertices().isEmpty()) {
			Dimension dimen = mVViewer.getSize();
			double minX = 0;
			double maxX = 0;
			double minY = 0;
			double maxY = 0;
			boolean initialised = false;
			int i = 0;
			
			for(Vertex v : graph.getVertices()) {
				Point2D p = mLayout.transform(v);
				double x = p.getX();
				double y = p.getY();
				System.out.println("vertex "+ i++ +": ("+ p.getX() +", "+ p.getY() +")");
				if(!initialised) {
					initialised = true;
					minX = x;
					maxX = minX;
					minY = y;
					maxY = minY;
				}
				else {
					if(x < minX)
						minX = x;
					else if(x > maxX)
						maxX = x;
					
					if(y < minY)
						minY = y;
					else if(y > maxY)
						maxY = y;
				}
			}
			return new Rectangle((int)minX, (int)minY, (int)(maxX - minX), (int)(maxY - minY));
		}
		else
			return null;
	}
	
	
	
	public void setMode(int mode) {
		if(mode == GraphCreationGUI.Mode.GRAPHCREATION)
			mGraphMouse.addEditingFunctionality();
		else if(mode == GraphCreationGUI.Mode.VISUALISATION) {
			mGraphMouse.removeEditingFunctionality();
		}
	}
	
	
	
}

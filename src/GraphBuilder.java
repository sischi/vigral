import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class GraphBuilder {
	
	/**
	 * numerical value that indicates the vertices radius
	 */
	private static final int VERTEXRADIUS = 15;
	/**
	 * numerical value that will be considered in modifying the position of the vertices
	 */
	private static final int PADDING = 10;

	private Layout<Vertex, Edge> mLayout;
	/**
	 * the graph, that inherits the vertices and egdes
	 */
	private SparseMultigraph<Vertex, Edge> mGraph;
	private SparseMultigraph<Vertex, Edge> mResultGraph;
	/**
	 * responsible for the visualization of the graph
	 */
	private VisualizationViewer<Vertex, Edge> mVViewer;
	/**
	 * responsible for the GraphMousePlugins (Drawing with the mouse and context menus)
	 */
	private MyModalGraphMouse<Vertex, Edge> mGraphMouse; 
	
	private Transformer<Vertex, Shape> mVertexShapeTransformer = new Transformer<Vertex, Shape>() {
		@Override
		public Shape transform(Vertex v) {
			Ellipse2D circle = new Ellipse2D.Double(-VERTEXRADIUS, -VERTEXRADIUS, 2*VERTEXRADIUS, 2*VERTEXRADIUS);
			//return AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
			return circle;
		}
	};

	private Transformer<Vertex, Paint> mVertexPaintTransformer = new Transformer<Vertex, Paint>() {
		@Override
		public Paint transform(Vertex v) {
			if(v.getCustomColor() != null)
				return v.getCustomColor();
			
			switch(v.getState()) {
			case UNVISITED:
				return Color.WHITE;
			case ACTIVE:
				return Color.PINK;
			case VISITED:
				return Color.CYAN;
			case FINISHED_AND_NOT_RELEVANT:
				return Color.LIGHT_GRAY;
			case FINISHED_AND_RELEVANT:
				return Color.RED;
			case PICKED:
				return Color.YELLOW;
			default:
				return Color.BLACK;
			}
		}
	};
	
	private Transformer<Edge, Paint> mEdgePaintTransformer = new Transformer<Edge, Paint>() {
		@Override
		public Paint transform(Edge e) {
			if(e.getCustomColor() != null)
				return e.getCustomColor();
			
			switch(e.getState()) {
			case UNVISITED:
				return Color.LIGHT_GRAY;
			case ACTIVE:
				return Color.PINK;
			case VISITED:
				return Color.CYAN;
			case FINISHED_AND_NOT_RELEVANT:
				return Color.LIGHT_GRAY;
			case FINISHED_AND_RELEVANT:
				return Color.RED;
			case PICKED:
				return Color.YELLOW;
			default:
				return Color.BLACK;
			}
		}
	};
	
	
	public GraphBuilder() {
		System.out.println("GraphBuilder Creation");
		
		// create a graph
		mGraph = new SparseMultigraph<Vertex, Edge>();
		mResultGraph = mGraph;
		// add the graph to the layout
		mLayout = new StaticLayout<Vertex, Edge>(mGraph);
		// add the layout to the VisualizationViewer
		mVViewer = new VisualizationViewer<Vertex, Edge>(mLayout);
		
		System.out.println("layout: "+ mLayout.getSize());
		
		mGraphMouse = new MyModalGraphMouse<Vertex, Edge>(mVViewer.getRenderContext());
		mVViewer.setGraphMouse(mGraphMouse);
		mGraphMouse.setMode(ModalGraphMouse.Mode.EDITING);		
		
		mVViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {
			@Override
			public String transform(Edge e) {
				return ""+ e.getWeight();
			}
		});
		
		mVViewer.setBackground(Color.WHITE);
		mVViewer.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);
		
		mVViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		mVViewer.getRenderContext().setVertexShapeTransformer(mVertexShapeTransformer);
		mVViewer.getRenderContext().setVertexFillPaintTransformer(mVertexPaintTransformer);
		mVViewer.getRenderContext().setEdgeDrawPaintTransformer(mEdgePaintTransformer);
		mVViewer.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(3.0f)));
		mVViewer.getRenderContext().setArrowFillPaintTransformer(mEdgePaintTransformer);
		mVViewer.getRenderContext().setEdgeFontTransformer(new ConstantTransformer(new Font("Helvetica", Font.PLAIN, 16)));
		//mVViewer.getRenderContext().setEdgeShapeTransformer(mEdgeShapeTransformer);

		mVViewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
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
		Dimension dimen = new Dimension(panel.getBounds().width, panel.getBounds().height);
		mVViewer.setPreferredSize(dimen);
		mVViewer.setSize(dimen);
		
		// possible reason for the modifylocationifoutofbounds not functioning properly !!!!! handle with care!!!!!
		//mLayout.setSize(dimen);
		
		//mVViewer.resize(dimen);
		modifyLocationsIfOutOfBounds(mGraph);
		modifyLocationsIfOutOfBounds(mResultGraph);
	}
	
	/**
	 * is called when resizing the frame
	 * checks for all vertices if their position will be gone out of view. if so, the position will
	 * be modified to avoid disappearing of some vertices. This will ensure, that the complete graph
	 * is visible all the time.
	 */
	public void modifyLocationsIfOutOfBounds(SparseMultigraph<Vertex, Edge> graph) {
		
		if(!graph.getVertices().isEmpty()) {
			Dimension dimen = mVViewer.getSize();
			int i = 0;
			
			System.out.println("dimen: "+ dimen.toString());
			for(Vertex v : graph.getVertices()) {
				Point2D p = mLayout.transform(v);
				p.setLocation(v.getLocation());
				System.out.println("vertex "+ i++ +": ("+ p.getX() +", "+ p.getY() +")");
				double x = p.getX();
				double y = p.getY();
				double newX = x;
				double newY = y;
				
				int min = PADDING + VERTEXRADIUS;
				int maxW = dimen.width - PADDING -VERTEXRADIUS;
				int maxH = dimen.height - PADDING - VERTEXRADIUS;
				if(x < min)
					newX = min;
				else if(x > maxW)
					newX = maxW;
				if(y < min)
					newY = min;
				else if(y > maxH)
					newY = maxH;
				
				if((newX != x) || (newY != y)) {
					p.setLocation(newX, newY);
					//v.updateLocation(p);
					//mLayout.setLocation(v, v.getLocation());
				}
			}
			mVViewer.repaint();
		}
	}
	
	
	/**
	 * calculates the rectangle of the drawed graph
	 * @return the rectangle of the graph or null, if no vertex is present
	 */
	public Rectangle getGraphRect() {
		if(!mGraph.getVertices().isEmpty()) {
			double minX = 0;
			double maxX = 0;
			double minY = 0;
			double maxY = 0;
			boolean initialised = false;
			int i = 0;
			
			for(Vertex v : mGraph.getVertices()) {
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
	
	public SparseMultigraph<Vertex, Edge> getGraph() {
		return mGraph;
	}
	
	public void setResultingGraph(Graph g) {
		mResultGraph = g.toSparseMultiGraph();
		mLayout.setGraph(mResultGraph);
		updateLocations();
		for(Vertex v : mVViewer.getGraphLayout().getGraph().getVertices())
			System.out.println(v.debug());

		mVViewer.repaint();
	}
	
	public void resetResultGraph() {
		mResultGraph = mGraph;
		mVViewer.repaint();
	}
	
	private void showOriginGraph() {
		mLayout.setGraph(mGraph);
		mVViewer.repaint();
	}
	
	private void showResultGraph() {
		mLayout.setGraph(mResultGraph);
		mVViewer.repaint();
	}
	
	public void updateLocations() {
		for(Vertex v : mResultGraph.getVertices()) {
			//mLayout.setLocation(v, v.getLocation());
			Point2D p = mLayout.transform(v);
			p.setLocation(v.getLocation());
		}
		modifyLocationsIfOutOfBounds(mResultGraph);
	}
	
	public void setMode(int mode) {
		if(mode == VigralGUI.Mode.GRAPHCREATION) {
			mGraphMouse.addEditingFunctionality();
			showOriginGraph();
			resetResultGraph();
		}
		else if(mode == VigralGUI.Mode.VISUALISATION) {
			mGraphMouse.removeEditingFunctionality();
			showResultGraph();
		}
	}
	
	public void resetVertexState() {
		for(Vertex v : mGraph.getVertices())
			v.setState(ElementState.UNVISITED);
	}
}

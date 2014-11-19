package de.chiller.vigral;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.ElementState;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.graph.Vertex;
import de.chiller.vigral.jung.MyColor;
import de.chiller.vigral.jung.MyModalGraphMouse;
import de.chiller.vigral.settings.Settings;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class GraphBuilder {
	
	/**
	 * numerical value that indicates the vertices radius
	 */
	private static final int VERTEXRADIUS = 20;
	/**
	 * numerical value that will be considered in modifying the position of the vertices
	 */
	private static final int PADDING = 10;

	private ArrayList<Layout<Vertex, Edge>> mLayoutList;
	
	
	/**
	 * the drawn or loaded graph
	 */
	private Graph mGraph;
	/**
	 * this graph represents the current step of the chosen algorithm
	 */
	private ArrayList<Graph> mResultingGraphs;
	
	
	
	/**
	 * responsible for the visualization of the graph
	 */
	private ArrayList<VisualizationViewer<Vertex, Edge>> mVViewerList;
	/**
	 * responsible for the GraphMousePlugins (Drawing with the mouse and context menus)
	 */
	private ArrayList<MyModalGraphMouse> mGraphMouseList;
	
	/**
	 * the settings instance
	 */
	private Settings mSettings = Settings.getInstance();
	
	
	
	
	
	// transformers that are responsible for the appearance of the vertices and edges
	private Transformer<Vertex, Paint> mVertexLineTransformer = new Transformer<Vertex, Paint>() {
		@Override
		public Paint transform(Vertex v) {
			return MyColor.LIGHT_GRAY;
		}
	};
	
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
			if(v.isPicked())
				return Color.decode(Settings.getInstance().getColor(Settings.COLOR_PICKED));
			
			if(v.getCustomColor() != null)
				return v.getCustomColor();
			
			return checkStateForColor(v.getState());
		}
	};
	
	private Transformer<Edge, Paint> mEdgePaintTransformer = new Transformer<Edge, Paint>() {
		@Override
		public Paint transform(Edge e) {
			if(e.getCustomColor() != null)
				return e.getCustomColor();
			
			return checkStateForColor(e.getState());
		}
	};
	
	private Transformer<Edge, String> mEdgeLabelTransformer = new Transformer<Edge, String>() {
		
		/*
		 * workaround!
		 * 
		 * ISSUE:
		 * changing the label size of edges in SettingsDialog will not take effect. Only if you change the view settings, the label size
		 * the edges is updated too.
		 * 
		 * REASON:
		 * the HTML string, that is returned by this transformer. If you return a static, normal, hardcoded string (e.g. "hello") the label
		 * size is updated every time when it is changed via the SettingsDialog.
		 * 
		 * WORKAROUND:
		 * every second time this transformer is asked to transform an edge to a string, append a whitespace. All the other times return just
		 * the desired string.
		 * This will force repainting of the edges because the label has changed
		 */
		private boolean mBOO = false;
		
		@Override
		public String transform(Edge e) {
			String lbl;
			int offset = 0;
			
			if(e.getCustomLabel().equals("")){
				
				lbl = "<html>";
				Settings settings = Settings.getInstance();
				
				
				if(settings.getView(Settings.VIEW_WEIGHT)) {
					offset++;
					double w = e.getWeight();
					if(w % 1 == 0)
						lbl += "w="+ (int) w;
					else
						lbl += "w="+ w;
				}
				if(settings.getView(Settings.VIEW_MIN_CAPACITY)) {
					if(!lbl.equals("<html>"))
						lbl += "<br />";
					offset++;
					double c = e.getMinCapacity();
					if(c % 1 == 0)
						lbl += "min C="+ (int) c;
					else
						lbl += "min C="+ c;
				}
				if(settings.getView(Settings.VIEW_MAX_CAPACITY)) {
					if(!lbl.equals("<html>"))
						lbl += "<br />";
					offset++;
					double c = e.getMaxCapacity();
					if(c % 1 == 0)
						lbl += "max C="+ (int) c;
					else
						lbl += "max C="+ c;
				}
				lbl += "</html>";
				
				
				if(mBOO) {
					mBOO = false;
					lbl += " ";
				}
				else {
					mBOO = true;
				}
			}
			else
			{
				lbl = e.getCustomLabel();
			}
			
			// set the label offset according to the number of lines of the label and edge label font size (center the label)
			for(int i = 0; i < mVViewerList.size(); i++)
				mVViewerList.get(i).getRenderContext().setLabelOffset(offset * mSettings.getLabelSize(Settings.LABEL_EDGE));
			
			return lbl;
		}
	};
	
	private Transformer<Vertex, String> mVertexLabelTransformer = new Transformer<Vertex, String>() {
		@Override
		public String transform(Vertex v) {
			if(v.getLabel().equals(""))
				return "<html>"+ v.getIdentifier() +"<br />"+ v.getLabelAddition() +"</html>";
			else
				return "<html>"+ v.getLabel() +"<br />"+ v.getLabelAddition() +"</html>";
		}
	};
	
	
	private Transformer<Edge, Font> mEdgeFontTransformer = new Transformer<Edge, Font>() {
		
		@Override
		public Font transform(Edge e) {
			return new Font("Helvetica", Font.PLAIN, mSettings.getLabelSize(Settings.LABEL_EDGE));
		}
	};
	
	private Transformer<Vertex, Font> mVertexFontTransformer = new Transformer<Vertex, Font>() {
		@Override
		public Font transform(Vertex v) {
			return new Font("Helvetica", Font.PLAIN, mSettings.getLabelSize(Settings.LABEL_VERTEX));
		}
	};
	
	
	
	
	
	
	
	
	/**
	 * this method returns the color according to the given state
	 * @param state the ElementState
	 * @return the color of 'state' set by the user
	 */
	public Paint checkStateForColor(ElementState state) {
		switch(state) {
		case UNVISITED:
			return Color.decode(Settings.getInstance().getColor(Settings.COLOR_UNVISITED));
		case ACTIVE:
			return Color.decode(Settings.getInstance().getColor(Settings.COLOR_ACTIVE));
		case VISITED:
			return Color.decode(Settings.getInstance().getColor(Settings.COLOR_VISITED));
		case FINISHED_AND_NOT_RELEVANT:
			return Color.decode(Settings.getInstance().getColor(Settings.COLOR_FINISHED_AND_NOT_RELEVANT));
		case FINISHED_AND_RELEVANT:
			return Color.decode(Settings.getInstance().getColor(Settings.COLOR_FINISHED_AND_RELEVANT));
		default:
			return Color.decode(Settings.getInstance().getColor(Settings.COLOR_UNVISITED));
		}
	}
	
	/**
	 * constructs the GraphBuilder
	 */
	public GraphBuilder() {
		// create a graph
		mGraph = new Graph();
		mResultingGraphs = new ArrayList<Graph>();
		mResultingGraphs.add(mGraph);
		
		mVViewerList = new ArrayList<VisualizationViewer<Vertex, Edge>>();
		mLayoutList = new ArrayList<Layout<Vertex, Edge>>();
		mGraphMouseList = new ArrayList<MyModalGraphMouse>();
		
		/*
		// add the graph to the layout
		mLayout = new StaticLayout<Vertex, Edge>(mGraph);
		// add the layout to the VisualizationViewer
		mVViewer1 = new VisualizationViewer<Vertex, Edge>(mLayout);
		
		// create mouse handler
		mGraphMouse = new MyModalGraphMouse(mVViewer1.getRenderContext());
		mVViewer1.setGraphMouse(mGraphMouse);
		mVViewer1.setFocusable(true);
		mGraphMouse.setMode(ModalGraphMouse.Mode.EDITING);
		
		
		mVViewer1.setBackground(Color.WHITE);
		
		// initialize the edge renderer
		mVViewer1.getRenderContext().setEdgeLabelTransformer(mEdgeLabelTransformer);
		mVViewer1.getRenderContext().setEdgeDrawPaintTransformer(mEdgePaintTransformer);
		mVViewer1.getRenderContext().setArrowDrawPaintTransformer(mEdgePaintTransformer);
		mVViewer1.getRenderContext().setArrowFillPaintTransformer(mEdgePaintTransformer);
		mVViewer1.getRenderContext().setEdgeArrowTransformer(new DirectionalEdgeArrowTransformer<Vertex, Edge>(20, 16, 10));
		
		mVViewer1.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(3.0f)));
		mVViewer1.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(true);
		mVViewer1.getRenderContext().setEdgeFontTransformer(mEdgeFontTransformer);
		mVViewer1.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<Vertex, Edge>(.5, .5));
		
		// initialize the vertex renderer
		mVViewer1.getRenderContext().setVertexLabelTransformer(mVertexLabelTransformer);
		mVViewer1.getRenderContext().setVertexShapeTransformer(mVertexShapeTransformer);
		mVViewer1.getRenderContext().setVertexFillPaintTransformer(mVertexPaintTransformer);
		mVViewer1.getRenderContext().setVertexDrawPaintTransformer(mVertexLineTransformer);
		mVViewer1.getRenderContext().setVertexFontTransformer(mVertexFontTransformer);
		mVViewer1.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
		*/
		
		mLayoutList.add(new StaticLayout<Vertex, Edge>(mGraph));
		mLayoutList.add(new StaticLayout<Vertex, Edge>(new Graph()));
		
		for(int i = 0; i < mLayoutList.size(); i++) {
			mVViewerList.add(new VisualizationViewer<Vertex, Edge>(mLayoutList.get(i)));
			mGraphMouseList.add(new MyModalGraphMouse(mVViewerList.get(i).getRenderContext()));
			initViewerLayout(mVViewerList.get(i), mLayoutList.get(i), mGraphMouseList.get(i));
		}
		
		// because the index 0 is for the graph drawing pane ...
		// mGraphMouseList.get(0).addEditingFunctionality();
		
	}

	
	
	private void initViewerLayout(VisualizationViewer<Vertex, Edge> vv, Layout<Vertex, Edge> layout, MyModalGraphMouse graphMouse) {
		
			

		vv.setGraphMouse(graphMouse);
		vv.setFocusable(true);
		
		vv.setBackground(Color.WHITE);
		
		// initialize the edge renderer
		vv.getRenderContext().setEdgeLabelTransformer(mEdgeLabelTransformer);
		vv.getRenderContext().setEdgeDrawPaintTransformer(mEdgePaintTransformer);
		vv.getRenderContext().setArrowDrawPaintTransformer(mEdgePaintTransformer);
		vv.getRenderContext().setArrowFillPaintTransformer(mEdgePaintTransformer);
		vv.getRenderContext().setEdgeArrowTransformer(new DirectionalEdgeArrowTransformer<Vertex, Edge>(20, 16, 10));
		vv.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(3.0f)));
		vv.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(true);
		vv.getRenderContext().setEdgeFontTransformer(mEdgeFontTransformer);
		vv.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<Vertex, Edge>(.5, .5));
		
		// initialize the vertex renderer
		vv.getRenderContext().setVertexLabelTransformer(mVertexLabelTransformer);
		vv.getRenderContext().setVertexShapeTransformer(mVertexShapeTransformer);
		vv.getRenderContext().setVertexFillPaintTransformer(mVertexPaintTransformer);
		vv.getRenderContext().setVertexDrawPaintTransformer(mVertexLineTransformer);
		vv.getRenderContext().setVertexFontTransformer(mVertexFontTransformer);
		vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
		
		graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
		graphMouse.removeEditingFunctionality();
	}
	
	
	
	/**
	 * adds the VisualisationViewer to the given panel
	 * @param panel the panel represents the graph drawing panel
	 */
	public void addToPanel(JPanel panel1, JPanel panel2) {
		panel1.add(mVViewerList.get(0));
		panel2.add(mVViewerList.get(1));
		onResizePanel(panel1, 0);
		onResizePanel(panel2, 1);
	}
	
	
	/**
	 * called if the panel (the frame) is resized
	 * @param panel the given panel that shows the graph
	 * @param the number of the given panel starting by 0
	 */
	public void onResizePanel(JPanel panel, int panel_nr) {
		// get current dimension of the panel
		Dimension dimen = new Dimension(panel.getBounds().width, panel.getBounds().height);
		
		// TODO: check how and if its necessary to check size of the optional graph panel
		// resize the drawing space
		mVViewerList.get(panel_nr).setPreferredSize(dimen);
		mVViewerList.get(panel_nr).setSize(dimen);
		
		// possible reason for the modifylocationifoutofbounds not functioning properly !!!!! handle with care!!!!!
		//mLayout.setSize(dimen);
		
		//mVViewer.resize(dimen);
		// resize graph if needed
		if(panel_nr == 0)
			modifyLocationsIfOutOfBounds(mGraph, 0);
		
		for(int i = 0; i < mResultingGraphs.size(); i++)
			modifyLocationsIfOutOfBounds(mResultingGraphs.get(i), i);
		
		//modifyLocationsIfOutOfBounds(mResultingGraphs.get(1));
	}
	
	/**
	 * is called when resizing the frame
	 * checks for all vertices if their position will be gone out of view. if so, the position will
	 * be modified to avoid disappearing of some vertices. This will ensure, that the complete graph
	 * is visible all the time.
	 * @param graph the graph to the size of
	 * @param number the number of that graph referring to the panel that shows the graph (0 or 1)
	 */
	private void modifyLocationsIfOutOfBounds(Graph graph, int number) {
		
		if(!graph.getVertices().isEmpty()) {
			
			// TODO check
			Dimension dimen = mVViewerList.get(number).getSize();
			int i = 0;
			
			//System.out.println("dimen: "+ dimen.toString());
			for(Vertex v : graph.getVertices()) {
				
				// TODO check
				Point2D p = mLayoutList.get(number).transform(v);
				p.setLocation(v.getLocation());
				//System.out.println("vertex "+ i++ +": ("+ p.getX() +", "+ p.getY() +")");
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
			
			// TODO check
			mVViewerList.get(number).repaint();
		}
	}
	
	
	/**
	 * calculates the rectangle of the drawn graph
	 * @return the rectangle of the graph or null, if no vertex is present
	 */
	private Rectangle getGraphRect() {
		if(!mGraph.getVertices().isEmpty()) {
			double minX = 0;
			double maxX = 0;
			double minY = 0;
			double maxY = 0;
			boolean initialised = false;
			int i = 0;
			
			for(Vertex v : mGraph.getVertices()) {
				
				// TODO check
				Point2D p = mLayoutList.get(0).transform(v);
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
	
	/**
	 * getter for the graph of creation mode
	 * @return returns a copy of the graph
	 */
	public Graph getGraph() {
		return new Graph(mGraph);
	}
	
	/**
	 * setter for the displayed graph in visualization mode
	 * @param g the graph that will be copied and displayed
	 */
	public void setResultingGraph(ArrayList<Graph> graphs) {
		mResultingGraphs.clear();
		for(int i = 0; i < graphs.size(); i++)
			mResultingGraphs.add(i, new Graph(graphs.get(i)));
		
		showResultGraph();
	}
	

	private void resetResultGraph() {
		mResultingGraphs.clear();
		
		mResultingGraphs.add(mGraph);
		mResultingGraphs.add(new Graph());
		
		for(int i = 0; i < mVViewerList.size(); i++)
			mVViewerList.get(i).repaint();
	}
	
	private void showOriginGraph() {
		mLayoutList.get(0).setGraph(mGraph);
		updateLocations();
		mVViewerList.get(0).repaint();
		
		//mResultingGraphs.clear();
		//mResultingGraphs.add(mGraph);
	}
	
	private void showResultGraph() {
		
		for(int i = 0; i < mResultingGraphs.size(); i++)
			mLayoutList.get(i).setGraph(mResultingGraphs.get(i));
		
		updateLocations();
		
		for(int i = 0; i < mResultingGraphs.size(); i++)
			mVViewerList.get(i).repaint();
		
	}
	
	
	private void updateLocations() {
		for(int i = 0; i < mVViewerList.size(); i++) {
			for(Vertex v : mVViewerList.get(i).getGraphLayout().getGraph().getVertices()) {
				//mLayout.setLocation(v, v.getLocation());
				Point2D p = mLayoutList.get(i).transform(v);
				p.setLocation(v.getLocation());
			}
			modifyLocationsIfOutOfBounds((Graph) mVViewerList.get(i).getGraphLayout().getGraph(), i);
		}
	}
	
	/**
	 * sets the mode
	 * @param mode the mode to be set according to VigralGUI modes
	 */
	public void setMode(int mode) {
		// toggle mouse editing functionality
		if(mode == VigralGUI.Mode.GRAPHCREATION) {
			mGraphMouseList.get(0).addEditingFunctionality();
			// to default to the drawn graph in the first and to an empty graph in the second panel
			resetResultGraph();
			showResultGraph();
			// to show the drawn graph in the first panel and to be able to edit this one
			showOriginGraph();
		}
		else if(mode == VigralGUI.Mode.VISUALISATION) {
			mGraphMouseList.get(0).removeEditingFunctionality();
			showResultGraph();
		}
	}
	
//	/**
//	 * 
//	 */
//	public void resetVertexState() {
//		for(Vertex v : mGraph.getVertices())
//			v.setState(ElementState.UNVISITED);
//	}
	
	/**
	 * displays the given graph
	 * @param g the graph that will be copied and displayed
	 */
	public void setGraph(Graph g) {
		mGraph = new Graph(g);
		showOriginGraph();
	}
	
	/**
	 * displays a new, empty graph
	 */
	public void resetGraph() {
		mGraph = new Graph();
		mLayoutList.get(0).setGraph(mGraph);
		mVViewerList.get(0).repaint();
		resetResultGraph();
	}
	
	/**
	 * getter for the VisualizationViewer
	 * @return the VisualizationViewer
	 */
	public VisualizationViewer<Vertex, Edge> getVisualizationViewer() {
		return mVViewerList.get(0);
	}
	
	/**
	 * tells the graphbuilder to redraw the graph
	 */
	public void redraw() {
		for(int i = 0; i < mVViewerList.size(); i++)
			mVViewerList.get(i).repaint();
	}
	
}

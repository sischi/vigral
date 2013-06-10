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
	
	private static final int PADDING = 20;

	private Layout<Vertex, Edge> mLayout;
	private SparseMultigraph<Vertex, Edge> mGraph;
	private VisualizationViewer<Vertex, Edge> mVViewer;
	private MyModalGraphMouse<Vertex, Edge> mGraphMouse; 
	
	public GraphBuilder(JPanel panel) {
		System.out.println("GraphBuilder Creation");
		
		mGraph = new SparseMultigraph<Vertex, Edge>();
		mLayout = new StaticLayout<Vertex, Edge>(mGraph);
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
		
		panel.add(mVViewer);
		
		onResizePanel(panel);
	}
	
	
	public void onResizePanel(JPanel panel) {
		System.out.println("onResize");
		Dimension dimen = new Dimension(panel.getBounds().width, panel.getBounds().height);
		System.out.println("panel dimension: "+ dimen.toString());
		mVViewer.setPreferredSize(dimen);
		//mLayout.setSize(dimen);
		Point2D p = new Point(0, 0);	
		
		resizeGraph();
	}
	
	
	
	public void resizeGraph() {
		
		modifyLocationsIfOutOfBounds();
		
	}
	
	
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
	
	
	
	
}

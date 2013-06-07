import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;


public class GraphBuilder {

	private Layout<Node, Edge> mLayout;
	private SparseMultigraph<Node, Edge> mGraph;
	private VisualizationViewer<Node, Edge> mVViewer;
	private MyModalGraphMouse<Node, Edge> mGraphMouse; 
	
	private Factory<Node> mNodeFactory;
	private Factory<Edge> mEdgeFactory;
	
	private int nodeCount;
	private int edgeCount;
	
	public GraphBuilder(JPanel panel) {
		System.out.println("GraphBuilder Creation");
		
		mGraph = new SparseMultigraph<Node, Edge>();
		mLayout = new StaticLayout<Node, Edge>(mGraph);
		mVViewer = new VisualizationViewer<Node, Edge>(mLayout);
		mLayout.setSize(new Dimension(500, 500));
		
		System.out.println("layout: "+ mLayout.getSize());
		
		mNodeFactory = new Factory<Node>() {
			@Override
			public Node create() {
				return new Node();
			}
		};
		mEdgeFactory = new Factory<Edge>() {
			@Override
			public Edge create() {
				return new Edge();
			}
		};
		
		mGraphMouse = new MyModalGraphMouse<Node, Edge>(mVViewer.getRenderContext(), mNodeFactory, mEdgeFactory);
		mVViewer.setGraphMouse(mGraphMouse);
		mGraphMouse.setMode(ModalGraphMouse.Mode.EDITING);
		mVViewer.setBackground(Color.green);
		
		panel.add(mVViewer);
		
		//onResizePanel(panel);
	}
	
	
	public void onResizePanel(JPanel panel) {
		System.out.println("onResize");
		Dimension dimen = new Dimension(panel.getBounds().width, panel.getBounds().height);
		System.out.println("panel dimension: "+ dimen.toString());
		//mLayout.setSize(dimen);
	}
	
}

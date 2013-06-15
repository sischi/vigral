import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.util.ArrowFactory;


public class EditSupport<V,E> {
	
	protected V mStartVertex;
    protected Point2D mDown;
    
    protected CubicCurve2D mRawEdge = new CubicCurve2D.Float();
    protected Shape mEdgeShape;
    protected Shape mRawArrowShape;
    protected Shape mArrowShape;
    protected VisualizationServer.Paintable mEdgePaintable;
    protected VisualizationServer.Paintable mArrowPaintable;
    protected EdgeType mEdgeIsDirected;

	
	/**
     * Used for the edge creation visual effect during mouse drag
     */
    class EdgePaintable implements VisualizationServer.Paintable {
        
        public void paint(Graphics g) {
            if(mEdgeShape != null) {
                Color oldColor = g.getColor();
                g.setColor(Color.black);
                ((Graphics2D)g).draw(mEdgeShape);
                g.setColor(oldColor);
            }
        }
        
        public boolean useTransform() {
            return false;
        }
    }
    
    /**
     * Used for the directed edge creation visual effect during mouse drag
     */
    class ArrowPaintable implements VisualizationServer.Paintable {
        
        public void paint(Graphics g) {
            if(mArrowShape != null) {
                Color oldColor = g.getColor();
                g.setColor(Color.black);
                ((Graphics2D)g).fill(mArrowShape);
                g.setColor(oldColor);
            }
        }
        
        public boolean useTransform() {
            return false;
        }
    }
    
    
	/**
	 * constructor
	 */
	public EditSupport() {
        mRawEdge.setCurve(0.0f, 0.0f, 0.33f, 100, .66f, -50, 1.0f, 0.0f);
        mRawArrowShape = ArrowFactory.getNotchedArrow(20, 16, 8);
        mEdgePaintable = new EdgePaintable();
        mArrowPaintable = new ArrowPaintable();
        mEdgeIsDirected = EdgeType.UNDIRECTED;
	}
	
	/**
	 * make ready to draw an edge
	 * @param e the mouseevent
	 * @param vertex the start vertex of the edge
	 * @param directed true if it is an directed edge and false otherwise
	 */
	public void startEdge(MouseEvent e, V vertex, EdgeType directed) {
		
		mEdgeIsDirected = directed;
		
		// get the clicked vv and the coordinates
    	final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
        final Point2D p = e.getPoint();
        
        // get an instance of the graphelementaccessor
        GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
        
        mStartVertex = vertex;
        mDown = e.getPoint();
        transformEdgeShape(mDown, mDown);
        if(mEdgeIsDirected == EdgeType.DIRECTED)
        	transformArrowShape(mDown, mDown);
        
        vv.addPostRenderPaintable(mEdgePaintable);
	}
	

	/**
	 * draw the edge from start vertex to mouse cursor
	 * @param e the mouse event
	 */
	public void drawEdge(MouseEvent e) {
		if(mStartVertex != null) {
            transformEdgeShape(mDown, e.getPoint());
            
            if(mEdgeIsDirected == EdgeType.DIRECTED)
                transformArrowShape(mDown, e.getPoint());

        }
        VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
        vv.repaint();
	}

	/**
	 * adds a new vertex
	 * @param e the mouse event
	 * @param vv the visualisation viewer
	 */
	public void addVertex(MouseEvent e, VisualizationViewer<V, E> vv) {
		
		// get the graph
    	Graph<V,E> graph = vv.getModel().getGraphLayout().getGraph();
		
    	Vertex.VertexFactory.getInstance().setLocation(e.getPoint());
		V newVertex = (V) Vertex.VertexFactory.getInstance().create();
		
        Layout<V,E> layout = vv.getModel().getGraphLayout();
        graph.addVertex(newVertex);
        layout.setLocation(newVertex, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint()));
		
	}
	
	
	/**
	 * adds an edge to the graph
	 * @param e the mouse event
	 * @param p the the point where the draw drag ended
	 * @param vertex the end vertex
	 * @param vv the visualisation viewer
	 */
	public void addEdge(MouseEvent e, Point2D p, V vertex, VisualizationViewer vv) {
		
		if((vertex != null) && (mStartVertex != null)) {
			if(!(mDown.getX() == p.getX() && mDown.getY() == p.getY())) {
	    		Graph<V,E> graph = vv.getGraphLayout().getGraph();
	    		Edge.EdgeFactory.getInstance().setStartAndEnd(mStartVertex, vertex);
	    		if(mEdgeIsDirected == EdgeType.DIRECTED)
	    			Edge.EdgeFactory.getInstance().setDirected(true);
	    		else
	    			Edge.EdgeFactory.getInstance().setDirected(false);
	    		graph.addEdge((E) Edge.EdgeFactory.getInstance().create(), mStartVertex, vertex, mEdgeIsDirected);
	    	}
		}
        vv.repaint();
        mStartVertex = null;
        mDown = null;
        mEdgeIsDirected = EdgeType.UNDIRECTED;
        vv.removePostRenderPaintable(mEdgePaintable);
        vv.removePostRenderPaintable(mArrowPaintable);
	}
	
	
	/**
     * code lifted from PluggableRenderer to move an edge shape into an
     * arbitrary position
     */
    private void transformEdgeShape(Point2D down, Point2D out) {
        float x1 = (float) down.getX();
        float y1 = (float) down.getY();
        float x2 = (float) out.getX();
        float y2 = (float) out.getY();

        AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);
        
        float dx = x2-x1;
        float dy = y2-y1;
        float thetaRadians = (float) Math.atan2(dy, dx);
        xform.rotate(thetaRadians);
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        xform.scale(dist / mRawEdge.getBounds().getWidth(), 1.0);
        mEdgeShape = xform.createTransformedShape(mRawEdge);
    }
    private void transformArrowShape(Point2D down, Point2D out) {
        float x1 = (float) down.getX();
        float y1 = (float) down.getY();
        float x2 = (float) out.getX();
        float y2 = (float) out.getY();

        AffineTransform xform = AffineTransform.getTranslateInstance(x2, y2);
        
        float dx = x2-x1;
        float dy = y2-y1;
        float thetaRadians = (float) Math.atan2(dy, dx);
        xform.rotate(thetaRadians);
        mArrowShape = xform.createTransformedShape(mRawArrowShape);
    }
}

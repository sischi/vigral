package de.chiller.vigral.jung;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.GraphElement;
import de.chiller.vigral.graph.Vertex;


import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.picking.PickedState;


public class PickSupport {
	
	protected Rectangle2D mRect;
    protected Edge mPickedEdge;
    protected Vertex mPickedVertex;
    protected Point2D mDown;
    /**
     * the x distance from the picked vertex center to the mouse point
     */
    protected double mOffsetx;
    
    /**
     * the y distance from the picked vertex center to the mouse point
     */
    protected double mOffsety;
    /**
     * the Paintable for the lens picking rectangle
     */
    protected Paintable mLensPaintable;
    /**
     * color for the picking rectangle
     */
    protected Color mLensColor;
    
    
    
    
    /**
     * a paintable to draw a rect to pick multiple vertices
     */
    class LensPaintable implements Paintable {

        public void paint(Graphics g) {
            Color oldColor = g.getColor();
            g.setColor(mLensColor);
            ((Graphics2D)g).draw(mRect);
            g.setColor(oldColor);
        }

        public boolean useTransform() {
            return false;
        }
    }
    
    /**
     * default constructor
     */
    public PickSupport() {
    	mLensPaintable = new LensPaintable();
    	mRect = new Rectangle2D.Float();
    	mLensColor = Color.cyan;
    }
    
    
    
    /**
     * picks a vertex by clicking on one
     * @param vv the visualisation viewer
     * @param vertex the clicked vertex
     * @param p the clicked point
     */
    public void pickVertex(VisualizationViewer<Vertex, Edge> vv, Vertex vertex, Point2D p) {
    	mPickedVertex = vertex;
    	mDown = p;
        Layout<Vertex,Edge> layout = vv.getGraphLayout();
    	
        PickedState<Vertex> pickedVertexState = vv.getPickedVertexState();
    	if(pickedVertexState.isPicked(mPickedVertex) == false) {
        	pickedVertexState.clear();
        	clearPickedCollection(vv);
        	mPickedVertex.setPicked(true);
        	pickedVertexState.pick(mPickedVertex, true);
        }
        
        // layout.getLocation applies the layout transformer so
        // q is transformed by the layout transformer only
        Point2D q = layout.transform(mPickedVertex);
        // transform the mouse point to graph coordinate system
        Point2D gp = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.LAYOUT, p);

        mOffsetx = (float) (gp.getX()-q.getX());
        mOffsety = (float) (gp.getY()-q.getY());
    }
    
    /**
     * prepares to draw the rect to pick multiple vertices
     * @param vv the visualisation viewer
     * @param p the start point of the rect
     */
    public void prepareToDrawRect(VisualizationViewer<Vertex, Edge> vv, Point2D p) {
    	mPickedVertex = null;
    	
    	mDown = p;
    	mRect.setFrameFromDiagonal(mDown,mDown);
    	
    	PickedState<Vertex> pickedVertexState = vv.getPickedVertexState();
        PickedState<Edge> pickedEdgeState = vv.getPickedEdgeState();
    	
    	vv.addPostRenderPaintable(mLensPaintable);
   	    pickedEdgeState.clear();
        pickedVertexState.clear();
    }

    
    /**
     * performs the drag gesture of picking
     * @param vv the visualisation viewer
     * @param p the actual position
     */
    public void performDrag(VisualizationViewer<Vertex, Edge> vv, Point2D p) {
    	if(mPickedVertex != null)
    		moveVertex(vv, p);
    	else
    		updateRect(vv, p);
    	
    	vv.repaint();
    }
    
    /**
     * draws a rect in case of dragging
     * @param vv the visualisation viewer
     * @param p the actual position
     */
    public void updateRect(VisualizationViewer<Vertex, Edge> vv, Point2D p) {
    	if(mPickedVertex == null)
    		mRect.setFrameFromDiagonal(mDown, p);
    }
    
    /**
     * moves a vertex
     * @param vv the visualisation viewer
     * @param p the actual position
     */
    public void moveVertex(VisualizationViewer<Vertex, Edge> vv, Point2D p) {
    	if(mPickedVertex != null) {
    		
	        Point2D graphPoint = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(p);
	        Point2D graphDown = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(mDown);
	        Layout<Vertex,Edge> layout = vv.getGraphLayout();
	        double dx = graphPoint.getX()-graphDown.getX();
	        double dy = graphPoint.getY()-graphDown.getY();
	        PickedState<Vertex> pickedState = vv.getPickedVertexState();
	        
	        for(Vertex v : pickedState.getPicked()) {
	            Point2D vertexPoint = layout.transform(v);
	            ((Vertex)v).updateLocation(vertexPoint);
	            vertexPoint.setLocation(vertexPoint.getX()+dx, vertexPoint.getY()+dy);
	            layout.setLocation(v, vertexPoint);
	        }
	        
	        
	        mDown = p;
    	}
    }
    
    /**
     * called when releasing the mouse in rect mode and pick all vertices contained by the rect
     * @param vv the visualisation viewer
     * @param p the end point of the rect
     */
    public void pickVerticesInRect(VisualizationViewer<Vertex, Edge> vv, Point2D p) {
    	if(mDown != null && mPickedVertex == null && heyThatsTooClose(mDown, p, 5) == false) {
    		pickContainedVertices(vv, mDown, p, true);
    	}
    	mDown = null;
        mPickedVertex = null;
        mPickedEdge = null;
        mRect.setFrame(0,0,0,0);
        vv.removePostRenderPaintable(mLensPaintable);
        vv.repaint();
    }
    
    /**
     * pick the vertices inside the rectangle created from points
     * 'down' and 'out'
     *
     */
    protected void pickContainedVertices(VisualizationViewer<Vertex,Edge> vv, Point2D down, Point2D out, boolean clear) {
        
        Layout<Vertex,Edge> layout = vv.getGraphLayout();
        PickedState<Vertex> pickedVertexState = vv.getPickedVertexState();
        
        Rectangle2D pickRectangle = new Rectangle2D.Double();
        pickRectangle.setFrameFromDiagonal(down,out);
         
        if(pickedVertexState != null) {
            if(clear) {
            	pickedVertexState.clear();
            }
            GraphElementAccessor<Vertex,Edge> pickSupport = vv.getPickSupport();

            Collection<Vertex> picked = pickSupport.getVertices(layout, pickRectangle);
            for(Vertex v : picked) {
            	pickedVertexState.pick(v, true);
            	((GraphElement) v).setPicked(true);
            }
        }
    }
    
    
    
    public void clearPickedCollection(VisualizationViewer<Vertex, Edge> vv) {
    	vv.getPickedVertexState().clear();
    	for(Vertex v : vv.getGraphLayout().getGraph().getVertices())
    		((GraphElement) v).setPicked(false);
        vv.getPickedEdgeState().clear();
    }
    
    
    public void addToSelection(Vertex v, VisualizationViewer<Vertex, Edge> vv, Point2D p) {
    	PickedState<Vertex> pickedVertexState = vv.getPickedVertexState();
    	mPickedVertex = v;
    	mDown = p;
    	
    	if(pickedVertexState.isPicked(mPickedVertex) == false) {
        	pickedVertexState.pick(mPickedVertex, true);
        	((Vertex) mPickedVertex).setPicked(true);
        }
    }
    
    
    
    
    
    /**
     * rejects picking if the rectangle is too small, like
     * if the user meant to select one vertex but moved the
     * mouse slightly
     * @param p
     * @param q
     * @param min
     * @return
     */
    private boolean heyThatsTooClose(Point2D p, Point2D q, double min) {
        return Math.abs(p.getX()-q.getX()) < min && Math.abs(p.getY()-q.getY()) < min;
    }
    
	
}

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;


import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.layout.PersistentLayout.Point;
import edu.uci.ics.jung.visualization.picking.PickedState;


public class PickSupport<V,E> {
	
	protected Rectangle2D mRect;
    protected E mPickedEdge;
    protected V mPickedVertex;
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
     * a Paintable to draw the rectangle used to pick multiple
     * Vertices
     * @author Tom Nelson
     *
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
    
    
    public PickSupport() {
    	mLensPaintable = new LensPaintable();
    	mRect = new Rectangle2D.Float();
    	mLensColor = Color.cyan;
    }
    
    
    public void mousePressed(MouseEvent e) {
    	
    	// get the clicked vv and the coordinates
    	final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
    	
    	// p is the screen point for the mouse event
        Point2D p = e.getPoint();
        
        mDown = p;
    	
    	// get an instance of the graphelementaccessor
        GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
    	
        PickedState<V> pickedVertexState = vv.getPickedVertexState();
        PickedState<E> pickedEdgeState = vv.getPickedEdgeState();
        Layout<V,E> layout = vv.getGraphLayout();
        
        mRect.setFrameFromDiagonal(mDown,mDown);
        

        mPickedVertex = pickSupport.getVertex(layout, p.getX(), p.getY());
        if(mPickedVertex != null) {
            if(pickedVertexState.isPicked(mPickedVertex) == false) {
            	pickedVertexState.clear();
            	pickedVertexState.pick(mPickedVertex, true);
            }
            
            // layout.getLocation applies the layout transformer so
            // q is transformed by the layout transformer only
            Point2D q = layout.transform(mPickedVertex);
            // transform the mouse point to graph coordinate system
            Point2D gp = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.LAYOUT, p);

            mOffsetx = (float) (gp.getX()-q.getX());
            mOffsety = (float) (gp.getY()-q.getY());
        } else if((mPickedEdge = pickSupport.getEdge(layout, p.getX(), p.getY())) != null) {
            pickedEdgeState.clear();
            pickedEdgeState.pick(mPickedEdge, true);
        } else {
        	/*
            vv.addPostRenderPaintable(mLensPaintable);
       	    pickedEdgeState.clear();
            pickedVertexState.clear();
            */
        }
    }
    
    
    public void pickVertex(VisualizationViewer vv, V vertex, Point2D p) {
    	mPickedVertex = vertex;
    	mDown = p;
        Layout<V,E> layout = vv.getGraphLayout();
    	
        PickedState<V> pickedVertexState = vv.getPickedVertexState();
    	if(pickedVertexState.isPicked(mPickedVertex) == false) {
        	pickedVertexState.clear();
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
    
    
    public void prepareToDrawRect(VisualizationViewer vv, Point2D p) {
    	mPickedVertex = null;
    	
    	mDown = p;
    	mRect.setFrameFromDiagonal(mDown,mDown);
    	
    	GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
    	
        PickedState<V> pickedVertexState = vv.getPickedVertexState();
        PickedState<E> pickedEdgeState = vv.getPickedEdgeState();
    	
    	vv.addPostRenderPaintable(mLensPaintable);
   	    pickedEdgeState.clear();
        pickedVertexState.clear();
    }

    
    public void mouseDragged(MouseEvent e) {
    	
    	VisualizationViewer<V,E> vv = (VisualizationViewer)e.getSource();
        if(mPickedVertex != null) {
            Point2D p = e.getPoint();
            Point2D graphPoint = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(p);
            Point2D graphDown = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(mDown);
            Layout<V,E> layout = vv.getGraphLayout();
            double dx = graphPoint.getX()-graphDown.getX();
            double dy = graphPoint.getY()-graphDown.getY();
            PickedState<V> pickedState = vv.getPickedVertexState();
            
            for(V v : pickedState.getPicked()) {
                Point2D vertexPoint = layout.transform(v);
                vertexPoint.setLocation(vertexPoint.getX()+dx, vertexPoint.getY()+dy);
                layout.setLocation(v, vertexPoint);
            }
            mDown = p;

        } else {
            Point2D out = e.getPoint();
            /*
            if(e.getModifiers() == this.addToSelectionModifiers || e.getModifiers() == modifiers) {
                rect.setFrameFromDiagonal(down,out);
            }
            */
            mRect.setFrameFromDiagonal(mDown,out);
        }
        if(mPickedVertex != null) e.consume();
        vv.repaint();
    }
    
    
    public void performDrag(VisualizationViewer vv, Point2D p) {
    	if(mPickedVertex != null)
    		moveVertex(vv, p);
    	else
    		updateRect(vv, p);
    	
    	vv.repaint();
    }
    
    
    public void updateRect(VisualizationViewer vv, Point2D p) {
    	if(mPickedVertex == null)
    		mRect.setFrameFromDiagonal(mDown, p);
    }
    
    
    public void moveVertex(VisualizationViewer vv, Point2D p) {
    	if(mPickedVertex != null) {
    		
	        Point2D graphPoint = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(p);
	        Point2D graphDown = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(mDown);
	        Layout<V,E> layout = vv.getGraphLayout();
	        double dx = graphPoint.getX()-graphDown.getX();
	        double dy = graphPoint.getY()-graphDown.getY();
	        PickedState<V> pickedState = vv.getPickedVertexState();
	        
	        for(V v : pickedState.getPicked()) {
	            Point2D vertexPoint = layout.transform(v);
	            vertexPoint.setLocation(vertexPoint.getX()+dx, vertexPoint.getY()+dy);
	            layout.setLocation(v, vertexPoint);
	        }
	        mDown = p;
    	}
    }
    
    
    public void mouseReleased(MouseEvent e) {
    	
    	VisualizationViewer<V,E> vv = (VisualizationViewer)e.getSource();

            if(mDown != null) {
                Point2D out = e.getPoint();

                if(mPickedVertex == null && heyThatsTooClose(mDown, out, 5) == false) {
                    pickContainedVertices(vv, mDown, out, true);
                }
            }

        mDown = null;
        mPickedVertex = null;
        mPickedEdge = null;
        mRect.setFrame(0,0,0,0);
        vv.removePostRenderPaintable(mLensPaintable);
        vv.repaint();
    }
    
    
    public void pickVerticesInRect(VisualizationViewer vv, Point2D p) {
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
    protected void pickContainedVertices(VisualizationViewer<V,E> vv, Point2D down, Point2D out, boolean clear) {
        
        Layout<V,E> layout = vv.getGraphLayout();
        PickedState<V> pickedVertexState = vv.getPickedVertexState();
        
        Rectangle2D pickRectangle = new Rectangle2D.Double();
        pickRectangle.setFrameFromDiagonal(down,out);
         
        if(pickedVertexState != null) {
            if(clear) {
            	pickedVertexState.clear();
            }
            GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();

            Collection<V> picked = pickSupport.getVertices(layout, pickRectangle);
            for(V v : picked) {
            	pickedVertexState.pick(v, true);
            }
        }
    }
    
    
    
    public void clearPickedCollection(VisualizationViewer<V, E> vv) {
    	vv.getPickedVertexState().clear();
        vv.getPickedEdgeState().clear();
    }
    
    
    public void addToSelection(V v, VisualizationViewer<V, E> vv) {
    	PickedState<V> pickedVertexState = vv.getPickedVertexState();
    	mPickedVertex = v;
    	
    	if(pickedVertexState.isPicked(mPickedVertex) == false) {
        	pickedVertexState.pick(mPickedVertex, true);
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

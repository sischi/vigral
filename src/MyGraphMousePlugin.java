
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import javax.swing.JComponent;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;

/**
 * A plugin that can create vertices, undirected edges and directed edges.
 * Further this Plugin can pick and move one or more vertices
 * by using the mouse in combination with modifier keys (SHIFT, CTRL) 
 * 
 */
public class MyGraphMousePlugin<V,E> extends AbstractGraphMousePlugin implements MouseListener, MouseMotionListener {
    
	
	private final int EDITING_MODE = 0;
	private final int PICKING_MODE = 1;
	
	
	private PickSupport<V,E> mPicking;
	private EditSupport<V, E> mEditing;
	
	private Factory<V> mVertexFactory;
	private Factory<E> mEdgeFactory;
	
	private boolean mEditingPossible = true;
	
    private int mMode;
    
    
    
    
    
    
    
    
    public MyGraphMousePlugin() {
        this(MouseEvent.BUTTON1_MASK);
    }

    /**
     * create instance and prepare shapes for visual effects
     * @param modifiers
     */
    public MyGraphMousePlugin(int modifiers) {
        super(modifiers);
        
		this.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
		
		mPicking = new PickSupport<V,E>();
		mEditing = new EditSupport<V,E>();
		mMode = EDITING_MODE;
		
    }
    
    /**
     * Overridden to be more flexible, and pass events with
     * key combinations. The default responds to both ButtonOne
     * and ButtonOne+Shift
     */
    @Override
    public boolean checkModifiers(MouseEvent e) {
        return (e.getModifiers() & modifiers) != 0;
    }
    

    @SuppressWarnings("unchecked")
    public void mousePressed(MouseEvent e) {
    	// on left mouse press
    	if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
    		// get the clicked vv and the coordinates
        	final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
            final Point2D p = e.getPoint();
            
            // get an instance of the graphelementaccessor
            GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
            if(pickSupport != null) {
            	final V vertex = pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
            	
            	// edge drawing
            	if((e.getModifiers() & MouseEvent.CTRL_MASK) != 0 && vertex != null) {
            		if(!mEditingPossible)
            			return;
            		
            		EdgeType directed = EdgeType.UNDIRECTED;
            		
            		// directed edge
            		if((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0)
            			directed = EdgeType.DIRECTED;
            		
            		mPicking.clearPickedCollection(vv);
            		mMode = EDITING_MODE;
            		mEditing.startEdge(e, vertex, directed);
            	}
            	// picking mode
            	else if((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0 && vertex != null) {
            		mMode = PICKING_MODE;
            		mPicking.addToSelection(vertex, vv, p);
            	}
            	// start rectangle
            	else if((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0 && vertex == null){
            		mMode = PICKING_MODE;
            		mPicking.clearPickedCollection(vv);
            		mPicking.prepareToDrawRect(vv, p);
            	}
            	else {
            		// create vertex
            		if(vertex == null && mMode == EDITING_MODE && (e.getModifiers() & MouseEvent.CTRL_MASK) == 0) {
            			// just create a new vertex if editing is enabled
            			if(!mEditingPossible)
                			return;
            			mEditing.addVertex(e, vv);
            		}
            		else if(vertex == null && mMode == PICKING_MODE) {
            			mMode = EDITING_MODE;
            			mPicking.clearPickedCollection(vv);
            		}
            		else if(vertex != null) {
            			mMode = PICKING_MODE;
            			mPicking.pickVertex(vv, vertex, p);
            		}
            	}
            }
    	}
    }

    
    /**
     * If startVertex is non-null, and the mouse is released over an
     * existing vertex, create an undirected edge from startVertex to
     * the vertex under the mouse pointer. If shift was also pressed,
     * create a directed edge instead.
     */
    
    
    
    @SuppressWarnings("unchecked")
    public void mouseDragged(MouseEvent e) {
    	
    	if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
	    	final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
	        final Point2D p = e.getPoint();
	        
	        if(mMode == EDITING_MODE)
	        	mEditing.drawEdge(e);
	        else if(mMode == PICKING_MODE)
	        	mPicking.performDrag(vv, p);
    	}
    }
    
    
    
    @SuppressWarnings("unchecked")
    public void mouseReleased(MouseEvent e) {
    	if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
    		final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
            final Point2D p = e.getPoint();
            Layout<V,E> layout = vv.getModel().getGraphLayout();
            GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
            
            if(pickSupport != null) {
            	if(mMode == EDITING_MODE) {
            		final V vertex = pickSupport.getVertex(layout, p.getX(), p.getY());
	                mEditing.addEdge(e, p, vertex, vv);
            	}
            	else if(mMode == PICKING_MODE)
            		mPicking.pickVerticesInRect(vv, p);
            }
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public void stopEditing() {
    	mEditingPossible = false;
    }
    
    public void startEditing() {
    	mEditingPossible = true;
    }
    
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {
        JComponent c = (JComponent)e.getSource();
        c.setCursor(cursor);
    }
    public void mouseExited(MouseEvent e) {
        JComponent c = (JComponent)e.getSource();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    public void mouseMoved(MouseEvent e) {}
}
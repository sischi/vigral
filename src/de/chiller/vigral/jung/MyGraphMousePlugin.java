package de.chiller.vigral.jung;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Vertex;
import de.chiller.vigral.gui.VigralGUI;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;

/**
 * A plugin that can create vertices, undirected edges (CTRL) and directed edges (SHIFT + CTRL).
 * Further this Plugin can pick and move one (LEFTMOUSE) or more vertices (SHIFT + DRAG_LEFTMOUSE)
 * by using the mouse in combination with modifier keys
 * 
 */
public class MyGraphMousePlugin extends AbstractGraphMousePlugin implements MouseListener, MouseMotionListener {
    
	
	private final int EDITING_MODE = 0;
	private final int PICKING_MODE = 1;
	
	private PickSupport mPicking;
	private EditSupport mEditing;
	
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
		mPicking = new PickSupport();
		mEditing = new EditSupport();
		mMode = EDITING_MODE;
		
    }
    

    @SuppressWarnings("unchecked")
    public void mousePressed(MouseEvent e) {
    	
    	//TODO handle SHIFT+CTRL+DRAG with an vertex as starting point in visualization mode
    	
    	// on left mouse press
    	if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
    		// get the clicked vv and the coordinates
        	final VisualizationViewer<Vertex, Edge> vv = (VisualizationViewer<Vertex, Edge>)e.getSource();
            final Point2D p = e.getPoint();
            
            // get an instance of the graphelementaccessor
            GraphElementAccessor<Vertex, Edge> pickSupport = vv.getPickSupport();
            if(pickSupport != null) {
            	final Vertex vertex = (Vertex) pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
            	
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

   
    
    
    @SuppressWarnings("unchecked")
    public void mouseDragged(MouseEvent e) {
    	
    	if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
	    	final VisualizationViewer<Vertex, Edge> vv = (VisualizationViewer<Vertex, Edge>)e.getSource();
	        final Point2D p = e.getPoint();
	        
	        if(mMode == EDITING_MODE) {
	        	mEditing.drawEdge(e);
	        }
	        else if(mMode == PICKING_MODE)
	        	mPicking.performDrag(vv, p);
    	}
    }
    
    
    
    @SuppressWarnings("unchecked")
    public void mouseReleased(MouseEvent e) {
    	if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
    		final VisualizationViewer<Vertex, Edge> vv = (VisualizationViewer<Vertex, Edge>) e.getSource();
            final Point2D p = e.getPoint();
            Layout<Vertex, Edge> layout = vv.getModel().getGraphLayout();
            GraphElementAccessor<Vertex, Edge> pickSupport = vv.getPickSupport();
            
            if(pickSupport != null) {
            	if(mMode == EDITING_MODE) {
            		final Vertex vertex = (Vertex) pickSupport.getVertex(layout, p.getX(), p.getY());
	                mEditing.addEdge(e, p, vertex, vv);
            	}
            	else if(mMode == PICKING_MODE)
            		mPicking.pickVerticesInRect(vv, p);
            }
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public void stopEditing() {
    	mEditingPossible = false;
    	mMode = EDITING_MODE;
    	mPicking.clearPickedCollection(VigralGUI.getInstance().getGraphBuilder().getVisualizationViewer());
    }
    
    public void startEditing() {
    	mEditingPossible = true;
    	mMode = EDITING_MODE;
    	mPicking.clearPickedCollection(VigralGUI.getInstance().getGraphBuilder().getVisualizationViewer());
    }
    
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}
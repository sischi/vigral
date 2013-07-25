package de.chiller.vigral.jung;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.prefs.Preferences;


import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardDownRightHandler;
import javax.swing.plaf.multi.MultiSeparatorUI;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Vertex;
import de.chiller.vigral.gui.SettingsFrame;
import de.chiller.vigral.gui.VigralGUI;
import de.chiller.vigral.menubar.FileOperator;
import de.chiller.vigral.settings.Settings;
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
public class MyGraphMousePlugin extends AbstractGraphMousePlugin implements MouseListener, MouseMotionListener, KeyListener {
    
	
	private final int EDITING_MODE = 0;
	private final int PICKING_MODE = 1;
	
	
	

	
	private final int NOKEY = 0;
	
	private PickSupport mPicking;
	private EditSupport mEditing;
	
	private int mKeyPressed = 0b0000;
	
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
    	System.out.println("Mouse down");
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
			// get the clicked vv and the coordinates
			final VisualizationViewer<Vertex, Edge> vv = (VisualizationViewer<Vertex, Edge>) e .getSource();
			final Point2D p = e.getPoint();

			GraphElementAccessor<Vertex, Edge> pickSupport = vv.getPickSupport();
			System.out.println("picksupport == null? -"+ (pickSupport == null));
			if (pickSupport != null) {
				final Vertex vertex = (Vertex) pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
				System.out.println("Vertex = "+ vertex);
				System.out.println("KeyEvent = "+ KeyEvent.getKeyText(mKeyPressed));
				if (mKeyPressed == 0b0001) {
					if (!mEditingPossible)
						return;

					mPicking.clearPickedCollection(vv);
					mMode = EDITING_MODE;
					mEditing.startEdge(e, vertex, EdgeType.UNDIRECTED);
				} else if (mKeyPressed == 0b0010) {
					if (!mEditingPossible)
						return;

					mPicking.clearPickedCollection(vv);
					mMode = EDITING_MODE;
					mEditing.startEdge(e, vertex, EdgeType.DIRECTED);

				} else if (mKeyPressed == 0b0100) {
					if (vertex != null) {
						mMode = PICKING_MODE;
						mPicking.addToSelection(vertex, vv, p);
					}

				} else if (mKeyPressed == 0b1000) {
					if (vertex == null) {
						mMode = PICKING_MODE;
						mPicking.clearPickedCollection(vv);
						mPicking.prepareToDrawRect(vv, p);
					}

				} else if (mKeyPressed == NOKEY) {
					// create vertex
					if (vertex == null && mMode == EDITING_MODE && (e.getModifiers() & MouseEvent.CTRL_MASK) == 0) {
						// just create a new vertex if editing is enabled
						if (!mEditingPossible)
							return;
						mEditing.addVertex(e, vv);
					} else if (vertex == null && mMode == PICKING_MODE) {
						mMode = EDITING_MODE;
						mPicking.clearPickedCollection(vv);
					} else if (vertex != null) {
						mMode = PICKING_MODE;
						mPicking.pickVertex(vv, vertex, p);
					}
				}
			}
		}
    	
//    	// on left mouse press
//    	if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
//    		// get the clicked vv and the coordinates
//        	final VisualizationViewer<Vertex, Edge> vv = (VisualizationViewer<Vertex, Edge>)e.getSource();
//            final Point2D p = e.getPoint();
//            
//            // get an instance of the graphelementaccessor
//            GraphElementAccessor<Vertex, Edge> pickSupport = vv.getPickSupport();
//            if(pickSupport != null) {
//            	final Vertex vertex = (Vertex) pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
//            	
//            	// edge drawing
//            	if((e.getModifiers() & MouseEvent.CTRL_MASK) != 0 && vertex != null) {
//            		if(!mEditingPossible)
//            			return;
//            		
//            		EdgeType directed = EdgeType.UNDIRECTED;
//            		
//            		// directed edge
//            		if((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0)
//            			directed = EdgeType.DIRECTED;
//            		
//            		mPicking.clearPickedCollection(vv);
//            		mMode = EDITING_MODE;
//            		mEditing.startEdge(e, vertex, directed);
//            	}
//            	// picking mode
//            	else if((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0 && vertex != null) {
//            		mMode = PICKING_MODE;
//            		mPicking.addToSelection(vertex, vv, p);
//            	}
//            	// start rectangle
//            	else if((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0 && vertex == null){
//            		mMode = PICKING_MODE;
//            		mPicking.clearPickedCollection(vv);
//            		mPicking.prepareToDrawRect(vv, p);
//            	}
//            	else {
//            		// create vertex
//            		if(vertex == null && mMode == EDITING_MODE && (e.getModifiers() & MouseEvent.CTRL_MASK) == 0) {
//            			// just create a new vertex if editing is enabled
//            			if(!mEditingPossible)
//                			return;
//            			mEditing.addVertex(e, vv);
//            		}
//            		else if(vertex == null && mMode == PICKING_MODE) {
//            			mMode = EDITING_MODE;
//            			mPicking.clearPickedCollection(vv);
//            		}
//            		else if(vertex != null) {
//            			mMode = PICKING_MODE;
//            			mPicking.pickVertex(vv, vertex, p);
//            		}
//            	}
//            }
//    	}
    }
    
    
    @SuppressWarnings("unchecked")
    public void mouseDragged(MouseEvent e) {
    	System.out.println("Mouse dragged");
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
    	System.out.println("Mouse up");
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

	@Override
	public void keyTyped(KeyEvent e) {	
		System.out.println("key typed: "+ KeyEvent.getKeyText(e.getKeyCode()));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("pressed key: "+ KeyEvent.getKeyText(e.getKeyCode()) +"at time "+ e.getWhen());
		if(e.getKeyCode() == Settings.mSettingsKey.get(Settings.KEY_UNDIRECTED_EDGE)) {
			if((mKeyPressed & 0b0001) == 0b0000)
				mKeyPressed |= 0b0001;
		}
		else if(e.getKeyCode() == Settings.mSettingsKey.get(Settings.KEY_DIRECTED_EDGE)) {
			if((mKeyPressed & 0b0010) == 0b0000)
				mKeyPressed |= 0b0010;
		}
		else if(e.getKeyCode() == Settings.mSettingsKey.get(Settings.KEY_MULTIPLE_SELECT)) {
			if((mKeyPressed & 0b0100) == 0b0000)
				mKeyPressed |= 0b0100;
		}
		else if(e.getKeyCode() == Settings.mSettingsKey.get(Settings.KEY_RECTANGULAR_SELECT)) {
			if((mKeyPressed & 0b1000) == 0b0000)
				mKeyPressed |= 0b1000;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("released key: "+ KeyEvent.getKeyText(e.getKeyCode()) +"at time "+ e.getWhen());
		if(e.getKeyCode() == Settings.mSettingsKey.get(Settings.KEY_UNDIRECTED_EDGE)) {
				mKeyPressed &= 0b1110;
		}
		else if(e.getKeyCode() == Settings.mSettingsKey.get(Settings.KEY_DIRECTED_EDGE)) {
				mKeyPressed &= 0b1101;
		}
		else if(e.getKeyCode() == Settings.mSettingsKey.get(Settings.KEY_MULTIPLE_SELECT)) {
				mKeyPressed &= 0b1011;
		}
		else if(e.getKeyCode() == Settings.mSettingsKey.get(Settings.KEY_RECTANGULAR_SELECT)) {
				mKeyPressed &= 0b0111;
		}
	}

}
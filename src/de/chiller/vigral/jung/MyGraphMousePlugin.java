package de.chiller.vigral.jung;


import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
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

import de.chiller.vigral.VigralGUI;
import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Vertex;
import de.chiller.vigral.settings.Settings;
import de.chiller.vigral.settings.SettingsDialog;
import de.chiller.vigral.util.FileOperator;
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
	
	
	
	private static final int MASK_UNDIRECTED_EDGE = 0b00001;
	private static final int MASK_DIRECTED_EDGE = 0b00010;
	private static final int MASK_MULTIPLE_SELECT = 0b00100;
	private static final int MASK_RECTANGULAR_SELECT = 0b01000;
	private static final int MASK_KEY_NOT_MAPPED = 0b10000;
	private static final int MASK_NOKEY = 0b0000;
	
	private static final int INVMASK_UNDIRECTED_EDGE = MASK_UNDIRECTED_EDGE ^ 0b11111;
	private static final int INVMASK_DIRECTED_EDGE = MASK_DIRECTED_EDGE ^ 0b11111;
	private static final int INVMASK_MULTIPLE_SELECT = MASK_MULTIPLE_SELECT ^ 0b11111;
	private static final int INVMASK_RECTANGULAR_SELECT = MASK_RECTANGULAR_SELECT ^ 0b11111;
	private static final int INVMASK_KEY_NOT_MAPPED = MASK_KEY_NOT_MAPPED ^ 0b11111;
	private static final int INVMASK_NOKEY = MASK_NOKEY ^ 0b11111;
	
	private PickSupport mPicking;
	private EditSupport mEditing;
	
	private int mKeyPressed = 0b0000;
	private Settings mSettings;
	
	private boolean mEditingPossible = true;
	
    private int mMode;
    
    
    
	private KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
		@Override
		public boolean dispatchKeyEvent(final KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				onKeyPressed(e);
			}
			else if (e.getID() == KeyEvent.KEY_RELEASED) {
				onKeyReleased(e);
			}
			return false;
		}
	};
	    
    
    
    public MyGraphMousePlugin() {
        this(MouseEvent.BUTTON1_MASK);
    }

    /**
     * create instance and prepare shapes for visual effects
     * @param modifiers
     */
    public MyGraphMousePlugin(int modifiers) {
        super(modifiers);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
		mPicking = new PickSupport();
		mEditing = new EditSupport();
		mSettings = Settings.getInstance();
		changeMode(EDITING_MODE);
    }
    

    @SuppressWarnings("unchecked")
    public void mousePressed(MouseEvent e) {
    	System.out.println("mouse pressed");
    	VigralGUI.getInstance().setFocusToDrawPanel();
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
			// get the clicked vv and the coordinates
			final VisualizationViewer<Vertex, Edge> vv = (VisualizationViewer<Vertex, Edge>) e .getSource();
			final Point2D p = e.getPoint();

			GraphElementAccessor<Vertex, Edge> pickSupport = vv.getPickSupport();
			if (pickSupport != null) {
				final Vertex vertex = (Vertex) pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());				
				if (mKeyPressed == 0b0001) {
					if (!mEditingPossible)
						return;

					mPicking.clearPickedCollection(vv);
					changeMode(EDITING_MODE);
					mEditing.startEdge(e, vertex, EdgeType.UNDIRECTED);
				} else if (mKeyPressed == 0b0010) {
					if (!mEditingPossible)
						return;

					mPicking.clearPickedCollection(vv);
					changeMode(EDITING_MODE);
					mEditing.startEdge(e, vertex, EdgeType.DIRECTED);

				} else if (mKeyPressed == 0b0100) {
					if (vertex != null) {
						changeMode(PICKING_MODE);
						mPicking.addToSelection(vertex, vv, p);
					}

				} else if (mKeyPressed == 0b1000) {
					if (vertex == null) {
						changeMode(PICKING_MODE);
						mPicking.clearPickedCollection(vv);
						mPicking.prepareToDrawRect(vv, p);
					}

				} else if (mKeyPressed == MASK_NOKEY) {
					System.out.println("nokey");
					// create vertex
					if (vertex == null && mMode == EDITING_MODE && (e.getModifiers() & MouseEvent.CTRL_MASK) == 0) {
						// just create a new vertex if editing is enabled
						if (!mEditingPossible)
							return;
						mEditing.addVertex(e, vv);
					} else if (vertex == null && mMode == PICKING_MODE) {
						changeMode(EDITING_MODE);
						mPicking.clearPickedCollection(vv);
					} else if (vertex != null) {
						changeMode(PICKING_MODE);
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
    	changeMode(EDITING_MODE);
    	mPicking.clearPickedCollection(VigralGUI.getInstance().getGraphBuilder().getVisualizationViewer());
    }
    
    public void startEditing() {
    	mEditingPossible = true;
    	changeMode(EDITING_MODE);
    	mPicking.clearPickedCollection(VigralGUI.getInstance().getGraphBuilder().getVisualizationViewer());
    }
    
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		onKeyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		onKeyReleased(e);
	}
	
	private void changeMode(int mode) {
		mMode = mode;
	}
	
	private void onKeyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DELETE && VigralGUI.getInstance().isGraphPanelFocused())
			e.consume();
			
		else if(e.getKeyCode() == mSettings.getKey(Settings.KEY_UNDIRECTED_EDGE))
			mKeyPressed |= MASK_UNDIRECTED_EDGE;
		
		else if(e.getKeyCode() == mSettings.getKey(Settings.KEY_DIRECTED_EDGE))
			mKeyPressed |= MASK_DIRECTED_EDGE;
		
		else if(e.getKeyCode() == mSettings.getKey(Settings.KEY_MULTIPLE_SELECT))
			mKeyPressed |= MASK_MULTIPLE_SELECT;
		
		else if(e.getKeyCode() == mSettings.getKey(Settings.KEY_RECTANGULAR_SELECT))
			mKeyPressed |= MASK_RECTANGULAR_SELECT;
		
		else
			mKeyPressed |= MASK_KEY_NOT_MAPPED;
	}
	
	private void onKeyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DELETE && VigralGUI.getInstance().isGraphPanelFocused()) {
			mEditing.deleteSelection();
			changeMode(EDITING_MODE);
		}
		else if(e.getKeyCode() == mSettings.getKey(Settings.KEY_UNDIRECTED_EDGE))
			mKeyPressed &= INVMASK_UNDIRECTED_EDGE;
		
		else if(e.getKeyCode() == mSettings.getKey(Settings.KEY_DIRECTED_EDGE))
			mKeyPressed &= INVMASK_DIRECTED_EDGE;
		
		else if(e.getKeyCode() == mSettings.getKey(Settings.KEY_MULTIPLE_SELECT))
			mKeyPressed &= INVMASK_MULTIPLE_SELECT;
		
		else if(e.getKeyCode() == mSettings.getKey(Settings.KEY_RECTANGULAR_SELECT))
			mKeyPressed &= INVMASK_RECTANGULAR_SELECT;
		
		else
			mKeyPressed &= INVMASK_KEY_NOT_MAPPED;
	}


}
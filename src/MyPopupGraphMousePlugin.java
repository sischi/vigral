import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;

/**
 * a plugin that uses popup menus to create vertices, undirected edges,
 * and directed edges.
 * 
 * @author Tom Nelson
 *
 */
public class MyPopupGraphMousePlugin<V,E> extends AbstractPopupGraphMousePlugin {
    
    protected JPopupMenu popup = new JPopupMenu();
    
    //protected JPopupMenu mVertexPopup;
    //protected JPopupMenu mEdgePopup;

    public MyPopupGraphMousePlugin() {
    	this(MouseEvent.BUTTON3_MASK);
    	//mEdgePopup = new EdgePopupMenu.EdgeMenu(null);
    	//mVertexPopup = new VertexPopupMenu.VertexMenu(null);
    }
    
    public MyPopupGraphMousePlugin(int modifiers) {
    	super(modifiers);
    }
    
    /*
    private void updateEdgeMenu(E edge, VisualizationViewer vv, Point2D point) {
        if (mEdgePopup == null) return;
        Component[] menuComps = mEdgePopup.getComponents();
        for (Component comp: menuComps) {
        	
            if (comp instanceof PopupMenuItemInterface) {
                ((PopupMenuItemInterface)comp).setComponentAndView(edge, vv);
            }
        }
    }
    
    private void updateVertexMenu(V vertex, VisualizationViewer vv, Point2D point) {
        if (mEdgePopup == null) return;
        Component[] menuComps = mVertexPopup.getComponents();
        for (Component comp: menuComps) {
        	
            if (comp instanceof PopupMenuItemInterface) {
                ((PopupMenuItemInterface)comp).setComponentAndView(vertex, vv);
            }
        }
    }
    */
    
    
    protected void handlePopup(MouseEvent e) {
        final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
        Point2D p = e.getPoint();
        
        GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
        if(pickSupport != null) {
            final V vertex = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            if(vertex != null) {
            	ElementPopupMenu.setMode(ElementPopupMenu.VERTEXMENU, (Vertex)vertex, null, vv);
            	new ElementPopupMenu.PopupMenu(null).show(vv, e.getX(), e.getY());
            } 
            else {
                final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
                if(edge != null) {
                	ElementPopupMenu.setMode(ElementPopupMenu.EDGEMENU, null, (Edge)edge, vv);
                	new ElementPopupMenu.PopupMenu(null).show(vv, e.getX(), e.getY());
                }
            }
        }
    }

}
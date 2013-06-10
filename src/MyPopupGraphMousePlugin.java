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

    public MyPopupGraphMousePlugin() {
    	this(MouseEvent.BUTTON3_MASK);
    }
    
    public MyPopupGraphMousePlugin(int modifiers) {
    	super(modifiers);
    }
    
    
    protected void handlePopup(MouseEvent e) {
        final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
        Point2D p = e.getPoint();
        
        // TODO inform the editing plugin about a popupmenu to be present so that no new vertex will be created when clicking outside the popupmenu
        
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
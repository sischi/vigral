package de.chiller.vigral.jung;
import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Vertex;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * 
 * @author Simon Schiller
 *
 * @param <GE> the GraphElement type (Vertex or Edge)
 */
public interface PopupMenuItemInterface<GE> {

	void setComponentAndView(GE elem, VisualizationViewer<Vertex, Edge> vv);    
}

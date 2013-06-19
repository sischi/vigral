package de.chiller.vigral.gui;
import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Vertex;
import edu.uci.ics.jung.visualization.VisualizationViewer;


public interface PopupMenuItemInterface<GE> {

	void setComponentAndView(GE elem, VisualizationViewer<Vertex, Edge> vv);    
}

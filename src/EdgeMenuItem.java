import edu.uci.ics.jung.visualization.VisualizationViewer;


public interface EdgeMenuItem<E> {

	void setEdgeAndView(E e, VisualizationViewer visView);
}

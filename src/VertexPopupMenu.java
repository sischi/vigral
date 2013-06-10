import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


import edu.uci.ics.jung.visualization.VisualizationViewer;


public class VertexPopupMenu<V> {

	public static class VertexMenu extends JPopupMenu {
        // private JFrame frame; 
        public VertexMenu(final JFrame frame) {
            super("Vertex Menu");
            // this.frame = frame;
            add(new DeleteVertexMenuItem<Vertex>());
            addSeparator();
            add(new LabelDisplay());
            addSeparator();
            add(new VertexPropertyItem(frame));           
        }
    }
	
	public static class VertexPropertyItem extends JMenuItem implements VertexMenuItem<Vertex> {
		Vertex mVertex;
		VisualizationViewer mVViewer;
		Point2D point;
		
		@Override
		public void setVertexAndView(Vertex v, VisualizationViewer vv) {
		    mVertex = v;
		    mVViewer = vv;
		}
		
		public void setPoint(Point2D point) {
		    point = point;
		}
		
		public VertexPropertyItem(final JFrame frame) {
		    super("Edit Vertex Properties...");
		    addActionListener(new ActionListener() {
		    	@Override
		        public void actionPerformed(ActionEvent e) {
		            VertexPropertyDialog dialog = new VertexPropertyDialog(frame, mVertex);
		            //dialog.setLocation((int)point.getX()+ frame.getX(), (int)point.getY()+ frame.getY());
		            dialog.setVisible(true);
		        }
		    });
		}
	}
	
	public static class LabelDisplay extends JMenuItem implements VertexMenuItem<Vertex> {
        public void setVertexAndView(Vertex v, VisualizationViewer vv) {
            setText("Label " + v + " = " + v.getLabel());
        }
    }
	
	public static class DeleteVertexMenuItem<V> extends JMenuItem implements VertexMenuItem<V> {
		
		private V mVertex;
	    private VisualizationViewer mVViewer;
		
		public DeleteVertexMenuItem() {
	        super("Delete Vertex");
	        this.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	                mVViewer.getPickedEdgeState().pick(mVertex, false);
	                mVViewer.getGraphLayout().getGraph().removeEdge(mVertex);
	                mVViewer.repaint();
	            }
	        });
	    }
		
		@Override
		public void setVertexAndView(V v, VisualizationViewer vv) {
	        mVertex = v;
	        mVViewer = vv;
	        setText("Delete Vertex " + v.toString());
	    }
	}
}

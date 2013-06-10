import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


import edu.uci.ics.jung.visualization.VisualizationViewer;


public class EdgePopupMenu<E> {
	
	public static class EdgeMenu extends JPopupMenu {
        // private JFrame frame; 
        public EdgeMenu(final JFrame frame) {
            super("Edge Menu");
            // this.frame = frame;
            add(new DeleteEdgeMenuItem<Edge>());
            addSeparator();
            add(new WeightDisplay());
            addSeparator();
            add(new EdgePropertyItem(frame));           
        }
    }
	
	public static class EdgePropertyItem extends JMenuItem implements EdgeMenuItem<Edge> {
		Edge mEdge;
		VisualizationViewer mVViewer;
		Point2D point;
		
		@Override
		public void setEdgeAndView(Edge edge, VisualizationViewer vv) {
		    mEdge = edge;
		    mVViewer = vv;
		}
		
		public void setPoint(Point2D point) {
		    point = point;
		}
		
		public EdgePropertyItem(final JFrame frame) {
		    super("Edit Edge Properties...");
		    addActionListener(new ActionListener() {
		    	@Override
		        public void actionPerformed(ActionEvent e) {
		            EdgePropertyDialog dialog = new EdgePropertyDialog(frame, mEdge);
		            //dialog.setLocation((int)point.getX()+ frame.getX(), (int)point.getY()+ frame.getY());
		            dialog.setVisible(true);
		        }
		    });
		}
	}
	
	public static class WeightDisplay extends JMenuItem implements EdgeMenuItem<Edge> {
        public void setEdgeAndView(Edge e, VisualizationViewer vv) {
            setText("Weight " + e + " = " + e.getWeight());
        }
    }
	
	public static class DeleteEdgeMenuItem<E> extends JMenuItem implements EdgeMenuItem<E> {
		
		private E mEdge;
	    private VisualizationViewer mVViewer;
		
		public DeleteEdgeMenuItem() {
	        super("Delete Edge");
	        this.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	                mVViewer.getPickedEdgeState().pick(mEdge, false);
	                mVViewer.getGraphLayout().getGraph().removeEdge(mEdge);
	                mVViewer.repaint();
	            }
	        });
	    }
		
		@Override
		public void setEdgeAndView(E edge, VisualizationViewer vv) {
	        mEdge = edge;
	        mVViewer = vv;
	        setText("Delete Edge " + edge.toString());
	    }
	}
	
}

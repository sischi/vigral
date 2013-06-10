import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;


public class ElementPopupMenu {
	
	public static final int VERTEXMENU = 0b01;
	public static final int EDGEMENU = 0b10;
	
	private static int mMenuMode;
	private static VisualizationViewer mVViewer;
	private static Vertex mVertex;
	private static Edge mEdge;
	
	
	public static void setMode(int mode, Vertex v, Edge e, VisualizationViewer vv) {
		mMenuMode = mode;
		mVertex = v;
		mEdge = e;
		mVViewer = vv;
	}
	

	public static class PopupMenu extends JPopupMenu {
        // private JFrame frame; 
        public PopupMenu(final JFrame frame) {
            super("Context Menu");
            // this.frame = frame;
            add(new DeleteElementMenuItem());
            addSeparator();
            add(new PropertyDisplay());
            addSeparator();
            add(new ElementPropertyItem(frame));   
        }
    }
	
	public static class ElementPropertyItem extends JMenuItem {
		
		
		public ElementPropertyItem(final JFrame frame) {
			super();
			
			if(mMenuMode == VERTEXMENU)
				setText("Edit Vertex Properties...");
			else // edge menu is called
				setText("Edit Edge Properties...");
		    
		    addActionListener(new ActionListener() {
		    	@Override
		        public void actionPerformed(ActionEvent e) {
		    		ElementPropertyDialog dialog;
		    		
					if(mMenuMode == VERTEXMENU)
		    			dialog = new ElementPropertyDialog(frame, mVertex);
					else // edge menu is called
						dialog = new ElementPropertyDialog(frame, mEdge);
					
					dialog.addWindowListener(new WindowAdapter() {
						@Override
					    public void windowClosed(WindowEvent e) {
					        mVViewer.repaint();
					    }
					});
		            
					dialog.setModal(true);
		            dialog.setVisible(true);
		        }
		    });
		}
	}
	
	public static class PropertyDisplay extends JMenuItem {
		
		public PropertyDisplay() {
			super();
			if(mMenuMode == VERTEXMENU)
				setText("Label of "+ mVertex.getIdentifier() +" = "+ mVertex.getLabel());
			else // edge menu is called
				setText("Weight of "+ mEdge +" = "+ mEdge.getWeight());
		}
    }
	
	public static class DeleteElementMenuItem extends JMenuItem {
		
		public DeleteElementMenuItem() {
	        super();
	        
	        if(mMenuMode == VERTEXMENU)
	        	setText("Delete Vertex");
	        else // edge menu is called
	        	setText("Delete Edge");
	        
	        this.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	                if(mMenuMode == VERTEXMENU) {
	                	mVViewer.getPickedVertexState().pick(mVertex, false);
	                	mVViewer.getGraphLayout().getGraph().removeVertex(mVertex);
	                }
	                else { // edge menu is called
	                	mVViewer.getPickedEdgeState().pick(mEdge, false);
	                	mVViewer.getGraphLayout().getGraph().removeEdge(mEdge);
	                }
	                mVViewer.repaint();
	            }
	        });
	    }
	}
}

package de.chiller.vigral.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.GraphElement;
import de.chiller.vigral.graph.Vertex;
import edu.uci.ics.jung.visualization.VisualizationViewer;


public class ElementPopupMenu {
	
	public static final int VERTEXMENU = 0b01;
	public static final int EDGEMENU = 0b10;
	
	private static int mMenuMode;
	private static VisualizationViewer<Vertex, Edge> mVViewer;
	private static Vertex mVertex;
	private static Edge mEdge;
	
	
	public static void setMode(int mode, Vertex v, Edge e, VisualizationViewer<Vertex, Edge> vv) {
		mMenuMode = mode;
		mVertex = v;
		mEdge = e;
		mVViewer = vv;
	}
	

	public static class PopupMenu extends JPopupMenu {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

        public PopupMenu() {
            super("Context Menu");
            
            JMenuItem label = new JMenuItem();
            JMenuItem property1 = new JMenuItem();
            JMenuItem property2 = new JMenuItem();
            JMenuItem property3 = new JMenuItem();
            JMenuItem edit = new JMenuItem();
            JMenuItem delete = new JMenuItem();
            
            
            
            if(mMenuMode == VERTEXMENU) {
            	label.setText("Vertex "+ mVertex.getIdentifier());
            	property1.setText("Label = "+ mVertex.getLabel());
            	edit.setText("Edit Label...");
            	delete.setText("Delete Vertex");
            	
            	add(label);
            	add(property1);
            	addSeparator();
            	add(edit);
            	addSeparator();
            	add(delete);
            }
            else {
            	label.setText("Edge "+ mEdge);
            	property1.setText("Weight = "+ mEdge.getWeight());
            	property2.setText("Min Capacity = "+ mEdge.getMinCapacity());
            	property3.setText("Max Capacity = "+ mEdge.getMaxCapacity());
            	edit.setText("Edit Properties...");
            	delete.setText("Delete Edge");
            	            	
            	add(label);
            	add(property1);
            	add(property2);
            	add(property3);
            	addSeparator();
            	add(edit);
            	addSeparator();
            	add(delete);
            }
            
            
            edit.addActionListener(new ActionListener() {
		    	@Override
		        public void actionPerformed(ActionEvent e) {
		    		ElementPropertyDialog<GraphElement> dialog;
		    		
					if(mMenuMode == VERTEXMENU)
		    			dialog = new ElementPropertyDialog<GraphElement>(mVertex);
					else // edge menu is called
						dialog = new ElementPropertyDialog<GraphElement>(mEdge);
					
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
            
            
            delete.addActionListener(new ActionListener(){
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

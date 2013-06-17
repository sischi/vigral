package de.chiller.vigral;
import java.awt.Dimension;
import java.awt.EventQueue;

import de.chiller.vigral.graph.GraphBuilder;
import de.chiller.vigral.gui.VigralGUI;


public class Vigral {

	/**
	 * responsible for the graph stuff
	 */
	private GraphBuilder mGraphBuilder;
	/**
	 * an instance of the graph creation GUI
	 */
	private VigralGUI mfrm_graphCreation;
	
	
	/**
	 * create an instance of the program
	 */
	public Vigral() {
		mGraphBuilder = new GraphBuilder();
		mfrm_graphCreation = new VigralGUI(mGraphBuilder);
		
		// workaround for linux:
		// without initially resizing the frame, the editing panel of the JUNG framework is not shown
		Dimension dim = mfrm_graphCreation.getSize();
		dim.height++;
		//mfrm_graphCreation.resize(dim);
		
		mfrm_graphCreation.setVisible(true);
	}

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// start the program
					Vigral vigral = new Vigral();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}

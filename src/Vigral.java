import java.awt.Dimension;
import java.awt.EventQueue;


public class Vigral {

	
	private GraphBuilder mGraphBuilder;
	private GraphCreationGUI mfrm_graphCreation;
	
	
	
	public Vigral() {
		mGraphBuilder = new GraphBuilder();
		mfrm_graphCreation = new GraphCreationGUI(mGraphBuilder);
		Dimension dim = mfrm_graphCreation.getSize();
		dim.height++;
		mfrm_graphCreation.resize(dim);
		mfrm_graphCreation.setVisible(true);
	}

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Vigral vigral = new Vigral();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}

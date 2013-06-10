import java.awt.Dimension;
import java.awt.EventQueue;


public class Vigral {

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphCreationGUI frame = new GraphCreationGUI();
					frame.setVisible(true);
					Dimension dimen = frame.getSize();
					dimen.height++;
					dimen.width++;
					frame.resize(dimen);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}

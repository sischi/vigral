import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;


public class GraphCreationGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphCreationGUI frame = new GraphCreationGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GraphCreationGUI() {
		setTitle("ViGrAl - Graph Creation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JComboBox cb_graphType = new JComboBox();
		cb_graphType.setBackground(Color.WHITE);
		cb_graphType.setBounds(12, 12, 153, 24);
		contentPane.add(cb_graphType);
		
		JComboBox cb_algorithm = new JComboBox();
		cb_algorithm.setBackground(Color.WHITE);
		cb_algorithm.setBounds(277, 12, 153, 24);
		contentPane.add(cb_algorithm);
		
		JPanel pnl_graph = new JPanel();
		pnl_graph.setBackground(Color.WHITE);
		pnl_graph.setBounds(12, 48, 418, 176);
		contentPane.add(pnl_graph);
		
		JButton btn_Visualisation = new JButton("Visualisation");
		btn_Visualisation.setBounds(304, 236, 126, 25);
		contentPane.add(btn_Visualisation);
	}
}

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class GraphCreationGUI extends JFrame {

	private JComboBox cb_graphType = new JComboBox();
	private JComboBox cb_algorithm = new JComboBox();
	JButton btn_visualisation = new JButton("Visualisation");
	private JPanel pnl_graph = new JPanel();
	private JFrame window = this;
	private JPanel contentPane;

	private ComponentListener resizeListener = new ComponentListener() {
		@Override
		public void componentResized(ComponentEvent e) {
			System.out.println("resized!!");
			resizeComponents();
		}

		@Override
		public void componentHidden(ComponentEvent e) {
		}
		@Override
		public void componentMoved(ComponentEvent e) {
		}
		@Override
		public void componentShown(ComponentEvent e) {
		}
	};
	
	
	private void resizeComponents() {
		Rectangle windowRect = contentPane.getBounds();
		
		int x = 10;
		int y = 10;
		int w = 153;
		int h = 24;
		cb_graphType.setBounds(x, y, w, h);
		
		w = 150;
		h = 25;
		x = windowRect.width - w - 10;
		y = windowRect.y + 10;
		cb_algorithm.setBounds(x, y, w, h);
		
		btn_visualisation.setBounds(304, 236, 126, 25);
		w = 125;
		h = 25;
		x = windowRect.width - w - 10;
		y = windowRect.height - h - 10;
		btn_visualisation.setBounds(x, y, w, h);
		
		x = windowRect.x + 10;
		y = cb_graphType.getBounds().y + cb_graphType.getBounds().height + 10;
		w = windowRect.width - 20;
		h = btn_visualisation.getBounds().y - 10 - cb_graphType.getBounds().y - cb_graphType.getBounds().height - 10;
		pnl_graph.setBounds(x, y, w, h);
	}

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
		
		setMinimumSize(new Dimension(460, 310));
		addComponentListener(resizeListener);
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cb_graphType.setBackground(Color.WHITE);
		contentPane.add(cb_graphType);
		
		cb_algorithm.setBackground(Color.WHITE);
		contentPane.add(cb_algorithm);
		
		pnl_graph.setBackground(Color.WHITE);
		contentPane.add(pnl_graph);
		
		contentPane.add(btn_visualisation);
		
		resizeComponents();
	}
	
	
	
	
}

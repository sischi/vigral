import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class GraphCreationGUI extends JFrame {

	private JComboBox mCb_graphType = new JComboBox();
	private JComboBox mCb_algorithm = new JComboBox();
	private JButton mBtn_visualisation = new JButton("Visualisation");
	private JPanel mPnl_graph = new JPanel();
	private JFrame mWindow = this;
	private JPanel mContentPane;
	
	private GraphBuilder mGraphBuilder;

	/**
	 * initializes the components
	 */
	private void initComponents() {
		mContentPane = new JPanel();
		mContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mContentPane);
		mContentPane.setLayout(null);
		
		mCb_graphType.setBackground(Color.WHITE);
		mContentPane.add(mCb_graphType);
		
		mCb_algorithm.setBackground(Color.WHITE);
		mContentPane.add(mCb_algorithm);
		
		mPnl_graph.setBackground(Color.WHITE);
		mContentPane.add(mPnl_graph);
		
		mContentPane.add(mBtn_visualisation);
	}
	
	private ComponentListener resizeListener = new ComponentListener() {
		@Override
		public void componentResized(ComponentEvent e) {
			System.out.println("resized!!");
			resizeComponents();
			mGraphBuilder.onResizePanel(mPnl_graph);
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
	
	/**
	 * resize the components shown by the JFrame
	 */
	private void resizeComponents() {
		// get the rectangle of the contentPane
		Rectangle windowRect = mContentPane.getBounds();
		
		System.out.println("window.rect = "+ windowRect);
		
		// for each component: align the component relative to the contentPane rectangle and the other components
		int x = 10;
		int y = 10;
		int w = 153;
		int h = 24;
		mCb_graphType.setBounds(x, y, w, h);
		
		w = 150;
		h = 25;
		x = windowRect.width - w - 10;
		y = windowRect.y + 10;
		mCb_algorithm.setBounds(x, y, w, h);
		
		w = 160;
		h = 25;
		x = windowRect.width - w - 10;
		y = windowRect.height - h - 10;
		mBtn_visualisation.setBounds(x, y, w, h);
		
		x = windowRect.x + 10;
		y = mCb_graphType.getBounds().y + mCb_graphType.getBounds().height + 10;
		w = windowRect.width - 20;
		h = mBtn_visualisation.getBounds().y - 10 - mCb_graphType.getBounds().y - mCb_graphType.getBounds().height - 10;
		mPnl_graph.setBounds(x, y, w, h);
	}


	/**
	 * Create the frame.
	 */
	public GraphCreationGUI(GraphBuilder gb) {
		setTitle("ViGrAl - Graph Creation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setMinimumSize(new Dimension(600, 400));
		addComponentListener(resizeListener);
		
		initComponents();
		resizeComponents();
		
		mGraphBuilder = gb;
		mGraphBuilder.addToPanel(mPnl_graph);
	}
	
	
	
	
}

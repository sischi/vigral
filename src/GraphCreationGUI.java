import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class GraphCreationGUI extends JFrame implements ActionListener {

	public static class Mode {
		public static final int GRAPHCREATION = 0;
		public static final int VISUALISATION = 1;
	}
	
	private int mMode;
	
	private JComboBox mCb_graphType = new JComboBox();
	private JComboBox mCb_algorithm = new JComboBox();
	private JButton mBtn_changeMode = new JButton();
	private JButton mBtn_play = new JButton();
	private JButton mBtn_pause = new JButton();
	private JButton mBtn_jumpToStart = new JButton();
	private JButton mBtn_stepBack = new JButton();
	private JButton mBtn_stepForward = new JButton();
	private JButton mBtn_jumpToEnd = new JButton();
	private JPanel mPnl_graph = new JPanel();
	private JPanel mPnl_buttonBar = new JPanel();
	private JPanel mContentPane;
	
	private GraphBuilder mGraphBuilder;
	
	
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
		
		changeMode(Mode.GRAPHCREATION);
		
		initSizes();
	}
	
	
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
		
		mBtn_changeMode.addActionListener(this);
		mContentPane.add(mBtn_changeMode);
		
		initButtonBar();
		mContentPane.add(mPnl_buttonBar);
		
		
		mBtn_play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mBtn_play.setVisible(false);
				mBtn_pause.setVisible(true);
			}
		});
		
		mBtn_pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mBtn_play.setVisible(true);
				mBtn_pause.setVisible(false);
			}
		});
	}
	
	
	public void initButtonBar() {
		initButtonFromButtonBar(mBtn_jumpToStart, new ImageIcon("res/jumptostart_inactive.png"), new ImageIcon("res/jumptostart_active.png"), 0);
		initButtonFromButtonBar(mBtn_stepBack, new ImageIcon("res/stepback_inactive.png"), new ImageIcon("res/stepback_active.png"), 1);
		initButtonFromButtonBar(mBtn_play, new ImageIcon("res/play_inactive.png"), new ImageIcon("res/play_active.png"), 2);
		initButtonFromButtonBar(mBtn_pause, new ImageIcon("res/pause_inactive.png"), new ImageIcon("res/pause_active.png"), 2);
		initButtonFromButtonBar(mBtn_stepForward, new ImageIcon("res/stepforward_inactive.png"), new ImageIcon("res/stepforward_active.png"), 3);
		initButtonFromButtonBar(mBtn_jumpToEnd, new ImageIcon("res/jumptoend_inactive.png"), new ImageIcon("res/jumptoend_active.png"), 4);
		mBtn_pause.setVisible(false);
	}
	
	
	public void initButtonFromButtonBar(JButton btn, Icon inactive, Icon active, int pos) {
		
		btn.setContentAreaFilled(false);
	    btn.setFocusPainted(false);
		int w = active.getIconWidth();
		int h = active.getIconHeight();
		int x = pos*w + pos*10;
		int y = 0;
		Dimension d = new Dimension(w, h);
		btn.setIcon(inactive);
		btn.setRolloverIcon(active);
		btn.setPressedIcon(active);
		btn.setPreferredSize(d);
		btn.setMinimumSize(d);
		btn.setMaximumSize(d);
		btn.setBounds(x, y, w, h);
		btn.setBorderPainted(false);
		mPnl_buttonBar.add(btn);
	}
	
	
	public void initSizes() {
		setBounds(100, 100, 600, 400);
		mCb_graphType.setBounds(10, 10, 150, 25);
		mCb_algorithm.setBounds(430, 10, 150, 25);
		mPnl_graph.setBounds(10, 45, 380, 175);
		mBtn_changeMode.setBounds(410, 345, 160, 25);
	}
	
	
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
		int w = 150;
		int h = 25;
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
		mBtn_changeMode.setBounds(x, y, w, h);
		
		w = 5 * mBtn_play.getBounds().width + 4 * 10;
		h = mBtn_play.getBounds().height + 10;
		x = 0;
		y = windowRect.height - h - 5;
		mPnl_buttonBar.setBounds(x, y, w, h);
		
		x = windowRect.x + 10;
		y = mCb_graphType.getBounds().y + mCb_graphType.getBounds().height + 10;
		w = windowRect.width - 20;
		h = mPnl_buttonBar.getBounds().y - 10 - mCb_graphType.getBounds().y - mCb_graphType.getBounds().height - 10;
		mPnl_graph.setBounds(x, y, w, h);
	}
	

	
	
	public void changeMode(int mode) {
		mMode = mode;
		mGraphBuilder.setMode(mMode);
		
		if(mMode == Mode.GRAPHCREATION) {
			mCb_algorithm.setEnabled(true);
			mCb_graphType.setEnabled(true);
			mBtn_changeMode.setText("Visualisation");
			mPnl_buttonBar.setVisible(false);
		}
		else if(mMode == Mode.VISUALISATION) {
			mCb_algorithm.setEnabled(false);
			mCb_graphType.setEnabled(false);
			mBtn_changeMode.setText("Graph Creation");
			mPnl_buttonBar.setVisible(true);
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(mMode == Mode.GRAPHCREATION)
			changeMode(Mode.VISUALISATION);
		else if(mMode == Mode.VISUALISATION)
			changeMode(Mode.GRAPHCREATION);
	}
	
	
	
	
}

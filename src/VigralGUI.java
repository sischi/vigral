import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;


public class VigralGUI extends JFrame {

	
	private JFrame mMainWindow = this;
	
	public static class Mode {
		public static final int GRAPHCREATION = 0;
		public static final int VISUALISATION = 1;
	}
	
	private ArrayList<AbstractAlgorithm> mAvailableAlgorithms;
	private AbstractAlgorithm mChosenAlgorithm;
	
	private int mMode;
	
	private JComboBox mCb_graphType = new JComboBox();
	private JComboBox mCb_algorithm = new JComboBox();
	private JSplitPane mSplt_contentPane = new JSplitPane();
	private JButton mBtn_changeMode = new JButton();
	private JButton mBtn_play = new JButton();
	private JButton mBtn_pause = new JButton();
	private JButton mBtn_jumpToStart = new JButton();
	private JButton mBtn_stepBack = new JButton();
	private JButton mBtn_stepForward = new JButton();
	private JButton mBtn_jumpToEnd = new JButton();
	private JPanel mPnl_graph = new JPanel();
	private JPanel mPnl_buttonBar = new JPanel();
	private JPanel mPnl_mainPanel = new JPanel();
	private JPanel mPnl_sidePanel = new JPanel();
	
	private GraphBuilder mGraphBuilder;
	
	
	private ActionListener mCreationListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeMode(Mode.VISUALISATION);
			mBtn_changeMode.removeActionListener(mCreationListener);
			mBtn_changeMode.addActionListener(mVisualisationListener);
			
			Graph graph = Graph.parseGraph(mGraphBuilder.getGraph());
			mChosenAlgorithm = mAvailableAlgorithms.get(mCb_algorithm.getSelectedIndex()); 
			ArrayList<Pair<ElementType, String>> require = mChosenAlgorithm.getRequirements();
			if(require != null) {
				RequirementDialog dialog = new RequirementDialog(mMainWindow, require, graph, mChosenAlgorithm);
				dialog.setModal(true);
				dialog.show();
			}
		}
	};
	
	private ActionListener mVisualisationListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeMode(Mode.GRAPHCREATION);
			mBtn_changeMode.removeActionListener(mVisualisationListener);
			mBtn_changeMode.addActionListener(mCreationListener);
		}
	};
	
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
	 * Create
	 */
	public VigralGUI(GraphBuilder gb) {
		initAlgorithms();
		
		setTitle("ViGrAl - Graph Creation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		setMinimumSize(new Dimension(750, 400));
		
		initComponents();
		resizeComponents();
		
		mGraphBuilder = gb;
		mGraphBuilder.addToPanel(mPnl_graph);
		
		changeMode(Mode.GRAPHCREATION);
		
		
		//initSizes();
	}
	
	
	/**
	 * initializes the components
	 */
	private void initComponents() {
		mPnl_mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mSplt_contentPane);
		mPnl_mainPanel.setLayout(null);
		mPnl_mainPanel.addComponentListener(resizeListener);
		
		mCb_graphType.setBackground(Color.WHITE);
		mPnl_mainPanel.add(mCb_graphType);
		
		mCb_algorithm.setBackground(Color.WHITE);
		initAlgorithmBox();
		mPnl_mainPanel.add(mCb_algorithm);
		
		mPnl_graph.setBackground(Color.WHITE);
		mPnl_mainPanel.add(mPnl_graph);
		
		mBtn_changeMode.addActionListener(mCreationListener);
		mPnl_mainPanel.add(mBtn_changeMode);
		
		initButtonBar();
		mPnl_mainPanel.add(mPnl_buttonBar);
		
		mSplt_contentPane.setMinimumSize(new Dimension(740, 370));
		mSplt_contentPane.setLeftComponent(mPnl_mainPanel);
		mSplt_contentPane.setRightComponent(mPnl_sidePanel);
		mSplt_contentPane.setResizeWeight(1.0);
		
		
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
	
	public void initAlgorithms() {
		mAvailableAlgorithms = new ArrayList<AbstractAlgorithm>();
		mAvailableAlgorithms.add(new Dijkstra());
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
		
		System.out.println("window = "+ getBounds());
		
		System.out.println("split pane = "+ mSplt_contentPane.getBounds());
		
		System.out.println("split divider = "+ mSplt_contentPane.getDividerSize());
		
		// get the rectangle of the contentPane
		Rectangle spltPaneRect = mSplt_contentPane.getBounds();
		Rectangle mainPanelRect = mPnl_mainPanel.getBounds();
		System.out.println("main panel = "+ mainPanelRect);
		
		/*
		Dimension sidePanelDim = new Dimension(240, 0);
		mPnl_sidePanel.setMaximumSize(sidePanelDim);
		mPnl_sidePanel.setMinimumSize(sidePanelDim);
		mPnl_sidePanel.setPreferredSize(sidePanelDim);
		*/
		
		Dimension mainPanelDim = new Dimension(spltPaneRect.width - mSplt_contentPane.getDividerSize(), spltPaneRect.height);
		mPnl_mainPanel.setMinimumSize(mainPanelDim);
		mPnl_mainPanel.setMaximumSize(mainPanelDim);
		mPnl_mainPanel.setPreferredSize(mainPanelDim);
		
		
		// for each component: align the component relative to the contentPane rectangle and the other components
		int x = 10;
		int y = 10;
		int w = 150;
		int h = 25;
		mCb_graphType.setBounds(x, y, w, h);
		
		w = 150;
		h = 25;
		x = mainPanelRect.width - w - 10;
		y = mainPanelRect.y + 10;
		mCb_algorithm.setBounds(x, y, w, h);
		
		w = 160;
		h = 25;
		x = mainPanelRect.width - w - 10;
		y = mainPanelRect.height - h - 10;
		mBtn_changeMode.setBounds(x, y, w, h);
		
		w = 5 * mBtn_play.getBounds().width + 4 * 10;
		h = mBtn_play.getBounds().height + 10;
		x = 0;
		y = mainPanelRect.height - h - 5;
		mPnl_buttonBar.setBounds(x, y, w, h);
		
		x = mainPanelRect.x + 10;
		y = mCb_graphType.getBounds().y + mCb_graphType.getBounds().height + 10;
		w = mainPanelRect.width - 20;
		h = mPnl_buttonBar.getBounds().y - 10 - mCb_graphType.getBounds().y - mCb_graphType.getBounds().height - 10;
		mPnl_graph.setBounds(x, y, w, h);
	}
	

	private void initAlgorithmBox() {
		String[] entries = new String[mAvailableAlgorithms.size()];
		
		for(int i = 0; i < mAvailableAlgorithms.size(); i++)
			entries[i] = mAvailableAlgorithms.get(i).getAlgorithmName();
		
		DefaultComboBoxModel model = new DefaultComboBoxModel(entries);
		mCb_algorithm.setModel(model);
	}
	
	
	public void changeMode(int mode) {
		mMode = mode;
		mGraphBuilder.setMode(mMode);
		Dimension sidePanelDim = null;
		
		
		if(mMode == Mode.GRAPHCREATION) {
			sidePanelDim = new Dimension(0, 0);
			mSplt_contentPane.setDividerLocation(1.0d);
			mCb_algorithm.setEnabled(true);
			mCb_graphType.setEnabled(true);
			mBtn_changeMode.setText("Visualisation");
			mPnl_buttonBar.setVisible(false);
		}
		else if(mMode == Mode.VISUALISATION) {
			sidePanelDim = new Dimension(240, 0);
			mSplt_contentPane.setDividerLocation(mSplt_contentPane.getWidth() - sidePanelDim.width - mSplt_contentPane.getDividerSize());
			mCb_algorithm.setEnabled(false);
			mCb_graphType.setEnabled(false);
			mBtn_changeMode.setText("Graph Creation");
			mPnl_buttonBar.setVisible(true);
		}
		
		mPnl_sidePanel.setMaximumSize(sidePanelDim);
		mPnl_sidePanel.setMinimumSize(sidePanelDim);
		mPnl_sidePanel.setPreferredSize(sidePanelDim);
	}
	
	
}

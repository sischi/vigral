package de.chiller.vigral.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;

import de.chiller.vigral.algorithm.AbstractAlgorithm;
import de.chiller.vigral.graph.ElementType;
import de.chiller.vigral.graph.Graph;

import de.chiller.vigral.plugins.PluginLoader;
import de.chiller.vigral.util.Pair;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;


public class VigralGUI extends JFrame {

	
	private static VigralGUI mMainWindow = new VigralGUI();
	
	public static class Mode {
		public static final int GRAPHCREATION = 0;
		public static final int VISUALISATION = 1;
	}
	
	private ArrayList<AbstractAlgorithm> mAvailableAlgorithms = null;
	private AbstractAlgorithm mChosenAlgorithm;
	
	private int mMode;
	
	private MenuBar mMenuBar;
	private JComboBox mCb_algorithm = new JComboBox();
	private DefaultComboBoxModel mAlgorithmBoxModel = new DefaultComboBoxModel();
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
	
	
	public static VigralGUI getInstance() {
		return mMainWindow;
	}
	
	private ActionListener mCreationListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Graph graph = mGraphBuilder.getGraph();
			if(graph.getVertexCount() != 0 && mAvailableAlgorithms != null) {
				mChosenAlgorithm = mAvailableAlgorithms.get(mCb_algorithm.getSelectedIndex()); 
				ArrayList<Pair<ElementType, String>> require = mChosenAlgorithm.getRequirements();
				if(require != null) {
					RequirementDialog dialog = new RequirementDialog(require, graph, mChosenAlgorithm);
					dialog.setModal(true);
					dialog.show();
				}
				else {
					requirementsApplied(graph);
				}
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
			resizeComponents();
			mGraphBuilder.onResizePanel(mPnl_graph);
		}

		@Override
		public void componentHidden(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentShown(ComponentEvent e) {}
	};
	
	
	private ActionListener mJumpStartListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Graph g = mChosenAlgorithm.getFirstStep();
			if(g != null)
				mGraphBuilder.setResultingGraph(g);
		}
	};
	
	private ActionListener mPreviousStepListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Graph g = mChosenAlgorithm.getPreviousStep();
			if(g != null)
				mGraphBuilder.setResultingGraph(g);
		}
	};
	
	private ActionListener mNextStepListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Graph g = mChosenAlgorithm.getNextStep();
			if(g != null)
				mGraphBuilder.setResultingGraph(g);
		}
	};
	
	private ActionListener mJumpEndListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Graph g = mChosenAlgorithm.getLastStep();
			if(g != null)
				mGraphBuilder.setResultingGraph(g);
		}
	};
	
	

	
	/**
	 * Create
	 */
	private VigralGUI() {
		mMainWindow = this;
		mGraphBuilder = new GraphBuilder();
		mGraphBuilder.addToPanel(mPnl_graph);
				
		setTitle("ViGrAl - Graph Creation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		setMinimumSize(new Dimension(750, 400));
		
		initComponents();
		resizeComponents();
		changeMode(Mode.GRAPHCREATION);
		
		setVisible(true);
		//initSizes();
	}
	
	
	/**
	 * initializes the components
	 */
	private void initComponents() {
		mMenuBar = new MenuBar(this);
		setJMenuBar(mMenuBar);
		
		mPnl_mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mSplt_contentPane);
		mPnl_mainPanel.setLayout(null);
		mPnl_mainPanel.addComponentListener(resizeListener);
		
		mCb_algorithm.setBackground(Color.WHITE);
		initAlgorithms();
		mCb_algorithm.setModel(mAlgorithmBoxModel);
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
		mBtn_jumpToStart.addActionListener(mJumpStartListener);
		initButtonFromButtonBar(mBtn_stepBack, new ImageIcon("res/stepback_inactive.png"), new ImageIcon("res/stepback_active.png"), 1);
		mBtn_stepBack.addActionListener(mPreviousStepListener);
		initButtonFromButtonBar(mBtn_play, new ImageIcon("res/play_inactive.png"), new ImageIcon("res/play_active.png"), 2);
		initButtonFromButtonBar(mBtn_pause, new ImageIcon("res/pause_inactive.png"), new ImageIcon("res/pause_active.png"), 2);
		initButtonFromButtonBar(mBtn_stepForward, new ImageIcon("res/stepforward_inactive.png"), new ImageIcon("res/stepforward_active.png"), 3);
		mBtn_stepForward.addActionListener(mNextStepListener);
		initButtonFromButtonBar(mBtn_jumpToEnd, new ImageIcon("res/jumptoend_inactive.png"), new ImageIcon("res/jumptoend_active.png"), 4);
		mBtn_jumpToEnd.addActionListener(mJumpEndListener);
		mBtn_pause.setVisible(false);
	}
	
	private void initAlgorithms() {
//		mAvailableAlgorithms = new ArrayList<AbstractAlgorithm>();
//		mAvailableAlgorithms.add(new Dijkstra());
		
		mAvailableAlgorithms = PluginLoader.getInstance().loadPlugins();
		if(mAvailableAlgorithms != null) {
			for(int i = 0; i < mAvailableAlgorithms.size(); i++)
				mAlgorithmBoxModel.addElement(mAvailableAlgorithms.get(i).getAlgorithmName());
		}
	}
	
	public void updateAlgorithmBox() {
		mAvailableAlgorithms.clear();
		mAlgorithmBoxModel.removeAllElements();
		
		initAlgorithms();
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
		mCb_algorithm.setBounds(430, 10, 150, 25);
		mPnl_graph.setBounds(10, 45, 380, 175);
		mBtn_changeMode.setBounds(410, 345, 160, 25);
	}
	
	
	/**
	 * resize the components shown by the JFrame
	 */
	private void resizeComponents() {
		
		//System.out.println("window = "+ getBounds());
		
		//System.out.println("split pane = "+ mSplt_contentPane.getBounds());
		
		//System.out.println("split divider = "+ mSplt_contentPane.getDividerSize());
		
		// get the rectangle of the contentPane
		Rectangle spltPaneRect = mSplt_contentPane.getBounds();
		Rectangle mainPanelRect = mPnl_mainPanel.getBounds();
		//System.out.println("main panel = "+ mainPanelRect);
		
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
		
		w = 160;
		h = 25;
		x = mainPanelRect.width - w - 10;
		y = mainPanelRect.height - h - 10;
		mBtn_changeMode.setBounds(x, y, w, h);
		
		mCb_algorithm.setSize(mCb_algorithm.getPreferredSize());
		w = mCb_algorithm.getWidth();
		h = 25;
		x = mainPanelRect.width - w - 10;
		y = mBtn_changeMode.getBounds().y - mCb_algorithm.getHeight() - 10;
		mCb_algorithm.setBounds(x, y, w, h);
		
		w = 5 * mBtn_play.getBounds().width + 4 * 10;
		h = mBtn_play.getBounds().height + 10;
		x = 0;
		y = mainPanelRect.height - h - 5;
		mPnl_buttonBar.setBounds(x, y, w, h);
		
		x = mainPanelRect.x + 10;
		y = mainPanelRect.y + 10;
		w = mainPanelRect.width - 20;
		h = mCb_algorithm.getBounds().y - 20;
		mPnl_graph.setBounds(x, y, w, h);
	}
	
	
	
	
	
	public void requirementsApplied(Graph g) {
		changeMode(Mode.VISUALISATION);
		mBtn_changeMode.removeActionListener(mCreationListener);
		mBtn_changeMode.addActionListener(mVisualisationListener);
		mChosenAlgorithm.setGraph(g);
		mChosenAlgorithm.perform();
		mGraphBuilder.setResultingGraph(mChosenAlgorithm.getFirstStep());
	}
	
	
	public void changeMode(int mode) {
		mMode = mode;
		mGraphBuilder.setMode(mMode);
		Dimension sidePanelDim = null;
		
		
		if(mMode == Mode.GRAPHCREATION) {
			sidePanelDim = new Dimension(0, 0);
			mSplt_contentPane.setDividerLocation(1.0d);
			mCb_algorithm.setEnabled(true);
			mBtn_changeMode.setText("Visualisation");
			mPnl_buttonBar.setVisible(false);
		}
		else if(mMode == Mode.VISUALISATION) {
			sidePanelDim = new Dimension(240, 0);
			mSplt_contentPane.setDividerLocation(mSplt_contentPane.getWidth() - sidePanelDim.width - mSplt_contentPane.getDividerSize());
			mCb_algorithm.setEnabled(false);
			mBtn_changeMode.setText("Graph Creation");
			mPnl_buttonBar.setVisible(true);
		}
		
		mPnl_sidePanel.setMaximumSize(sidePanelDim);
		mPnl_sidePanel.setMinimumSize(sidePanelDim);
		mPnl_sidePanel.setPreferredSize(sidePanelDim);
	}
	
	
	public GraphBuilder getGraphBuilder() {
		return mGraphBuilder;
	}
	
	
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// start the program
					VigralGUI vigral = VigralGUI.getInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

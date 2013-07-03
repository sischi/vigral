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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.DefaultCaret;
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
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class VigralGUI extends JFrame {

	
	private static VigralGUI mMainWindow = new VigralGUI();
	private static final int MARGIN = 10;
	
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
	private JTextArea mTxt_explanation = new JTextArea();
	private DefaultCaret mDefCar;
	private JScrollPane mScp_scrollPane = new JScrollPane(mTxt_explanation);
	private JButton mBtn_clearExplanation = new JButton();
	
	private GraphBuilder mGraphBuilder;
	
	private Timer mPlayTimer;
	
	
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
	
	private ComponentListener onResizeMainPanel = new ComponentListener() {
		@Override
		public void componentResized(ComponentEvent e) {
			System.out.println("main panel resized");
			System.out.println("main panel minimum size: "+ mPnl_mainPanel.getMinimumSize());
			System.out.println("main panel size: "+ mPnl_mainPanel.getSize());
			resizeMainPanel();
			System.out.println("main panel size: "+ mPnl_mainPanel.getSize());
			mGraphBuilder.onResizePanel(mPnl_graph);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			System.out.println("main panel hidden!!");
		}
		@Override
		public void componentMoved(ComponentEvent e) {
			System.out.println("main panel moved!!");
		}
		@Override
		public void componentShown(ComponentEvent e) {
			System.out.println("main panel shown!!");
		}
	};
	
	private ComponentListener onResizeSidePanel = new ComponentListener() {
		@Override
		public void componentResized(ComponentEvent e) {
			System.out.println("side panel resized");
			resizeSidePanel();
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
			update(mChosenAlgorithm.getFirstStep());
		}
	};
	
	private ActionListener mPreviousStepListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			update(mChosenAlgorithm.getPreviousStep());
		}
	};
	
	private ActionListener mNextStepListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			update(mChosenAlgorithm.getNextStep());
		}
	};
	
	private ActionListener mJumpEndListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			update(mChosenAlgorithm.getLastStep());
		}
	};
	
	private ActionListener mPlayListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			mBtn_play.setVisible(false);
			mBtn_pause.setVisible(true);
			System.out.println("Start timer!");
			mPlayTimer = new Timer();
			mPlayTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					Pair p = mChosenAlgorithm.getNextStep();
					if(p == null)
						mBtn_pause.doClick();
					else
						update(p);
				}
			}, 
			2000, 2000);
		}
	};
	
	private ActionListener mPauseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			mBtn_play.setVisible(true);
			mBtn_pause.setVisible(false);
			System.out.println("cancel timer!");
			mPlayTimer.cancel();
		}
	};
	
	
	
	/**
	 * Create
	 */
	private VigralGUI() {
		mMainWindow = this;
		mGraphBuilder = new GraphBuilder();
		mGraphBuilder.addToPanel(mPnl_graph);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		setMinimumSize(new Dimension(750, 400));
		setVisible(true);
		
		initComponents();
		changeMode(Mode.GRAPHCREATION);
		resizeMainPanel();
		
		mSplt_contentPane.setDividerLocation(1.0d);
		pack();
	}
	
	
	/**
	 * initializes the components
	 */
	private void initComponents() {
		// init the menubar
		mMenuBar = new MenuBar(this);
		setJMenuBar(mMenuBar);
		
		
		// init the main panel
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
		
		mPnl_mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mPnl_mainPanel.setLayout(null);
		mPnl_mainPanel.setMinimumSize(new Dimension(550, 400));
		mPnl_mainPanel.addComponentListener(onResizeMainPanel);
		
		
		// init the side panel
		mBtn_clearExplanation.setText("Clear");
		mTxt_explanation.setLineWrap(true);
		mTxt_explanation.setEditable(false);
		// used for automated scrolling
		mDefCar = (DefaultCaret) mTxt_explanation.getCaret();
		mDefCar.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		mPnl_sidePanel.setLayout(null);
		mPnl_sidePanel.add(mScp_scrollPane);
		mPnl_sidePanel.add(mBtn_clearExplanation);
		mPnl_sidePanel.addComponentListener(onResizeSidePanel);
		
		
		// init the split panel
		mSplt_contentPane.setMinimumSize(new Dimension(740, 370));
		mSplt_contentPane.setLeftComponent(mPnl_mainPanel);
		mSplt_contentPane.setRightComponent(mPnl_sidePanel);
		mSplt_contentPane.setResizeWeight(1.0d);
		mSplt_contentPane.setOneTouchExpandable(true);
		setContentPane(mSplt_contentPane);
	}
	
	
	public void initButtonBar() {
		initButtonFromButtonBar(mBtn_jumpToStart, new ImageIcon("res/jumptostart_inactive.png"), new ImageIcon("res/jumptostart_active.png"), 0);
		mBtn_jumpToStart.addActionListener(mJumpStartListener);
		initButtonFromButtonBar(mBtn_stepBack, new ImageIcon("res/stepback_inactive.png"), new ImageIcon("res/stepback_active.png"), 1);
		mBtn_stepBack.addActionListener(mPreviousStepListener);
		initButtonFromButtonBar(mBtn_play, new ImageIcon("res/play_inactive.png"), new ImageIcon("res/play_active.png"), 2);
		mBtn_play.addActionListener(mPlayListener);
		initButtonFromButtonBar(mBtn_pause, new ImageIcon("res/pause_inactive.png"), new ImageIcon("res/pause_active.png"), 2);
		mBtn_pause.addActionListener(mPauseListener);
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
		resizeMainPanel();
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
	
	
	/**
	 * resize the components shown by the JFrame
	 */
	private void resizeMainPanel() {
		
		if(mSplt_contentPane.getDividerLocation() < mPnl_mainPanel.getMinimumSize().width)
			mSplt_contentPane.setDividerLocation(mPnl_mainPanel.getMinimumSize().width);
		
		
		// get the rectangle of the mainPanel
		Rectangle mainPanelRect = mPnl_mainPanel.getBounds();
		
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
		update(mChosenAlgorithm.getFirstStep());
	}
	
	
	public void changeMode(int mode) {
		mMode = mode;
		mGraphBuilder.setMode(mMode);		
		
		if(mMode == Mode.GRAPHCREATION) {
			//((BasicSplitPaneUI) mSplt_contentPane.getUI()).getDivider().removeMouseListener(mSplitPaneDividerListener);
			setTitle("ViGrAl - Graph Creation");
			mCb_algorithm.setEnabled(true);
			mBtn_changeMode.setText("Visualization");
			mPnl_buttonBar.setVisible(false);
		}
		else if(mMode == Mode.VISUALISATION) {
			//sidePanelDim = new Dimension(240, 0);
			//mSplt_contentPane.setDividerLocation(mSplt_contentPane.getWidth() - sidePanelDim.width - mSplt_contentPane.getDividerSize());
			//((BasicSplitPaneUI) mSplt_contentPane.getUI()).getDivider().addMouseListener(mSplitPaneDividerListener);
			setTitle("ViGrAl - Visualization");
			mCb_algorithm.setEnabled(false);
			mBtn_changeMode.setText("Graph Creation");
			mPnl_buttonBar.setVisible(true);
		}
		
		
	}
	
	
	public GraphBuilder getGraphBuilder() {
		return mGraphBuilder;
	}
	
	
	private void resizeSidePanel() {
		Dimension sidePanelDim = mPnl_sidePanel.getSize();
		
		int x = MARGIN;
		int h = mBtn_clearExplanation.getPreferredSize().height;
		int y = sidePanelDim.height - MARGIN - h;
		int w = sidePanelDim.width - 2 * MARGIN;
		mBtn_clearExplanation.setBounds(x, y, w, h);
		
		x = MARGIN;
		y = MARGIN;
		w = sidePanelDim.width - 2 * MARGIN;
		h = sidePanelDim.height - mBtn_clearExplanation.getBounds().height - 3 * MARGIN;
		mTxt_explanation.setBounds(0, 0, w, h);
		
		mScp_scrollPane.setBounds(x, y, w, h);
	}
	
	
	private void update(Pair<Graph, String> pair) {
		if(pair == null)
			return;
		
		mGraphBuilder.setResultingGraph(pair.getL());
		mTxt_explanation.setText(pair.getR());
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

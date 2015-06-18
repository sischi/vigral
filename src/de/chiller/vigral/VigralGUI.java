package de.chiller.vigral;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import de.chiller.vigral.algorithm.AbstractAlgorithm;
import de.chiller.vigral.algorithm.RequirementDialog;
import de.chiller.vigral.graph.ElementType;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.menubar.MenuBar;
import de.chiller.vigral.util.ErrorDialog;
import de.chiller.vigral.util.Pair;
import de.chiller.vigral.util.PluginLoader;

/**
 * This represents the main class, that is responsible for the interaction with the user
 * @author Simon
 *
 */
public class VigralGUI extends JFrame {

	
	
	
	/**
	 * the current version number
	 */
	private static final String mVersion = "0.4";
	
	/**
	 * method to query the version number of the current release
	 * @return the running version number as a string
	 */
	public String getVersionNo() {
		return mVersion;
	}
	
	
	
	/**
	 * The Singleton instance of VigralGUI
	 */
	private static VigralGUI mMainWindow = new VigralGUI();
	
	// some constant values
	private static final int MARGIN = 10;
	private static final int BUTTON_PANEL_HEIGHT = 100;
	
	private static final int SPEED_MIN = -10;
	private static final int SPEED_MAX = 10;
	private static final int SPEED_INIT = 0;
	
	private static final int DEFAULT_PLAY_SPEED = 2001;
	private static final int PLAY_STEP_SIZE = 200;
	
	
	
	
	/**
	 * This inner class defines the possible modes of the class 'VigralGUI'
	 * @author Simon Schiller
	 *
	 */
	public static class Mode {
		public static final int GRAPHCREATION = 0;
		public static final int VISUALISATION = 1;
	}
	
	
	// member fields
	private ArrayList<AbstractAlgorithm> mAvailableAlgorithms = null;
	private AbstractAlgorithm mChosenAlgorithm;
	
	private int mMode;
	
	private MenuBar mMenuBar;
	private JComboBox mCb_algorithm = new JComboBox();
	private DefaultComboBoxModel mAlgorithmBoxModel = new DefaultComboBoxModel();
	private JSplitPane mSplt_ContentPanel = new JSplitPane();
	private JSplitPane mSplt_GraphPanel = new JSplitPane();
	private JButton mBtn_changeMode = new JButton();
	private JButton mBtn_play = new JButton();
	private JButton mBtn_pause = new JButton();
	private JButton mBtn_jumpToStart = new JButton();
	private JButton mBtn_stepBack = new JButton();
	private JButton mBtn_stepForward = new JButton();
	private JButton mBtn_jumpToEnd = new JButton();
	private JPanel mGraphPanel1 = new JPanel();
	private JPanel mGraphPanel2 = new JPanel();
	private JPanel mButtonBar = new JPanel();
	private JSplitPane mSplt_MainPanel = new JSplitPane();
	private JPanel mButtonPanel = new JPanel();
	private JTextArea mTxt_explanation = new JTextArea();
	private DefaultCaret mDefCar;
	private JScrollPane mScp_scrollPane = new JScrollPane(mTxt_explanation);
	private JSlider mSldr_playSpeed = new JSlider(JSlider.HORIZONTAL, SPEED_MIN, SPEED_MAX, SPEED_INIT);
	
	private Rectangle mSliderSpace = new Rectangle();
	
	private GraphBuilder mGraphBuilder;
	
	private Timer mPlayTimer;
	
	
	/**
	 * Singleton method
	 * @return returns the only instance of this class
	 */
	public static VigralGUI getInstance() {
		return mMainWindow;
	}
	


	private ActionListener mOnTimerTick = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// get graph of next step
			Pair<ArrayList<Graph>, String> p = mChosenAlgorithm.getNextStep();
			
			// show graph or pause, if there are no further graphs
			if(p == null)
				mBtn_pause.doClick();
			else
				update(p);
		}
	};
	
	
	// When clicking "visualization" in creation mode
	private ActionListener mCreationListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("onclick graphcreation");
			
			// query the drawn graph
			Graph graph = mGraphBuilder.getGraph();
			
			if(graph.getVertexCount() != 0 && mAvailableAlgorithms != null) {
				
				// query chosen algorithm
				mChosenAlgorithm = mAvailableAlgorithms.get(mCb_algorithm.getSelectedIndex()); 
				
				// get requirements of the chosen algorithm
				try {
					ArrayList<Pair<ElementType, String>> require = mChosenAlgorithm.getRequirements();
						
					if(require != null) {
						// show requirements dialog
						RequirementDialog dialog = new RequirementDialog(require, graph, mChosenAlgorithm);
						dialog.setModal(true);
						dialog.setVisible(true);
					}
					else {
						// next step, if there are no requirements
						requirementsApplied(graph);
					}
				} catch(Exception ex) {
					ErrorDialog.showErrorDialog(null, "somethings wrong with requirements of algorithm '"+ mChosenAlgorithm.getAlgorithmName() +"'", ex);
				}
			}
		}
	};
	
	
	// when clicking "creation" in visualization mode
	private ActionListener mVisualisationListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// change mode
			changeMode(Mode.GRAPHCREATION);
			
			// pause visualization
			mBtn_pause.doClick();
		}
	};
	
	private ComponentListener onResizeGraphPanelListener = new ComponentListener() {
		@Override
		public void componentResized(ComponentEvent e) {
			if(mSplt_ContentPanel.getDividerLocation() < mGraphPanel1.getMinimumSize().width)
				mSplt_ContentPanel.setDividerLocation(mGraphPanel1.getMinimumSize().width);
			
			mGraphBuilder.onResizePanel(mGraphPanel1, 0);
			mGraphBuilder.onResizePanel(mGraphPanel2, 1);
		}
		@Override
		public void componentHidden(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentShown(ComponentEvent e) {}
	};
		
	private ComponentListener onResizeButtonPanelListener = new ComponentListener() {		
		@Override
		public void componentResized(ComponentEvent e) {
			onResizeButtonPanel();
		}
		@Override
		public void componentShown(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentHidden(ComponentEvent e) {}
	};
	
	private ComponentListener onResizeContentPanelListener = new ComponentListener() {		
		@Override
		public void componentResized(ComponentEvent e) {
			mSplt_MainPanel.setDividerLocation(mSplt_MainPanel.getBounds().height - mSplt_MainPanel.getDividerSize() - BUTTON_PANEL_HEIGHT);
		}
		@Override
		public void componentShown(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentHidden(ComponentEvent e) {}
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
			// System.out.println("Start timer!");
			int speed = calcPlaySpeed();
//			mPlayTimer = new Timer(speed, mOnTimerTick);
			mPlayTimer.setDelay(speed);
			mPlayTimer.setInitialDelay(speed);
			mPlayTimer.start();
		}
	};
	
	
	
	private ActionListener mPauseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			mBtn_play.setVisible(true);
			mBtn_pause.setVisible(false);
			// System.out.println("cancel timer!");
			mPlayTimer.stop();
		}
	};
	private ChangeListener onSliderValueChanged = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			// System.out.println("slider value: "+ mSldr_playSpeed.getValue());
			int speed = calcPlaySpeed();
			mPlayTimer.setDelay(speed);
		}
	};
	
	
	
	/**
	 * Create the single instance
	 */
	private VigralGUI() {
		mMainWindow = this;
		
		mGraphBuilder = new GraphBuilder();
		mGraphBuilder.addToPanel(mGraphPanel1, mGraphPanel2);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		setMinimumSize(new Dimension(750, 400));
		setVisible(true);
		
		initComponents();
		changeMode(Mode.GRAPHCREATION);
		
		mSplt_ContentPanel.setDividerLocation(1.0d);
		pack();
		
		mPlayTimer = new Timer(calcPlaySpeed(), mOnTimerTick);
	}
	
	
	/**
	 * initializes the components
	 */
	private void initComponents() {
		// init the menubar
		mMenuBar = new MenuBar(this);
		setJMenuBar(mMenuBar);
		
		// init button bar (play, pause, forward, ...)
		initButtonBar();
		mButtonBar.setSize(mButtonBar.getPreferredSize());
		
		// init list of algorithms (plugins)
		mCb_algorithm.setBackground(Color.WHITE);
		initAlgorithms();
		mCb_algorithm.setModel(mAlgorithmBoxModel);	
		
		// init speed modificator (slider)
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(SPEED_MIN), new JLabel("Slow"));
		labelTable.put(new Integer(SPEED_INIT), new JLabel("Default"));
		labelTable.put(new Integer(SPEED_MAX), new JLabel("Fast"));
		mSldr_playSpeed.setLabelTable(labelTable);
		mSldr_playSpeed.setPaintLabels(true);
		mSldr_playSpeed.setMajorTickSpacing(SPEED_MAX - SPEED_INIT);
		mSldr_playSpeed.setPaintTicks(true);
		mSldr_playSpeed.addChangeListener(onSliderValueChanged );
		
		// init button panel (show buttons available in creation mode)
		mButtonPanel.setLayout(null);
		mButtonPanel.add(mBtn_changeMode);
		mButtonPanel.add(mButtonBar);
		mButtonPanel.add(mCb_algorithm);
		mButtonPanel.add(mSldr_playSpeed);
		mButtonPanel.addComponentListener(onResizeButtonPanelListener);
		
		// init 'main' graph panel (graph drawing area)
		mGraphPanel1.setBackground(Color.WHITE);
		mGraphPanel1.setBorder(new EmptyBorder(5, 5, 5, 5));
		mGraphPanel1.setLayout(null);
		mGraphPanel1.addComponentListener(onResizeGraphPanelListener);
		mGraphPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		// init optional graph panel to be able to show a second graph in visualization mode
		mGraphPanel2.setBackground(Color.WHITE);
		mGraphPanel2.setBorder(new EmptyBorder(5, 5, 5, 5));
		mGraphPanel2.setLayout(null);
		mGraphPanel2.addComponentListener(onResizeGraphPanelListener);
		mGraphPanel2.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		// init the side panel
		mTxt_explanation.setLineWrap(true);
		mTxt_explanation.setEditable(false);
		mTxt_explanation.setBorder(new EmptyBorder(5, 5, 5, 5));
		mTxt_explanation.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		// used for automated scrolling
		mDefCar = (DefaultCaret) mTxt_explanation.getCaret();
		mDefCar.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		// init the content split panel (graph and description)
		mSplt_ContentPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mSplt_ContentPanel.setLeftComponent(mSplt_GraphPanel);
		mSplt_ContentPanel.setRightComponent(mScp_scrollPane);
		mSplt_ContentPanel.setResizeWeight(1.0d);
		mSplt_ContentPanel.setOneTouchExpandable(true);
		mSplt_ContentPanel.setDividerLocation(1.0d);
		
		// init the split panel for the graph and the optional second graph
		mSplt_GraphPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mSplt_GraphPanel.setTopComponent(mGraphPanel1);
		mSplt_GraphPanel.setBottomComponent(mGraphPanel2);
		mSplt_GraphPanel.setResizeWeight(1.0d);
		mSplt_GraphPanel.setOneTouchExpandable(true);
		mSplt_GraphPanel.setDividerLocation(1.0d);
		
		// init the main split panel (content panel and buttonbar)
		mSplt_MainPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mSplt_MainPanel.setTopComponent(mSplt_ContentPanel);
		mSplt_MainPanel.setBottomComponent(mButtonPanel);
		mSplt_MainPanel.setResizeWeight(1.0d);
		mSplt_MainPanel.addComponentListener(onResizeContentPanelListener);
		// mSpltContentPanel.setEnabled(false);
		mSplt_MainPanel.setDividerSize(0);
		setContentPane(mSplt_MainPanel);
		
		initSizesAndPositions();
	}
	
	/**
	 * sets the focus to the drawing pane
	 */
	public void setFocusToDrawPanel() {
		mGraphPanel1.requestFocus();
	}

	
	private void initButtonBar() {
		
		// init play, pause, etc. buttons
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
		
		// show pause button by default
		mBtn_pause.setVisible(false);
	}
	
	
	
	private void initAlgorithms() {
		// load plugins into algorithm list
		mAvailableAlgorithms = PluginLoader.getInstance().loadPlugins();
		if(mAvailableAlgorithms != null) {
			for(int i = 0; i < mAvailableAlgorithms.size(); i++)
				mAlgorithmBoxModel.addElement(mAvailableAlgorithms.get(i).getAlgorithmName());
		}
	}
	
	/**
	 * initiates the reloading of the plugins
	 */
	public void updateAlgorithmBox() {
		// reload plugins and refill algorithm list
		mAvailableAlgorithms.clear();
		mAlgorithmBoxModel.removeAllElements();
		
		initAlgorithms();
//		onResizeButtonPanel();
	}
	
	// calculates the automated visualization speed 
	private int calcPlaySpeed() {
		int speed = DEFAULT_PLAY_SPEED;
		int usersChoice = mSldr_playSpeed.getValue();
		speed -= (usersChoice * PLAY_STEP_SIZE);
		return speed;
	}
	
	
	private void initButtonFromButtonBar(JButton btn, Icon inactive, Icon active, int pos) {
		
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
		mButtonBar.add(btn);
	}
	
	// and again some GUI-foo
	private void initSizesAndPositions() {
		
		mScp_scrollPane.setMinimumSize(new Dimension(0,0));
		
		mCb_algorithm.setSize(new Dimension(200, 30));
		
		Dimension btnDimen = new Dimension(200, 30);
		mBtn_changeMode.setSize(btnDimen);
		
		mGraphPanel1.setMinimumSize(new Dimension(550, 200));
		
		mButtonBar.setSize(mButtonBar.getPreferredSize());
		Rectangle buttonBarRect = mButtonBar.getBounds();
		buttonBarRect.x = MARGIN;
		buttonBarRect.y = (BUTTON_PANEL_HEIGHT / 2) - (buttonBarRect.height / 2);
		mButtonBar.setBounds(buttonBarRect);
		
		mSliderSpace.x = buttonBarRect.x + buttonBarRect.width + MARGIN;
		mSliderSpace.width = mButtonPanel.getWidth() - MARGIN - btnDimen.width - MARGIN - buttonBarRect.width;
		mSliderSpace.y = MARGIN;
		mSliderSpace.height = BUTTON_PANEL_HEIGHT;
		mSldr_playSpeed.setSize(new Dimension(200, buttonBarRect.height));
		int x = mSliderSpace.x + (mSliderSpace.width / 2) - (mSldr_playSpeed.getWidth() / 2);
		int y = buttonBarRect.y;
		mSldr_playSpeed.setLocation(x, y);
	}
	
	
	private void onResizeButtonPanel() {
		Rectangle rect = mButtonPanel.getBounds();
		//System.out.println("contentRect: "+ rect);
		
		Rectangle buttonRect = mBtn_changeMode.getBounds();
		buttonRect.x = rect.width - buttonRect.width - MARGIN;
		buttonRect.y = rect.height - buttonRect.height - MARGIN;
		mBtn_changeMode.setBounds(buttonRect);
		
		Rectangle comboRect = mCb_algorithm.getBounds();
		comboRect.x = rect.width - comboRect.width - MARGIN;
		comboRect.y = MARGIN;
		mCb_algorithm.setBounds(comboRect);
		
		mSliderSpace.width = buttonRect.x - mButtonBar.getWidth() - 3*MARGIN;
		Point p = mSldr_playSpeed.getLocation();
		p.x = mSliderSpace.x + (mSliderSpace.width / 2) - (mSldr_playSpeed.getWidth() / 2);
		mSldr_playSpeed.setLocation(p);
	}
	
	/**
	 * this method tells the main class, that the requirements demanded by the algorithm (plugin) has been applied correctly.
	 * So the mode can be changed to visualization mode.
	 * @param g the graph on what the algorithm will work
	 */
	public void requirementsApplied(Graph g) {
		// System.out.println("requirements applied");
		// change mode to visualization
		changeMode(Mode.VISUALISATION);
		
		// load the first step of the algorithm
		mChosenAlgorithm.setGraph(g);
		try {
			mChosenAlgorithm.perform();
		} catch(Exception e) {
			ErrorDialog.showErrorDialog(null, "cant perform algorithm '" + mChosenAlgorithm.getAlgorithmName() + "'", e);
		}
		update(mChosenAlgorithm.getFirstStep());
	}
	
	/**
	 * getter for the mode
	 * @return returns the current valid mode as an int
	 */
	public int getActualMode() {
		return mMode;
	}
	
	/**
	 * this method initiates all necessary actions that have to be done to change the mode to the desired one
	 * @param mode the mode to be changed to (0 = GRAPHCREATION, 1 = VISUALISATION)
	 */
	public void changeMode(int mode) {
		mMode = mode;
		mGraphBuilder.setMode(mMode);
		
		// toggle button visibilities
		if(mMode == Mode.GRAPHCREATION) {
			setTitle("ViGrAl - Graph Creation");
			mCb_algorithm.setEnabled(true);
			mSldr_playSpeed.setVisible(false);
			mBtn_changeMode.setText("Visualization");
			mButtonBar.setVisible(false);
			mBtn_changeMode.removeActionListener(mVisualisationListener);
			mBtn_changeMode.addActionListener(mCreationListener);
		}
		else if(mMode == Mode.VISUALISATION) {
			setTitle("ViGrAl - Visualization");
			mCb_algorithm.setEnabled(false);
			mBtn_changeMode.setText("Graph Creation");
			mButtonBar.setVisible(true);
			mSldr_playSpeed.setVisible(true);
			mBtn_changeMode.removeActionListener(mCreationListener);
			mBtn_changeMode.addActionListener(mVisualisationListener);
		}
	}
	
	/**
	 * getter for the graphbuilder. that class is responsible for displaying the graph.
	 * @return the graphbuilder
	 */
	public GraphBuilder getGraphBuilder() {
		return mGraphBuilder;
	}
	
	
	private void update(Pair<ArrayList<Graph>, String> pair) {
		if(pair == null)
			return;
		
		// show the given graph and its explanation
		mGraphBuilder.setResultingGraph(pair.getL());
		mTxt_explanation.setText(pair.getR());
	}
	
	/**
	 * this method indicates, if the drawing pane has the focus
	 * @return returns true if the drawing pane has the focus, false otherwise
	 */
	public boolean isGraphPanelFocused() {
		return mGraphPanel1.hasFocus();
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

package de.chiller.vigral.gui;

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
import de.chiller.vigral.graph.ElementType;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.plugins.PluginLoader;
import de.chiller.vigral.util.Pair;


public class VigralGUI extends JFrame {

	
	private static VigralGUI mMainWindow = new VigralGUI();
	private static final int MARGIN = 10;
	private static final int BUTTON_PANEL_HEIGHT = 100;
	
	private static final int SPEED_MIN = -10;
	private static final int SPEED_MAX = 10;
	private static final int SPEED_INIT = 0;
	
	private static final int DEFAULT_PLAY_SPEED = 2001;
	private static final int PLAY_STEP_SIZE = 200;
	
	
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
	private JSplitPane mSpltMainPanel = new JSplitPane();
	private JButton mBtn_changeMode = new JButton();
	private JButton mBtn_play = new JButton();
	private JButton mBtn_pause = new JButton();
	private JButton mBtn_jumpToStart = new JButton();
	private JButton mBtn_stepBack = new JButton();
	private JButton mBtn_stepForward = new JButton();
	private JButton mBtn_jumpToEnd = new JButton();
	private JPanel mGraphPanel = new JPanel();
	private JPanel mButtonBar = new JPanel();
	private JSplitPane mSpltContentPanel = new JSplitPane();
	private JPanel mButtonPanel = new JPanel();
	private JTextArea mTxt_explanation = new JTextArea();
	private DefaultCaret mDefCar;
	private JScrollPane mScp_scrollPane = new JScrollPane(mTxt_explanation);
	private JSlider mSldr_playSpeed = new JSlider(JSlider.HORIZONTAL, SPEED_MIN, SPEED_MAX, SPEED_INIT);
	
	private Rectangle mSliderSpace = new Rectangle();
	
	private GraphBuilder mGraphBuilder;
	
	private Timer mPlayTimer;
	
	
	
	public static VigralGUI getInstance() {
		return mMainWindow;
	}
	
	
	private ActionListener mOnTimerTick = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Pair<Graph, String> p = mChosenAlgorithm.getNextStep();
			if(p == null)
				mBtn_pause.doClick();
			else
				update(p);
		}
	};
	
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
					dialog.setVisible(true);
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
	
	private ComponentListener onResizeGraphPanelListener = new ComponentListener() {
		@Override
		public void componentResized(ComponentEvent e) {
			if(mSpltMainPanel.getDividerLocation() < mGraphPanel.getMinimumSize().width)
				mSpltMainPanel.setDividerLocation(mGraphPanel.getMinimumSize().width);
			
			mGraphBuilder.onResizePanel(mGraphPanel);
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
			mSpltContentPanel.setDividerLocation(mSpltContentPanel.getBounds().height - mSpltContentPanel.getDividerSize() - BUTTON_PANEL_HEIGHT);
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
			System.out.println("Start timer!");
			int speed = calcPaySpeed();
			mPlayTimer = new Timer(speed, mOnTimerTick);
//			mPlayTimer.scheduleAtFixedRate(mTimerTask, speed, speed);
			mPlayTimer.start();
		}
	};
	
	
	
	private ActionListener mPauseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			mBtn_play.setVisible(true);
			mBtn_pause.setVisible(false);
			System.out.println("cancel timer!");
			mPlayTimer.stop();
		}
	};
	private ChangeListener onSliderValueChanged = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			System.out.println("slider value: "+ mSldr_playSpeed.getValue());
			int speed = calcPaySpeed();
			mPlayTimer.setDelay(speed);
		}
	};
	
	
	
	/**
	 * Create
	 */
	private VigralGUI() {
		mMainWindow = this;
		mGraphBuilder = new GraphBuilder();
		mGraphBuilder.addToPanel(mGraphPanel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		setMinimumSize(new Dimension(750, 400));
		setVisible(true);
		
		initComponents();
		changeMode(Mode.GRAPHCREATION);
		
		mSpltMainPanel.setDividerLocation(1.0d);
		pack();
	}
	
	
	/**
	 * initializes the components
	 */
	private void initComponents() {
		// init the menubar
		mMenuBar = new MenuBar(this);
		setJMenuBar(mMenuBar);
		
		initButtonBar();
		mButtonBar.setSize(mButtonBar.getPreferredSize());
				
		mCb_algorithm.setBackground(Color.WHITE);
		initAlgorithms();
		mCb_algorithm.setModel(mAlgorithmBoxModel);		
		
		mBtn_changeMode.addActionListener(mCreationListener);
		
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(SPEED_MIN), new JLabel("Slow"));
		labelTable.put(new Integer(SPEED_INIT), new JLabel("Default"));
		labelTable.put(new Integer(SPEED_MAX), new JLabel("Fast"));
		mSldr_playSpeed.setLabelTable(labelTable);
		mSldr_playSpeed.setPaintLabels(true);
		mSldr_playSpeed.setMajorTickSpacing(SPEED_MAX - SPEED_INIT);
		mSldr_playSpeed.setPaintTicks(true);
		mSldr_playSpeed.addChangeListener(onSliderValueChanged );
		
		mButtonPanel.setLayout(null);
		mButtonPanel.add(mBtn_changeMode);
		mButtonPanel.add(mButtonBar);
		mButtonPanel.add(mCb_algorithm);
		mButtonPanel.add(mSldr_playSpeed);
		mButtonPanel.addComponentListener(onResizeButtonPanelListener);
		
		mGraphPanel.setBackground(Color.WHITE);
		mGraphPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mGraphPanel.setLayout(null);
		mGraphPanel.addComponentListener(onResizeGraphPanelListener);
		mGraphPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		// init the side panel
		mTxt_explanation.setLineWrap(true);
		mTxt_explanation.setEditable(false);
		mTxt_explanation.setBorder(new EmptyBorder(5, 5, 5, 5));
		mTxt_explanation.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		// used for automated scrolling
		mDefCar = (DefaultCaret) mTxt_explanation.getCaret();
		mDefCar.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		// init the split panel
		mSpltMainPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mSpltMainPanel.setLeftComponent(mGraphPanel);
		mSpltMainPanel.setRightComponent(mScp_scrollPane);
		mSpltMainPanel.setResizeWeight(1.0d);
		mSpltMainPanel.setOneTouchExpandable(true);
		mSpltMainPanel.setDividerLocation(1.0d);
		
		mSpltContentPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mSpltContentPanel.setTopComponent(mSpltMainPanel);
		mSpltContentPanel.setBottomComponent(mButtonPanel);
		mSpltContentPanel.setResizeWeight(1.0d);
		mSpltContentPanel.addComponentListener(onResizeContentPanelListener);
//		mSpltContentPanel.setEnabled(false);
		mSpltContentPanel.setDividerSize(0);
		setContentPane(mSpltContentPanel);
		
		initSizesAndPositions();
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
//		onResizeButtonPanel();
	}
	
	private int calcPaySpeed() {
		int speed = DEFAULT_PLAY_SPEED;
		int usersChoice = mSldr_playSpeed.getValue();
		speed -= (usersChoice * PLAY_STEP_SIZE);
		return speed;
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
		mButtonBar.add(btn);
	}
	
	private void initSizesAndPositions() {
		
		mScp_scrollPane.setMinimumSize(new Dimension(0,0));
		
		mCb_algorithm.setSize(new Dimension(200, 30));
		
		Dimension btnDimen = new Dimension(200, 30);
		mBtn_changeMode.setSize(btnDimen);
		
		mGraphPanel.setMinimumSize(new Dimension(550, 200));
		
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
		System.out.println("contentRect: "+ rect);
		
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
	
	
	public void requirementsApplied(Graph g) {
		changeMode(Mode.VISUALISATION);
		mBtn_changeMode.removeActionListener(mCreationListener);
		mBtn_changeMode.addActionListener(mVisualisationListener);
		mChosenAlgorithm.setGraph(g);
		mChosenAlgorithm.perform();
		update(mChosenAlgorithm.getFirstStep());
	}
	
	
	public int getActualMode() {
		return mMode;
	}
	
	
	public void changeMode(int mode) {
		mMode = mode;
		mGraphBuilder.setMode(mMode);
		
		if(mMode == Mode.GRAPHCREATION) {
			setTitle("ViGrAl - Graph Creation");
			mCb_algorithm.setEnabled(true);
			mSldr_playSpeed.setVisible(false);
			mBtn_changeMode.setText("Visualization");
			mButtonBar.setVisible(false);
		}
		else if(mMode == Mode.VISUALISATION) {
			setTitle("ViGrAl - Visualization");
			mCb_algorithm.setEnabled(false);
			mBtn_changeMode.setText("Graph Creation");
			mButtonBar.setVisible(true);
			mSldr_playSpeed.setVisible(true);
		}
	}
	
	
	public GraphBuilder getGraphBuilder() {
		return mGraphBuilder;
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

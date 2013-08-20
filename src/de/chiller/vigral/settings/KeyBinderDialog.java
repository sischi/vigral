package de.chiller.vigral.settings;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.chiller.vigral.VigralGUI;
import de.chiller.vigral.util.Pair;

import java.awt.GridLayout;

public class KeyBinderDialog extends JDialog implements KeyListener{

	public static final int CANCELED = -2;
	public static final int NO_CHOICE = -1;
	
	private final JPanel contentPanel = new JPanel();

	private JButton mOkButton = new JButton("Apply");
	private JButton mCancelButton = new JButton("Cancel");
	private JPanel mButtonPane = new JPanel();
	private JLabel mDescription = new JLabel();
	private JLabel mKeyLabel = new JLabel();
	private int mKeyCode = -1;
	private boolean mCanceled = false;
	private String mKeyChar = "";
	private static ArrayList<Integer> mAllowedKeyCodes = initAllowedKeyCodes();

	
	
	private String mAction;
	/**
	 * Create the dialog.
	 */
	public KeyBinderDialog(String action) {
		mAction = action;
		
		initAllowedKeyCodes();
			
		
		Rectangle rect = VigralGUI.getInstance().getBounds();
		setBounds(rect.x + 100, rect.y + 100, 500, 150);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		
		initComponents();
		
		
		initButtonBar();

		setResizable(false);
		
	}

	
	private static ArrayList<Integer> initAllowedKeyCodes() {
		ArrayList<Integer> allowedKeyCodes = new ArrayList<Integer>();
		
		// a - z
		for(int i = 65; i <= 90; i++)
			allowedKeyCodes.add(i);
		
		// ,
		allowedKeyCodes.add(44);
		// .
		allowedKeyCodes.add(45);
		// -
		allowedKeyCodes.add(46);
		// #
		allowedKeyCodes.add(520);
		// +
		allowedKeyCodes.add(521);
		
		// 0 - 9
		for(int i = 48; i <= 57; i++)
			allowedKeyCodes.add(i);
		
		// NUM0 - NUM9
		for(int i = 96; i <= 105; i++)
			allowedKeyCodes.add(i);
		
		// F1 - F12
		for(int i = 112; i <= 123; i++)
			allowedKeyCodes.add(i);
		
		// SHIFT
		allowedKeyCodes.add(16);
		// STRG
		allowedKeyCodes.add(17);
		// ALT
		allowedKeyCodes.add(18);
		// ALTGR
		allowedKeyCodes.add(65406);
		
		return allowedKeyCodes;
	}


	private void initComponents() {
		Box box = Box.createVerticalBox();
		contentPanel.add(box);
		mDescription.setText("Please press the desired Key for Action '"+ mAction +"'");
		mDescription.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		box.add(mDescription);
		mKeyLabel.setText("<YOUR CHOICE>");
		mKeyLabel.setEnabled(true);
		mKeyLabel.addKeyListener(this);
		mKeyLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mKeyLabel.setFocusable(true);
		box.add(mKeyLabel);
	}


	private void initButtonBar() {
		
		mButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(mButtonPane);

		mOkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("the desired keycode is '"+ mKeyCode +"' ("+ mKeyChar +")");
				setVisible(false);
				dispose();
			}
		});
		mButtonPane.add(mOkButton);

		mCancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mCanceled = true;
				setVisible(false);
				dispose();
			}
		});
		mButtonPane.add(mCancelButton);
	}
	
	
	public int getKey() {
		if(mCanceled)
			return -2;
		
		return mKeyCode;
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if (mAllowedKeyCodes.contains(e.getKeyCode())) {
        	mKeyCode = e.getKeyCode();
        	mKeyChar = KeyEvent.getKeyText(e.getKeyCode());
        	System.out.println("choosen key: "+ mKeyCode +" ("+ mKeyChar +")");
    		mKeyLabel.setText("<"+ mKeyChar +">");
        }  
	}

}

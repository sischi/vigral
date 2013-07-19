package de.chiller.vigral.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Vertex;


@SuppressWarnings("serial")
public class ElementPropertyDialog<GE> extends JDialog {


	private GE mElement;
	private static final int MARGIN = 10;
	

	private final JPanel mContentPanel = new JPanel();
	private JTextField mTxt_field = new JTextField();
	private JLabel mLbl_propName = new JLabel();
	private JLabel mLbl_errorHint = new JLabel();
	private JPanel mButtonPane = new JPanel();
	private JButton mOkButton = new JButton();
	private JButton mCancelButton = new JButton();
	private Border mDefaultBorder;
	private Border mErrorBorder;

	/**
	 * Create the dialog.
	 */
	public ElementPropertyDialog(GE elem) {
		mElement = elem;
		mDefaultBorder = mTxt_field.getBorder();
		mErrorBorder = BorderFactory.createLineBorder(Color.RED, 2);
		initComponents();
		
		
		
		if(mElement instanceof Vertex)
			mTxt_field.setText(""+((Vertex) mElement).getLabel());
		else {// it is an Edge
//			txt_field.addKeyListener(new KeyListener() {
//				@Override
//				public void keyTyped(KeyEvent e) {
//					char c = e.getKeyChar();
//					
//					if((!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) || txt_field.getText().length() == MAXIMUMDIGITS)
//						e.consume();
//				}
//				@Override
//				public void keyReleased(KeyEvent e) {
//				}
//				@Override
//				public void keyPressed(KeyEvent arg0) {
//				}
//			});
			mTxt_field.setText(""+((Edge) mElement).getWeight());
		}
		
		if(mElement instanceof Vertex)
			mLbl_propName.setText("Label");
		else // it is an edge
			mLbl_propName.setText("Weight");
		
		
		initButtonPane();
		

	}
	
	
	private void initComponents() {
		Rectangle rect = VigralGUI.getInstance().getBounds();
		setBounds(rect.x + 100, rect.y + 100, 312, 115);
		
		getContentPane().setLayout(new BorderLayout());
		mContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mContentPanel.setLayout(null);
		getContentPane().add(mContentPanel, BorderLayout.CENTER);
		
		
		mTxt_field.setBounds(150, 25, 150, 20);
		mTxt_field.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				mTxt_field.selectAll();
			}
		});
		mContentPanel.add(mTxt_field);
		
		mLbl_errorHint.setBounds(150, 10, 150, 15);
		mLbl_errorHint.setText("only floating numbers");
		mLbl_errorHint.setForeground(Color.RED);
		Font font = mLbl_errorHint.getFont().deriveFont(10.0f);
		mLbl_errorHint.setFont(font);
		mLbl_errorHint.setVisible(false);
		mContentPanel.add(mLbl_errorHint);
		
		mLbl_propName.setBounds(MARGIN, 32, 130, 15);
		mContentPanel.add(mLbl_propName);
	}
	
	private void initButtonPane() {
		
		mButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(mButtonPane, BorderLayout.SOUTH);
		
		mOkButton.setText("OK");
		mOkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetErrorStates();
				if(mElement instanceof Vertex) {
					String newLabel = mTxt_field.getText().toString().trim();
					boolean alreadyUsed = false;
					for(Vertex v : VigralGUI.getInstance().getGraphBuilder().getGraph().getVertices()) {
						if(v.getLabel().equals(newLabel) || v.getIdentifier().equals(newLabel)) {
							alreadyUsed = true;
							break;
						}
					}
					if(!alreadyUsed)
						((Vertex) mElement).setLabel(newLabel);
				}
				else { // it is an Edge
					String in = mTxt_field.getText().toString().trim();
					if(in.equals(""))
						in = "1";
					
					try {
						Double inVal = Double.parseDouble(in);
						((Edge) mElement).setWeight(inVal);
						dispose();
					} catch(NumberFormatException ex) {
						mTxt_field.setBorder(mErrorBorder);
						mLbl_errorHint.setVisible(true);
						ex.printStackTrace();
					}
					
				}
			}
		});
		mOkButton.setActionCommand("OK");
		mButtonPane.add(mOkButton);
		getRootPane().setDefaultButton(mOkButton);
		

		mCancelButton.setText("Cancel");
		mCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mCancelButton.setActionCommand("Cancel");
		mButtonPane.add(mCancelButton);
	}
	
	
	private void resetErrorStates() {
		mTxt_field.setBorder(mDefaultBorder);
		mLbl_errorHint.setVisible(false);
	}


}

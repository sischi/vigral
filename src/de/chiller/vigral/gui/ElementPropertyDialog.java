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
import java.util.ArrayList;

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
import de.chiller.vigral.util.Pair;


@SuppressWarnings("serial")
public class ElementPropertyDialog<GE> extends JDialog {


	private GE mElement;
	private static final int MARGIN = 10;
	

	private final JPanel mContentPanel = new JPanel();
	private ArrayList<Pair<JLabel, JTextField>> mComponents = new ArrayList<Pair<JLabel, JTextField>>();
	private ArrayList<JLabel> mErrorLabels = new ArrayList<JLabel>();
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
		mErrorBorder = BorderFactory.createLineBorder(Color.RED, 2);
		
		mComponents.add(new Pair<JLabel, JTextField>(new JLabel(), new JTextField()));
		mErrorLabels.add(new JLabel());
		if(mElement instanceof Edge) {
			mComponents.add(new Pair<JLabel, JTextField>(new JLabel(), new JTextField()));
			mComponents.add(new Pair<JLabel, JTextField>(new JLabel(), new JTextField()));
			mErrorLabels.add(new JLabel());
			mErrorLabels.add(new JLabel());
		}
		mDefaultBorder = mComponents.get(0).getR().getBorder();
		
		
		initComponents();
		
		
		
		if(mElement instanceof Vertex) {
			mComponents.get(0).getR().setText(""+((Vertex) mElement).getLabel());
			mComponents.get(0).getL().setText("Label");
			mErrorLabels.get(0).setText("name is already used");
		}
		else {// it is an Edge
			mComponents.get(0).getR().setText(""+((Edge) mElement).getWeight());
			mComponents.get(0).getL().setText("Weight");
			mComponents.get(1).getR().setText(""+((Edge) mElement).getMinCapacity());
			mComponents.get(1).getL().setText("Min Capacity");
			mComponents.get(2).getR().setText(""+((Edge) mElement).getMaxCapacity());
			mComponents.get(2).getL().setText("Max Capacity");
		}

		
		
		initButtonPane();
		

	}
	
	
	private void initComponents() {
		Rectangle rect = VigralGUI.getInstance().getBounds();
		if(mElement instanceof Vertex)
			setBounds(rect.x + 100, rect.y + 100, 312, 115);
		else
			setBounds(rect.x + 100, rect.y + 100, 312, 265);
		
		getContentPane().setLayout(new BorderLayout());
		mContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mContentPanel.setLayout(null);
		getContentPane().add(mContentPanel, BorderLayout.CENTER);
		

		for(int i = 0; i < mComponents.size(); i++) {
			final Pair<JLabel, JTextField> p = mComponents.get(i);
			p.getL().setBounds(MARGIN, (i * 50) + 30, 130, 15);
			
			p.getR().setBounds(150, (i * 50) + 25, 150, 20);
			p.getR().addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {}
				@Override
				public void focusGained(FocusEvent e) {
					p.getR().selectAll();
				}
			});
			
			JLabel errorLabel = mErrorLabels.get(i);
			errorLabel.setBounds(150, (i * 50) + 10, 150, 15);
			errorLabel.setForeground(Color.RED);
			errorLabel.setText("only floating numbers");
			Font font = mErrorLabels.get(0).getFont().deriveFont(10.0f);
			errorLabel.setFont(font);
			errorLabel.setVisible(false);
			
			mContentPanel.add(p.getL());
			mContentPanel.add(p.getR());
			mContentPanel.add(errorLabel);
		}

	}
	
	private void initButtonPane() {
		
		mButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(mButtonPane, BorderLayout.SOUTH);
		
		mOkButton.setText("OK");
		mOkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetErrorStates();
				if(mElement instanceof Vertex) {
					String newLabel = mComponents.get(0).getR().getText().toString().trim();
					boolean alreadyUsed = false;
					if(((Vertex) mElement).getLabel().equals(newLabel) || ((Vertex) mElement).getIdentifier().equals(newLabel)) {
						((Vertex) mElement).setLabel(newLabel);
						dispose();
					}
					for(Vertex v : VigralGUI.getInstance().getGraphBuilder().getGraph().getVertices()) {
						if(v.getLabel().equals(newLabel) || v.getIdentifier().equals(newLabel)) {
							alreadyUsed = true;
							break;
						}
					}
					
					if(!alreadyUsed) {
						((Vertex) mElement).setLabel(newLabel);
						dispose();
					}
					else {
						mComponents.get(0).getR().setBorder(mErrorBorder);
						mErrorLabels.get(0).setVisible(true);
					}
				}
				else { // it is an Edge
					ArrayList<Double> values = new ArrayList<Double>();
					boolean errorOccured = false;
					for(int i = 0; i < 3; i++) {
						String in = mComponents.get(i).getR().getText().toString().trim();
						if(in.equals(""))
							in = "1";
						
						try {
							values.add(Double.parseDouble(in));
						} catch(NumberFormatException ex) {
							mComponents.get(i).getR().setBorder(mErrorBorder);
							mErrorLabels.get(i).setVisible(true);
							errorOccured = true;
							ex.printStackTrace();
						}
					}
					
					if(!errorOccured) {
						((Edge) mElement).setWeight(values.get(0));
						((Edge) mElement).setMinCapacity(values.get(1));
						((Edge) mElement).setMaxCapacity(values.get(2));
						dispose();
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
		for(int i = 0; i < mComponents.size(); i++) {
			mComponents.get(i).getR().setBorder(mDefaultBorder);
			mErrorLabels.get(i).setVisible(false);
		}
	}


}

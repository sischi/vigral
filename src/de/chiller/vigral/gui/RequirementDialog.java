package de.chiller.vigral.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.chiller.vigral.Pair;
import de.chiller.vigral.algorithm.AbstractAlgorithm;
import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.ElementType;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.graph.Vertex;


public class RequirementDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MARGIN = 20;
	private static final Dimension MAX_LABEL_DIMENSION = new Dimension(500, 25);
	
	private AbstractAlgorithm mAlgorithm;
	private Graph mGraph;
	
	private final JPanel contentPanel = new JPanel();
	private ArrayList<Pair<JLabel, JComboBox>> mComboBoxes = new ArrayList<Pair<JLabel, JComboBox>>();
	private ArrayList<Pair<ElementType, String>> mRequirements;
	
	private ArrayList<Vertex> mVertices;
	private ArrayList<Edge> mEdges;
	
	
	/**
	 * Create the dialog.
	 */
	public RequirementDialog(ArrayList<Pair<ElementType, String>> requirements, Graph graph, AbstractAlgorithm algorithm) {
		mRequirements = requirements;
		mAlgorithm = algorithm;
		mGraph = graph;
		
		// get the vertices of the graph
		mVertices = new ArrayList<Vertex>(graph.getVertices());
		System.out.println("unsorted: "+ mVertices);
		// sort the vertices (by label)
		Collections.sort(mVertices);
		System.out.println("sorted: "+ mVertices);
		
		// get the edges of the graph
		mEdges = new ArrayList<Edge>(graph.getEdges());
		// sort edges (by id)
		Collections.sort(mEdges);
		
		// put the labels in lists
		ArrayList<String> vertexLabels = new ArrayList<String>();
		ArrayList<String> edgeLabels = new ArrayList<String>();
		for(int i = 0; i < mVertices.size(); i++)
			vertexLabels.add(i, mVertices.get(i).getLabel());
		for(int i = 0; i < mEdges.size(); i++)
			edgeLabels.add(i, mEdges.get(i).toString());
		
		
		for(int i = 0; i < mRequirements.size(); i++) {
			
			// init the label
			JLabel lbl = new JLabel();
			ElementType type = mRequirements.get(i).getL();
			String name = mRequirements.get(i).getR();
			if(type == ElementType.VERTEX || type == ElementType.EDGE)
				lbl.setText(name);
			else
				lbl.setText(name +" (optional)");
			
			// set size of the label
			lbl.setMaximumSize(MAX_LABEL_DIMENSION);
			int x = MARGIN;
			int y = i * (MAX_LABEL_DIMENSION.height + MARGIN);
			lbl.setBounds(x, y, lbl.getPreferredSize().width, lbl.getPreferredSize().height);
			
			// init the combobox
			JComboBox box = new JComboBox();
			DefaultComboBoxModel<?> model = null;
			if(type == ElementType.VERTEX)
				model = new DefaultComboBoxModel<Object>(vertexLabels.toArray());
			else if(type == ElementType.EDGE)
				model = new DefaultComboBoxModel<Object>(edgeLabels.toArray());
			else if(type == ElementType.OPTIONAL_VERTEX) {
				vertexLabels.add(0, "--");
				model = new DefaultComboBoxModel<Object>(vertexLabels.toArray());
				vertexLabels.remove(0);
			}
			else if(type == ElementType.OPTIONAL_EDGE) {
				edgeLabels.add(0, "--");
				model = new DefaultComboBoxModel<Object>(edgeLabels.toArray());
				edgeLabels.remove(0);
			}
			
			// set size of combobox
			box.setModel(model);
			x = 250;
			y = i * (box.getPreferredSize().height + MARGIN);
			box.setBounds(x, y, box.getPreferredSize().width, box.getPreferredSize().height);
			
			// add the label and combobox
			mComboBoxes.add(i, new Pair<JLabel, JComboBox>(lbl, box));
			contentPanel.add(lbl);
			contentPanel.add(box);
		}
		
		setBounds(VigralGUI.getInstance().getX() + 100, VigralGUI.getInstance().getY() + 100, 350, 300);
		
		
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						ArrayList<Integer> required = new ArrayList<Integer>();
						for(int i = 0; i < mComboBoxes.size(); i++) {
							Pair<JLabel, JComboBox> pair = mComboBoxes.get(i);
							String lbl = (String) pair.getR().getSelectedItem();
							ElementType requiredType = mRequirements.get(i).getL();
							switch (requiredType) {
							
							case OPTIONAL_VERTEX:
								if(lbl.equals("--"))
									required.add(-1);
								else {
									required.add(mVertices.get(pair.getR().getSelectedIndex() - 1).getId());
								}
								break;
							
							case OPTIONAL_EDGE:
								if(lbl.equals("--"))
									required.add(-1);
								else
									required.add(mEdges.get(pair.getR().getSelectedIndex() - 1).getId());
								break;
							
							case EDGE:
								required.add(mEdges.get(pair.getR().getSelectedIndex()).getId());
								break;
								
							case VERTEX:
								required.add(mVertices.get(pair.getR().getSelectedIndex()).getId());
								break;
							
							default:
								break;
							}
						}
						
						mAlgorithm.setRequirements(required);
						mAlgorithm.setGraph(mGraph);
						VigralGUI.getInstance().requirementsApplied();
						
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
}

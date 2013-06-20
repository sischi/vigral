package de.chiller.vigral.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	
	/**
	 * Create the dialog.
	 */
	public RequirementDialog(ArrayList<Pair<ElementType, String>> requirements, Graph graph, AbstractAlgorithm algorithm) {
		
		mAlgorithm = algorithm;
		mGraph = graph;
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(graph.getVertices());
		ArrayList<Edge> edges = new ArrayList<Edge>(graph.getEdges());
		ArrayList<String> vertexLabels = new ArrayList<String>();
		ArrayList<String> edgeLabels = new ArrayList<String>();
		
		for(int i = 0; i < vertices.size(); i++)
			vertexLabels.add(i, vertices.get(i).getLabel());
		
		for(int i = 0; i < edges.size(); i++)
			edgeLabels.add(i, edges.get(i).toString());
		
		for(int i = 0; i < requirements.size(); i++) {
			
			// TODO handle optional requirements
			JLabel lbl = new JLabel();
			ElementType type = requirements.get(i).getL();
			String name = requirements.get(i).getR();
			
			if(type == ElementType.VERTEX || type == ElementType.EDGE)
				lbl.setText(name);
			else
				lbl.setText(name +" (optional)");
			
			lbl.setMaximumSize(MAX_LABEL_DIMENSION);
			int x = MARGIN;
			int y = i * (MAX_LABEL_DIMENSION.height + MARGIN);
			lbl.setBounds(x, y, lbl.getPreferredSize().width, lbl.getPreferredSize().height);
			
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
			
			box.setModel(model);
			x = 250;
			y = i * (box.getPreferredSize().height + MARGIN);
			box.setBounds(x, y, box.getPreferredSize().width, box.getPreferredSize().height);
			mComboBoxes.add(new Pair<JLabel, JComboBox>(lbl, box));
			contentPanel.add(lbl);
			contentPanel.add(box);
		}
		
		setBounds(VigralGUI.getMainWindow().getX() + 100, VigralGUI.getMainWindow().getY() + 100, 350, 300);
		
		
		
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
						
						ArrayList<Integer> require = new ArrayList<Integer>();
						for(Pair<JLabel, JComboBox> p : mComboBoxes) {
							String lbl = (String) p.getR().getSelectedItem();
							if(lbl.equals("--"))
								require.add(-1);
							else {
								for(Vertex v : mGraph.getVertices()) {
									if(v.getLabel() == lbl)
										require.add(v.getId());
								}
							}
						}
						
						mAlgorithm.setRequirements(require);
						mAlgorithm.setGraph(mGraph);
						VigralGUI.getMainWindow().requirementsApplied();
						
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

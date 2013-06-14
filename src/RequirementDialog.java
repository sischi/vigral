import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class RequirementDialog extends JDialog {

	private static final int MARGIN = 20;
	private static final Dimension MAX_LABEL_DIMENSION = new Dimension(500, 25);
	
	private AbstractAlgorithm mAlgorithm;
	private Graph mGraph;
	
	private final JPanel contentPanel = new JPanel();
	private ArrayList<Pair<JLabel, JComboBox>> mComboBoxes = new ArrayList<Pair<JLabel, JComboBox>>();
	
	/**
	 * Create the dialog.
	 */
	public RequirementDialog(final JFrame parent, ArrayList<Pair<ElementType, String>> requirements, Graph graph, AbstractAlgorithm algorithm) {
		
		mAlgorithm = algorithm;
		mGraph = graph;
		ArrayList<Vertex> vertices = graph.getVertices();
		ArrayList<Edge> edges = graph.getEdges();
		
		String[] vertexLabels = new String[vertices.size()];
		String[] edgeLabels = new String[edges.size()];
		
		for(int i = 0; i < vertices.size(); i++)
			vertexLabels[i] = vertices.get(i).getLabel();
		
		for(int i = 0; i < edges.size(); i++)
			edgeLabels[i] = edges.get(i).toString();
		
		for(int i = 0; i < requirements.size(); i++) {
			
			// TODO handle optional requirements
			JLabel lbl = new JLabel();
			lbl.setText(requirements.get(i).getR());
			lbl.setMaximumSize(MAX_LABEL_DIMENSION);
			int x = MARGIN;
			int y = i * (MAX_LABEL_DIMENSION.height + MARGIN);
			lbl.setBounds(x, y, lbl.getPreferredSize().width, lbl.getPreferredSize().height);
			
			JComboBox box = new JComboBox();
			DefaultComboBoxModel model;
			if(requirements.get(i).getL() == ElementType.VERTEX)
				model = new DefaultComboBoxModel(vertexLabels);
			else
				model = new DefaultComboBoxModel(edgeLabels);
			
			box.setModel(model);
			x = 250;
			y = i * (box.getPreferredSize().height + MARGIN);
			box.setBounds(x, y, box.getPreferredSize().width, box.getPreferredSize().height);
			mComboBoxes.add(new Pair(lbl, box));
			contentPanel.add(lbl);
			contentPanel.add(box);
		}
		
		setBounds(parent.getX() + 100, parent.getY() + 100, 350, 300);
		
		
		
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
							String vertexLbl = (String) p.getR().getSelectedItem();
							for(Vertex v : mGraph.getVertices()) {
								if(v.getLabel() == vertexLbl)
									require.add(v.getId());
							}
						}
						
						mAlgorithm.setRequirements(require);
						mAlgorithm.setGraph(mGraph);
						((VigralGUI) parent).requirementsApplied();
						
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

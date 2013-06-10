import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class VertexPropertyDialog extends JDialog {

	private Vertex mVertex;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txt_label;

	/**
	 * Create the dialog.
	 */
	public VertexPropertyDialog(JFrame parent, Vertex v) {
		mVertex = v;
		
		setBounds(100, 100, 251, 115);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			txt_label = new JTextField();
			txt_label.setBounds(100, 12, 114, 19);
			contentPanel.add(txt_label);
			txt_label.setColumns(10);
			txt_label.setText(""+mVertex.getLabel());
		}
		
		JLabel lbl_label = new JLabel("Label");
		lbl_label.setBounds(12, 14, 70, 15);
		contentPanel.add(lbl_label);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mVertex.setLabel(txt_label.getText().toString().trim());
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

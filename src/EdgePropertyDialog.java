import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class EdgePropertyDialog extends JDialog {

	private Edge mEdge;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txt_weight;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		try {
			EdgePropertyDialog dialog = new EdgePropertyDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/

	/**
	 * Create the dialog.
	 */
	/*
	public EdgePropertyDialog() {		
		this(null, new Edge());
	}
	*/

	public EdgePropertyDialog(JFrame parent, Edge edge) {
		mEdge = edge;
		
		setBounds(100, 100, 251, 115);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			txt_weight = new JTextField();
			txt_weight.setBounds(100, 12, 114, 19);
			contentPanel.add(txt_weight);
			txt_weight.setColumns(10);
			txt_weight.setText(""+mEdge.getWeight());
		}
		
		JLabel lbl_weight = new JLabel("Weight");
		lbl_weight.setBounds(12, 14, 70, 15);
		contentPanel.add(lbl_weight);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mEdge.setWeight(Integer.parseInt(txt_weight.getText().toString().trim()));
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

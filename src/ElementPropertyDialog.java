import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class ElementPropertyDialog<GE> extends JDialog {

private GE mElement;
	
	private static final int MAXIMUMDIGITS = 7;

	private final JPanel contentPanel = new JPanel();
	private JTextField txt_field;

	/**
	 * Create the dialog.
	 */
	public ElementPropertyDialog(JFrame parent, GE elem) {
		mElement = elem;
		
		setBounds(100, 100, 300, 115);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			txt_field = new JTextField();
			txt_field.setBounds(150, 12, 100, 20);
			contentPanel.add(txt_field);
			if(mElement instanceof Vertex)
				txt_field.setText(""+((Vertex) mElement).getLabel());
			else {// it is an Edge
				txt_field.addKeyListener(new KeyListener() {
					@Override
					public void keyTyped(KeyEvent e) {
						char c = e.getKeyChar();
						
						if((!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) || txt_field.getText().length() == MAXIMUMDIGITS)
							e.consume();
					}
					@Override
					public void keyReleased(KeyEvent e) {
					}
					@Override
					public void keyPressed(KeyEvent arg0) {
					}
				});
				txt_field.setText(""+((Edge) mElement).getWeight());
			}
		}
		
		JLabel lbl_propName = new JLabel();
		if(mElement instanceof Vertex)
			lbl_propName.setText("Label");
		else // it is an edge
			lbl_propName.setText("Weight (1 - 9.999.999)");
		
		lbl_propName.setBounds(12, 14, 130, 15);
		contentPanel.add(lbl_propName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(mElement instanceof Vertex)
							((Vertex) mElement).setLabel(txt_field.getText().toString().trim());
						else { // it is an Edge
							String in = txt_field.getText().toString().trim();
							if(in.equals(""))
								in = "1";
							
							((Edge) mElement).setWeight(Integer.parseInt(in));
						}
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
	
	
	private void allowNumbersOnly() {
		
	}


}

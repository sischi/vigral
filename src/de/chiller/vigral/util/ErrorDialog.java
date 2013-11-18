package de.chiller.vigral.util;

import java.awt.*;
import java.io.*;
 
import javax.swing.*;
 
public class ErrorDialog {
 
	public ErrorDialog() {
		super();
	}
	
	
	public static void showQuickErrorDialog(JFrame parent, Exception e) 
	{
		ErrorDialog.showQuickErrorDialog(parent, "An error occured", e);
	}
	
	
	public static void showQuickErrorDialog(JFrame parent, String title, Exception e) {
		
		// create and configure a text area - fill it with exception text.
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
		textArea.setEditable(false);
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		textArea.setText(writer.toString());
		
		// stuff it in a scrollpane with a controlled size.
		JScrollPane scrollPane = new JScrollPane(textArea);		
		scrollPane.setPreferredSize(new Dimension(350, 150));
		
		// pass the scrollpane to the joptionpane.				
		JOptionPane.showMessageDialog(parent, scrollPane, title, JOptionPane.ERROR_MESSAGE);
	}
}

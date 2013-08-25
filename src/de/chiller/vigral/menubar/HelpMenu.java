package de.chiller.vigral.menubar;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import de.chiller.vigral.VigralGUI;


/**
 * this class is responsible for displaying the helpmenu to help the user using this software
 * @author Simon Schiller
 *
 */
public class HelpMenu extends JEditorPane {
	private static String mUrl = System.getProperty("user.dir") + File.separator + "help" + File.separator + "index.html";
	
	/**
	 * shows the help menu
	 */
	public static void showHelpMenu() {
		try {
			JFrame helpmenu = new JFrame("ViGrAl - Help");
			Rectangle rect = VigralGUI.getInstance().getBounds();
			helpmenu.setBounds(rect.x+100, rect.y+100, 400, 300);
			final JEditorPane htmlPane = new JEditorPane();
			File htmlFile = new File(mUrl);
			htmlPane.setContentType("text/html");
			htmlPane.setEditable(false);
			htmlPane.setPage(htmlFile.toURI().toURL());
			
			htmlPane.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						URL url = e.getURL();
						try {
							htmlPane.setPage(url);
						} catch (IOException e1) {
							System.out.println("Error displaying " + mUrl);
							e1.printStackTrace();
						}
					}
				}
			});
			
			JScrollPane jsp=new JScrollPane(htmlPane);
			helpmenu.add(new JScrollPane(htmlPane));
			helpmenu.setVisible(true);
		} catch (Exception e) {
			System.out.println("Error displaying " + mUrl);
			e.printStackTrace();
		}
	}
}

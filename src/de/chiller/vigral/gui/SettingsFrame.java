package de.chiller.vigral.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTabbedPane;

import de.chiller.vigral.jung.MyGraphMousePlugin;
import de.chiller.vigral.menubar.FileOperator;
import de.chiller.vigral.settings.Settings;
import de.chiller.vigral.util.Pair;

public class SettingsFrame extends JDialog {

	private JPanel contentPane;
	private JPanel mColorTab = new JPanel();
	private JPanel mKeyTab = new JPanel();
	private JPanel mButtonPane = new JPanel();
	private JButton mApplyButton = new JButton();
	private JButton mCancelButton = new JButton();
	private JTabbedPane mTabbedPane;
	private JButton mRestoreColorsButton = new JButton();
	private JButton mRestoreKeysButton = new JButton();
	private JLabel mColorLabel = new JLabel();
	private JLabel mKeyLabel = new JLabel();
	
	private JTable mColorsTable;
	private JTable mKeyTable;
	

	private static ArrayList<String> mDefaultColors = initDefColors();
	private HashMap<String, Integer> mChosenKeys = new HashMap<String, Integer>();



	/**
	 * Create the frame.
	 */
	public SettingsFrame() {
		initComponents();
	}




	



	private static ArrayList<String> initDefColors() {
		ArrayList<String> colors = new ArrayList<String>();
		colors.add(Settings.DEF_COLOR_UNVISITED);
		colors.add(Settings.DEF_COLOR_ACTIVE);
		colors.add(Settings.DEF_COLOR_VISITED);
		colors.add(Settings.DEF_COLOR_FINISHED_AND_RELEVANT);
		colors.add(Settings.DEF_COLOR_FINISHED_AND_NOT_RELEVANT);
		colors.add(Settings.DEF_COLOR_PICKED);
		return colors;
	}








	private void initComponents() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Rectangle rect = VigralGUI.getInstance().getBounds();
		setBounds(rect.x + 100, rect.y + 100, 500, 280);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		mTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mTabbedPane.addTab("Colors", null, mColorTab, "change state colors");
		mTabbedPane.addTab("Keys", null, mKeyTab, "edit drawing keys");
		
		initButtonPane();
		
		contentPane.add(mTabbedPane, BorderLayout.CENTER);

		initColorTab();
		
		initKeyTab();
		
	}


	private void initColorTab() {
		mColorLabel.setText("Change the hexaecimal value in the middle column.");
		mColorLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mColorTab.add(mColorLabel);
		
		String[] columnNames = {"State", "Color (#RRGGBB)", "Color preview"};
		Object[][] data = {{"UNVISITED", Settings.mSettingsColor.get(Settings.COLOR_UNVISITED), ""},
				{"ACTIVE", Settings.mSettingsColor.get(Settings.COLOR_ACTIVE), ""},
				{"VISITED", Settings.mSettingsColor.get(Settings.COLOR_VISITED), ""},
				{"FINISHED_AND_RELEVANT", Settings.mSettingsColor.get(Settings.COLOR_FINISHED_AND_RELEVANT), ""},
				{"FINISHED_AND_NOT_RELEVANT", Settings.mSettingsColor.get(Settings.COLOR_FINISHED_AND_NOT_RELEVANT), ""},
				{"PICKED", Settings.mSettingsColor.get(Settings.COLOR_PICKED), ""}
		};
		mColorsTable = new JTable(data, columnNames) {
			public boolean isCellEditable(int row, int col) {
				if(col == 1)
					return true;
				return false;
			}
		};
		mColorsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected, boolean hasFocus, int row, int col) {
//				System.out.println("Table renderer is called for ("+row+"/"+col+")");
				final Component c = super.getTableCellRendererComponent(table, val, isSelected, hasFocus, row, col);
				if((col+1) % 2 == 0) {
					((AbstractTableModel) mColorsTable.getModel()).fireTableCellUpdated(row, 2);
					c.setBackground(Color.WHITE);
				}
				else if((col+1) % 3 == 0) {
		        	String colorStr = (String) table.getValueAt(row, col - 1);
//		        	System.out.println("column 3 with color: "+ colorStr);
		        	try {
		        		Color color = Color.decode(colorStr);
		        		c.setBackground(color);
		        	} catch (Exception e) {
		        		String defColor = mDefaultColors.get(row);
		        		table.setValueAt(defColor, row, 1);
		        		c.setBackground(Color.decode(defColor));
		        		e.printStackTrace();
		        	}
		        }
		        else {
		        	c.setBackground(Color.WHITE);
		        }
		        return c;
			}
		});

		TableColumn column = mColorsTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(150);

		JScrollPane scrollPane = new JScrollPane(mColorsTable);
		Dimension dimen = new Dimension(450, 120);
		scrollPane.setMaximumSize(dimen);
		scrollPane.setMinimumSize(dimen);
		scrollPane.setPreferredSize(dimen);
		scrollPane.setBorder(null);
		mColorTab.add(scrollPane);
		
		mRestoreColorsButton.setText("Restore Defaults");
		mRestoreColorsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restoreDefaultColors();
			}
		});
		mRestoreColorsButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		mColorTab.add(mRestoreColorsButton);
	}
	
	
	private void initKeyTab() {
		
		mKeyLabel.setText("Double click on a row and press the desired key.");
		mKeyLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mKeyTab.add(mKeyLabel);
		
		String[] columnNames = {"Action", "Key"};
		Object[][] data = {{Settings.mKeyKeyset.get(0), KeyEvent.getKeyText(Settings.mSettingsKey.get(Settings.KEY_UNDIRECTED_EDGE))},
				{Settings.mKeyKeyset.get(1), KeyEvent.getKeyText(Settings.mSettingsKey.get(Settings.KEY_DIRECTED_EDGE))},
				{Settings.mKeyKeyset.get(2), KeyEvent.getKeyText(Settings.mSettingsKey.get(Settings.KEY_MULTIPLE_SELECT))},
				{Settings.mKeyKeyset.get(3), KeyEvent.getKeyText(Settings.mSettingsKey.get(Settings.KEY_RECTANGULAR_SELECT))},
		};
		mKeyTable = new JTable(data, columnNames) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		mKeyTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					int index = mKeyTable.getSelectedRow();

					KeyBinderDialog dialog = new KeyBinderDialog(Settings.mKeyKeyset.get(index));
					dialog.setModal(true);
					dialog.setVisible(true);
					int result = dialog.getKey();
					if(result != KeyBinderDialog.CANCELED && result != KeyBinderDialog.NO_CHOICE) {
						mChosenKeys.put(Settings.mKeyKeyset.get(index), result);
						mKeyTable.setValueAt(KeyEvent.getKeyText(result), index, 1);
					}
				}
			}
		});

		TableColumn column = mKeyTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(150);

		JScrollPane scrollPane = new JScrollPane(mKeyTable);
		Dimension dimen = new Dimension(450, 120);
		scrollPane.setMaximumSize(dimen);
		scrollPane.setMinimumSize(dimen);
		scrollPane.setPreferredSize(dimen);
		scrollPane.setBorder(null);
		mKeyTab.add(scrollPane);
		
		mRestoreKeysButton.setText("Restore Defaults");
		mRestoreKeysButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restoreDefaultKeys();
			}
		});
		mRestoreKeysButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		mKeyTab.add(mRestoreKeysButton);
	}

	
	private void initButtonPane() {
		
		mButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		contentPane.add(mButtonPane, BorderLayout.SOUTH);
		
		mApplyButton.setText("Apply");
		mApplyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSettings();
				dispose();
			}
		});
		mApplyButton.setActionCommand("Apply");
		mButtonPane.add(mApplyButton);
		getRootPane().setDefaultButton(mApplyButton);
		

		mCancelButton.setText("Cancel");
		mCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mCancelButton.setActionCommand("Cancel");
		mButtonPane.add(mCancelButton);
	}
		
	
	private void saveSettings() {
		
		Settings.mSettingsColor.put(Settings.COLOR_UNVISITED, (String) mColorsTable.getValueAt(0, 1));
		Settings.mSettingsColor.put(Settings.COLOR_ACTIVE, (String) mColorsTable.getValueAt(1, 1));
		Settings.mSettingsColor.put(Settings.COLOR_VISITED, (String) mColorsTable.getValueAt(2, 1));
		Settings.mSettingsColor.put(Settings.COLOR_FINISHED_AND_RELEVANT, (String) mColorsTable.getValueAt(3, 1));
		Settings.mSettingsColor.put(Settings.COLOR_FINISHED_AND_NOT_RELEVANT, (String) mColorsTable.getValueAt(4, 1));
		Settings.mSettingsColor.put(Settings.COLOR_PICKED, (String) mColorsTable.getValueAt(5, 1));
		
		for(String key : mChosenKeys.keySet()) {
			Settings.mSettingsKey.put(key, mChosenKeys.get(key));
		}
//		System.out.println("new keymap: "+ Settings.mSettingsKey);
		
		Settings.saveSettings();
	}
	
	
	private void restoreDefaultColors() {
//		mPreferences.put(COLOR_UNVISITED, DEF_COLOR_UNVISITED);
//		mPreferences.put(COLOR_ACTIVE, DEF_COLOR_ACTIVE);
//		mPreferences.put(COLOR_VISITED, DEF_COLOR_VISITED);
//		mPreferences.put(COLOR_FINISHED_AND_RELEVANT, DEF_COLOR_FINISHED_AND_RELEVANT);
//		mPreferences.put(COLOR_FINISHED_AND_NOT_RELEVANT, DEF_COLOR_FINISHED_AND_NOT_RELEVANT);
//		mPreferences.put(COLOR_PICKED, DEF_COLOR_PICKED);
//		

		Settings.restoreDefaultColors();
		for(int i = 0; i < Settings.mColorKeyset.size(); i++)
			mColorsTable.setValueAt(Settings.mSettingsColor.get(Settings.mColorKeyset.get(i)), i, 1);
//		mColorsTable.setValueAt(Settings.DEF_COLOR_ACTIVE, 1, 1);
//		mColorsTable.setValueAt(Settings.DEF_COLOR_VISITED, 2, 1);
//		mColorsTable.setValueAt(DEF_COLOR_FINISHED_AND_RELEVANT, 3, 1);
//		mColorsTable.setValueAt(DEF_COLOR_FINISHED_AND_NOT_RELEVANT, 4, 1);
//		mColorsTable.setValueAt(DEF_COLOR_PICKED, 5, 1);
	}
	
	
	private void restoreDefaultKeys() {
//		mPreferences.putInt(KEY_UNDIRECTED_EDGE, DEF_KEY_UNDIRECTED_EDGE);
//		mPreferences.putInt(KEY_DIRECTED_EDGE, DEF_KEY_DIRECTED_EDGE);
//		mPreferences.putInt(KEY_MULTIPLE_SELECT, DEF_KEY_MULTIPLE_SELECT);
//		mPreferences.putInt(KEY_RECTANGULAR_SELECT, DEF_KEY_RECTANGULAR_SELECT);
		
		Settings.restoreDefaultKeys();
		for(int i = 0; i < Settings.mKeyKeyset.size(); i++)
			mKeyTable.setValueAt(Settings.mSettingsKey.get(Settings.mKeyKeyset.get(i)), i, 1);
		
//		mKeyTable.setValueAt(KeyEvent.getKeyText(DEF_KEY_UNDIRECTED_EDGE), 0, 1);
//		mKeyTable.setValueAt(KeyEvent.getKeyText(DEF_KEY_DIRECTED_EDGE), 1, 1);
//		mKeyTable.setValueAt(KeyEvent.getKeyText(DEF_KEY_MULTIPLE_SELECT), 2, 1);
//		mKeyTable.setValueAt(KeyEvent.getKeyText(DEF_KEY_RECTANGULAR_SELECT), 3, 1);
	}

	
}

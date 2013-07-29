package de.chiller.vigral.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

public class SettingsDialog extends JDialog {

	private JPanel contentPane;
	private JPanel mColorTab = new JPanel();
	private JPanel mKeyTab = new JPanel();
	private JPanel mViewTab = new JPanel();
	private JPanel mButtonPane = new JPanel();
	private JButton mApplyButton = new JButton();
	private JButton mCancelButton = new JButton();
	private JTabbedPane mTabbedPane;
	private JButton mRestoreColorsButton = new JButton();
	private JButton mRestoreKeysButton = new JButton();
	private JLabel mColorLabel = new JLabel();
	private JLabel mKeyLabel = new JLabel();
	private JLabel mViewLabel = new JLabel();
	
	private JTable mColorsTable;
	private JTable mKeyTable;
	

//	private static ArrayList<String> mDefaultColors = new ArrayList<String>();
	private HashMap<String, Integer> mChosenKeys = new HashMap<String, Integer>();
	private ArrayList<String> mCheckedProperties = new ArrayList<String>();


	/**
	 * Create the frame.
	 */
	public SettingsDialog() {
		initComponents();
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
		mTabbedPane.addTab("View", null, mViewTab, "modify visibility of edge properties");
		
		initButtonPane();
		
		contentPane.add(mTabbedPane, BorderLayout.CENTER);

		initColorTab();
		
		initKeyTab();
		
		initViewTab();
		
	}


	private void initViewTab() {
		mViewTab.setLayout(new BorderLayout());
		Box box = Box.createVerticalBox();
		box.setAlignmentX(Box.LEFT_ALIGNMENT);
		mViewLabel.setText("choose the displayed edge properties");
		mViewLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		mViewLabel.setLocation(10, 10);
		box.add(mViewLabel);
		
		JCheckBox chkWeight = new JCheckBox("Weight");
		chkWeight.setAlignmentX(JCheckBox.LEFT_ALIGNMENT);
		chkWeight.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("weight selected? -"+ (e.getStateChange() == ItemEvent.SELECTED));
				if(e.getStateChange() == ItemEvent.SELECTED) 
					mCheckedProperties.add(Settings.VIEW_WEIGHT);
				else
					mCheckedProperties.remove(Settings.VIEW_WEIGHT);
				System.out.println("save properties: "+ mCheckedProperties);
			}
		});
		if(Settings.getView(Settings.VIEW_WEIGHT) == true)
			chkWeight.setSelected(true);
		
		JCheckBox chkMinCapacity = new JCheckBox("Minimum Capacity");
		chkMinCapacity.setAlignmentX(JCheckBox.LEFT_ALIGNMENT);
		chkMinCapacity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("min capacity selected? -"+ (e.getStateChange() == ItemEvent.SELECTED));
				if(e.getStateChange() == ItemEvent.SELECTED) 
					mCheckedProperties.add(Settings.VIEW_MIN_CAPACITY);
				else
					mCheckedProperties.remove(Settings.VIEW_MIN_CAPACITY);
				System.out.println("save properties: "+ mCheckedProperties);
			}
		});
		if(Settings.getView(Settings.VIEW_MIN_CAPACITY) == true)
			chkMinCapacity.setSelected(true);
		
		JCheckBox chkMaxCapacity = new JCheckBox("Maximum Capacity");
		chkMaxCapacity.setAlignmentX(JCheckBox.LEFT_ALIGNMENT);
		chkMaxCapacity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("max capacity selected? -"+ (e.getStateChange() == ItemEvent.SELECTED));
				if(e.getStateChange() == ItemEvent.SELECTED) 
					mCheckedProperties.add(Settings.VIEW_MAX_CAPACITY);
				else
					mCheckedProperties.remove(Settings.VIEW_MAX_CAPACITY);
				System.out.println("save properties: "+ mCheckedProperties);
			}
		});
		if(Settings.getView(Settings.VIEW_MAX_CAPACITY) == true)
			chkMaxCapacity.setSelected(true);
		
		box.add(chkWeight);
		box.add(chkMinCapacity);
		box.add(chkMaxCapacity);
		
		mViewTab.add(box, BorderLayout.WEST);
	}







	private void initColorTab() {
		mColorLabel.setText("Change the hexaecimal value in the middle column.");
		mColorLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mColorTab.add(mColorLabel);
		
		String[] columnNames = {"State", "Color (#RRGGBB)", "Color preview"};
		Object[][] data = {{Settings.COLOR_UNVISITED, Settings.getColor(Settings.COLOR_UNVISITED), ""},
				{Settings.COLOR_ACTIVE, Settings.getColor(Settings.COLOR_ACTIVE), ""},
				{Settings.COLOR_VISITED, Settings.getColor(Settings.COLOR_VISITED), ""},
				{Settings.COLOR_FINISHED_AND_RELEVANT, Settings.getColor(Settings.COLOR_FINISHED_AND_RELEVANT), ""},
				{Settings.COLOR_FINISHED_AND_NOT_RELEVANT, Settings.getColor(Settings.COLOR_FINISHED_AND_NOT_RELEVANT), ""},
				{Settings.COLOR_PICKED, Settings.getColor(Settings.COLOR_PICKED), ""}
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
		        		colorStr = Settings.getColor((String) table.getValueAt(row, 0));
		        		table.setValueAt(colorStr, row, 1);
		        		c.setBackground(Color.decode(colorStr));
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
		Object[][] data = {{Settings.mKeyKeyset.get(0), KeyEvent.getKeyText(Settings.getKey(Settings.KEY_UNDIRECTED_EDGE))},
				{Settings.mKeyKeyset.get(1), KeyEvent.getKeyText(Settings.getKey(Settings.KEY_DIRECTED_EDGE))},
				{Settings.mKeyKeyset.get(2), KeyEvent.getKeyText(Settings.getKey(Settings.KEY_MULTIPLE_SELECT))},
				{Settings.mKeyKeyset.get(3), KeyEvent.getKeyText(Settings.getKey(Settings.KEY_RECTANGULAR_SELECT))},
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
					System.out.println("selected index = "+ index);
					KeyBinderDialog dialog = new KeyBinderDialog(Settings.mKeyKeyset.get(index));
					dialog.setModal(true);
					dialog.setVisible(true);
					int result = dialog.getKey();
					System.out.println("result = "+ result +"("+ KeyEvent.getKeyText(result) +")");
					if(result != KeyBinderDialog.CANCELED && result != KeyBinderDialog.NO_CHOICE) {
						boolean alreadyUsed = false;
						for(int i = 0; i < mKeyTable.getRowCount(); i++) {
							System.out.println("value at i = "+ i +" is "+ mKeyTable.getValueAt(i, 1));
							if(mKeyTable.getValueAt(i, 1).equals(KeyEvent.getKeyText(result)) && i != index) {
								alreadyUsed = true;
							}
						}
						System.out.println("alreadyUsed = "+ alreadyUsed);
						if(!alreadyUsed) {
							mChosenKeys.put(Settings.mKeyKeyset.get(index), result);
							mKeyTable.setValueAt(KeyEvent.getKeyText(result), index, 1);
						}
						else {
							JOptionPane.showMessageDialog(null, "This key is already used.");
						}
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
				VigralGUI.getInstance().getGraphBuilder().redraw();
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
		
		for(int row = 0; row < mColorsTable.getRowCount(); row++) {
			Settings.updateColorSetting((String) mColorsTable.getValueAt(row, 0), (String) mColorsTable.getValueAt(row, 1));
		}
		
		for(String key : mChosenKeys.keySet()) {
			Settings.updateKeySetting(key, mChosenKeys.get(key));
		}
		
		System.out.println("save properties: "+ mCheckedProperties);
		for(String key : Settings.mViewKeyset) {
			if(mCheckedProperties.contains(key))
				Settings.updateViewSetting(key, true);
			else
				Settings.updateViewSetting(key, false);
		}
		
		Settings.saveSettings();
	}
	
	
	private void restoreDefaultColors() {
		Settings.restoreDefaultColors();
		for(int i = 0; i < Settings.mColorKeyset.size(); i++)
			mColorsTable.setValueAt(Settings.getColor(Settings.mColorKeyset.get(i)), i, 1);
	}
	
	
	private void restoreDefaultKeys() {
		Settings.restoreDefaultKeys();
		for(int i = 0; i < Settings.mKeyKeyset.size(); i++)
			mKeyTable.setValueAt(KeyEvent.getKeyText(Settings.getKey(Settings.mKeyKeyset.get(i))), i, 1);
	}

	
}

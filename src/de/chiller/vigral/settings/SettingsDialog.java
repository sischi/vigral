package de.chiller.vigral.settings;

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
import java.lang.reflect.Array;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTabbedPane;

import de.chiller.vigral.VigralGUI;
import de.chiller.vigral.jung.MyGraphMousePlugin;
import de.chiller.vigral.util.FileOperator;
import de.chiller.vigral.util.Pair;



/**
 * this class represents the settings dialog and gives the user the ability to change the settings
 * @author Simon Schiller
 *
 */
public class SettingsDialog extends JDialog {

	private JPanel contentPane;
	private JPanel mColorTab = new JPanel();
	private JPanel mKeyTab = new JPanel();
	private JPanel mViewTab = new JPanel();
	private JPanel mLabelTab = new JPanel();
	private JPanel mButtonPane = new JPanel();
	private JButton mApplyButton = new JButton();
	private JButton mCancelButton = new JButton();
	private JTabbedPane mTabbedPane;
	private JButton mRestoreColorsButton = new JButton();
	private JButton mRestoreKeysButton = new JButton();
	private JButton mRestoreLabelsButton = new JButton();
	private JLabel mColorLabel = new JLabel();
	private JLabel mKeyLabel = new JLabel();
	private JLabel mViewLabel = new JLabel();
	private JLabel mLabelLabel = new JLabel();
	
	private JTable mColorsTable;
	private JTable mKeyTable;
	private JTextField mVertexLabelSize;
	private JTextField mEdgeLabelSize;
	private Settings mSettings;

	private HashMap<String, Integer> mChosenKeys = new HashMap<String, Integer>();
	private ArrayList<String> mCheckedProperties = new ArrayList<String>();


	private ArrayList<String> mKeyKeyset;
	private ArrayList<String> mColorKeyset;
	private ArrayList<String> mViewKeyset;
	private ArrayList<String> mLabelKeyset;
	
	/**
	 * Create the frame.
	 */
	public SettingsDialog() {
		mSettings = Settings.getInstance();
		mKeyKeyset = mSettings.getKeyKeySet();
		mColorKeyset = mSettings.getColorKeySet();
		mViewKeyset = mSettings.getViewKeySet();
		mLabelKeyset = mSettings.getLabelKeySet();
		initComponents();
	}






	private void initComponents() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Rectangle rect = VigralGUI.getInstance().getBounds();
		setBounds(rect.x + 100, rect.y + 100, 500, 280);
		setTitle("ViGrAl - Settings");
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		mTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mTabbedPane.addTab("Colors", null, mColorTab, "change state colors");
		mTabbedPane.addTab("Keys", null, mKeyTab, "edit drawing keys");
		mTabbedPane.addTab("View", null, mViewTab, "modify visibility of edge properties");
		mTabbedPane.addTab("Labels", null, mLabelTab, "modify label sizes of vertices and edges");
		
		initButtonPane();
		
		contentPane.add(mTabbedPane, BorderLayout.CENTER);

		initColorTab();
		
		initKeyTab();
		
		initViewTab();
		
		initLabelTab();
		
	}
	
	
	
	
	private void initLabelTab() {
		mLabelTab.setLayout(new GridLayout(3, 1, 10, 10));
		mLabelLabel.setText("enter the desired labelsize for the vertices and edges");
		mLabelLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		mLabelLabel.setLocation(10, 10);
		mLabelTab.add(mLabelLabel);
		
		JPanel temp = new JPanel();
		
		temp.setLayout(new GridLayout(2, 2, 10, 10));
		mVertexLabelSize = new JTextField(Integer.toString(mSettings.getLabelSize(mLabelKeyset.get(0))));
		mEdgeLabelSize = new JTextField(Integer.toString(mSettings.getLabelSize(mLabelKeyset.get(1))));
		
		temp.add(new JLabel("Labelsize of Vertices"));
		temp.add(mVertexLabelSize);
		temp.add(new JLabel("Labelsize of Edges"));
		temp.add(mEdgeLabelSize);
		
		mLabelTab.add(temp);
		
		
		mRestoreLabelsButton.setText("Restore Defaults");
		mRestoreLabelsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restoreDefaultLabels();
			}
		});
		mLabelTab.add(mRestoreLabelsButton);
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
		if(mSettings.getView(Settings.VIEW_WEIGHT) == true)
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
		if(mSettings.getView(Settings.VIEW_MIN_CAPACITY) == true)
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
		if(mSettings.getView(Settings.VIEW_MAX_CAPACITY) == true)
			chkMaxCapacity.setSelected(true);
		
		box.add(chkWeight);
		box.add(chkMinCapacity);
		box.add(chkMaxCapacity);
		
		mViewTab.add(box, BorderLayout.WEST);
	}







	private void initColorTab() {
		mColorLabel.setText("Change the hexadecimal value of the middle column.");
		mColorLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mColorTab.add(mColorLabel);
		
		String[] columnNames = {"State", "#RRGGBB", "Color preview"};
		Object[][] data = {{Settings.COLOR_UNVISITED, mSettings.getColor(Settings.COLOR_UNVISITED), ""},
				{Settings.COLOR_ACTIVE, mSettings.getColor(Settings.COLOR_ACTIVE), ""},
				{Settings.COLOR_VISITED, mSettings.getColor(Settings.COLOR_VISITED), ""},
				{Settings.COLOR_FINISHED_AND_RELEVANT, mSettings.getColor(Settings.COLOR_FINISHED_AND_RELEVANT), ""},
				{Settings.COLOR_FINISHED_AND_NOT_RELEVANT, mSettings.getColor(Settings.COLOR_FINISHED_AND_NOT_RELEVANT), ""},
				{Settings.COLOR_PICKED, mSettings.getColor(Settings.COLOR_PICKED), ""}
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
				final Component c = super.getTableCellRendererComponent(table, val, isSelected, hasFocus, row, col);
				if((col+1) % 2 == 0) {
					((AbstractTableModel) mColorsTable.getModel()).fireTableCellUpdated(row, 2);
					c.setBackground(Color.WHITE);
				}
				else if((col+1) % 3 == 0) {
		        	String colorStr = (String) table.getValueAt(row, col - 1);
		        	try {
		        		Color color = Color.decode(colorStr);
		        		c.setBackground(color);
		        	} catch (Exception e) {
		        		colorStr = mSettings.getColor((String) table.getValueAt(row, 0));
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
		column.setPreferredWidth(230);


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
		Object[][] data = {{mKeyKeyset.get(0), KeyEvent.getKeyText(mSettings.getKey(Settings.KEY_UNDIRECTED_EDGE))},
				{mKeyKeyset.get(1), KeyEvent.getKeyText(mSettings.getKey(Settings.KEY_DIRECTED_EDGE))},
				{mKeyKeyset.get(2), KeyEvent.getKeyText(mSettings.getKey(Settings.KEY_MULTIPLE_SELECT))},
				{mKeyKeyset.get(3), KeyEvent.getKeyText(mSettings.getKey(Settings.KEY_RECTANGULAR_SELECT))},
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
					KeyBinderDialog dialog = new KeyBinderDialog(mKeyKeyset.get(index));
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
							mChosenKeys.put(mKeyKeyset.get(index), result);
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
				//workAroundNotRedrawingEdgeLabelsByChangingSize();
				VigralGUI.getInstance().getGraphBuilder().redraw();
				//dispose();
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
	
	
	private void workAroundNotRedrawingEdgeLabelsByChangingSize() {
		mSettings.updateViewSetting(Settings.VIEW_WEIGHT, false);
		VigralGUI.getInstance().getGraphBuilder().redraw();
		mSettings.updateViewSetting(Settings.VIEW_WEIGHT, true);
		VigralGUI.getInstance().getGraphBuilder().redraw();
		saveSettings();
	}
		
	
	private void saveSettings() {
		
		
		mSettings.updateLabelSetting(mLabelKeyset.get(0), Integer.parseInt(mVertexLabelSize.getText()));
		mSettings.updateLabelSetting(mLabelKeyset.get(1), Integer.parseInt(mEdgeLabelSize.getText()));
		
		for(int row = 0; row < mColorsTable.getRowCount(); row++) {
			mSettings.updateColorSetting((String) mColorsTable.getValueAt(row, 0), (String) mColorsTable.getValueAt(row, 1));
		}
		
		for(String key : mChosenKeys.keySet()) {
			mSettings.updateKeySetting(key, mChosenKeys.get(key));
		}
		
		for(String key : mViewKeyset) {
			if(mCheckedProperties.contains(key))
				mSettings.updateViewSetting(key, true);
			else
				mSettings.updateViewSetting(key, false);
		}
		
		mSettings.saveSettings();
	}
	
	
	private void restoreDefaultColors() {
		mSettings.restoreDefaultColors();
		for(int i = 0; i < mColorKeyset.size(); i++)
			mColorsTable.setValueAt(mSettings.getColor(mColorKeyset.get(i)), i, 1);
	}
	
	
	private void restoreDefaultKeys() {
		mSettings.restoreDefaultKeys();
		for(int i = 0; i < mKeyKeyset.size(); i++)
			mKeyTable.setValueAt(KeyEvent.getKeyText(mSettings.getKey(mKeyKeyset.get(i))), i, 1);
	}
	
	private void restoreDefaultLabels() {
		mSettings.restoreDefaultLabels();
		mVertexLabelSize.setText(Integer.toString(mSettings.getLabelSize(mLabelKeyset.get(0))));
		mEdgeLabelSize.setText(Integer.toString(mSettings.getLabelSize(mLabelKeyset.get(1))));
	}

	
}

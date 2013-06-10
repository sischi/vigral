import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.plaf.basic.BasicIconFactory;

import org.apache.commons.collections15.Factory;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.annotations.AnnotatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;

public class MyModalGraphMouse<V,E> extends AbstractModalGraphMouse implements ModalGraphMouse, ItemSelectable {
	
	protected MyGraphMousePlugin<V,E> editingPlugin;
	protected MyPopupGraphMousePlugin<V,E> popupEditingPlugin;
	protected MultiLayerTransformer basicTransformer;
	protected RenderContext<V,E> rc;

	/**
	 * create an instance with default values
	 *
	 */
	public MyModalGraphMouse(RenderContext<V,E> rc) {
		this(rc, 1.1f, 1/1.1f);
	}

	/**
	 * create an instance with passed values
	 * @param in override value for scale in
	 * @param out override value for scale out
	 */
	public MyModalGraphMouse(RenderContext<V,E> rc, float in, float out) {
		super(in,out);
		this.rc = rc;
		this.basicTransformer = rc.getMultiLayerTransformer();
		loadPlugins();
	}

	/**
	 * create the plugins, and load the plugins for TRANSFORMING mode
	 *
	 */
	@Override
    protected void loadPlugins() {
		editingPlugin = new MyGraphMousePlugin<V,E>();
		popupEditingPlugin = new MyPopupGraphMousePlugin<V,E>();
		setEditingMode();
	}

	protected void setEditingMode() {
		add(editingPlugin);
		add(popupEditingPlugin);
	}

	/**
	 * @return the editingPlugin
	 */
	public MyGraphMousePlugin<V, E> getEditingPlugin() {
		return editingPlugin;
	}
}

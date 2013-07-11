package de.chiller.vigral.jung;

import java.awt.ItemSelectable;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Vertex;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

public class MyModalGraphMouse extends AbstractModalGraphMouse implements ModalGraphMouse, ItemSelectable {
	
	protected MyGraphMousePlugin mEditingPlugin;
	protected MyPopupGraphMousePlugin mPopupEditingPlugin;
	protected MultiLayerTransformer mBasicTransformer;

	/**
	 * create an instance with default values
	 *
	 */
	public MyModalGraphMouse(RenderContext<Vertex,Edge> rc) {
		this(rc, 1.1f, 1/1.1f);
	}

	/**
	 * create an instance with passed values
	 * @param in override value for scale in
	 * @param out override value for scale out
	 */
	public MyModalGraphMouse(RenderContext<Vertex,Edge> rc, float in, float out) {
		super(in,out);
		mBasicTransformer = rc.getMultiLayerTransformer();
		loadPlugins();
	}

	/**
	 * create the plugins, and load the plugins
	 *
	 */
	@Override
    protected void loadPlugins() {
		mEditingPlugin = new MyGraphMousePlugin();
		mPopupEditingPlugin = new MyPopupGraphMousePlugin();
		add(mPopupEditingPlugin);
		add(mEditingPlugin);
	}

	public void addEditingFunctionality() {
		add(mEditingPlugin);
		add(mPopupEditingPlugin);
		// enables editing in the plugin itself
		mEditingPlugin.startEditing();
	}
	
	public void removeEditingFunctionality() {
		remove(mPopupEditingPlugin);
		// disable editing in the plugin itself
		mEditingPlugin.stopEditing();
	}

	/**
	 * @return returns the editingPlugin
	 */
	public MyGraphMousePlugin getEditingPlugin() {
		return mEditingPlugin;
	}
}

package de.chiller.vigral.jung;

import java.awt.ItemSelectable;

import javax.swing.JFrame;

import de.chiller.vigral.graph.Edge;
import de.chiller.vigral.graph.Vertex;

import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;

public class MyModalGraphMouse extends AbstractModalGraphMouse implements ModalGraphMouse, ItemSelectable {
	
	protected MyGraphMousePlugin editingPlugin;
	protected MyPopupGraphMousePlugin popupEditingPlugin;
	protected MultiLayerTransformer basicTransformer;
	protected RenderContext<Vertex,Edge> rc;

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
		rc = rc;
		basicTransformer = rc.getMultiLayerTransformer();
		loadPlugins();
	}

	/**
	 * create the plugins, and load the plugins
	 *
	 */
	@Override
    protected void loadPlugins() {
		editingPlugin = new MyGraphMousePlugin();
		popupEditingPlugin = new MyPopupGraphMousePlugin();
		add(popupEditingPlugin);
		add(editingPlugin);
	}

	public void addEditingFunctionality() {
		add(popupEditingPlugin);
		add(editingPlugin);
		editingPlugin.startEditing();
	}
	
	public void removeEditingFunctionality() {
		remove(popupEditingPlugin);
		editingPlugin.stopEditing();
	}

	/**
	 * @return the editingPlugin
	 */
	public MyGraphMousePlugin getEditingPlugin() {
		return editingPlugin;
	}
}

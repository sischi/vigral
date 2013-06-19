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
	private JFrame mParent;

	/**
	 * create an instance with default values
	 *
	 */
	public MyModalGraphMouse(RenderContext<Vertex,Edge> rc, JFrame frame) {
		this(rc, 1.1f, 1/1.1f, frame);
	}

	/**
	 * create an instance with passed values
	 * @param in override value for scale in
	 * @param out override value for scale out
	 */
	public MyModalGraphMouse(RenderContext<Vertex,Edge> rc, float in, float out, JFrame frame) {
		super(in,out);
		rc = rc;
		basicTransformer = rc.getMultiLayerTransformer();
		loadPlugins();
		mParent = frame;
	}

	/**
	 * create the plugins, and load the plugins for TRANSFORMING mode
	 *
	 */
	@Override
    protected void loadPlugins() {
		editingPlugin = new MyGraphMousePlugin();
		popupEditingPlugin = new MyPopupGraphMousePlugin(mParent);
		addEditingFunctionality();
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

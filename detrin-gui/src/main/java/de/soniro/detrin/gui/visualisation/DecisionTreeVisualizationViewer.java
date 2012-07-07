package de.soniro.detrin.gui.visualisation;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;

import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * TreeVisualisation.
 *
 * @author Nina Rothenberg
 *
 * @param <V> - Typ of nodes
 * @param <E> - Typ of branches
 */
public class DecisionTreeVisualizationViewer<V, E> extends VisualizationViewer<V, E> {

	private static final long serialVersionUID = -5270688414442098392L;

	public DecisionTreeVisualizationViewer(Layout<V, E> layout) {
		super(layout);
		init();
	}

	public DecisionTreeVisualizationViewer(Layout<V, E> layout,
			Dimension preferredSize) {
		super(layout, preferredSize);
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		setBackground(Color.white);
        getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<V, E>());
        getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<E>());
        getRenderContext().setVertexLabelTransformer(new ToStringLabeller<V>());
//        getRenderContext().setVertexShapeTransformer(new ConstantTransformer(
//        		new RoundRectangle2D.Float(-40,-20,80,40, 10, 10)));
        getRenderContext().setVertexIconTransformer(new ConstantTransformer(new ImageIcon(getClass().getResource("/images/knoten.png"))));
        getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        // add a listener for ToolTips
        setVertexToolTipTransformer(new ToStringLabeller<V>());
        getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.black));
        setGraphMouse(new DecisionTreeGraphMouse());
		setToolTipText(getName());
		setEdgeToolTipTransformer(new ToStringLabeller<E>());
	}

}

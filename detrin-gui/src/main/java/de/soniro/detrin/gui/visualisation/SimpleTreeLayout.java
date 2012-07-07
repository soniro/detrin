package de.soniro.detrin.gui.visualisation;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Tree;

public class SimpleTreeLayout<V, E> extends TreeLayout<V, E> {

	public SimpleTreeLayout(Tree<V, E> graph) {
		super(graph, 75, 80); //150,130
	}

}

package de.soniro.detrin.gui.visualisation;

import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

public class DecisionTreeGraphMouse<V, E> extends DefaultModalGraphMouse<V, E>{
	
	public DecisionTreeGraphMouse() {
		setMode(ModalGraphMouse.Mode.PICKING);
	}

}

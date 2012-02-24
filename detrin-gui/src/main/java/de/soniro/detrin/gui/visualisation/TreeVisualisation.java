package de.soniro.detrin.gui.visualisation;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.soniro.detrin.gui.i18n.Messages;
import de.soniro.detrin.gui.panel.ExplanationTabPanel;
import de.soniro.detrin.model.Branch;
import de.soniro.detrin.model.DecisionTree;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbsoluteCrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

public class TreeVisualisation extends JPanel {

	private static final long serialVersionUID = -145440605200175191L;
	
	final List<DecisionTree> shownExplanations = new ArrayList<DecisionTree>();
	final List<Branch> shownBranchExplanations = new ArrayList<Branch>();
	
	public TreeVisualisation() {
		super();
		setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
	}

	public void updateTree(Tree<DecisionTree, Branch> graph) {
		removeAll();
		TreeLayout<DecisionTree, Branch> treeLayout = new SimpleTreeLayout<DecisionTree, Branch>(graph);
        final VisualizationViewer<DecisionTree, Branch> vv =  new DecisionTreeVisualizationViewer<DecisionTree,Branch>(treeLayout, Toolkit.getDefaultToolkit().getScreenSize());
        vv.addGraphMouseListener(new GraphMouseListener<DecisionTree>() {

			@Override
			public void graphClicked(DecisionTree decisionTree, MouseEvent arg1) {
				if (shownExplanations.contains(decisionTree)) {
					shownExplanations.remove(decisionTree);
					ExplanationTabPanel.getInstance().hideExplanation(decisionTree);
				} else {
					shownExplanations.add(decisionTree);
					ExplanationTabPanel.getInstance().showExplanation(decisionTree, decisionTree.getLabel());
				}
				
			}

			@Override
			public void graphPressed(DecisionTree arg0, MouseEvent arg1) {
				
			}

			@Override
			public void graphReleased(DecisionTree arg0, MouseEvent arg1) {
				
			}
        	
		});
        MouseListener ml = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent event) {
				GraphElementAccessor<DecisionTree, Branch> pickSupport = vv.getPickSupport();
			    Layout<DecisionTree, Branch> layout = vv.getGraphLayout();
				if(pickSupport != null) {
					DecisionTree vertex = pickSupport.getVertex(layout, event.getX(), event.getY());
					Branch edge = pickSupport.getEdge(layout, event.getX(), event.getY()); 
					if (vertex  == null && edge != null) {
						if (shownBranchExplanations.contains(edge)) {
							shownBranchExplanations.remove(edge);
							ExplanationTabPanel.getInstance().hideExplanation(edge);
						} else {
							shownBranchExplanations.add(edge);
							ExplanationTabPanel.getInstance().showExplanation(edge, edge.getLabel(Messages.getInstance().getLocale()));
						}
					}
				} 
			}
		};
        vv.addMouseListener(ml);
        setLayout(new BorderLayout());
        add(vv, BorderLayout.WEST);
        vv.scaleToLayout(new AbsoluteCrossoverScalingControl());
        revalidate();
        repaint();
	}
	
}

package net.logicim.ui.simulation;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.InputDialog;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import net.logicim.ui.simulation.subcircuit.SubcircuitTopEditorSimulation;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.DEFAULT_WIDTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class ShowSimulationsDialog
    extends InputDialog
{
  private Logicim editor;

  public ShowSimulationsDialog(Frame owner, Logicim editor)
  {
    super(owner, "Simulations", true, new Dimension(480, owner.getHeight() - 24));
    this.editor = editor;
  }

  @Override
  public void build()
  {
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());

    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
    JTree tree = new JTree(rootNode);
    tree.setRootVisible(true);

    for (SubcircuitTopEditorSimulation subcircuitTopEditorSimulation : editor.getSubcircuitTopSimulations())
    {
      SubcircuitEditor subcircuitEditor = subcircuitTopEditorSimulation.getSubcircuitEditor();
      SubcircuitTopSimulation subcircuitTopSimulation = subcircuitTopEditorSimulation.getSubcircuitTopSimulation();
      CircuitSimulation circuitSimulation = subcircuitTopEditorSimulation.getCircuitSimulation();

      DefaultMutableTreeNode simulationNode = new DefaultMutableTreeNode(subcircuitTopSimulation.getDescription());
      rootNode.add(simulationNode);

      DefaultMutableTreeNode circuitNode = new DefaultMutableTreeNode(subcircuitEditor.getTypeName());
      simulationNode.add(circuitNode);

      SubcircuitView subcircuitView = subcircuitEditor.getCircuitSubcircuitView();
      recurseFindChildCircuits(circuitNode, subcircuitView, circuitSimulation);
    }

    tree.setRootVisible(false);
    tree.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    contentPane.add(tree, gridBagConstraints(0, 0, 1, 1, BOTH, new Insets(5, 5, 5, 5)));

    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getRoot();
    setNodeExpandedState(tree, node, true);

    JPanel bottomPanel = buildButtons(DEFAULT_WIDTH,
                                      new CancelButton("Close", this));
    contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));
    bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
  }

  protected void recurseFindChildCircuits(DefaultMutableTreeNode parentNode, SubcircuitView subcircuitView, CircuitSimulation circuitSimulation)
  {
    Set<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitView.findAllSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      String description = subcircuitInstanceView.getShortDescription();
      DefaultMutableTreeNode child = new DefaultMutableTreeNode(description);
      parentNode.add(child);
      SubcircuitView instanceSubcircuitView = subcircuitInstanceView.getInstanceSubcircuitView();
      if (instanceSubcircuitView.getSubcircuitSimulation(circuitSimulation) != null)
      {
        recurseFindChildCircuits(child, instanceSubcircuitView, circuitSimulation);
      }
    }
  }

  public static void setNodeExpandedState(JTree tree, DefaultMutableTreeNode node, boolean expanded)
  {
    List<DefaultMutableTreeNode> list = (List) Collections.list(node.children());
    for (DefaultMutableTreeNode treeNode : list)
    {
      setNodeExpandedState(tree, treeNode, expanded);
    }
    if (!expanded && node.isRoot())
    {
      return;
    }
    TreePath path = new TreePath(node.getPath());
    if (expanded)
    {
      tree.expandPath(path);
    }
    else
    {
      tree.collapsePath(path);
    }
  }

  @Override
  public void okay()
  {
  }
}


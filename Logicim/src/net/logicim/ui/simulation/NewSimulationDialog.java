package net.logicim.ui.simulation;

import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.InputDialog;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.simulation.subcircuit.SubcircuitTopEditorSimulation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class NewSimulationDialog
    extends InputDialog
{
  protected SimulationPropertiesPanel propertiesPanel;

  public NewSimulationDialog(Frame owner,
                             Logicim editor)
  {
    super(owner,
          "Create simulation",
          true,
          new Dimension(DEFAULT_WIDTH, SMALL_HEIGHT),
          editor);
  }

  public void build()
  {
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());

    JPanel editorPanel = createEditorPanel();
    propertiesPanel = (SimulationPropertiesPanel) editorPanel;
    contentPane.add(editorPanel, gridBagConstraints(0, 0, 1, 1, BOTH));

    JPanel bottomPanel = buildButtons(DEFAULT_WIDTH,
                                      setOkayButton(new ActionButton("Okay", this)),
                                      new CancelButton("Cancel", this));
    contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));
    bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
  }

  @Override
  public void okay()
  {
    String name = propertiesPanel.getSimulationName();
    editor.addEditorEvent(new NewSimulationEvent(name));

    close();
  }

  protected JPanel createEditorPanel()
  {
    List<SubcircuitTopEditorSimulation> subcircuitTopSimulations = editor.getSubcircuitTopSimulations();
    return new SimulationPropertiesPanel("Top " + editor.getCurrentSubcircuitEditor().getTypeName() + " " + (subcircuitTopSimulations.size() + 1));
  }
}


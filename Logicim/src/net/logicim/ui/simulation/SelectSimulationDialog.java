package net.logicim.ui.simulation;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.InputDialog;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.property.FormPanel;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class SelectSimulationDialog
    extends InputDialog
{
  private Logicim editor;
  private JComboBox<SubcircuitSimulation> comboBox;

  public SelectSimulationDialog(Frame owner, Logicim editor)
  {
    super(owner, "Select Simulation", true, new Dimension(DEFAULT_WIDTH, SMALL_HEIGHT));
    this.editor = editor;
  }

  @Override
  public void build()
  {
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());

    FormPanel formPanel = new FormPanel();
    contentPane.add(formPanel, gridBagConstraints(0, 0, 1, 1, BOTH));

    comboBox = new JComboBox<>();
    SubcircuitEditor currentSubcircuitEditor = editor.getCurrentSubcircuitEditor();
    List<SubcircuitSimulation> simulations = new ArrayList<>(currentSubcircuitEditor.getSubcircuitSimulations());
    SubcircuitSimulation currentSimulation = editor.getSubcircuitSimulation();
    for (SubcircuitSimulation simulation : simulations)
    {
      comboBox.addItem(simulation);
    }
    comboBox.setSelectedItem(currentSimulation);

    formPanel.addLabeledComponent("Simulation", comboBox);

    ActionButton okayButton = new ActionButton("Okay", this);
    JPanel bottomPanel = buildButtons(DEFAULT_WIDTH,
                                      okayButton,
                                      new CancelButton("Cancel", this));
    setOkayButton(okayButton);
    contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));
    bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
  }

  @Override
  public void okay()
  {
    SubcircuitSimulation simulation = (SubcircuitSimulation) comboBox.getSelectedItem();
    editor.setCurrentSimulation(simulation);

    close();
  }
}


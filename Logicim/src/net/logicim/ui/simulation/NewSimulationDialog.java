package net.logicim.ui.simulation;

import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.InputDialog;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.CancelButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class NewSimulationDialog
    extends InputDialog
{
  protected Logicim editor;
  protected SimulationPropertiesPanel propertiesPanel;

  public NewSimulationDialog(Frame owner,
                             Logicim editor)
  {
    super(owner, "Create simulation", true, new Dimension(DEFAULT_WIDTH, SMALL_HEIGHT));

    this.editor = editor;
  }

  public void build()
  {
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());

    JPanel editorPanel = createEditorPanel();
    propertiesPanel = (SimulationPropertiesPanel) editorPanel;
    contentPane.add(editorPanel, gridBagConstraints(0, 0, 1, 1, BOTH));

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
    String name = propertiesPanel.getSimulationName();
    editor.addEditorEvent(new NewSimulationEvent(name));

    close();
  }

  protected JPanel createEditorPanel()
  {
    return new SimulationPropertiesPanel("");
  }
}


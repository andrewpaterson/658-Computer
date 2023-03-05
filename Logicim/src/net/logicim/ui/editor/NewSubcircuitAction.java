package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.SimulatorPanel;
import net.logicim.ui.circuit.EditSubcircuitDialogHelper;
import net.logicim.ui.circuit.NewSubcircuitDialog;
import net.logicim.ui.circuit.NewSubcircuitEvent;

public class NewSubcircuitAction
    extends SimulatorEditorAction
{
  protected SimulatorPanel simulatorPanel;

  public NewSubcircuitAction(SimulatorEditor editor, SimulatorPanel simulatorPanel)
  {
    super(editor);
    this.simulatorPanel = simulatorPanel;
  }

  @Override
  public void executeEditorAction()
  {
    NewSubcircuitDialog subcircuitDialog = new NewSubcircuitDialog(simulatorPanel.getFrame(), editor);
    new EditSubcircuitDialogHelper().showPropertyEditorDialog(subcircuitDialog);
  }

  @Override
  public String getDescription()
  {
    return "New Subcircuit";
  }
}


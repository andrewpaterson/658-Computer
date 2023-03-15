package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.SimulatorPanel;
import net.logicim.ui.circuit.EditSubcircuitDialog;
import net.logicim.ui.circuit.EditorDialogHelper;

public class EditSubcircuitAction
    extends SimulatorEditorAction
{
  protected SimulatorPanel simulatorPanel;

  public EditSubcircuitAction(SimulatorEditor editor, SimulatorPanel simulatorPanel)
  {
    super(editor);
    this.simulatorPanel = simulatorPanel;
  }

  @Override
  public void executeEditorAction()
  {
    EditSubcircuitDialog subcircuitDialog = new EditSubcircuitDialog(simulatorPanel.getFrame(), editor);
    new EditorDialogHelper().showPropertyEditorDialog(subcircuitDialog);
  }

  @Override
  public String getDescription()
  {
    return "New Subcircuit";
  }
}


package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.SimulatorPanel;
import net.logicim.ui.circuit.EditSubcircuitDialog;
import net.logicim.ui.circuit.EditorDialogHelper;
import net.logicim.ui.circuit.SubcircuitView;

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
    SubcircuitView subcircuitView = editor.getCircuitEditor().getCurrentSubcircuitView();
    EditSubcircuitDialog subcircuitDialog = new EditSubcircuitDialog(simulatorPanel.getFrame(), editor, subcircuitView);
    new EditorDialogHelper().showPropertyEditorDialog(subcircuitDialog);
  }

  @Override
  public String getDescription()
  {
    return "New Subcircuit";
  }
}


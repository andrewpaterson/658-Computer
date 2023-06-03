package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.EditSubcircuitDialog;
import net.logicim.ui.circuit.EditorDialogHelper;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.panels.SimulatorPanel;

public class EditSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Edit Subcircuit";

  protected SimulatorPanel simulatorPanel;

  public EditSubcircuitAction(Logicim editor, SimulatorPanel simulatorPanel)
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
}


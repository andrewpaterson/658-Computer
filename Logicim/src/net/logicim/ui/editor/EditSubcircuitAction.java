package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.EditSubcircuitDialog;
import net.logicim.ui.circuit.EditorDialogHelper;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.panels.LogicimPanel;

public class EditSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Edit Subcircuit";

  protected LogicimPanel logicimPanel;

  public EditSubcircuitAction(Logicim editor, LogicimPanel logicimPanel)
  {
    super(editor);
    this.logicimPanel = logicimPanel;
  }

  @Override
  public void executeEditorAction()
  {
    SubcircuitView subcircuitView = editor.getCircuitEditor().getCurrentSubcircuitView();
    EditSubcircuitDialog subcircuitDialog = new EditSubcircuitDialog(logicimPanel.getFrame(), editor, subcircuitView);
    new EditorDialogHelper().showDialog(subcircuitDialog);
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


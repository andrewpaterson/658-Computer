package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.EditorDialogHelper;
import net.logicim.ui.circuit.NewSubcircuitDialog;
import net.logicim.ui.panels.LogicimPanel;

public class NewSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "New Subcircuit";

  protected LogicimPanel logicimPanel;

  public NewSubcircuitAction(Logicim editor, LogicimPanel logicimPanel)
  {
    super(editor);
    this.logicimPanel = logicimPanel;
  }

  @Override
  public void executeEditorAction()
  {
    NewSubcircuitDialog subcircuitDialog = new NewSubcircuitDialog(logicimPanel.getFrame(), editor);
    new EditorDialogHelper().showDialog(subcircuitDialog);
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.LogicimPanel;

public class AddSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Add Subcircuit";

  protected LogicimPanel logicimPanel;

  public AddSubcircuitAction(Logicim editor, LogicimPanel logicimPanel)
  {
    super(editor);
    this.logicimPanel = logicimPanel;
  }

  @Override
  public void executeEditorAction()
  {
    //NewSubcircuitDialog subcircuitDialog = new AddSubcircuitDialog(simulatorPanel.getFrame(), editor);
    //new EditorDialogHelper().showDialog(subcircuitDialog);
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


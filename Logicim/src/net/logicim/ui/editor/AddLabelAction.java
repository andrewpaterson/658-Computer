package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.LogicimPanel;

public class AddLabelAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Add Label";

  protected LogicimPanel logicimPanel;

  public AddLabelAction(Logicim editor, LogicimPanel logicimPanel)
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


package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.SimulatorPanel;

public class AddPowerAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Add Power";

  protected SimulatorPanel simulatorPanel;

  public AddPowerAction(Logicim editor, SimulatorPanel simulatorPanel)
  {
    super(editor);
    this.simulatorPanel = simulatorPanel;
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


package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.EditorDialogHelper;
import net.logicim.ui.circuit.NewSubcircuitDialog;
import net.logicim.ui.panels.SimulatorPanel;

public class NewSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "New Subcircuit";

  protected SimulatorPanel simulatorPanel;

  public NewSubcircuitAction(Logicim editor, SimulatorPanel simulatorPanel)
  {
    super(editor);
    this.simulatorPanel = simulatorPanel;
  }

  @Override
  public void executeEditorAction()
  {
    NewSubcircuitDialog subcircuitDialog = new NewSubcircuitDialog(simulatorPanel.getFrame(), editor);
    new EditorDialogHelper().showPropertyEditorDialog(subcircuitDialog);
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


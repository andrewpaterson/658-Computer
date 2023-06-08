package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.SimulatorPanel;

public class AddTraceAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Add Trace";

  public AddTraceAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class AddConnectionAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Add Connection";

  public AddConnectionAction(Logicim editor)
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


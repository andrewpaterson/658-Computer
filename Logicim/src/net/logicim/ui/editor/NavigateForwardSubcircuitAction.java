package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class NavigateForwardSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Navigate Forward";

  public NavigateForwardSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.navigateForwardSubcircuit();
  }

  @Override
  public boolean isAvailable()
  {
    return false;
  }
}


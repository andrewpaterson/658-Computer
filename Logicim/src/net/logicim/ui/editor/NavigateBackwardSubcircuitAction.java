package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class NavigateBackwardSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Navigate Backward";

  public NavigateBackwardSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.navigateBackwardSubcircuit();
  }

  @Override
  public boolean isAvailable()
  {
    return false;
  }
}


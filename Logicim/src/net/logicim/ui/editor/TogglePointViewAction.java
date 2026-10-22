package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class TogglePointViewAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Toggle Point View";

  public TogglePointViewAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.togglePointGrid();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


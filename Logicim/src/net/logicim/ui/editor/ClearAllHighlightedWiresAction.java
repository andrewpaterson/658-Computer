package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ClearAllHighlightedWiresAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Clear all highlights";

  public ClearAllHighlightedWiresAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.clearAllHighlightedWires();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


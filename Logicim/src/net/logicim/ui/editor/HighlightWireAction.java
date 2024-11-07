package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class HighlightWireAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Highlight wire";

  public HighlightWireAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.highlightWire();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


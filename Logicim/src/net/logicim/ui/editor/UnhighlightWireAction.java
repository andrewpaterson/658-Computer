package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class UnhighlightWireAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Un-highlight wire";

  public UnhighlightWireAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.unhighlightWire();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


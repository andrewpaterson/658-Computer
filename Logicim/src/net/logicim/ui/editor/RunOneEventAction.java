package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class RunOneEventAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Step Once";

  public RunOneEventAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.runOneEvent();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


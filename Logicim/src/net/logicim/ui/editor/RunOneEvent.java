package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class RunOneEvent
    extends SimulatorEditorAction
{
  public RunOneEvent(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.runOneEvent();
  }

  @Override
  public String getDescription()
  {
    return "Step Once";
  }
}


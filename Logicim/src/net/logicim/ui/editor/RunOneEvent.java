package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class RunOneEvent
    extends SimulatorEditorAction
{
  public RunOneEvent(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.runOneEvent();
  }
}


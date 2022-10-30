package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class RunOneEvent extends EditorAction
{
  public RunOneEvent(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.runOneEvent();
  }
}


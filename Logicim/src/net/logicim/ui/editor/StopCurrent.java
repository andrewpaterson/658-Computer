package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class StopCurrent
    extends SimulatorEditorAction
{
  public StopCurrent(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.stopCurrentEdit();
  }
}

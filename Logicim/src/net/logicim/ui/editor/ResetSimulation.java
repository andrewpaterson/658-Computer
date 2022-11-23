package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class ResetSimulation
    extends SimulatorEditorAction
{
  public ResetSimulation(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.resetSimulation();
  }
}


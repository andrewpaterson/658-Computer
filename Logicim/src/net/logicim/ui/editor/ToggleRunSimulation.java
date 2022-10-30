package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class ToggleRunSimulation
    extends EditorAction
{
  public ToggleRunSimulation(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.toggleTunSimulation();
  }
}


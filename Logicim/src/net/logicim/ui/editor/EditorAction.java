package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public abstract class EditorAction
{
  SimulatorEditor editor;

  public EditorAction(SimulatorEditor editor)
  {
    this.editor = editor;
  }

  public abstract void execute();
}

package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public abstract class SimulatorEditorAction
    extends EditorAction
{
  protected SimulatorEditor editor;

  public SimulatorEditorAction(SimulatorEditor editor)
  {
    this.editor = editor;
  }
}


package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public abstract class SimulatorEditorAction
    extends EditorAction
{
  protected Logicim editor;

  public SimulatorEditorAction(Logicim editor)
  {
    this.editor = editor;
  }
}


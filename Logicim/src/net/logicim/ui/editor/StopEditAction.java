package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class StopEditAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Discard Current Edit";

  public StopEditAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.stopSimulatorEdit();
  }
}


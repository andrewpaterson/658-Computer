package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class PlacementRotateRight
    extends SimulatorEditorAction
{
  public PlacementRotateRight(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.rotateRight();
  }
}


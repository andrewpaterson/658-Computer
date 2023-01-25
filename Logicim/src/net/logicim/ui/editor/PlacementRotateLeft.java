package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class PlacementRotateLeft
    extends SimulatorEditorAction
{

  public PlacementRotateLeft(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.rotateLeft();
  }
}

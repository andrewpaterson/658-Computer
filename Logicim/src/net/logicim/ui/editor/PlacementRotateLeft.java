package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PlacementRotateLeft
    extends SimulatorEditorAction
{

  public PlacementRotateLeft(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.rotateLeft();
  }

  @Override
  public String getDescription()
  {
    return "Rotate Left";
  }
}

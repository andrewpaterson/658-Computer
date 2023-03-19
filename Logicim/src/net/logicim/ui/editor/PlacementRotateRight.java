package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PlacementRotateRight
    extends SimulatorEditorAction
{
  public PlacementRotateRight(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.rotateRight();
  }

  @Override
  public String getDescription()
  {
    return "Rotate Right";
  }
}


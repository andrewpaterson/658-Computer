package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PlacementRotateRightAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Rotate Right";

  public PlacementRotateRightAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.rotateRight();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canTransformComponents();
  }
}


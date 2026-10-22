package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PlacementRotateLeftAction
    extends SimulatorEditorAction
{

  public static final String NAME = "Rotate Left";

  public PlacementRotateLeftAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.rotateLeft();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canTransformComponents();
  }
}

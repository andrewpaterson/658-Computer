package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PlacementFlipHorizontallyAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Flip Horizontally";

  public PlacementFlipHorizontallyAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.flipHorizontally();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canTransformComponents();
  }
}


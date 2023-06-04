package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PlacementFlipVerticallyAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Flip Vertically";

  public PlacementFlipVerticallyAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.flipVertically();
  }
}


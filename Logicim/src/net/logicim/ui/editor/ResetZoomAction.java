package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ResetZoomAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Reset Zoom";

  public ResetZoomAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.resetZoom();
  }
}


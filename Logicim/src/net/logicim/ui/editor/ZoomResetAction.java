package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ZoomResetAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Reset Zoom";

  public ZoomResetAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.zoomReset();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canZoomReset();
  }
}


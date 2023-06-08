package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ZoomOutAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Zoom Out";

  public ZoomOutAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.zoomOut();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canZoomOut();
  }
}


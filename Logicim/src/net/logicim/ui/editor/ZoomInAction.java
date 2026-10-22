package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ZoomInAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Zoom In";

  public ZoomInAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.zoomIn();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canZoomIn();
  }
}


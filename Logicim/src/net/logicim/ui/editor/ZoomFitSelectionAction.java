package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ZoomFitSelectionAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Zoom to Fit Selection";

  public ZoomFitSelectionAction(Logicim editor)
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
    return editor.canZoomSelection();
  }
}


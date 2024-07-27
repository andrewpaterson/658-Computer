package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ZoomFitAllAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Zoom to Fit All";

  public ZoomFitAllAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.zoomFitAll();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


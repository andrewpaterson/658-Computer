package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ResetZoomAction
    extends SimulatorEditorAction
{
  public ResetZoomAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.resetZoom();
  }

  @Override
  public String getDescription()
  {
    return "Reset Zoom";
  }
}


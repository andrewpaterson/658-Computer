package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class RegeneratePathsAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Regenerate Connections";

  public RegeneratePathsAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.getCircuitEditor().createCircuitInstanceViewPaths();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


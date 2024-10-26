package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.LogicimPanel;

public class LoadSimulationAction
    extends EditorAction
{
  public static final String NAME = "Load Project";

  private LogicimPanel panel;

  public LoadSimulationAction(Logicim editor, LogicimPanel panel)
  {
    super(editor);
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.loadSimulation();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


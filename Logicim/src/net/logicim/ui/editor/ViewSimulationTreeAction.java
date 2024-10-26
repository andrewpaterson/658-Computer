package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.LogicimPanel;

public class ViewSimulationTreeAction
    extends EditorAction
{
  public static final String NAME = "View Simulation Tree";

  private LogicimPanel panel;

  public ViewSimulationTreeAction(Logicim editor, LogicimPanel panel)
  {
    super(editor);
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.showSimulations();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


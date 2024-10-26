package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.LogicimPanel;

public class NewSimulationAction
    extends EditorAction
{
  public static final String NAME = "New Simulation";

  private LogicimPanel panel;

  public NewSimulationAction(Logicim editor, LogicimPanel panel)
  {
    super(editor);
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.newSimulation();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.LogicimPanel;

public class SaveSimulationAction
    extends EditorAction
{
  public static final String NAME = "Save Project";

  private LogicimPanel panel;

  public SaveSimulationAction(Logicim editor, LogicimPanel panel)
  {
    super(editor);
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.saveSimulation();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}


package net.logicim.ui.circuit;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.input.event.SimulatorEditorEvent;

public class NewSubcircuitEvent
    extends SimulatorEditorEvent
{
  public String subcircuitName;

  public NewSubcircuitEvent(String subcircuitName)
  {
    this.subcircuitName = subcircuitName;
  }

  @Override
  public void execute(SimulatorEditor editor)
  {
    editor.newSubcircuitAction(subcircuitName);
  }
}


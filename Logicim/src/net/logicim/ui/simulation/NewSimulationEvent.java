package net.logicim.ui.simulation;

import net.logicim.ui.Logicim;
import net.logicim.ui.input.event.SimulatorEditorEvent;

public class NewSimulationEvent
    extends SimulatorEditorEvent
{
  public String simulationName;

  public NewSimulationEvent(String simulationName)
  {
    this.simulationName = simulationName;
  }

  @Override
  public void execute(Logicim editor)
  {
    editor.newSimulationAction(simulationName);
  }
}


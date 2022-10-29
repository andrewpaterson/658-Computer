package net.logicim.ui;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.View;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircuitEditor
{
  protected List<View> views;
  protected Circuit circuit;
  protected Simulation simulation;

  public CircuitEditor()
  {
    circuit = new Circuit();
    views = new ArrayList<>();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    for (View view : views)
    {
      view.paint(graphics, viewport);
    }
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public void add(View view)
  {
    views.add(view);
  }

  public void remove(IntegratedCircuitView<?> view)
  {
    circuit.remove(view.getIntegratedCircuit());
    views.remove(view);
  }

  public Simulation reset()
  {
    return circuit.resetSimulation();
  }

  public void runSimultaneous()
  {
    ensureSimulation();

    simulation.runSimultaneous();
  }

  public void ensureSimulation()
  {
    if (simulation == null)
    {
      simulation = circuit.resetSimulation();
    }
  }
}


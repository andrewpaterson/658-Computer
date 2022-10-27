package net.logicim.domain.common;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.state.State;

import java.util.ArrayList;
import java.util.List;

public class Circuit
{
  protected List<IntegratedCircuit<? extends Pins, ? extends State>> integratedCircuits;

  public Circuit()
  {
    integratedCircuits = new ArrayList<>();
  }

  public void setSimulation(Simulation simulation)
  {
    for (IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit : integratedCircuits)
    {
      if (!integratedCircuit.isStateless())
      {
        State state = simulation.getState(integratedCircuit);
        if (state == null)
        {
          state = integratedCircuit.simulationStarted(simulation);
        }
        integratedCircuit.setState(state);
      }
    }
  }

  public Simulation resetSimulation()
  {
    Simulation simulation = new Simulation();
    for (IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit : integratedCircuits)
    {
      if (!integratedCircuit.isStateless())
      {
        State state = integratedCircuit.simulationStarted(simulation);
        integratedCircuit.setState(state);
      }
    }
    return simulation;
  }

  public void add(IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit)
  {
    integratedCircuits.add(integratedCircuit);
  }
}


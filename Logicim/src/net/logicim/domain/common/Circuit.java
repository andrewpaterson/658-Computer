package net.logicim.domain.common;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.BasePort;
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

  public Simulation resetSimulation()
  {
    Simulation simulation = new Simulation();
    for (IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit : integratedCircuits)
    {
      integratedCircuit.reset(simulation);
      integratedCircuit.simulationStarted(simulation);
    }
    return simulation;
  }

  public void add(IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit)
  {
    integratedCircuits.add(integratedCircuit);
  }

  public void remove(IntegratedCircuit<?, ?> integratedCircuit, Simulation simulation)
  {
    integratedCircuits.remove(integratedCircuit);
    for (BasePort port : integratedCircuit.getPorts())
    {
      port.disconnect(simulation);
    }
  }

  public List<IntegratedCircuit<? extends Pins, ? extends State>> getIntegratedCircuits(String type)
  {
    ArrayList<IntegratedCircuit<? extends Pins, ? extends State>> result = new ArrayList<>();
    for (IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit : integratedCircuits)
    {
      if (integratedCircuit.getType().equals(type))
      {
        result.add(integratedCircuit);
      }
    }
    return result;
  }
}


package net.logicim.domain.common;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.state.State;
import net.logicim.domain.passive.common.Passive;

import java.util.ArrayList;
import java.util.List;

public class Circuit
{
  protected List<IntegratedCircuit<? extends Pins, ? extends State>> integratedCircuits;
  protected List<Passive> passives;

  public Circuit()
  {
    integratedCircuits = new ArrayList<>();
    passives = new ArrayList<>();
  }

  public void resetSimulation(CircuitSimulation circuitSimulation)
  {
    Simulation simulation = circuitSimulation.getSimulation();
    for (IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit : integratedCircuits)
    {
      integratedCircuit.reset(circuitSimulation);
      integratedCircuit.simulationStarted(simulation);
    }
    for (Passive passive : passives)
    {
      passive.reset(circuitSimulation);
    }
  }

  public void add(IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit)
  {
    integratedCircuits.add(integratedCircuit);
  }

  public void add(Passive passive)
  {
    passives.add(passive);
  }

  public void remove(IntegratedCircuit<?, ?> integratedCircuit)
  {
    integratedCircuits.remove(integratedCircuit);
  }

  public void remove(Passive passive)
  {
    passives.remove(passive);
  }

  public void disconnectComponent(Component component, Simulation simulation)
  {
    component.disconnect(simulation);
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


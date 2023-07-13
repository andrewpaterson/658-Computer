package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
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
      integratedCircuit.reset();
      integratedCircuit.simulationStarted(simulation);
    }
    for (Passive passive : passives)
    {
      passive.reset();
      passive.simulationStarted(simulation);
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

  protected String getDescription()
  {
    return "?";
  }

  public void remove(IntegratedCircuit<?, ?> integratedCircuit)
  {
    boolean removed = integratedCircuits.remove(integratedCircuit);
    if (!removed)
    {
      throw new SimulatorException("Could not remove Integrated Circuit [%s] from Circuit [%s].", integratedCircuit.getDescription(), getDescription());
    }
  }

  public void remove(Passive passive)
  {
    boolean removed = passives.remove(passive);
    if (!removed)
    {
      throw new SimulatorException("Could not remove Passive [%s] from Circuit [%s].", passive.getDescription(), getDescription());
    }
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


package net.logicim.domain.passive.common;

import net.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortHolder;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public abstract class Passive
    extends PortHolder
    implements Component
{
  protected SubcircuitSimulation containingSubcircuitSimulation;
  protected String name;

  public Passive(SubcircuitSimulation containingSubcircuitSimulation, String name)
  {
    this.containingSubcircuitSimulation = containingSubcircuitSimulation;
    this.name = name;
    getCircuit().add(this);
  }

  @Override
  public String getName()
  {
    return name;
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  public Port getPort(String name)
  {
    for (Port port : getPorts())
    {
      if (port.getName().equals(name))
      {
        return port;
      }
    }
    throw new SimulatorException("Cannot get port named [%s] on %s [%s].", name, getClass().getSimpleName(), getName());
  }

  @Override
  public void disconnect(Simulation simulation)
  {
  }

  public void reset()
  {
    for (Port port : getPorts())
    {
      port.reset();
    }
  }

  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public Component getComponent()
  {
    return this;
  }

  public void reset(Simulation simulation)
  {
    reset();
    simulationStarted(simulation);
  }

  public Circuit getCircuit()
  {
    return containingSubcircuitSimulation.getCircuit();
  }

  @Override
  public SubcircuitSimulation getContainingSubcircuitSimulation()
  {
    return containingSubcircuitSimulation;
  }

  public abstract String getType();
}


package net.logicim.domain.common;

import net.common.SimulatorException;
import net.common.collection.linkedlist.LinkedList;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PowerInPort;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public abstract class IntegratedCircuit<PINS extends Pins, STATE extends State>
    implements Component
{
  protected SubcircuitSimulation containingSubcircuitSimulation;
  protected PINS pins;
  protected STATE state;
  protected String name;

  protected LinkedList<IntegratedCircuitEvent> events;

  public IntegratedCircuit(SubcircuitSimulation containingSubcircuitSimulation,
                           String name,
                           PINS pins)
  {
    this.containingSubcircuitSimulation = containingSubcircuitSimulation;
    this.name = name;
    this.pins = pins;
    this.pins.setIntegratedCircuit(this);
    this.state = null;
    this.events = new LinkedList<>();
    getCircuit().add(this);
  }

  public PINS getPins()
  {
    return pins;
  }

  public String getName()
  {
    return name;
  }

  public void executeTick(Simulation simulation)
  {
  }

  public boolean isStateless()
  {
    if (state != null)
    {
      return state.isStateless();
    }
    else
    {
      return false;
    }
  }

  public Port getPort(String name)
  {
    return pins.getPort(name);
  }

  public void setState(State state)
  {
    if (this.state != null)
    {
      throw new SimulatorException("Cannot set state on %s.  It is already set.", getDescription());
    }

    if (state != null)
    {
      this.state = (STATE) state;
    }
    else
    {
      throw new SimulatorException("Cannot set state to [null] on %s.", getDescription());
    }
  }

  protected void reset()
  {
    events.clear();
    for (Port port : pins.getPorts())
    {
      port.reset();
    }

    State state = createState();
    setState(state);
  }

  public void traceConnected(Simulation simulation, Port port)
  {
  }

  protected float getVCC(long time)
  {
    PowerInPort voltageCommon = getPins().getVoltageCommon();
    return voltageCommon.getVoltageIn(time);
  }

  protected float getGND(long time)
  {
    PowerInPort voltageGround = getPins().getVoltageGround();
    return voltageGround.getVoltageIn(time);
  }

  public STATE getState()
  {
    return state;
  }

  public List<Port> getPorts()
  {
    return pins.getPorts();
  }

  public void add(IntegratedCircuitEvent event)
  {
    events.add(event);
  }

  public LinkedList<IntegratedCircuitEvent> getEvents()
  {
    return events;
  }

  public void remove(IntegratedCircuitEvent event)
  {
    boolean removed = events.remove(event);
    if (!removed)
    {
      throw new SimulatorException("Cannot remove event on %s [%s].", getClass().getSimpleName(), getDescription());
    }
  }

  protected boolean isPowered(long time)
  {
    float vcc = getVCC(time);
    float gnd = getGND(time);
    return (!Float.isNaN(vcc) && vcc > 0.0f) && (!Float.isNaN(gnd) && gnd == 0.0f);
  }

  @Override
  public void disconnect(Simulation simulation)
  {
    List<IntegratedCircuitEvent> events = this.events.toList();
    for (IntegratedCircuitEvent event : events)
    {
      simulation.removeEvent(event);
    }
    this.events.clear();
  }

  @Override
  public void reset(Simulation simulation)
  {
    reset();
    simulationStarted(simulation);
  }

  @Override
  public Circuit getCircuit()
  {
    return containingSubcircuitSimulation.getCircuit();
  }

  public SubcircuitSimulation getContainingSubcircuitSimulation()
  {
    return containingSubcircuitSimulation;
  }

  public abstract STATE createState();

  public abstract void simulationStarted(Simulation simulation);

  public abstract void inputTransition(Timeline timeline, LogicPort port);

  public abstract String getType();
}


package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.util.StringUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PowerInPort;
import net.logicim.domain.common.state.State;

import java.util.List;

public abstract class IntegratedCircuit<PINS extends Pins, STATE extends State>
    implements Component
{
  protected Circuit circuit;
  protected PINS pins;
  protected STATE state;
  protected String name;
  protected boolean enabled;

  protected LinkedList<IntegratedCircuitEvent> events;

  public IntegratedCircuit(Circuit circuit, String name, PINS pins)
  {
    this.circuit = circuit;
    this.name = name;
    this.pins = pins;
    this.pins.setIntegratedCircuit(this);
    this.state = null;
    this.enabled = true;
    this.events = new LinkedList<>();
    circuit.add(this);
  }

  public PINS getPins()
  {
    return pins;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    if (StringUtil.isEmptyOrNull(name))
    {
      return getType();
    }
    else
    {
      return getType() + " \"" + name + "\"";
    }
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

  public List<Port> getPorts(String commonName)
  {
    return pins.getPorts(commonName);
  }

  public void setState(State state)
  {
    if (!isStateless())
    {
      if (state != null)
      {
        this.state = (STATE) state;
      }
      else
      {
        throw new SimulatorException("Cannot set state to [null] on %s.", getDescription());
      }
    }
  }

  public boolean isEnabled()
  {
    return enabled;
  }

  public void disable()
  {
    if (enabled)
    {
      if (!isStateless())
      {
        state = null;
      }
      enabled = false;
    }
  }

  public void enable(Simulation simulation)
  {
    enabled = true;
    reset(simulation);
  }

  public void reset(Simulation simulation)
  {
    if (enabled)
    {
      events.clear();
      for (Port port : pins.getPorts())
      {
        port.reset();
      }

      if (!isStateless())
      {
        State state = createState(simulation);
        setState(state);
      }
    }
  }

  public void traceConnected(Simulation simulation, Port port)
  {
  }

  public abstract State createState(Simulation simulation);

  public abstract void simulationStarted(Simulation simulation);

  public abstract void inputTransition(Simulation simulation, LogicPort port);

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

  public abstract String getType();

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
}


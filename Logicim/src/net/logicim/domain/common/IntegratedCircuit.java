package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;

import java.util.List;

public abstract class IntegratedCircuit<PINS extends Pins, STATE extends State>
{
  protected Circuit circuit;
  protected PINS pins;
  protected STATE state;
  protected String name;
  protected boolean enabled;

  public IntegratedCircuit(Circuit circuit, String name, PINS pins)
  {
    this.circuit = circuit;
    this.name = name;
    this.pins = pins;
    this.pins.setIntegratedCircuit(this);
    this.state = null;
    this.enabled = true;

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

  public void setState(State state)
  {
    if (!isStateless())
    {
      this.state = (STATE) state;
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
      if (!isStateless())
      {
          State state = simulationStarted(simulation);
          setState(state);
      }
    }
  }

  public abstract State simulationStarted(Simulation simulation);

  public abstract void inputTransition(Simulation simulation, Port port);

  public abstract String getType();

  public STATE getState()
  {
    return state;
  }

  public List<Port> getPorts()
  {
    return pins.getPorts();
  }
}

